package task3;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public final class BitVectorTest {

    private BitVector bitVector;
    
    @Before 
    public void before() {
        bitVector = new BitVector(100);
    }
    
    @Test
    public void readWriteBit() {
        bitVector.writeBit(12, true);
        assertTrue(bitVector.read(12));
        bitVector.writeBit(12, false);
        assertFalse(bitVector.read(12));
        assertFalse(bitVector.read(13));
    }
    
    @Test
    public void rankFirst() {
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
        
        bitVector.buildIndices();
        
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
        
        bitVector.writeBit(99, true);
        
        assertEquals(10, bitVector.rankFirst(99));
    }
}
