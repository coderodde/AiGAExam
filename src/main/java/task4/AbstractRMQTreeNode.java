package task4;

import java.util.Objects;

public abstract class AbstractRMQTreeNode
        <N extends AbstractRMQTreeNode<N, K, V>,
         K extends Comparable<? super K>,
         V extends Comparable<? super V>>
        implements Comparable<AbstractRMQTreeNode<N, K, V>>{
    
//    protected K key;
    protected V value;
    protected AbstractRMQTreeNode<N, K, V> parent;

//    public K getKey() {
//        return key;
//    }
//    
//    public void setKey(K key) {
//        this.key = key;
//    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public AbstractRMQTreeNode<N, K, V> getParent() {
        return parent;
    }

    public void setParent(AbstractRMQTreeNode<N, K, V> parent) {
        this.parent = parent;
    }
    
//    @Override
//    public int compareTo(AbstractRMQTreeNode<N, K, V> o) {
//        return key.compareTo(o.key);
//    }
//    
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(key);
//    }
    
//    @Override
//    public boolean equals(Object o) {
//        return key.equals(((AbstractRMQTreeNode<N, K, V>) o).key);
//    }
}
