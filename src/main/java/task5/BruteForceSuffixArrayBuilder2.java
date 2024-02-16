package task5;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class BruteForceSuffixArrayBuilder2 implements SuffixArrayBuilder {

    @Override
    public SuffixArray buildSuffixArray(String text) {
        text += '\u0000';
        
        String[] cyclicShifts = buildCyclicShifts(text);
        Map<String, Integer> indexMap = buildIndexMap(cyclicShifts);
        Arrays.sort(cyclicShifts);
        return buildSuffixArray(text, cyclicShifts, indexMap);
    }
    
    private static SuffixArray buildSuffixArray(
            String text,
            String[] suffixes,
            Map<String, Integer> indexMap) {
        
        SuffixArray suffixArray = new SuffixArray(text);
        int entryIndex = 0;
        
        for (String suffix : suffixes) {
            int suffixArrayIndex = indexMap.get(suffix);
            suffixArray.set(entryIndex++, suffixArrayIndex);
        }
        
        return suffixArray;
    }
    
    private static Map<String, Integer> buildIndexMap(String[] cyclicShifts) {
        Map<String, Integer> indexMap = new HashMap<>(cyclicShifts.length);
        
        for (int i = 0; i < cyclicShifts.length; i++) {
            indexMap.put(cyclicShifts[i], i);
        }
        
        return indexMap;
    }
    
    private static String[] buildCyclicShifts(String text) {
        String[] result = new String[text.length()];
        
        for (int i = 0; i < text.length(); i++) {
            result[i] = text;
            text = doCyclicShift(text);
        }
        
        return result;
    }
    
    private static String doCyclicShift(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append(text.charAt(text.length() - 1));
        
        for (int i = 1; i < text.length(); i++) {
            sb.append(text.charAt(i));
        }
        
        return sb.toString();
    }
}
