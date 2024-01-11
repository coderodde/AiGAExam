package task1;

import util.PAM250CostMatrix;

public final class MultipleSequenceAlignmentInstance {
    
    private static final char GAP_CHARACTER = '-';
    
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
    
    public int getWeight(LatticeNode tail, LatticeNode head) {
        // Extract the column represented by taking a single hop from 'tail' to
        // 'head':
        int[] tailCoordinates = tail.getCoordinates();
        int[] headCoordinates = head.getCoordinates();

        for (int i = 0; i < sequenceArray.length; ++i) {
            if (tailCoordinates[i] + 1 == headCoordinates[i]) {
                column[i] = sequenceArray[i].charAt(tailCoordinates[i]);
            } else {
                column[i] = GAP_CHARACTER;
            }
        }

        // Compute the hop cost as the sum of pairwise hops in any plane:
        int cost = 0;

        for (int i = 0; i < column.length; ++i) {
            char character1 = column[i];

            for (int j = i + 1; j < column.length; ++j) {
                char character2 = column[j];

                if (character1 != GAP_CHARACTER) {
                    if (character2 != GAP_CHARACTER) {
                        cost += costMatrix.getCost(character1, character2);
                    } else {
                        cost += gapPenalty;
                    }
                } else {
                    // character1 IS the gap character:
                    if (character2 != GAP_CHARACTER) {
                        cost += gapPenalty;
                    } else {
                        // Do nothing since we have a pair (gap, gap).
                    }
                }
            }
        }

        return cost;
    }
}
