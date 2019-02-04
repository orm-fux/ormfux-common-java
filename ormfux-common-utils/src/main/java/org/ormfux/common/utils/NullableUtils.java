package org.ormfux.common.utils;

import java.util.Collection;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

import org.ormfux.common.utils.object.Objects;

/**
 * Utility to interact with values that can be {@code null}. Treat this as the {@link java.util.Optional},
 * but with more lambda support.
 */
public final class NullableUtils {
    
    private NullableUtils() {
        throw new IllegalAccessError(NullableUtils.class.getSimpleName() + " class is not intended to be instantiated");
    }
    
    /**
     * Checks, if the object is {@code null}.
     * 
     * @param object The object.
     * @return {@code true} when it is {@code null}.
     */
    public static boolean isNull(final Object object) {
        return object == null;
    }
    
    /**
     * Checks, if the object or function result is {@code null}.
     * 
     * @param object The object.
     * @param function The function to apply to the object.
     * @return {@code true} when object or function result are {@code null}.
     */
    public static <S, R> boolean isNull(final S object, final Function<S, R> function) {
        java.util.Objects.requireNonNull(function);
        
        return isNull(object) || isNull(function.apply(object));
    }
    
    /**
     * Checks, if the object is <i>not</i> {@code null}.
     * 
     * @param object The object.
     * @return {@code true} when it is {@code null}.
     */
    public static boolean nonNull(final Object object) {
        return object != null;
    }
    
    /**
     * Checks, if the object and function result are both <i>not</i> {@code null}.
     * 
     * @param object The object.
     * @param function The function to apply to the object.
     * @return {@code true} when object and function result are both <i>not</i> {@code null}.
     */
    public static <S, R> boolean nonNull(final S object, final Function<S, R> function) {
        java.util.Objects.requireNonNull(function);
        
        return nonNull(object) && nonNull(function.apply(object));
    }
    
    /**
     * Performs {@code null}-check on and retrieves a value from the given object with 
     * the given function. 
     * 
     * @param object The object from which to retrieve the value.
     * @param function The retrieval function.
     * @return The function result; {@code null} when the object is {@code null}.
     */
    public static <S, R> R retrieve(final S object, final Function<S, R> function) {
        return retrieve(object, function, null);
    }
    
    /**
     * Performs {@code null}-check on and retrieves a value from the given object with 
     * the given function. In case of {@code null} returns a fallback.
     * 
     * @param object The object from which to retrieve the value.
     * @param function The retrieval function.
     * @param fallback The value to fall back to.
     * @return The function result; {@code fallback} when the object is {@code null} or 
     *         the function returns {@code null}.
     */
    public static <S, R> R retrieve(final S object, final Function<S, R> function, final R fallback) {
        java.util.Objects.requireNonNull(function);
        
        if (nonNull(object)) {
            final R result = function.apply(object);
            
            if (nonNull(result)) {
                return result;
            } 
        }
        
        return fallback;
    }
    
    /**
     * When the provided {@link Boolean} object is null treats it as {@code false}.
     * 
     * @param boolValue
     * @return {@code true} when the value is not null and represents {@code true}.
     */
    public static boolean check(final Boolean boolValue) {
        return nonNull(boolValue) && boolValue;
    }
    
    /**
     * Checks that the object is not null and the boolean value is {@code true} as well. 
     * 
     * @param object The object.
     * @param predicate The predicate.
     * @return {@code true} when the object is not {@code null} and the boolean is {@code true}.
     */
    public static <S> boolean check(final S object, final BooleanSupplier predicate) {
        java.util.Objects.requireNonNull(predicate);
        
        return nonNull(object) && predicate.getAsBoolean();
    }
    
    /**
     * Applies the predicate to the object in a {@code null}-safe fashion. 
     * 
     * @param object The object.
     * @param predicate The predicate to apply to the object.
     * @return The predicate result when object is not {@code null}; {@code false} when 
     *         the object is {@code null}.
     */
    public static <S> boolean check(final S object, final Predicate<S> predicate) {
        return check(object, predicate, false);
    }
    
    /**
     * Applies the predicate to the object in a {@code null}-safe fashion. Returns the fallback
     * when the object is {@code null}.
     * 
     * @param object The object.
     * @param predicate The predicate to apply to the object.
     * @param fallback The fallback when the object is undefined.
     * @return The predicate result when object is not {@code null}; the {@code fallback} when 
     *         the object is {@code null}.
     */
    public static <S> boolean check(final S object, final Predicate<S> predicate, final boolean fallback) {
        java.util.Objects.requireNonNull(predicate);
        
        return nonNull(object) ? predicate.test(object) : fallback;
    }
    
    /**
     * Applies a predicate to a function result in a {@code null}-safe fashion.
     * 
     * @param object The object.
     * @param function The function to apply on the object. 
     * @param predicate The predicate to apply on the function result.
     * 
     * @return The result of the predicate; {@code false} when the object or function result is {@code null}.
     */
    public static <S, T> boolean check(final S object, final Function<S, T> function, final Predicate<T> predicate) {
        return check(object, function, predicate, false);
    }
    /**
     * Applies a predicate to a function result in a {@code null}-safe fashion.
     * 
     * @param object The object.
     * @param function The function to apply on the object. 
     * @param predicate The predicate to apply on the function result.
     * @param fallback The value to fall back to when the object or function result is {@code null}.
     * 
     * @return The result of the predicate; {@code fallback} when the object or function result is {@code null}.
     */
    public static <S, T> boolean check(final S object, final Function<S, T> function, final Predicate<T> predicate, final boolean fallback) {
        java.util.Objects.requireNonNull(function);
        java.util.Objects.requireNonNull(predicate);
        
        if (nonNull(object)) {
            final T functionResult = function.apply(object);
            
            if (nonNull(functionResult)) {
                return predicate.test(functionResult);
            }
        }
        
        return fallback;
    }
    
    /** 
     * The negation of the provided predicate. Allows for invocations like this:
     * <pre>check(obj, not(Type::testSomething))</pre> 
     * 
     * @param predicate The predicate.
     * @return The negated predicate.
     */
    public static <T> Predicate<T> not(final Predicate<T> predicate) {
        return predicate.negate();
    }
    
    /**
     * A predicate representing {@link Collection#isEmpty()}. Allows for invocations like this:
     * <pre>check(obj, Type::getList, isEmpty())</pre>
     * 
     *  @return The predicate for {@link Collection#isEmpty()}.
      */
    public static <T> Predicate<Collection<? extends T>> isEmpty() {
        return Collection::isEmpty;
    }
    
    /** 
     * A predicate to check for value equality. Allows for invocations like this:
     * <pre>check(obj, Type::getProperty, equalTo(otherValue))</pre>
     * 
     * @param comparisonReference The second value for the equals check.
     * @return The predicate representing {@link Objects#equals(Object, Object)}.
     */
    public static <T> Predicate<T> equalTo(final T comparisonReference) {
        return source -> Objects.equals(source, comparisonReference);
    }
}
