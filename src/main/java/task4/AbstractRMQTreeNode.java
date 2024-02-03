package task4;

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
}
