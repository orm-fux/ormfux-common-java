package org.blue.bunny.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public final class CollectionUtils {
    
    private CollectionUtils() {
    }
    
    /**
     * Maps from source collection to a target collection.
     * 
     * @param <S> source type
     * @param <T> target type
     * @param source source collection
     * @param result target collection
     * @param functor mapping functor
     * @return target collection
     */
    public static <S, T> Collection<T> map(final S[] source, final Collection<T> result, final Function<S, T> functor) {
        for (final S t : source) {
            result.add(functor.apply(t));
        }
        return result;
    }
    
    /**
     * Maps from source collection to a target collection.
     * 
     * @param <S> source type
     * @param <T> target type
     * @param source source collection
     * @param result target collection
     * @param functor mapping functor
     * @return target collection
     */
    public static <S, T> Collection<T> map(final Collection<? extends S> source, final Collection<T> result, final Function<S, T> functor) {
        for (final S t : source) {
            result.add(functor.apply(t));
        }
        return result;
    }
    
    /**
     * Maps from source collection to a target array.
     * 
     * @param source source collection
     * @param functor mapping functor
     * @return target array
     * 
     * @param <S> source type
     * @param <T> target type
     * 
     */
    @SuppressWarnings("unchecked")
    public static <S, T> T[] mapToArray(final Collection<S> source, final Function<S, T> functor) {
        
        List<T> resultList = new ArrayList<T>(source.size());
        
        for (final S t : source) {
            final T executeResult = functor.apply(t);
            
            if (executeResult != null) {
                // add the non-null results to the list only
                resultList.add(executeResult);
            }
        }
        
        return (T[]) resultList.toArray();
    }
    
    /**
     * Filters a source collection and add result to a target collection.
     * 
     * @param <T> source/target type
     * @param source source collection
     * @param result target collection
     * @param functor filtering predicate
     * @return target array
     */
    public static <T> Collection<T> filter(final Collection<T> source, final Collection<T> result, final Predicate<T> functor) {
        for (final T t : source) {
            if (functor.test(t)) {
                result.add(t);
            }
        }
        return result;
    }
    
    /**
     * Checks whether a collection is null or empty.
     * 
     * @param <T> type
     * @param collection collection
     * @return true, if null or empty
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.size() == 0;
    }
    
    /**
     * Returns a collection of elements from the given collection that evaluate the given predicate
     * to <code>true</code>.
     * 
     * @param <T> Parameterized type.
     * 
     * @param collection The collection.
     * @param predicate The predicate from type <code>Predicate1</code>.
     * @param result The result
     */
    public static <T> void select(final Collection<T> collection, final Predicate<T> predicate, final Collection<T> result) {
        for (final T t : collection) {
            if (predicate.test(t)) {
                result.add(t);
            }
        }
    }
    
    /**
     * Returns the first element from the given collection that evaluates the given predicate to
     * <code>true</code>.
     * 
     * @param <T> Parameterized type.
     * 
     * @param collection a collection.
     * @param predicate The predicate from type <code>Predicate1</code>.
     * @return the first element from the given collection that evaluates the given predicate to
     *         <code>true</code> or <code>null</code> if no element evaluates the given predicate to
     *         <code>true</code>.
     */
    public static <T> T selectFirst(final Collection<T> collection, final Predicate<T> predicate) {
        for (final T t : collection) {
            if (predicate.test(t)) {
                return t;
            }
        }
        return null;
    }
    
    /**
     * Determines whether an element in the given collection evaluates the given predicate to
     * <code>true</code>.
     * 
     * @param <T> Parameterized type.
     * 
     * @param collection a collection.
     * @param predicate a predicate.
     * 
     * @return <code>true</code> if an element in the given collection evaluates the given predicate
     *         to <code>true</code>, <code>false</code> otherwise.
     */
    public static <T> boolean exists(final Collection<T> collection, final Predicate<T> predicate) {
        for (final T t : collection) {
            if (predicate.test(t)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Compares two collections for equality. The elements in both collections should be unique,
     * otherwise the result might not be very accurate.
     * 
     * @param <T> Parameterized type.
     * 
     * @param col1 source collection.
     * @param col2 target collection.
     * 
     * @return <code>true</code> if both collections contain same elements, <code>false</code>
     *         otherwise.
     */
    public static <T> boolean isEqual(final Collection<T> col1, final Collection<T> col2) {
        // equal reference or both null
        if (col1 == col2) {
            return true;
        }
        
        // compare size and elements, when both are not null
        if (col1 != null && col2 != null) {
            return col1.size() == col2.size() && col1.containsAll(col2);
        }
        
        return false;
    }
    
    /**
     * Finds the first entry of a collection or null if the collection is empty.
     * 
     * 
     * @param col collection.
     * 
     * @return the first entry or null.
     */
    public static Object findEntry(final Collection<?> col) {
        if (!isEmpty(col)) {
            for (Object obj : col) {
                if (obj != null) {
                    return obj;
                }
            }
        }
        return null;
    }
    
    /**
     * Try to find at least one entry out of 2 collections for introspection.
     * 
     * @param col1 collection1
     * @param col2 collection2
     * @return the first entry found or null.
     */
    public static Object findEntry(Collection<?> col1, Collection<?> col2) {
        Object entry = CollectionUtils.findEntry(col1);
        if (entry == null) {
            entry = CollectionUtils.findEntry(col2);
        }
        return entry;
    }
    
    /**
     * Converts a collection to a map.
     * 
     * @param <K> key type
     * @param <E> entry type
     * @param source source collection
     * @param functor functor
     * @return target map
     */
    public static <K, E> Map<K, E> map(Collection<E> source, Function<E, K> functor) {
        Map<K, E> map = new HashMap<K, E>();
        for (E e : source) {
            map.put(functor.apply(e), e);
        }
        return map;
    }
    
}
