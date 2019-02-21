package org.ormfux.common.ioc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.ormfux.common.ioc.exception.ConfigValueLoadException;

public class ConfigValueTypeConversionTest extends AbstractDependencyInjectionTest {
    
    @Test
    public void testStringValue() {
        assertThat(ConfigValueContext.convertValue("value", String.class)).isEqualTo("value");
        assertThat(ConfigValueContext.convertValue("", String.class)).isEqualTo("");
    }
    
    @Test
    public void testEnumValue() {
        assertThat(ConfigValueContext.convertValue("HOURS", TimeUnit.class)).isEqualTo(TimeUnit.HOURS);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", TimeUnit.class)).isExactlyInstanceOf(ConfigValueLoadException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("nope", TimeUnit.class)).isExactlyInstanceOf(ConfigValueLoadException.class);
    }
    
    @Test
    public void testClassValue() {
        assertThat(ConfigValueContext.convertValue(ConfigValueTypeConversionTest.class.getName(), Class.class)).isEqualTo(ConfigValueTypeConversionTest.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", Class.class)).isExactlyInstanceOf(ConfigValueLoadException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("nope", Class.class)).isExactlyInstanceOf(ConfigValueLoadException.class);
    }
    
    @Test
    public void testCharacterValue() {
        assertThat(ConfigValueContext.convertValue("c", Character.class)).isEqualTo('c');
        assertThat(ConfigValueContext.convertValue("c", char.class)).isEqualTo('c');
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", Character.class)).isExactlyInstanceOf(ConfigValueLoadException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", char.class)).isExactlyInstanceOf(ConfigValueLoadException.class);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", Class.class)).isExactlyInstanceOf(ConfigValueLoadException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", char.class)).isExactlyInstanceOf(ConfigValueLoadException.class);
    }
    
    @Test
    public void testByteValue() {
        assertThat(ConfigValueContext.convertValue("100", Byte.class)).isEqualTo((byte) 100);
        assertThat(ConfigValueContext.convertValue("100", byte.class)).isEqualTo((byte) 100);
        
        assertThat(ConfigValueContext.convertValue("" + Byte.MIN_VALUE, Byte.class)).isEqualTo(Byte.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Byte.MIN_VALUE, byte.class)).isEqualTo(Byte.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Byte.MAX_VALUE, Byte.class)).isEqualTo(Byte.MAX_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Byte.MAX_VALUE, byte.class)).isEqualTo(Byte.MAX_VALUE);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + (Byte.MIN_VALUE - 1), Byte.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + (Byte.MIN_VALUE - 1), byte.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + (Byte.MAX_VALUE + 1), Byte.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + Byte.MAX_VALUE + 1, byte.class)).isExactlyInstanceOf(NumberFormatException.class);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", Byte.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", byte.class)).isExactlyInstanceOf(NumberFormatException.class);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", Byte.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", byte.class)).isExactlyInstanceOf(NumberFormatException.class);
    }
    
    @Test
    public void testShortValue() {
        assertThat(ConfigValueContext.convertValue("100", Short.class)).isEqualTo((short) 100);
        assertThat(ConfigValueContext.convertValue("100", short.class)).isEqualTo((short) 100);
        
        assertThat(ConfigValueContext.convertValue("" + Short.MIN_VALUE, Short.class)).isEqualTo(Short.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Short.MIN_VALUE, short.class)).isEqualTo(Short.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Short.MAX_VALUE, Short.class)).isEqualTo(Short.MAX_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Short.MAX_VALUE, short.class)).isEqualTo(Short.MAX_VALUE);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + (Short.MIN_VALUE - 1), Short.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + (Short.MIN_VALUE - 1), short.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + (Short.MAX_VALUE + 1), Short.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + Short.MAX_VALUE + 1, short.class)).isExactlyInstanceOf(NumberFormatException.class);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", Short.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", short.class)).isExactlyInstanceOf(NumberFormatException.class);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", Short.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", short.class)).isExactlyInstanceOf(NumberFormatException.class);
    }
    
    @Test
    public void testIntegerValue() {
        assertThat(ConfigValueContext.convertValue("100", Integer.class)).isEqualTo((int) 100);
        assertThat(ConfigValueContext.convertValue("100", int.class)).isEqualTo((int) 100);
        
        assertThat(ConfigValueContext.convertValue("" + Integer.MIN_VALUE, Integer.class)).isEqualTo(Integer.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Integer.MIN_VALUE, int.class)).isEqualTo(Integer.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Integer.MAX_VALUE, Integer.class)).isEqualTo(Integer.MAX_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Integer.MAX_VALUE, int.class)).isEqualTo(Integer.MAX_VALUE);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + BigInteger.valueOf(Integer.MIN_VALUE).subtract(BigInteger.ONE), Integer.class))
                    .isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + BigInteger.valueOf(Integer.MIN_VALUE).subtract(BigInteger.ONE), int.class))
                    .isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + BigInteger.valueOf(Integer.MAX_VALUE).add(BigInteger.ONE), Integer.class))
                    .isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + BigInteger.valueOf(Integer.MAX_VALUE).add(BigInteger.ONE), int.class))
                    .isExactlyInstanceOf(NumberFormatException.class);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", Integer.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", int.class)).isExactlyInstanceOf(NumberFormatException.class);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", Integer.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", int.class)).isExactlyInstanceOf(NumberFormatException.class);
    }
    
    @Test
    public void testLongValue() {
        assertThat(ConfigValueContext.convertValue("100", Long.class)).isEqualTo((long) 100);
        assertThat(ConfigValueContext.convertValue("100", long.class)).isEqualTo((long) 100);
        
        assertThat(ConfigValueContext.convertValue("" + Long.MIN_VALUE, Long.class)).isEqualTo(Long.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Long.MIN_VALUE, long.class)).isEqualTo(Long.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Long.MAX_VALUE, Long.class)).isEqualTo(Long.MAX_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Long.MAX_VALUE, long.class)).isEqualTo(Long.MAX_VALUE);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE), Long.class))
                    .isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE), long.class))
                    .isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE), Long.class))
                    .isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("" + BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE), long.class))
                    .isExactlyInstanceOf(NumberFormatException.class);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", Long.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", long.class)).isExactlyInstanceOf(NumberFormatException.class);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", Long.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", long.class)).isExactlyInstanceOf(NumberFormatException.class);
    }
    
    @Test
    public void testFloatValue() {
        assertThat(ConfigValueContext.convertValue("100", Float.class)).isEqualTo((float) 100);
        assertThat(ConfigValueContext.convertValue("100", float.class)).isEqualTo((float) 100);
        
        assertThat(ConfigValueContext.convertValue("" + Float.MIN_VALUE, Float.class)).isEqualTo(Float.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Float.MIN_VALUE, float.class)).isEqualTo(Float.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Float.MAX_VALUE, Float.class)).isEqualTo(Float.MAX_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Float.MAX_VALUE, float.class)).isEqualTo(Float.MAX_VALUE);
        
        assertThat(ConfigValueContext.convertValue("1.41e-45f", Float.class)).isEqualTo(Float.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("1.41e-45f", float.class)).isEqualTo(Float.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("3.40282351e+38f", Float.class)).isEqualTo(Float.MAX_VALUE);
        assertThat(ConfigValueContext.convertValue("3.40282351e+38f", float.class)).isEqualTo(Float.MAX_VALUE);
        assertThat(ConfigValueContext.convertValue("-3.4028235e+38f", Float.class)).isEqualTo(-Float.MAX_VALUE);
        assertThat(ConfigValueContext.convertValue("-3.4028235e+38f", float.class)).isEqualTo(-Float.MAX_VALUE);
        
        assertThat(ConfigValueContext.convertValue("1.4e-46f", Float.class)).isEqualTo(0f);
        assertThat(ConfigValueContext.convertValue("1.4e-46f", float.class)).isEqualTo(0f);
        assertThat(ConfigValueContext.convertValue("3.4028235e+39f", Float.class)).isEqualTo(Float.POSITIVE_INFINITY);
        assertThat(ConfigValueContext.convertValue("3.4028235e+39f", float.class)).isEqualTo(Float.POSITIVE_INFINITY);
        assertThat(ConfigValueContext.convertValue("-3.4028235e+39f", Float.class)).isEqualTo(Float.NEGATIVE_INFINITY);
        assertThat(ConfigValueContext.convertValue("-3.4028235e+39f", float.class)).isEqualTo(Float.NEGATIVE_INFINITY);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", Float.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", float.class)).isExactlyInstanceOf(NumberFormatException.class);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", Float.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", float.class)).isExactlyInstanceOf(NumberFormatException.class);
    }
    
    @Test
    public void testDoubleValue() {
        assertThat(ConfigValueContext.convertValue("100", Double.class)).isEqualTo((double) 100);
        assertThat(ConfigValueContext.convertValue("100", double.class)).isEqualTo((double) 100);
        
        assertThat(ConfigValueContext.convertValue("" + Double.MIN_VALUE, Double.class)).isEqualTo(Double.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Double.MIN_VALUE, double.class)).isEqualTo(Double.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Double.MAX_VALUE, Double.class)).isEqualTo(Double.MAX_VALUE);
        assertThat(ConfigValueContext.convertValue("" + Double.MAX_VALUE, double.class)).isEqualTo(Double.MAX_VALUE);
        
        assertThat(ConfigValueContext.convertValue("4.9e-324", Double.class)).isEqualTo(Double.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("4.9e-324", double.class)).isEqualTo(Double.MIN_VALUE);
        assertThat(ConfigValueContext.convertValue("1.7976931348623157e+308", Double.class)).isEqualTo(Double.MAX_VALUE);
        assertThat(ConfigValueContext.convertValue("1.7976931348623157e+308", double.class)).isEqualTo(Double.MAX_VALUE);
        assertThat(ConfigValueContext.convertValue("-1.7976931348623157e+308", Double.class)).isEqualTo(-Double.MAX_VALUE);
        assertThat(ConfigValueContext.convertValue("-1.7976931348623157e+308", double.class)).isEqualTo(-Double.MAX_VALUE);
        
        assertThat(ConfigValueContext.convertValue("4.9e-325", Double.class)).isEqualTo(0d);
        assertThat(ConfigValueContext.convertValue("4.9e-325", double.class)).isEqualTo(0d);
        assertThat(ConfigValueContext.convertValue("1.7976931348623157e+309", Double.class)).isEqualTo(Double.POSITIVE_INFINITY);
        assertThat(ConfigValueContext.convertValue("1.7976931348623157e+309", double.class)).isEqualTo(Double.POSITIVE_INFINITY);
        assertThat(ConfigValueContext.convertValue("-1.7976931348623157e+309", Double.class)).isEqualTo(Double.NEGATIVE_INFINITY);
        assertThat(ConfigValueContext.convertValue("-1.7976931348623157e+309", double.class)).isEqualTo(Double.NEGATIVE_INFINITY);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", Double.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", double.class)).isExactlyInstanceOf(NumberFormatException.class);
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", Double.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", double.class)).isExactlyInstanceOf(NumberFormatException.class);
    }
    
    @Test
    public void testBigIntegerValue() {
        assertThat(ConfigValueContext.convertValue("100", BigInteger.class)).isEqualTo(new BigInteger("100"));
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", BigInteger.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", BigInteger.class)).isExactlyInstanceOf(NumberFormatException.class);
    }
    
    @Test
    public void testBigDecimalValue() {
        assertThat(ConfigValueContext.convertValue("100", BigDecimal.class)).isEqualTo(new BigDecimal("100"));
        
        assertThatThrownBy(() -> ConfigValueContext.convertValue("", BigDecimal.class)).isExactlyInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> ConfigValueContext.convertValue("no", BigDecimal.class)).isExactlyInstanceOf(NumberFormatException.class);
    }
    
    @Test
    public void testArrayValueDefaultSeparator() {
        assertThat(ConfigValueContext.convertValue("val1,v2,value3", String[].class))
                            .isExactlyInstanceOf(String[].class)
                            .matches(val -> Arrays.deepEquals((String[]) val, new String[] {"val1", "v2", "value3"}));
        
        assertThat(ConfigValueContext.convertValue("", String[].class))
                            .isExactlyInstanceOf(String[].class)
                            .matches(val -> Arrays.deepEquals((String[]) val, new String[0]));
        
        assertThat(ConfigValueContext.convertValue(" ", String[].class))
                            .isExactlyInstanceOf(String[].class)
                            .matches(val -> Arrays.deepEquals((String[]) val, new String[] {" "}));
        
        assertThat(ConfigValueContext.convertValue("34,-12", Integer[].class))
                            .isExactlyInstanceOf(Integer[].class)
                            .matches(val -> Arrays.deepEquals((Integer[]) val, new Integer[] {34, -12}));
    }
    
    @Test
    public void testArrayValueCustomSeparator() {
        assertThat(ConfigValueContext.convertValue("-/val1,-v2,value3", String[].class))
                            .isExactlyInstanceOf(String[].class)
                            .matches(val -> Arrays.deepEquals((String[]) val, new String[] {"val1,", "v2,value3"}));
        
        assertThat(ConfigValueContext.convertValue("-/", String[].class))
                            .isExactlyInstanceOf(String[].class)
                            .matches(val -> Arrays.deepEquals((String[]) val, new String[0]));
        
        assertThat(ConfigValueContext.convertValue("-/ ", String[].class))
                            .isExactlyInstanceOf(String[].class)
                            .matches(val -> Arrays.deepEquals((String[]) val, new String[] {" "}));
        
        assertThat(ConfigValueContext.convertValue(";/34;-12", Integer[].class))
                            .isExactlyInstanceOf(Integer[].class)
                            .matches(val -> Arrays.deepEquals((Integer[]) val, new Integer[] {34, -12}));
    }
}
