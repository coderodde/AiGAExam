package task3;

import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

public final class RankSelectBitVectorTest {
    
    @Test
    public void lastBitRank() {
        RankSelectBitVector bv = new RankSelectBitVector(8);
        
        bv.writeBitOn(2);
        bv.writeBitOn(6);
        bv.writeBitOn(7);
        
        assertEquals(3, bv.rankFirst(8));
        assertEquals(3, bv.rankSecond(8));
        assertEquals(3, bv.rankThird(8));
    }
    
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
        
        assertEquals(2, bv.selectFirst(1));
        assertEquals(4, bv.selectFirst(2));
        assertEquals(5, bv.selectFirst(3));
        assertEquals(7, bv.selectFirst(4));
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
        
        assertEquals(2, bv.selectFirst(1));
        assertEquals(4, bv.selectFirst(2));
        assertEquals(5, bv.selectFirst(3));
        assertEquals(7, bv.selectFirst(4));
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
        
        assertEquals(2, bv.selectFirst(1));
        assertEquals(4, bv.selectFirst(2));
        assertEquals(5, bv.selectFirst(3));
        assertEquals(7, bv.selectFirst(4));
        
        assertEquals(8, bv.selectFirst(5));
        assertEquals(10, bv.selectFirst(6));
        assertEquals(12, bv.selectFirst(7));
        assertEquals(13, bv.selectFirst(8));
        assertEquals(15, bv.selectFirst(9));
    }
    
    @Test
    public void debugTest3() {
        // 00101101 10101101 00010010
        RankSelectBitVector bv = new RankSelectBitVector(24);
        
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
        assertEquals(2, bv.selectFirst(1));
        assertEquals(4, bv.selectFirst(2));
        assertEquals(5, bv.selectFirst(3));
        assertEquals(7, bv.selectFirst(4));
        
        assertEquals(8, bv.selectFirst(5));
        assertEquals(10, bv.selectFirst(6));
        assertEquals(12, bv.selectFirst(7));
        assertEquals(13, bv.selectFirst(8));
        assertEquals(15, bv.selectFirst(9));
        
        assertEquals(19, bv.selectFirst(10));
        assertEquals(22, bv.selectFirst(11));
    }
    
    @Test
    public void bruteForceTest() {
        long seed = System.currentTimeMillis();
        seed = 1706163778488L;
        Random random = new Random(seed);
        System.out.println("-- bruteForceTest, seed = " + seed);
        
        RankSelectBitVector bv = getRandomBitVector(random);
        BruteForceBitVector referenceBv = copy(bv);
        
        bv.buildIndices();
       
        int numberOfOneBits = bv.rankThird(bv.getNumberOfSupportedBits());
        
        for (int i = 0; i < bv.getNumberOfSupportedBits(); i++) {
            int actualRank = referenceBv.rank(i);
            int rank1 = bv.rankFirst(i);
            int rank2 = bv.rankSecond(i);
            int rank3 = bv.rankThird(i);
            
            int selectIndex = random.nextInt(numberOfOneBits) + 1;
            int actualSelect = referenceBv.select(selectIndex);
            int select1 = bv.selectFirst(selectIndex);
            
            if (select1 != actualSelect) {
                System.out.printf(
                        "ERROR: i = %d, actualSelect = %d, select1 = %d.\n",
                        i,
                        actualSelect,
                        select1);
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
            
            assertEquals(actualRank, rank1);
            assertEquals(actualRank, rank2);
            assertEquals(actualRank, rank3);
            assertEquals(actualSelect, select1);
        }
    }
    
    private static RankSelectBitVector getRandomBitVector(Random random) {
        RankSelectBitVector bv = new RankSelectBitVector(5973);
        
        for (int i = 0; i < bv.getNumberOfSupportedBits(); i++) {
            if (random.nextDouble() < 0.3) {
                bv.writeBitOn(i);
            }
        }
        
        return bv;
    }
    
    private static BruteForceBitVector copy(RankSelectBitVector bv) {
        BruteForceBitVector referenceBv = 
                new BruteForceBitVector(bv.getNumberOfSupportedBits());
        
        for (int i = 0; i < bv.getNumberOfSupportedBits(); i++) {
            if (bv.readBit(i)) {
                referenceBv.writeBitOn(i);
            }
        }
        
        return referenceBv;
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
    
    @Test
    public void debugFindSmallestFailingBitVector() {
        int startIndex = (int) Math.pow(2.0, 25.0) - 13;
        
        for (int len = startIndex; len < startIndex + 1; len++) {
            System.out.println(len);
            RankSelectBitVector bv = new RankSelectBitVector(len);
            int numberOfBits = bv.getNumberOfSupportedBits();
            
            int rank1 = bv.rankFirst(numberOfBits);
            int rank3 = bv.rankThird(numberOfBits);
            
            if (rank1 != rank3) {
                System.out.printf(
                        "ERROR: " + 
                        "Disagreed at len = %d, rank1 = %d, rank3 = %d.\n",
                        len,
                        rank1,
                        rank3);
                
                fail("Should not get here.");
            }
        }
    }
    
    @Test
    public void countSetBits() {
        RankSelectBitVector bv = new RankSelectBitVector(11);
        
        assertEquals(0, bv.getNumberOfSetBits());
        
        bv.writeBitOn(10);
        
        assertEquals(1, bv.getNumberOfSetBits());
        
        bv.writeBitOn(5);
        
        assertEquals(2, bv.getNumberOfSetBits());
        
        bv.writeBitOff(10);
        
        assertEquals(1, bv.getNumberOfSetBits());
        
        bv.writeBitOff(5);
        
        assertEquals(0, bv.getNumberOfSetBits());
    }
}
