package task3;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public final class BitVectorTest {

    private BitVector bitVector;
    
    @Before 
    public void before() {
        bitVector = new BitVector();
    }
    
    @Test
    public void testGetNumberOfBits() {
        assertEquals(8, bitVector.getNumberOfBits());
        bitVector.writeBit(8, false);
        assertEquals(16, bitVector.getNumberOfBits());
        bitVector.writeBit(23, true);
        assertEquals(32, bitVector.getNumberOfBits());
    }
    
    @Test
    public void readWriteBit() {
        bitVector.writeBit(12, true);
        assertTrue(bitVector.read(12));
        bitVector.writeBit(12, false);
        assertFalse(bitVector.read(12));
        assertFalse(bitVector.read(13));
    }
}
