package task4;

import java.util.Objects;

public final class RMQTree<N extends AbstractRMQTreeNode<N, K, V>,
                           K extends Comparable<? super K>,
                           V extends Comparable<? super V>> {
    
    private final AbstractRMQTreeNode<N, K, V> root;
    
    public RMQTree(AbstractRMQTreeNode<N, K, V> root) {
        this.root = Objects.requireNonNull(root, "The root node is null.");
    }
    
    public void update(K key, V newValue) {
        
    }
    
    public V getRangeMinimum(K leftKey, K rightKey) {
        return null;
    }
}
