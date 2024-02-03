package task4;

import java.util.Objects;

public final class LeafRMQTreeNode<N extends AbstractRMQTreeNode<N, K, V>, K extends Comparable<? super K>, V> 
extends AbstractRMQTreeNode<N, K, 
                            V> {
    
    private V value;
    
    public V getValue() {
        return value;
    }
    
    public void setValue(V value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return String.format("[LEAF: key = \"%s\", value = \"%s\"]",
                             Objects.toString(key),
                             Objects.toString(value));
    }
}
