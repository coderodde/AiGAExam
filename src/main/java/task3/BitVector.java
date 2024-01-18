package task3;

public final class BitVector {
    
    private byte[] bytes;
    
    public BitVector() {
        bytes = new byte[1];
    }
    
    public int getNumberOfBits() {
        return bytes.length * Byte.SIZE;
    }
    
    public void writeBit(int index, boolean on) {
        expandByteArrayIfNeeded(index);
        
        if (on) {
            turnBitOn(index);
        } else {
            turnBitOff(index);
        }
    }
    
    public boolean read(int index) {
        expandByteArrayIfNeeded(index);
        
        int byteIndex = index / Byte.SIZE;
        int targetByteBitIndex = index % Byte.SIZE;
        
        byte targetByte = bytes[byteIndex];
        
        return (targetByte & (1 << targetByteBitIndex)) != 0;
    }
    
    private void expandByteArrayIfNeeded(int index) {
        int currentCapacity = bytes.length * Byte.SIZE;
        
        while (currentCapacity <= index) {
            currentCapacity *= 2;
        }
        
        byte[] newBytes = new byte[currentCapacity / Byte.SIZE];
        
        System.arraycopy(
                bytes,
                0,
                newBytes, 
                0, 
                bytes.length);
        
        bytes = newBytes;
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
}

