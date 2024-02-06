package task4;

import java.util.Objects;

public final class InternalRMQTreeNode<N extends AbstractRMQTreeNode<N, K, V>,
                                       K extends Comparable<? super K>, 
                                       V extends Comparable<? super V>>
extends AbstractRMQTreeNode<N, K, V> {
    
    private AbstractRMQTreeNode<N, K, V> leftChild;
    private AbstractRMQTreeNode<N, K, V> rightChild;

    public V getValue() {
        return value;
    }
    
    public AbstractRMQTreeNode<N, K, V> getLeftChild() {
        return leftChild;
    }

    public AbstractRMQTreeNode<N, K, V> getRightChild() {
        return rightChild;
    }
    
    public void setValue(V value) {
        this.value = value;
    }

    public void setLeftChild(AbstractRMQTreeNode<N, K, V> leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(AbstractRMQTreeNode<N, K, V> rightChild) {
        this.rightChild = rightChild;
    }
    
    @Override
    public String toString() {
        return String.format("[INTERNAL: value = \"%s\"]", 
                             Objects.toString(value));
    }

    @Override
    public int compareTo(AbstractRMQTreeNode<N, K, V> o) {
        throw new UnsupportedOperationException("Sorting internal RMQ tree nodes not uspported.");
    }
}
