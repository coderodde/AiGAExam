package task5;

import java.util.Arrays;

/**
 * This class specifies the suffix array.
 */
public final class SuffixArray {
    
    private final String text;
    private final int[] suffixArray;
    private final SuffixDescriptor[] suffixDescriptorArray;
    
    public SuffixArray(String text) {
        this.text = text;
        this.suffixArray = new int[text.length()];
        this.suffixDescriptorArray =
                new SuffixDescriptor[this.suffixArray.length];
        
        constructSuffixDescriptorArray();
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
    
    void constructSuffixDescriptorArray() {
        for (int len = text.length(), i = 0; len > 0; len--, i++) {
            suffixDescriptorArray[i] = new SuffixDescriptor(text, len);
        }
    }
    
    void sortByFirstCharacter() {
        Arrays.sort(suffixDescriptorArray);
    }
}
