package task4;

import java.util.Objects;

final class LeafRMQTreeNode<V> extends AbstractRMQTreeNode<V> {
    
    @Override
    public String toString() {
        return String.format("[LEAF: value = \"%s\"]",
                             Objects.toString(getValue()));
    }
}
