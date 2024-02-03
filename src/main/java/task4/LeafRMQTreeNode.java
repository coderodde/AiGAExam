package task4;

import java.util.Objects;

public final class LeafRMQTreeNode<N extends AbstractRMQTreeNode<N, K, V>, 
                                   K extends Comparable<? super K>, 
                                   V extends Comparable<? super V>>
extends AbstractRMQTreeNode<N, K, 
                            V> {
    
    @Override
    public String toString() {
        return String.format("[LEAF: key = \"%s\", value = \"%s\"]",
                             Objects.toString(key),
                             Objects.toString(value));
    }
}
