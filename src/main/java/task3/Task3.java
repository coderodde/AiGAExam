package task3;

import java.util.Random;

public final class Task3 {
    
    /**
     * The number of bits in the bit vector.
     */
    private static final int BIT_VECTOR_LENGTH = 5973;
    
    public static void main(String[] args) {
        long seed = 1706101186687L; //Utils.parseSeed(args);
        
        System.out.println("-- Task 3 --");
        System.out.printf("Seed = %d\n", seed);
        Random random = new Random(seed);
        
        RankSelectBitVector rankSelectBitVector = createRandomBitVector(random);
        
        long st = System.currentTimeMillis(); // st - start time.
        rankSelectBitVector.buildIndices();
        
        System.out.printf("Preprocessed the bit vector in %d milliseconds.\n",
                          System.currentTimeMillis() - st);
        
        int numberOfBits = rankSelectBitVector.getNumberOfSupportedBits();
        
        int[] answers1 = new int[numberOfBits];
        int[] answers2 = new int[numberOfBits];
        int[] answers3 = new int[numberOfBits];
        
        st = System.currentTimeMillis(); // st - start time.
        
        for (int i = 0; i != numberOfBits; i++) {
            answers1[i] = rankSelectBitVector.rankFirst(i);
        }
        
        long answersDuration1 = System.currentTimeMillis() - st;
        
        System.out.println("rankFirst() measured.");
        
        st = System.currentTimeMillis();
        
        for (int i = 0; i != numberOfBits; i++) {
            answers2[i] = rankSelectBitVector.rankSecond(i);
        }
        
        long answersDuration2 = System.currentTimeMillis() - st;
        
        System.out.println("rankSecond() measured.");
        
        st = System.currentTimeMillis();
        
        for (int i = 0; i != numberOfBits; i++) {
            answers3[i] = rankSelectBitVector.rankThird(i);
        }
        
        long answersDuration3 = System.currentTimeMillis() - st;
        
        System.out.println("rankThird() measured.");
        
        if (!rankArraysEqual(answers1, answers2)) {
            System.err.println("Failed on rankFirst vs. rankSecond.");
            return;
        }
        
        if (!rankArraysEqual(answers1, answers3)) {
            System.err.println("Failed on rankFirst vs. rankThird.");
            return;
        }
        
        System.out.printf("rankFirst() : %d milliseconds.\n", answersDuration1);
        System.out.printf("rankSecond(): %d milliseconds.\n", answersDuration2);
        System.out.printf("rankThird() : %d milliseconds.\n", answersDuration3);
    }
    
    private static RankSelectBitVector createRandomBitVector(Random random) {
        RankSelectBitVector rankSelectBitVector =
                new RankSelectBitVector(BIT_VECTOR_LENGTH);
        
        for (int bitIndex = 0;
                bitIndex != rankSelectBitVector.getNumberOfSupportedBits(); 
                bitIndex++) {
            
            if (random.nextBoolean()) {
                rankSelectBitVector.writeBitOn(bitIndex);
            }
        }
        
        return rankSelectBitVector;
    }
    
    private static boolean rankArraysEqual(int[] rankArray1, int[] rankArray2) {
        if (rankArray1.length != rankArray2.length) {
            throw new IllegalArgumentException("Rank array length mismatch.");
        }
        
        int n = Math.max(rankArray1.length, rankArray2.length);
        
        for (int i = 0; i != n; i++) {
            int rank1 = rankArray1[i];
            int rank2 = rankArray2[i];
            
            if (rank1 != rank2) {
                System.err.printf(
                        "ERROR: Mismatch at index = %d, " + 
                        "rank1 = %d, rank2 = %d.\n",
                        i,
                        rank1,
                        rank2);
                
                return false;
            }
        }
        
        return true;
    }
}
