package task3;

import java.util.ArrayList;
import java.util.List;

public final class BitVector {
    
    private boolean hasDirtyState = true;
    private byte[] bytes;
    private int ell;
    private int[] first;
    
    public BitVector(int capacity) {
        capacity++;
        
        bytes = new byte[capacity / 
                         Byte.SIZE + (capacity % Byte.SIZE != 0 ? 1 : 0)];
    }
    
    public void buildIndices() {
        //// Deal with the 'first'.
        // n - total number of bit slots:
        int n = getNumberOfBits();
        // l - the l value:
        this.ell = (int) Math.pow(Math.ceil(log2(n) / 2.0), 2.0);
        this.first = new int[n / ell + 1];
        
        for (int i = 0; i < first.length; i++) {
            first[i] = bruteForceRank(i * ell);
        }
    }
    
    public int getNumberOfBits() {
        return bytes.length * Byte.SIZE;
    }
    
    public void writeBit(int index, boolean on) {
        if (on) {
            turnBitOn(index);
        } else {
            turnBitOff(index);
        }
        
        hasDirtyState = true;
    }
    
    public boolean read(int index) {
        int byteIndex = index / Byte.SIZE;
        int targetByteBitIndex = index % Byte.SIZE;
        
        byte targetByte = bytes[byteIndex];
        
        return (targetByte & (1 << targetByteBitIndex)) != 0;
    }
    
    public int rank(int index) {
        checkDirtyState();
        int startIndex = ell * (index / ell) + 1;
        int endIndex = index;
        return first[index / ell] + count(startIndex, endIndex);
    }
    
    private void checkDirtyState() {
        if (hasDirtyState) {
            hasDirtyState = false;
            buildIndices();
        }
    }
    
    private void turnBitOn(int index) {
        int byteIndex = index / Byte.SIZE;
        int bitIndex = index % Byte.SIZE;
        byte flag = 1;
        flag <<= bitIndex;
        bytes[byteIndex] |= flag;
    }
    
    private void turnBitOff(int index) {
        int byteIndex = index / Byte.SIZE;
        int bitIndex = index % Byte.SIZE;
        byte flag = 1;
        flag <<= bitIndex;
        flag = (byte)(~flag);
        bytes[byteIndex] &= flag;
    }
    
    /**
     * Converts the user zero-based index to the internal index that is one-
     * based.
     * 
     * @param index the index to convert.
     * @return the internal index.
     */
    private int convertIndexToInternal(int index) {
        return index + 1;
    }
    
    private int bruteForceRank(int index) {
        int rank = 0;
        
        for (int i = 0; i < index; i++) {
            if (read(i)) {
                rank++;
            }
        }
        
        return rank;
    }
    
    private int count(int startIndex, int endIndex) {
        int c = 0;
        
        for (int i = startIndex; i <= endIndex; i++) {
            if (read(i)) {
                c++;
            }
        }
        
        return c;
    }
    
    private static double log2(double v) {
        return Math.log(v) / Math.log(2.0);
    }
}

