package task3;

import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

public final class RankSelectBitVectorTest {
    
    @Test
    public void smallSelect() {
        RankSelectBitVector bv = new RankSelectBitVector(8);
        
        bv.writeBitOn(2);
        bv.writeBitOn(4);
        bv.writeBitOn(5);
        bv.writeBitOn(7);
        
        // 00101101
        // select(1) = 2
        // select(2) = 4
        // select(3) = 5
        // select(4) = 7
        
        assertEquals(2, bv.select(1));
        assertEquals(4, bv.select(2));
        assertEquals(5, bv.select(3));
        assertEquals(7, bv.select(4));
    }
    
    @Test
    public void debugTest1() {
        // 00101101
        RankSelectBitVector bv = new RankSelectBitVector(8);
        bv.writeBitOn(2);
        bv.writeBitOn(4);
        bv.writeBitOn(5);
        bv.writeBitOn(7);
        
        assertEquals(0, bv.rankThird(0));
        assertEquals(0, bv.rankThird(1));
        assertEquals(0, bv.rankThird(2));
        assertEquals(1, bv.rankThird(3));
        assertEquals(1, bv.rankThird(4));
        assertEquals(2, bv.rankThird(5));
        assertEquals(3, bv.rankThird(6));
        assertEquals(3, bv.rankThird(7));
        assertEquals(4, bv.rankThird(8));
        
        assertEquals(0, bv.select(0));
        assertEquals(2, bv.select(1));
        assertEquals(4, bv.select(2));
        assertEquals(5, bv.select(3));
        assertEquals(7, bv.select(4));
    }
    
    @Test
    public void debugTest2() {
        // 00101101 10101101
        RankSelectBitVector bv = new RankSelectBitVector(16);
        
        bv.writeBitOn(2);
        bv.writeBitOn(4);
        bv.writeBitOn(5);
        bv.writeBitOn(7);
        
        bv.writeBitOn(8);
        bv.writeBitOn(10);
        bv.writeBitOn(12);
        bv.writeBitOn(13);
        bv.writeBitOn(15);
        
        assertEquals(0, bv.rankThird(0));
        assertEquals(0, bv.rankThird(1));
        assertEquals(0, bv.rankThird(2));
        assertEquals(1, bv.rankThird(3));
        assertEquals(1, bv.rankThird(4));
        assertEquals(2, bv.rankThird(5));
        assertEquals(3, bv.rankThird(6));
        assertEquals(3, bv.rankThird(7));
        assertEquals(4, bv.rankThird(8));
        
        assertEquals(5, bv.rankThird(9));
        assertEquals(5, bv.rankThird(10));
        assertEquals(6, bv.rankThird(11));
        assertEquals(6, bv.rankThird(12));
        assertEquals(7, bv.rankThird(13));
        assertEquals(8, bv.rankThird(14));
        assertEquals(8, bv.rankThird(15));
        assertEquals(9, bv.rankThird(16));
        
        assertEquals(0, bv.select(0));
        assertEquals(2, bv.select(1));
        assertEquals(4, bv.select(2));
        assertEquals(5, bv.select(3));
        assertEquals(7, bv.select(4));
        
        assertEquals(8, bv.select(5));
        assertEquals(10, bv.select(6));
        assertEquals(12, bv.select(7));
        assertEquals(13, bv.select(8));
        assertEquals(15, bv.select(9));
    }
    
    @Test
    public void debugTest3() {
        // 00101101 10101101 00010010
        RankSelectBitVector bv = new RankSelectBitVector(17);
        
        bv.writeBitOn(2);
        bv.writeBitOn(4);
        bv.writeBitOn(5);
        bv.writeBitOn(7);
        
        bv.writeBitOn(8);
        bv.writeBitOn(10);
        bv.writeBitOn(12);
        bv.writeBitOn(13);
        bv.writeBitOn(15);
        
        bv.writeBitOn(19);
        bv.writeBitOn(22);
        
        assertEquals(0, bv.rankThird(0));
        assertEquals(0, bv.rankThird(1));
        assertEquals(0, bv.rankThird(2));
        assertEquals(1, bv.rankThird(3));
        assertEquals(1, bv.rankThird(4));
        assertEquals(2, bv.rankThird(5));
        assertEquals(3, bv.rankThird(6));
        assertEquals(3, bv.rankThird(7));
        assertEquals(4, bv.rankThird(8));
        
        assertEquals(5, bv.rankThird(9));
        assertEquals(5, bv.rankThird(10));
        assertEquals(6, bv.rankThird(11));
        assertEquals(6, bv.rankThird(12));
        assertEquals(7, bv.rankThird(13));
        assertEquals(8, bv.rankThird(14));
        assertEquals(8, bv.rankThird(15));
        assertEquals(9, bv.rankThird(16));
        
        // 00010010
        assertEquals(9, bv.rankThird(17));
        assertEquals(9, bv.rankThird(18));
        assertEquals(9, bv.rankThird(19));
        assertEquals(10, bv.rankThird(20));
        assertEquals(10, bv.rankThird(21));
        assertEquals(10, bv.rankThird(22));
        assertEquals(11, bv.rankThird(23));
        assertEquals(11, bv.rankThird(24));
        
        // select():
        assertEquals(0, bv.select(0));
        assertEquals(2, bv.select(1));
        assertEquals(4, bv.select(2));
        assertEquals(5, bv.select(3));
        assertEquals(7, bv.select(4));
        
        assertEquals(8, bv.select(5));
        assertEquals(10, bv.select(6));
        assertEquals(12, bv.select(7));
        assertEquals(13, bv.select(8));
        assertEquals(15, bv.select(9));
        
        assertEquals(19, bv.select(10));
        assertEquals(22, bv.select(11));
    }
    
    @Test
    public void bruteForceTest() {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        System.out.println("-- bruteForceTest, seed = " + seed);
        
        RankSelectBitVector bv = getRandomBitVector(random);
        BruteForceBitVector referenceBv = copy(bv);
        
        bv.buildIndices();
        
        int matches = 0;
        int errors = 0;
        int numberOfOneBits = bv.rankThird(bv.getNumberOfBits());
        
        for (int i = 0; i < bv.getNumberOfBits(); i++) {
            int actualRank = referenceBv.rank(i);
            int rank1 = bv.rankFirst(i);
            int rank2 = bv.rankSecond(i);
            int rank3 = bv.rankThird(i);
            
            int selectIndex = random.nextInt(numberOfOneBits) + 1;
            int actualSelect = referenceBv.select(selectIndex);
            int select1 = bv.select(selectIndex);
            
            if (select1 != actualSelect) {
                System.out.printf("select1 = %d, actualSelect = %d.\n",
                                  select1,
                                  actualSelect);
            }

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
            assertEquals(actualRank, rank3);
            assertEquals(actualSelect, select1);
        }
        
        System.out.printf("Matches: %d.\n", matches);
        System.out.printf("Errors:  %d.\n", errors);
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
    
    @Test
    public void bruteForceBitVectorSelect() {
        BruteForceBitVector bv = new BruteForceBitVector(8);
        
        bv.writeBitOn(2);
        bv.writeBitOn(4);
        bv.writeBitOn(6);
        bv.writeBitOn(7);
        
        assertEquals(2, bv.select(1));
        assertEquals(4, bv.select(2));
        assertEquals(6, bv.select(3));
        assertEquals(7, bv.select(4));
    }
}
