package task4;

public abstract class AbstractRMQTreeNode
        <N extends AbstractRMQTreeNode<N, V>,
         V extends Comparable<? super V>>
        
        implements Comparable<AbstractRMQTreeNode<N, V>>{
    
    protected V value;
    protected AbstractRMQTreeNode<N, V> parent;

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public AbstractRMQTreeNode<N, V> getParent() {
        return parent;
    }

    public void setParent(AbstractRMQTreeNode<N, V> parent) {
        this.parent = parent;
     }
}
