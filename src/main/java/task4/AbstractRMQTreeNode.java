package task4;

import java.util.Objects;

public abstract class AbstractRMQTreeNode
        <N extends AbstractRMQTreeNode<N, K, V>,
         K extends Comparable<? super K>,
         V>
        implements Comparable<AbstractRMQTreeNode<N, K, V>>{
    
    protected K key;
    
    public K getKey() {
        return key;
    }
    
    public void setKey(K key) {
        this.key = key;
    }

    @Override
    public int compareTo(AbstractRMQTreeNode<N, K, V> o) {
        return key.compareTo(o.key);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }
    
    @Override
    public boolean equals(Object o) {
        return key.equals(((AbstractRMQTreeNode<N, K, V>) o).key);
    }
}
