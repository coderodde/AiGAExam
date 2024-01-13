package task1;

public final class LatticeWeightFunction 
        implements IntWeightFunction<LatticeNode> {
    
    private final MultipleSequenceAlignmentInstance instance;
    
    public LatticeWeightFunction(MultipleSequenceAlignmentInstance instance) {
        this.instance = instance;
    }
    
    @Override
    public int getWeight(LatticeNode tail, LatticeNode head) {
        int weight = instance.getWeight(tail, head);
        return Math.max(0, weight);
    }
}
