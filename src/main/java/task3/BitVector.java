package task3;

public final class BitVector {
    
    private static final int MAXIMUM_NUMBER_OF_BITS = 31;
    
    private boolean hasDirtyState = true;
    private final byte[] bytes;
    private int ell;
    private int k;
    private int[] first;
    private int[] second;
    private int[][] third;
    
    public BitVector(int capacity) {
        capacity++;
        
        bytes = new byte[
                 capacity / Byte.SIZE + 
                (capacity % Byte.SIZE != 0 
                ? 1 
                : 0)
        ];
    }
    
    public void buildIndices() {
        //// Deal with the 'first'.
        // n - total number of bit slots:
        int n = getNumberOfBits();
        
        // elll - the l value:
        this.ell = (int) Math.pow(Math.ceil(log2(n) / 2.0), 2.0);
        this.first = new int[n / ell + 1];
        
        for (int i = ell; i < n; i++) {
            if (i % ell == 0) {
                int firstArraySlotIndex = i / ell;
                int startIndex = i - ell;
                int endIndex   = i - 1;
                
                first[firstArraySlotIndex]     =
                first[firstArraySlotIndex - 1] + 
                bruteForceRank(startIndex,
                               endIndex);
            }
        }
        
        //// Deal with the 'second'.
        this.k = (int) Math.ceil(log2(n) / 2.0);
        this.second = new int[n / k + 1];
        
        for (int i = k; i < n; i++) {
            if (i % k == 0) {
                second[i/k] = bruteForceRank(ell * (i / ell) + 1, i);
            }
        }
        
        //// Deal with the 'third': four Russians technique:
        this.third = new int[(int) Math.pow(2.0, k)][];
        
        for (int i = 0; i < third.length; i++) {
            third[i] = new int[k - 1];
        }
        
        
    }
    
    public int getNumberOfBits() {
        return bytes.length * Byte.SIZE;
    }
    
    public void writeBit(int index, boolean on) {
        index = convertIndexToInternal(index);
        
        if (on) {
            turnBitOn(index);
        } else {
            turnBitOff(index);
        }
        
        hasDirtyState = true;
    }
    
    public boolean readBit(int index) {
        return readBitImpl(convertIndexToInternal(index));
    }
    
    public int rankFirst(int index) {
        checkDirtyState();
        
        int startIndex = ell * (index / ell) + 1;
        int endIndex = index;
        
        int firstIndex = first[index / ell];
        return firstIndex + count(startIndex, endIndex);
    }
    
    public int rankSecond(int index) {
        checkDirtyState();
        
        int startIndex = k * (index / k) + 1;
        int endIndex = index;
        
        return first[index / ell] + second[index / k] + count(startIndex, 
                                                              endIndex);
    }
    
    private boolean readBitImpl(int index) {
        int byteIndex = index / Byte.SIZE;
        int targetByteBitIndex = index % Byte.SIZE;
        byte targetByte = bytes[byteIndex];
        
        return (targetByte & (1 << targetByteBitIndex)) != 0;
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
    
    int toInteger() {
        int integer = 0;
        
        for (int i = 0, n = Math.min(MAXIMUM_NUMBER_OF_BITS, getNumberOfBits());
                i < n; 
                i++) {
            
            boolean bit = readBit(i);
            
            if (bit == true) {
                integer |= (1 << (i - 1));
            }
        }
        
        return integer;
    }
    
    private BitVector extractBitVector(int i, int k) {
        int startIndex = k * (i / k) + 1;
        int endIndex = k * (i / k + 1) - 1;
        int extractedBitVectorLength = endIndex - startIndex;
        BitVector extractedBitVector = new BitVector(extractedBitVectorLength);
        
        for (int index = 0, j = startIndex; j <= endIndex; j++, index++) {
            extractedBitVector.writeBit(index, this.readBit(j));
        }
        
        return extractedBitVector;
    }
    
    private int bruteForceRank(int startIndex, int endIndex) {
        int rank = 0; 
        
        for (int i = startIndex; i <= endIndex; i++) {
            if (readBit(i)) {
                rank++;
            }
        }
        
        return rank;
    }
    
    private int count(int startIndex, int endIndex) {
        return bruteForceRank(startIndex, endIndex);
    }
    
    private static double log2(double v) {
        return Math.log(v) / Math.log(2.0);
    }
}

