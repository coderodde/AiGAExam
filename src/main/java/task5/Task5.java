package task5;

import java.util.Random;

public final class Task5 {
    
    private static final int TEXT_LENGTH = 5000;
    
    public static void main(String[] args) {
        Random random = new Random(13L);
        String text = computeRandomString(TEXT_LENGTH, random);
        
        benchmark(new BruteForceSuffixArrayBuilder1(), text);
        benchmark(new BruteForceSuffixArrayBuilder2(), text);
    }
    
    private static SuffixArray benchmark(SuffixArrayBuilder suffixArrayBuilder, 
                                  String text) {
        long start = System.currentTimeMillis();
        SuffixArray suffixArray = suffixArrayBuilder.buildSuffixArray(text);
        long end = System.currentTimeMillis();
        
        System.out.printf("%s in %d milliseconds.\n", 
                          suffixArrayBuilder.getClass().getSimpleName(),
                          end - start);
        
        return suffixArray;
    }
    
    private static String computeRandomString(int length, Random random) {
        StringBuilder sb = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            char ch = (char)('a' + random.nextInt(26));
            sb.append(ch);
        }
        
        return sb.toString();
    }
}
