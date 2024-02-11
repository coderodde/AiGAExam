package task5;

public final class Task5 {
    
    public static void main(String[] args) {
        BruteForceSuffixArrayBuilder1 builder1 =
                new BruteForceSuffixArrayBuilder1();
        
        SuffixArray suffixArray1 = builder1.buildSuffixArray("dabbb");
        
        System.out.println(suffixArray1);
    }
}
