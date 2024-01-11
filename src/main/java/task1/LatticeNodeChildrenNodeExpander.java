package task1;

public final class LatticeNodeChildrenNodeExpander implements NodeExpander<LatticeNode> {

    @Override
    public LatticeNode[] expand(LatticeNode node) {
        return node.getChildren();
    }
}
