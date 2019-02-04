package org.ormfux.common.utils.object;

import java.util.function.Function;

import org.ormfux.common.utils.NullableUtils;
import org.ormfux.common.utils.reflection.ClassUtils;

/**
 * Stateful utility inspired by Apache's {@code EqualsBuilder}. Provides convenience 
 * functionality to check equality of objects by their property values. Each instance 
 * is supposed to be used for a one time equals check as this builder conducts the
 * equals checks in an eager fashion. I.e. the equality of property value is checked as 
 * soon as the property is "appended" to this builder. As soon as the state of this 
 * builder indicates "not equal" no further checks are conducted anymore. 
 * 
 * @see org.apache.commons.lang3.builder.EqualsBuilder
 */
public class EqualsBuilder<T> {
    
    /**
     * The current equality state of the builder.
     */
    private EqualsState state;
    
    /**
     * The first of the values to check for equality.
     */
    private final T value1;
    
    /**
     * The second of the values to check for equality.
     */
    private final T value2;
    
    /**
     * A new EqualsBuilder which also initializes the "equals"-state of the builder.
     * 
     * @param value1 The first value. Must be {@code not null}.
     * @param value2 The second value.
     */
    public EqualsBuilder(final T value1, final Object value2) {
        this(value1, value2, true);
    }
    
    /**
     * A new EqualsBuilder which also initializes the "equals"-state of the builder.
     * Use when there is inheritance for value1 and the super-class equals result
     * of value1 should be considered. Use the super-equals value as {@code initialEquals}
     * parameter value.
     * 
     * @param value1 The first value. Must be {@code not null}.
     * @param value2 The second value.
     * @param initialEquals Initial equals state when the values actually can be compared
     *                      (i.e. value2 is assignable to the type of value1).
     */
    @SuppressWarnings("unchecked")
    public EqualsBuilder(final T value1, final Object value2, final boolean initialEquals) {
        java.util.Objects.requireNonNull(value1);
        
        this.value1 = value1;
        
        if (NullableUtils.isNull(value2)) {
            this.value2 = null;
            this.state = EqualsState.NEVER_EQUAL;
            
        } else if (Objects.isSame(value1, value2)) {
            this.value2 = (T) value2;
            this.state = EqualsState.ALWAYS_EQUAL;
            
        } else if (ClassUtils.getClass(value1).isAssignableFrom(ClassUtils.getClass(value2))) {
            this.value2 = (T) value2;
            
            if (initialEquals) {
                this.state = EqualsState.EQUAL;
            } else {
                this.state = EqualsState.NOT_EQUAL;
            }
            
        } else {
            this.value2 = null;
            this.state = EqualsState.NEVER_EQUAL;
        }
        
    }
    
    /**
     * "Appends" the comparison result of the property value with the function
     * from the compared values to the equals state of the builder. The equals
     * check is only conducted when this builder is still in state "equal".
     *  
     * @param propertySelector The property selector function.
     * @return This builder.
     */
    public EqualsBuilder<T> append(final Function<T, ?> propertySelector) {
        if (state == EqualsState.EQUAL) { //this is the only state which can still change.
            final Object propertyValue1 = propertySelector.apply(value1);
            final Object propertyValue2 = propertySelector.apply(value2);
            
            if (!Objects.isSame(propertyValue1, propertyValue2) && !Objects.equals(propertyValue1, propertyValue2)) {
                state = EqualsState.NOT_EQUAL;
            }
        }
        
        return this;
    }
    
    /**
     * If the equality state of this builder's compared values is currently 
     * considering the values as "equal".
     */
    public boolean isEquals() {
        return state == EqualsState.EQUAL || state == EqualsState.ALWAYS_EQUAL;
    }
    
    /**
     * States that the builder can have for the equals state of the compared values.
     */
    private static enum EqualsState {
        /**
         * Currently equal.
         */
        EQUAL,
        
        /**
         * Currently not equal.
         */
        NOT_EQUAL,
        
        /**
         * Will always be equal (i.e. "same object").
         */
        ALWAYS_EQUAL,
        
        /**
         * Will never be equal (incompatible types or one value is {@code null}).
         */
        NEVER_EQUAL
    }
    
}
