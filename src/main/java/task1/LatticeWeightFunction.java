package task1;

public final class LatticeWeightFunction 
        implements IntWeightFunction<LatticeNode> {
    
    private final MultipleSequenceAlignmentInstance instance;
    
    public LatticeWeightFunction(MultipleSequenceAlignmentInstance instance) {
        this.instance = instance;
    }
    
    @Override
    public int getWeight(LatticeNode tail, LatticeNode head) {
        return instance.getWeight(tail, head) + 17; // + 17: normalize to non-
                                                    // negative weights. Dumb,
                                                    // but no can do.
    }
}
