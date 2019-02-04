package org.ormfux.common.utils.object;

/**
 * Utility for working with Objects.
 */
public final class Objects {
    
    
    private Objects() {}
    
    /**
     * Checks if the two objects are the same object (identity and not equality!). Use this method to explicitly state 
     * that you do this kind of object comparison on purpose and not by accident.
     * 
     * @param object1 This first object.
     * @param object2 The second object.
     * @return {@code object1 == object2}
     */
    public static boolean isSame(final Object object1, final Object object2) {
        return object1 == object2;
    }
    
    /**
     * Directly delegates to {@link java.util.Objects#equals(Object, Object)}.
     * 
     * @param object1 The first object.
     * @param object2 The second object.
     * @return {@code true} if the values are considered equal.
     */
    public static boolean equals(final Object object1, final Object object2) {
        return java.util.Objects.equals(object1, object2);
    }
    
}
