package task4;

import java.util.Objects;

public final class RMQTreeNode<K extends Comparable<? super K>, V> 
        implements Comparable<RMQTreeNode<K, V>> {
    
    private K key;
    private V value;
    private RMQTreeNode<K, V> leftChild;
    private RMQTreeNode<K, V> rightChild;

    public RMQTreeNode(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    @Override
    public String toString() {
        return String.format(
                "[key = %s, value = %s]", 
                Objects.toString(key), 
                Objects.toString(value));
    }
    
    @Override
    public int compareTo(RMQTreeNode<K, V> o) {
        return key.compareTo(o.key);
    }
    
    public K getKey() {
        return key;
    }
    
    public V getValue() {
        return value;
    }
    
    public RMQTreeNode<K, V> getLeftChild() {
        return leftChild;
    }
    
    public RMQTreeNode<K, V> getRightChild() {
        return rightChild;
    }
    
    public void setKey(K key) {
        this.key = key;
    }
    
    public void setValue(V value) {
        this.value = value;
    }
    
    public void setLeftChild(RMQTreeNode<K, V> node) {
        this.leftChild = node;
    }
    
    public void setRightChild(RMQTreeNode<K, V> node) {
        this.rightChild = node;
    }
}
