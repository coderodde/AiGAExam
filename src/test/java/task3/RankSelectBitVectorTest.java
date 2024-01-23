package task3;

import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

public final class RankSelectBitVectorTest {
    
    @Test
    public void bruteForceTest() {
        long seed = System.currentTimeMillis();
        seed = 1705906498693L;
        Random random = new Random(seed);
        System.out.println("-- bruteForceTest, seed = " + seed);
        
        RankSelectBitVector bv = getRandomBitVector(random);
        BruteForceBitVector referenceBv = copy(bv);
        
        bv.buildIndices();
        
        int matches = 0;
        int errors = 0;
        
        for (int i = 0; i < bv.getNumberOfBits(); i++) {
            int actualRank = referenceBv.rank(i);
            int rank1 = bv.rankFirst(i);
            int rank2 = bv.rankSecond(i);
            int rank3 = bv.rankThird(i);

            if (rank3 != actualRank) {
                System.out.printf(
                        "ERROR: i = %d, actual rank = %d, rank1 = %d, " + 
                        "rank2 = %d, rank3 = %d.\n",
                                  i,
                                  actualRank,
                                  rank1,
                                  rank2,
                                  rank3);
            }
            
            if (rank3 == actualRank) {
                matches++;
            } else {
                errors++;
            }
            
            assertEquals(actualRank, rank1);
            assertEquals(actualRank, rank2);
//            assertEquals(actualRank, rank3);
        }
        
        System.out.printf("Matches: %d.\n", matches);
        System.out.printf("Errors:  %d.\n", errors);
        
        System.out.println("");
    }
    
    private static RankSelectBitVector getRandomBitVector(Random random) {
        RankSelectBitVector bv = new RankSelectBitVector(100);
        
        for (int i = 0; i < bv.getNumberOfBits(); i++) {
            if (random.nextDouble() < 0.3) {
                bv.writeBitOn(i);
            }
        }
        
        return bv;
    }
    
    private static BruteForceBitVector copy(RankSelectBitVector bv) {
        BruteForceBitVector referenceBv = new BruteForceBitVector(100);
        
        for (int i = 0; i < bv.getNumberOfBits(); i++) {
            if (bv.readBit(i)) {
                referenceBv.writeBitOn(i);
            }
        }
        
        return referenceBv;
    }
    
    @Test
    public void toInteger() {
        RankSelectBitVector bitVector = new RankSelectBitVector(31);
        assertEquals(0, bitVector.toInteger(20));
        
        bitVector.writeBit(1, true);
        assertEquals(2, bitVector.toInteger(20));
        
        bitVector.writeBit(2, true);
        assertEquals(6, bitVector.toInteger(20));
        
        bitVector.writeBit(4, true);
        assertEquals(22, bitVector.toInteger(20));
    }
    
    @Test
    public void readWriteBit() {
        RankSelectBitVector bitVector = new RankSelectBitVector(30);
        bitVector.writeBit(12, true);
        assertTrue(bitVector.readBit(12));
        bitVector.writeBit(12, false);
        assertFalse(bitVector.readBit(12));
        assertFalse(bitVector.readBit(13));
    }
}
