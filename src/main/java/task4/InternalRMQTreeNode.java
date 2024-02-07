package task4;

import java.util.Objects;

public final class InternalRMQTreeNode<N extends AbstractRMQTreeNode<N, V>,
                                       V extends Comparable<? super V>>
extends AbstractRMQTreeNode<N, V> {
    
    private AbstractRMQTreeNode<N, V> leftChild;
    private AbstractRMQTreeNode<N, V> rightChild;

    public AbstractRMQTreeNode<N, V> getLeftChild() {
        return leftChild;
    }

    public AbstractRMQTreeNode<N, V> getRightChild() {
        return rightChild;
    }

    public void setLeftChild(AbstractRMQTreeNode<N, V> leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(AbstractRMQTreeNode<N, V> rightChild) {
        this.rightChild = rightChild;
    }
    
    @Override
    public String toString() {
        return String.format("[INTERNAL: value = \"%s\"]", 
                             Objects.toString(value));
    }

    @Override
    public int compareTo(AbstractRMQTreeNode<N, V> o) {
        throw new UnsupportedOperationException(
                "Sorting internal RMQ tree nodes not uspported.");
    }
}
