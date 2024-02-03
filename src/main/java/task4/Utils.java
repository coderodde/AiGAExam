package task4;

public final class Utils {
    
    /**
     * Returns the smaller of the two given values.
     * 
     * @param <V>    the value of type. Must be {@link java.lang.Comparable}.
     * @param value1 the first value.
     * @param value2 the second value.
     * @return the smaller of the two input values.
     */
    public static <V extends Comparable<? super V>> V min(V value1, V value2) {
        return value1.compareTo(value2) < 0 ? value1 : value2;
    }
}
