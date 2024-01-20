package task3;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public final class BitVectorTest {

    private static BitVector bitVector = new BitVector(100);
    
    @BeforeClass
    public static void beforeClass() {
        // 01101000001
        bitVector.writeBit(2, true);
        bitVector.writeBit(3, true);
        bitVector.writeBit(5, true);
        bitVector.writeBit(11, true);
        bitVector.writeBit(20, true);
        bitVector.writeBit(21, true);
        bitVector.writeBit(23, true);
        bitVector.writeBit(50, true);
        bitVector.writeBit(70, true);
        bitVector.writeBit(99, true);
    }
    
    @Test
    public void readWriteBit() {
        BitVector bitVector = new BitVector(30);
        bitVector.writeBit(12, true);
        assertTrue(bitVector.read(12));
        bitVector.writeBit(12, false);
        assertFalse(bitVector.read(12));
        assertFalse(bitVector.read(13));
    }
    
    @Test
    public void rankFirst() {
        assertEquals(0, bitVector.rankFirst(1));
        assertEquals(1, bitVector.rankFirst(2));
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
    
    @Test
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
}
