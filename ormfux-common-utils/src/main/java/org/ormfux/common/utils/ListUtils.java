package org.ormfux.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Utilities to operate on lists.
 */
public final class ListUtils {
    
    private ListUtils() {
    }
    
    /**
     * Transforms the elements of a collection to a list of other typed elements.
     * 
     * @param <S> source type
     * @param <T> target type
     * 
     * @param source source list.
     * @param functor transformation function.
     * @return list with transformed elements.
     */
    public static <S, T> List<T> map(final Collection<S> source, final Function<S, T> functor) {
        return (List<T>) CollectionUtils.map(source, new ArrayList<>(), functor);
    }
    
    /**
     * Filters a source list according to a predicate.
     * 
     * @param <T> type parameter
     * 
     * @param source source list
     * @param predicate predicate
     * @return filtered list
     */
    public static <T> List<T> filter(final Collection<T> source, final Predicate<T> predicate) {
        return (List<T>) CollectionUtils.filter(source, new ArrayList<>(), predicate);
    }
    
    /**
     * Returns the first element from the given list that evaluates the given predicate to
     * {@code true}.
     * 
     * @param <T> The type of the list.
     * 
     * @param list a list.
     * @param predicate a predicate.
     * @return the first element from the given list that evaluates the given predicate to
     *         {@code true};  {@code null} when there is none.
     */
    public static <T> T selectFirst(final List<T> list, final Predicate<T> predicate) {
        return CollectionUtils.selectFirst(list, predicate);
    }
    
    /**
     * Returns the last element from the given list that evaluates the given predicate to
     * {@code true}.
     * 
     * @param <T> The type of the list.
     * 
     * @param list a list.
     * @param predicate a predicate.
     * @return the last element from the given list that evaluates the given predicate to
     *         {@code true};  {@code null} when there is none.
     */
    public static <T> T selectLast(final List<T> list, final Predicate<T> predicate) {
        if (predicate == null) {
            throw new IllegalArgumentException("The predicate is required.");
        }
        
        if (list.isEmpty()) {
            return null;
        } else {
            for (int elemIndex = maxIndex(list); elemIndex >= 0; elemIndex--) {
                final T element = list.get(elemIndex);
                
                if (predicate.test(element)) {
                    return element;
                }
            }
        }
        
        return null;
    }
    
    /**
     * The first element in the list (i.e. a better notation for {@code list.get(0)}).
     * 
     * @param list The list.
     * @return The first element. {@code null} when the list is empty.
     */
    public static <T> T first(final List<T> list) {
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }
    
    /**
     * The last element in the list (i.e. a better notation for {@code list.get(list.size() - 1)}).
     * 
     * @param list The list.
     * @return The last element. {@code null} when the list is empty.
     */
    public static <T> T last(final List<T> list) {
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(maxIndex(list));
        }
    }
    
    /**
     * Alternate notation for {@code list.size() -1}.
     * 
     * @param list The list.
     * @return The maximum list element index.
     */
    public static int maxIndex(final List<?> list) {
        return list.size() - 1;
    }
    
    /**
     * Determines whether an element in the given list evaluates the given predicate to
     * {@code true}.
     * 
     * @param <T> The type of the list.
     * 
     * @param list a list.
     * @param predicate a predicate.
     * @return {@code true} when there is a matching element.
     */
    public static <T> boolean exists(final Collection<T> list, final Predicate<T> predicate) {
        return CollectionUtils.exists(list, predicate);
    }
    
    /**
     * Creates a modifiable list from the sequence of objects. The list is <i>not</i> backed
     * by the array (in contrast to {@code java.util.Arrays.asList()}). 
     * 
     * @param <T> The type of the objects.
     * @param objects The sequence of objects.
     * 
     * @return The list containing the objects.
     */
    @SafeVarargs
    public static <T> List<T> fromArray(final T... objects) {
        final List<T> result = new ArrayList<>();
        
        if (objects != null && objects.length > 0) {
            Collections.addAll(result, objects);
        }
        
        return result;
    }
    
    /**
     * Splits a list into a list of sublists with maximum size of chunk size.
     * 
     * @param list source list
     * @param chunkSize chunk size. Must be greater than {@code 0}.
     * 
     * @return a list of sublists
     */
    public static <T> List<List<T>> split(final List<T> list, final int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("Chunk size must be greater than 0.");
        }
        
        final List<List<T>> subLists = new ArrayList<>();
        
        if (!list.isEmpty()) {
            final int totalSize = list.size();
            int from = 0;
            
            while (true) {
                final int to = from + chunkSize;
                
                if (to >= totalSize) {
                    subLists.add(new ArrayList<>(list.subList(from, totalSize)));
                    break;
                } else {
                    subLists.add(new ArrayList<>(list.subList(from, to)));
                }
                
                from = to;
            }
        }
        
        return subLists;
    }
}
