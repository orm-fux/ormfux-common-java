package org.ormfux.common.ioc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.ormfux.common.ioc.annotations.ConfigValue;
import org.ormfux.common.ioc.exception.ConfigValueLoadException;

/**
 * Context providing the configuration values that can be injected into beans. The
 * values can be loaded from manually defined value sets or, as fallback from system 
 * properties.
 */
final class ConfigValueContext {
    
    /**
     * The sets of configuration values (by name).
     */
    private static final Map<String, Properties> CONFIG_VALUE_SETS = new HashMap<>();
    
    private ConfigValueContext() {
        throw new UnsupportedOperationException("The ConfigValueContext is static and not supposed to be instantiated.");
    }
    
    /**
     * Adds a new set of configuration values to the context. The configuration values
     * are loaded from a file that is not on the application's class path.
     * 
     * @param name The identifying name of the value set.
     * @param path The full path to the config value file.
     * 
     * @throws ConfigValueLoadException
     */
    public static void addExternalConfigValueSet(final String name, final String path) throws ConfigValueLoadException {
        try (final InputStream valueStream = new FileInputStream(path)) {
            addConfigValueSet(name, path, valueStream);
        } catch (final IOException e) {
            throw new ConfigValueLoadException("Error reading configuration values '" + name + "' from: " + path, e);
        }
    }
    
    /**
     * Adds a new set of configuration values to the context. The configuration values
     * are loaded from a file that is on the application's class path.
     * 
     * @param name The identifying name of the value set.
     * @param path The path to the config value file.
     * 
     * @throws ConfigValueLoadException
     */
    public static void addConfigValueSet(final String name, final String path) throws ConfigValueLoadException {
        try (final InputStream valueStream = ConfigValueContext.class.getResourceAsStream(path)) {
            if (valueStream != null) {
                addConfigValueSet(name, path, valueStream);
            } else {
                throw new ConfigValueLoadException("Config value file " + path + " not found.");
            }
        } catch (final IOException e) {
            throw new ConfigValueLoadException("Error reading configuration values '" + name + "' from: " + path, e);
        }
    }
    
    /**
     * Adds a new set of configuration values to the context. The configuration values
     * are loaded from a stream.
     * 
     * @param name The identifying name of the value set.
     * @param path The path to the config value file.
     * @param valueStream The stream with the config values.
     * 
     * @throws ConfigValueLoadException
     */
    private static void addConfigValueSet(final String name, final String path, final InputStream valueStream) throws ConfigValueLoadException {
        final Properties valueSet = new Properties();
        
        try {
            if (path.endsWith(".xml")) {
                valueSet.loadFromXML(valueStream);
            } else {
                valueSet.load(valueStream);
            }
            
        } catch (final IOException e) {
            throw new ConfigValueLoadException("Error reading configuration values '" + name + "' from: " + path, e);
        }
        
        CONFIG_VALUE_SETS.put(name, valueSet);
        
    }
    
    /**
     * Gets the value for the config value definition.
     * 
     * @param configValueDefinition The config value definition.
     * @param valueType The type of the value.
     * @return The value; {@code null} when not found.
     * 
     * @throws ConfigValueLoadException
     * 
     * @see #getConfigValue(String, String, Class)
     * 
     */
    public static Object getConfigValue(final ConfigValue configValueDefinition, final Class<?> valueType) throws ConfigValueLoadException {
        return getConfigValue(configValueDefinition.set(), 
                              !configValueDefinition.key().isEmpty() ? configValueDefinition.key() : configValueDefinition.value(), 
                              valueType);
    }
    
    /**
     * Looks up the config value matching the provided key. When a value set name is provided
     * only the provided set is searched. Otherwise looks through all the sets and takes the first 
     * match. When looking through all sets the last config set, in which to look, are the the
     * system properties ({@link System#getProperties()}). 
     * 
     * @param valueSet The name of the config value set.
     * @param valueKey The key of the config value.
     * @param valueType The type of  the value.
     * @return The value; {@code null} when not found.
     * 
     * @throws ConfigValueLoadException
     */
    public static Object getConfigValue(final String valueSet, final String valueKey, final Class<?> valueType) throws ConfigValueLoadException {
        if (valueSet == null || valueSet.isEmpty()) {
            for (final Properties configValues : CONFIG_VALUE_SETS.values()) {
                final Object value = getConfigValue(configValues, valueKey, valueType);
                
                if (value != null) {
                    return value;
                }
            }
            
            return getConfigValue(System.getProperties(), valueKey, valueType);
            
        } else {
            final Properties configValues = CONFIG_VALUE_SETS.get(valueSet);
            
            if (configValues != null) {
                return getConfigValue(configValues, valueKey, valueType);
            }
            
            return null;
        }
        
    }
    
    /**
     * Gets the config value from the value set.
     * 
     * @param valueSet The value set.
     * @param valueKey The value key.
     * @param valueType The type of the value.
     * @return The value; {@code null} when not found.
     * 
     * @throws ConfigValueLoadException
     */
    private static Object getConfigValue(final Properties valueSet, final String valueKey, final Class<?> valueType) throws ConfigValueLoadException {
        final String value = valueSet.getProperty(valueKey);
        
        if (value != null) {
            try {
                return convertValue(value, valueType);
            } catch (final ConfigValueLoadException | NumberFormatException e) {
                throw new ConfigValueLoadException("Error loading config value '" + valueSet + "." + valueKey + "'", e);
            }
        } else {
            return null;
        }
    }
    
    /**
     * Converts the String to the provided type.
     * 
     * @param value The value to convert.
     * @param valueType The target type.
     * @return The converted value.
     * 
     * @throws ConfigValueLoadException
     * @throws NumberFormatException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    /* private -> test */ static Object convertValue(final String value, final Class<?> valueType) throws ConfigValueLoadException, NumberFormatException {
        if (String.class.equals(valueType)) {
            return value;
        }
        
        if (valueType.isEnum()) {
            try {
                return Enum.valueOf((Class<Enum>) valueType, value);
            } catch (final IllegalArgumentException e) {
                throw new ConfigValueLoadException("Cannot load config value.", e);
            }
        } 
        
        if (valueType.equals(Class.class)) {
            try {
                return Class.forName(value);
            } catch (final ClassNotFoundException e) {
                throw new ConfigValueLoadException("Cannot load config value.", e);
            }
        }
        
        if (Character.class.equals(valueType) || char.class.equals(valueType)) {
            if (value.length() == 1) {
                return value.charAt(0);
            } else {
                throw new ConfigValueLoadException("The value does not represent a single character.");
            }
        }
        
        if (Byte.class.equals(valueType) || byte.class.equals(valueType)) {
            return Byte.parseByte(value);
        }
        
        if (Short.class.equals(valueType) || short.class.equals(valueType)) {
            return Short.parseShort(value);
        }
        
        if (Integer.class.equals(valueType) || int.class.equals(valueType)) {
            return Integer.parseInt(value);
        }
        
        if (Long.class.equals(valueType) || long.class.equals(valueType)) {
            return Long.parseLong(value);
        }
        
        if (Float.class.equals(valueType) || float.class.equals(valueType)) {
            return Float.parseFloat(value);
        }
        
        if (Double.class.equals(valueType) || double.class.equals(valueType)) {
            return Double.parseDouble(value);
        }
        
        if (BigDecimal.class.equals(valueType)) {
            return new BigDecimal(value);
        }
        
        if (BigInteger.class.equals(valueType)) {
            return new BigInteger(value);
        }
        
        if (valueType.isArray()) {
            return convertToArray(value, valueType);
        }
        
        throw new ConfigValueLoadException("Unsupported value type: " + valueType);
    }
    
    /**
     * Converts the String value to an array containing values of the provided type.
     * The default array value separator is the comma symbol (","). A custom one can 
     * be specified as prefix of the String: For a custom separator use the format
     * {@code $separator/$values} - i.e. end the separator with a "slash". This also 
     * means that you cannot use "/" as custom separator!
     * 
     * @param value The array as String.
     * @param valueType The types of values in the array.
     * @return The Array parsed from the String.
     */
    private static Object convertToArray(final String value, final Class<?> valueType) {
        final Class<?> arrayValueType = valueType.getComponentType();
        
        if (value.isEmpty()) {
            return Array.newInstance(arrayValueType, 0);
            
        } else {
            final int separatorEndIndex = value.indexOf('/');
            final String separator;
            
            if (separatorEndIndex < 1) {
                separator = ",";
            } else {
                separator = value.substring(0, separatorEndIndex);
            }
            
            if (separatorEndIndex == value.length() - 1) {
                return Array.newInstance(arrayValueType, 0);
            } else {
                final String[] rawValues = value.substring(separatorEndIndex + 1).split(separator);
                final Object array = Array.newInstance(arrayValueType, rawValues.length);
                
                for (int valueIndex = 0; valueIndex < rawValues.length; valueIndex++) {
                    Array.set(array, valueIndex, convertValue(rawValues[valueIndex], arrayValueType));
                }
                
                return array;
            }
        }
    }
    
}
