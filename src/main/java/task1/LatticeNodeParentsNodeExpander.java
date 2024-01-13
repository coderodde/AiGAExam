package task1;

import java.util.List;

public final class LatticeNodeParentsNodeExpander 
        implements NodeExpander<LatticeNode> {

    @Override
    public List<LatticeNode> expand(LatticeNode node) {
        return node.getParents();
    }
}
