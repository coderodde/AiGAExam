package task5;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class LinearithmicSuffixArrayBuilder 
        implements SuffixArrayBuilder {

    @Override
    public SuffixArray buildSuffixArray(String text) {
        SuffixArray suffixArray = new SuffixArray(text);
        
        StringFactor[] stringFactors = new StringFactor[text.length()];
        
        for (int i = 0; i < stringFactors.length; i++) {
            stringFactors[i] = new StringFactor(text, i, 1);
        }
        
        Arrays.sort(stringFactors);
        
        for (int ell = 2; ell < text.length(); ell *= 2) {
            sort(stringFactors, ell);
        }
        
        return suffixArray;
    }
    
    private static void sort(StringFactor[] stringFactors, int ell) {
        Map<StringFactor, Integer> comparisonMap = new HashMap<>();
        
        for (int i = 0; i < stringFactors.length; i++) {
            StringFactor sfi = stringFactors[i];
            Map<Integer, Integer> counterMap = new HashMap<>();
            
            for (int j = 0; j < stringFactors.length; j++) {
                StringFactor sfj = stringFactors[j];
                
                if (sfj.compareTo(sfi) < 0) {
                    counterMap.put(i, counterMap.getOrDefault(i, 0) + 1);
                }
            }
            
            comparisonMap.put(sfi, counterMap.get(i));
        }
        
            
    }
}
