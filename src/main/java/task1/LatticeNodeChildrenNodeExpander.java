package task1;

import java.util.List;

public final class LatticeNodeChildrenNodeExpander implements NodeExpander<LatticeNode> {

    @Override
    public List<LatticeNode> expand(LatticeNode node) {
        return node.getChildren();
    }
}
