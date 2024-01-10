package task1;

import util.PAM250CostMatrix;

public final class MultipleSequenceAlignmentInstance {
    
    // The penalty for an amino acid and a gap that appear in the same column.
    private final int gapPenalty;
    
    private final String[] sequenceArray;
    
    // Gonna allocate once to speed up computation.
    private final char[] column;
    
    private final PAM250CostMatrix costMatrix;
    
    public MultipleSequenceAlignmentInstance(PAM250CostMatrix costMatrix, 
                                             int gapPenalty,
                                             String... sequences) {
        this.costMatrix = costMatrix;
        this.gapPenalty = gapPenalty;
        this.sequenceArray = sequences.clone();
        this.column = new char[sequences.length];
        
        for (int i = 0; i < sequenceArray.length; i++) {
            this.sequenceArray[i] = sequences[i];
        }
    }
        
    public LatticeNode getSourceNode() {
        int[] sourceCoordinates = new int[sequenceArray.length];
        return new LatticeNode(this, sourceCoordinates);
    }

    public LatticeNode getTargetNode() {
        int[] targetCoordinates = new int[sequenceArray.length];

        for (int i = 0; i != sequenceArray.length; i++) {
            targetCoordinates[i] = sequenceArray[i].length();
        }

        return new LatticeNode(this, targetCoordinates);
    }

    public String[] getSequenceArray() {
        return sequenceArray;
    }
}
