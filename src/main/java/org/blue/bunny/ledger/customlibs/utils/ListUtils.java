package org.blue.bunny.ledger.customlibs.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Utilities to operate on lists.
 */
public final class ListUtils {
    
    private ListUtils() {
    }
    
    /**
     * Sorts the given list with the specified sort algorithm.
     * 
     * @param <T> the list-entries datatype
     * @param toSort the list
     * @param predicate2 the sort algorithm
     * @return the sorted list
     */
    public static <T> List<T> sort(List<T> toSort, final BiFunction<T, T, Integer> sortFunction) {
        Collections.sort(toSort, new Comparator<T>() {
            public int compare(T o1, T o2) {
                return sortFunction.apply(o1, o2);
            }
        });
        return toSort;
    }
    
    /**
     * Maps a source list to a target list.
     * 
     * @param <S> source type
     * @param <T> target type
     * @param source source list
     * @param functor functor
     * @return target list
     */
    public static <S, T> List<T> map(Collection<S> source, Function<S, T> functor) {
        
        return (List<T>) CollectionUtils.map(source, new ArrayList<T>(), functor);
    }
    
    /**
     * Filters a source list according to a predicate.
     * 
     * @param <T> type parameter
     * @param source source list
     * @param predicate predicate
     * @return filtered list
     */
    public static <T> List<T> filter(Collection<T> source, Predicate<T> predicate) {
        return (List<T>) CollectionUtils.filter(source, new ArrayList<T>(), predicate);
    }
    
    /**
     * Returns a list of elements from the given list that evaluate the given predicate to
     * <code>true</code>.
     * 
     * @param <T> The type of the list.
     * 
     * @param list a list.
     * @param predicate a predicate.
     * @return a list of elements from the given list that evaluate the given predicate to
     *         <code>true</code>.
     */
    public static <T> List<T> select(Collection<T> list, Predicate<T> predicate) {
        ArrayList<T> result = new ArrayList<T>();
        CollectionUtils.select(list, predicate, result);
        return result;
    }
    
    /**
     * Returns the first element from the given list that evaluates the given predicate to
     * <code>true</code>.
     * 
     * @param <T> The type of the list.
     * 
     * @param list a list.
     * @param predicate a predicate.
     * @return the first element from the given list that evaluates the given predicate to
     *         <code>true</code> or <code>null</code> if no element evaluates the given predicate to
     *         <code>true</code>.
     */
    public static <T> T selectFirst(Collection<T> list, Predicate<T> predicate) {
        return CollectionUtils.selectFirst(list, predicate);
    }
    
    /**
     * Determines whether an element in the given list evaluates the given predicate to
     * <code>true</code>.
     * 
     * @param <T> The type of the list.
     * 
     * @param list a list.
     * @param predicate a predicate.
     * @return <code>true</code> if an element in the given list evaluates the given predicate to
     *         <code>true</code>, <code>false</code> otherwise.
     */
    public static <T> boolean exists(Collection<T> list, Predicate<T> predicate) {
        return CollectionUtils.exists(list, predicate);
    }
    
    /**
     * Creates a list from the sequence of objects.
     * 
     * @param <T> The type of the objects.
     * @param objects The sequence of objects.
     * 
     * @return The list containing the objects.
     */
    @SafeVarargs
    public static <T> List<T> fromArray(T... objects) {
        List<T> result = new ArrayList<T>();
        if (objects != null && objects.length > 0) {
            Collections.addAll(result, objects);
        }
        return result;
    }
    
    /**
     * Splits a list into a list of sublists with maximum size of chuck size.
     * 
     * @param <T> type parameter
     * @param list source list
     * @param chunkSize chunk size
     * @return a list of sublists
     */
    public static <T> List<List<T>> split(List<T> list, int chunkSize) {
        List<List<T>> subLists = new ArrayList<List<T>>();
        int totalSize = list.size();
        int from = 0;
        while (true) {
            int to = from + chunkSize;
            if (to >= totalSize) {
                subLists.add(list.subList(from, totalSize));
                break;
            } else {
                subLists.add(list.subList(from, to));
            }
            from = to;
        }
        return subLists;
    }
}
