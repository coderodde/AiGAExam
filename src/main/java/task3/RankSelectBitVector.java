package task3;

public final class RankSelectBitVector {
    
    /**
     * Indicates whether some bits were changed since the previous building of
     * the index data structures.
     */
    private boolean hasDirtyState = true;
    
    /**
     * The actual bit storage array.
     */
    private final byte[] bytes;
    
    private int ell;
    private int k;
    private int[] first;
    private int[] second;
    private int[][] third;
    
    public RankSelectBitVector(int capacity) {
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
                second[i/k] = bruteForceRank(ell * (i / ell), i - 1);
            }
        }
        
        //// Deal with the 'third': four Russians' technique:
        this.third = new int[(int) Math.pow(2.0, k - 1)][];
        
        for (int i = 0; i < third.length; i++) {
            third[i] = new int[k - 1];
            third[i][0] = bitIsSet(i, k - 2) ? 1 : 0;
            
            for (int j = 1; j < k - 1; j++) {
                third[i][j] = third[i][j - 1] 
                            + (bitIsSet(i, k - 2 - j) ? 1 : 0);
            }
        }
    }
    
    /**
     * Returns the number of bits this bit vector supports.
     * 
     * @return the number of bits supported.
     */
    public int getNumberOfBits() {
        return bytes.length * Byte.SIZE;
    }
    
    /**
     * Sets the {@code index}th bit to one (1).
     * 
     * @param index the index of the target bit.
     */
    public void writeBitOn(int index) {
        writeBit(index, true);
    }
    
    /**
     * Sets the {@code index}th bit to zero (0).
     * 
     * @param index the index of the target bit.
     */
    public void writeBitOff(int index) {
        writeBit(index, false);
    }
    
    /**
     * Writes the {@code index}th bit to {@code on}.
     * 
     * @param index the index of the target bit.
     * @param on    the selector of the bit: if {@code true}, the bit will be 
     *              set to one, otherwise set zero.
     */
    public void writeBit(int index, boolean on) {
        checkBitIndex(index);
        writeBitImpl(index, on);
    }
    
    /**
     * The delegate for manipulating bits.
     * 
     * @param index the index of the target bit.
     * @param on    the flag deciding the value of the bit in question.
     */
    private void writeBitImpl(int index, boolean on) {
        boolean previousBitValue = readBit(index);
        
        if (on) {
            if (previousBitValue == false) {
                hasDirtyState = true;
            }
            
            turnBitOn(index);
        } else {
            if (previousBitValue == true) {
                hasDirtyState = true;
            }
            
            turnBitOff(index);
        }
    }
    
    /**
     * Returns the rank of {@code index}, i.e., the number of set bits in the 
     * subvector {@code vector[1..index]}. Runs in {@code O((log n)^2)} time.
     * 
     * @param index the target index.
     * @return the rank for the input target.
     */
    public int rankFirst(int index) {
        makeSureStateIsCompiled();
//        index++;
        
        int startIndex = ell * (index / ell);
        int endIndex = index - 1;
        
        int firstIndex = first[index / ell];
        return firstIndex + bruteForceRank(startIndex, endIndex);
    }
    
    /**
     * Returns the {@code index}th rank. Runs in {@code O(log n)} time.
     * 
     * @param index the target index.
     * @return the rank of the input index.
     */
    public int rankSecond(int index) {
        makeSureStateIsCompiled();
        
        int startIndex = k * (index / k);
        int endIndex = index - 1;
        
        return first[index / ell] +
               second[index / k] + 
               bruteForceRank(startIndex, 
                              endIndex);
    }
    
    /**
     * Returns the {@code index}th rank. Runs in {@code O(1)} time.
     * 
     * @param index the target index.
     * @return the rank of the input index.
     */
    public int rankThird(int index) {
        makeSureStateIsCompiled();
        index++;
        
        int selectorIndex = 
                extractBitVector(index)
                        .toInteger(k - 1);
        
        return first[index / ell] + 
               second[index / k] +
               third[selectorIndex][index % k - 1];
    }
    
    /**
     * Reads the {@code index}th bit where indexation starts from zero (0).
     * 
     * @param index the bit index.
     * @return {@code true} if and only if the {@code index}th bit is set.
     */
    public boolean readBit(int index) {
        checkBitIndex(index);
        return readBitImpl(index);
    }
    
    /**
     * Implements the actual reading of a bit.
     * 
     * @param index the index of the target bit to read.
     * @return the value of the target bit.
     */
    public boolean readBitImpl(int index) {
        int byteIndex = index / Byte.SIZE;
        int targetByteBitIndex = index % Byte.SIZE;
        byte targetByte = bytes[byteIndex];
        return (targetByte & (1 << targetByteBitIndex)) != 0;
    }
    
    /**
     * Makes sure that the state of the internal data structures is up to date.
     */
    private void makeSureStateIsCompiled() {
        if (hasDirtyState) {
            hasDirtyState = false;
            buildIndices();
        }
    }
    
    /**
     * Turns the {@code index}th bit on. Indexation is zero-based.
     * 
     * @param index the target bit index.
     */
    private void turnBitOn(int index) {
        int byteIndex = index / Byte.SIZE;
        int bitIndex = index % Byte.SIZE;
        byte mask = 1;
        mask <<= bitIndex;
        bytes[byteIndex] |= mask;
    }
    
    /**
     * Turns the {@code index}th bit off. Indexation is zero-based.
     * 
     * @param index the target bit index.
     */
    private void turnBitOff(int index) {
        int byteIndex = index / Byte.SIZE;
        int bitIndex = index % Byte.SIZE;
        byte mask = 1;
        mask <<= bitIndex;
        bytes[byteIndex] &= ~mask;
    }
    
    private RankSelectBitVector getCi(int i) {
        return extractBitVector(i);
    }
    
    private void checkBitIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(
                    String.format("Negative bit index: %d.", index));
        } 
        
        if (index >= getNumberOfBits()) {
            throw new IndexOutOfBoundsException(
                    String.format(
                            "Too large bit index (%d), number of bits " + 
                            "supported is %d.",
                            index, 
                            getNumberOfBits()));
        }
    }
    
    /**
     * Returns {@code true} if and only if the {@code bitIndex}th bit in 
     * {@code value} is set.
     * 
     * @param value    the value of which to inspect the bit.
     * @param bitIndex the bit index.
     * @return {@code true} if and only if the specified bit is set.
     */
    private boolean bitIsSet(int value, int bitIndex) {
        return (value & (1 << bitIndex)) != 0;
    }
    
    int toInteger(int numberOfBitsToRead) {
        int integer = 0;
        
        for (int i = 0; i < numberOfBitsToRead; i++) {
            
            boolean bit = readBit(i);
            
            if (bit == true) {
                integer |= 1 << i;
            }
        }
        
        return integer;
    }
    
    private RankSelectBitVector extractBitVector(int i) {
        int startIndex = k * (i / k) + 1;
        int endIndex = k * (i / k + 1) - 1;
        int extractedBitVectorLength = endIndex - startIndex + 1;
        RankSelectBitVector extractedBitVector = new RankSelectBitVector(extractedBitVectorLength);
        
        for (int index = 0, j = startIndex; j <= endIndex; j++, index++) {
            extractedBitVector.writeBit(index, this.readBit(j));
        }
        
        return extractedBitVector;
    }
    
    private int bruteForceRank(int startIndex, int endIndex) {
        int rank = 0; 
        
        for (int i = startIndex; i <= endIndex; i++) {
            if (readBitImpl(i)) {
                rank++;
            }
        }
        
        return rank;
    }
    
    private static double log2(double v) {
        return Math.log(v) / Math.log(2.0);
    }
}

