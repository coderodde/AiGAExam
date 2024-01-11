package task1;

public final class LatticeNodeParentsNodeExpander 
        implements NodeExpander<LatticeNode> {

    @Override
    public LatticeNode[] expand(LatticeNode node) {
        return node.getParents();
    }
}
