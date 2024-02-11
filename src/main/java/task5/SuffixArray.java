package task5;

import java.util.Arrays;

/**
 * This class specifies the suffix array.
 */
public final class SuffixArray {
    
    private final int[] suffixArray;
    
    public SuffixArray(int length) {
        this.suffixArray = new int[length];
    }
    
    @Override
    public boolean equals(Object o) {
        return Arrays.equals(suffixArray, ((SuffixArray) o).suffixArray);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("[");
        
        boolean first = true;
        
        for (int suffixIndex : suffixArray) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            
            sb.append(suffixIndex);
        }
        
        return sb.append("]").toString();
    }
    
    public int get(int index) {
        return suffixArray[index];
    }
    
    void set(int suffixArrayIndex, int suffixIndex) {
        suffixArray[suffixArrayIndex] = suffixIndex;
    }
}
