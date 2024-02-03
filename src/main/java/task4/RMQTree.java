package task4;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import static task4.Utils.min;

public final class RMQTree<N extends AbstractRMQTreeNode<N, K, V>,
                           K extends Comparable<? super K>,
                           V extends Comparable<? super V>> {
    
    private final AbstractRMQTreeNode<N, K, V> root;
    private final Map<K, LeafRMQTreeNode<N, K, V>> leafMap = new HashMap<>();
    
    public RMQTree(AbstractRMQTreeNode<N, K, V> root) {
        this.root = Objects.requireNonNull(root, "The root node is null.");
        loadLeafMap();
    }
    
    public AbstractRMQTreeNode<N, K, V> getRoot() {
        return root;
    }
    
    public void update(K key, V newValue) {
        AbstractRMQTreeNode<N, K, V> node = leafMap.get(key);
        
        while (node != null) {
            node.setValue(min(node.getValue(), newValue));
            node = node.getParent();
        }
    }
    
    public V getRangeMinimum(K leftKey, K rightKey) {
        return null;
    }
    
    private void loadLeafMap() {
        loadLeafMapProcess(root);
    }
    
    private void loadLeafMapProcess(AbstractRMQTreeNode<N, K, V> node) {
        if (node instanceof InternalRMQTreeNode) {
            
            InternalRMQTreeNode<N, K, V> internalNode = 
                    (InternalRMQTreeNode<N, K, V>) node;
            
            loadLeafMapProcess(internalNode.getLeftChild());
            loadLeafMapProcess(internalNode.getRightChild());
            
        } else {
            leafMap.put(node.getKey(), (LeafRMQTreeNode<N, K, V>) node);
        }
    }
}
