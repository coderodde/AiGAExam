package task4;

import java.util.Objects;

public final class LeafRMQTreeNode<N extends AbstractRMQTreeNode<N, K, V>, 
                                   K extends Comparable<? super K>, 
                                   V extends Comparable<? super V>>
extends AbstractRMQTreeNode<N, K, V> {
    
    private K key;
    
    public K getKey() {
        return key;
    }
    
    public void setKey(K key) {
        this.key = key;
    }
    
    @Override
    public String toString() {
        return String.format("[LEAF: key = \"%s\", value = \"%s\"]",
                             Objects.toString(key),
                             Objects.toString(value));
    }

    @Override
    public int compareTo(AbstractRMQTreeNode<N, K, V> o) {
        return key.compareTo(((LeafRMQTreeNode<N, K, V>) o).getKey());
    }
}
