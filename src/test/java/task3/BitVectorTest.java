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
    public void rank() {
        bitVector.writeBit(2, true);
        bitVector.writeBit(3, true);
        bitVector.writeBit(5, true);
        bitVector.writeBit(11, true);
        
        bitVector.buildIndices();
        
        assertEquals(0, bitVector.rank(0));
        assertEquals(0, bitVector.rank(1));
        assertEquals(1, bitVector.rank(2));
        assertEquals(2, bitVector.rank(3));
        assertEquals(2, bitVector.rank(4));
        assertEquals(3, bitVector.rank(5));
        assertEquals(3, bitVector.rank(6));
        assertEquals(3, bitVector.rank(7));
        assertEquals(3, bitVector.rank(8));
        assertEquals(3, bitVector.rank(9));
        assertEquals(3, bitVector.rank(10));
        assertEquals(4, bitVector.rank(11));
        assertEquals(4, bitVector.rank(12));
        assertEquals(4, bitVector.rank(13));
        
    }
}
