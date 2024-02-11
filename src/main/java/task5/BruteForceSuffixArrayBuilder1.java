package task5;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class BruteForceSuffixArrayBuilder1 implements SuffixArrayBuilder {

    @Override
    public SuffixArray buildSuffixArray(String text) {
        String[] suffixes = buildSuffixes(text);
        Map<String, Integer> indexMap = buildIndexMap(suffixes);
        Arrays.sort(suffixes);
        return buildSuffixArray(suffixes, indexMap);
    }
    
    private static SuffixArray buildSuffixArray(
            String[] suffixes,
            Map<String, Integer> indexMap) {
        SuffixArray suffixArray = new SuffixArray(suffixes.length);
        int entryIndex = 0;
        
        for (String suffix : suffixes) {
            int suffixArrayIndex = indexMap.get(suffix);
            suffixArray.set(entryIndex++, suffixArrayIndex);
        }
        
        return suffixArray;
    }
    
    private static Map<String, Integer> buildIndexMap(String[] suffixes) {
        Map<String, Integer> indexMap = new HashMap<>(suffixes.length);
        
        for (int i = 0; i < suffixes.length; i++) {
            indexMap.put(suffixes[i], i);
        }
        
        return indexMap;
    }
    
    private static String[] buildSuffixes(String text) {
        String[] suffixes = new String[text.length()];
        
        for (int i = 0; i < suffixes.length; i++) {
            suffixes[i] = text.substring(i);
        }
        
        return suffixes;
    }
}
