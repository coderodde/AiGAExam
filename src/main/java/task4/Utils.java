package task4;

import java.util.List;

public final class Utils {
    
    /**
     * Returns the smaller of the two given values.
     * 
     * @param <E>    the value of type. Must be {@link java.lang.Comparable}.
     * @param value1 the first value.
     * @param value2 the second value.
     * @return the smaller of the two input values.
     */
    public static <E extends Comparable<? super E>> E min(E value1, E value2) {
        return value1.compareTo(value2) < 0 ? value1 : value2;
    }
}
