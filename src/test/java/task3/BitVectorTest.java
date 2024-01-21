package task3;

import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public final class BitVectorTest {

    private static BitVector bitVector = new BitVector(100);
    
    @BeforeClass
    public static void beforeClass() {
        bitVector.writeBit(1, true);
        bitVector.writeBit(2, true);
        bitVector.writeBit(4, true);
        bitVector.writeBit(10, true);
        bitVector.writeBit(19, true);
        bitVector.writeBit(20, true);
        bitVector.writeBit(22, true);
        bitVector.writeBit(49, true);
        bitVector.writeBit(69, true);
        bitVector.writeBit(98, true);
    }
    
    @Test
    public void bruteForceTest() {
        long seed = System.currentTimeMillis();
        seed = 1705844564957L;
        Random random = new Random(seed);
        System.out.println("-- bruteForceTest, seed = " + seed);
        
        BitVector bv = getRandomBitVector(random);
        BruteForceBitVector referenceBv = copy(bv);
        
        for (int i = 0; i < bv.getNumberOfBits(); i++) {
            System.out.println("he = " + i);
            int actualRank = referenceBv.rank(i);
            
            int rank1 = bv.rankFirst(i);
            int rank2 = bv.rankSecond(i);
            int rank3 = bv.rankThird(i);
            
            assertEquals(actualRank, rank1);
            assertEquals(actualRank, rank2);
            assertEquals(actualRank, rank3);
        }
    }
    
    private static BitVector getRandomBitVector(Random random) {
        BitVector bv = new BitVector(100);
        
        for (int i = 0; i < bv.getNumberOfBits(); i++) {
            if (random.nextDouble() < 0.3) {
                bv.writeBitOn(i);
            }
        }
        
        return bv;
    }
    
    private static BruteForceBitVector copy(BitVector bv) {
        BruteForceBitVector referenceBv = new BruteForceBitVector(100);
        
        for (int i = 0; i < bv.getNumberOfBits(); i++) {
            if (bv.readBit(i)) {
                referenceBv.writeBitOn(i);
            }
        }
        
        return referenceBv;
    }
    
//    @Test
    public void rankFirst() {
        assertEquals(1, bitVector.rankFirst(1));
        assertEquals(2, bitVector.rankFirst(2));
        assertEquals(2, bitVector.rankFirst(3));
        assertEquals(2, bitVector.rankFirst(4));
        assertEquals(3, bitVector.rankFirst(5));
        assertEquals(3, bitVector.rankFirst(6));
        assertEquals(3, bitVector.rankFirst(7));
        assertEquals(3, bitVector.rankFirst(8));
        assertEquals(3, bitVector.rankFirst(9));
        assertEquals(3, bitVector.rankFirst(10));
        assertEquals(4, bitVector.rankFirst(11));
        assertEquals(4, bitVector.rankFirst(12));
        assertEquals(4, bitVector.rankFirst(13));
        assertEquals(4, bitVector.rankFirst(14));
        assertEquals(10, bitVector.rankFirst(99));
    }
    
//    @Test
    public void rankSecond() {
        assertEquals(0, bitVector.rankSecond(1));
        assertEquals(1, bitVector.rankSecond(2));
        assertEquals(2, bitVector.rankSecond(3));
        assertEquals(2, bitVector.rankSecond(4));
        assertEquals(3, bitVector.rankSecond(5));
        assertEquals(3, bitVector.rankSecond(6));
        assertEquals(3, bitVector.rankSecond(7));
        assertEquals(3, bitVector.rankSecond(8));
        assertEquals(3, bitVector.rankSecond(9));
        assertEquals(3, bitVector.rankSecond(10));
        assertEquals(4, bitVector.rankSecond(11));
        assertEquals(4, bitVector.rankSecond(12));
        assertEquals(4, bitVector.rankSecond(13));
        assertEquals(4, bitVector.rankSecond(14));
        assertEquals(10, bitVector.rankSecond(99));
    }
    
//    @Test
    public void debugRankThird() {
        BitVector bv = new BitVector(63);
        
        bv.writeBitOn(1);
        bv.writeBitOn(2);
        bv.writeBitOn(4);
        bv.writeBitOn(6);
        bv.writeBitOn(7);
        bv.writeBitOn(11);
        bv.writeBitOn(12);
        bv.writeBitOn(13);
        bv.writeBitOn(15);
        bv.writeBitOn(17);
        bv.writeBitOn(19);
        bv.writeBitOn(21);
        bv.writeBitOn(22);
        
        assertEquals(10, bv.rankThird(18));
    }
    
//    @Test
    public void rankThird() {
        assertEquals(0, bitVector.rankThird(1));
        assertEquals(1, bitVector.rankThird(2));
        assertEquals(2, bitVector.rankThird(3));
        assertEquals(2, bitVector.rankThird(4));
        assertEquals(3, bitVector.rankThird(5));
        assertEquals(3, bitVector.rankThird(6));
        assertEquals(3, bitVector.rankThird(7));
        assertEquals(3, bitVector.rankThird(8));
        assertEquals(3, bitVector.rankThird(9));
        assertEquals(3, bitVector.rankThird(10));
        assertEquals(4, bitVector.rankThird(11));
        assertEquals(4, bitVector.rankThird(12));
        assertEquals(4, bitVector.rankThird(13));
        assertEquals(4, bitVector.rankThird(14));
        assertEquals(10, bitVector.rankThird(99));
        
        System.out.println("done");
    }

    @Test
    public void toInteger() {
        BitVector bitVector = new BitVector(31);
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
        BitVector bitVector = new BitVector(30);
        bitVector.writeBit(12, true);
        assertTrue(bitVector.readBit(12));
        bitVector.writeBit(12, false);
        assertFalse(bitVector.readBit(12));
        assertFalse(bitVector.readBit(13));
    }
}
