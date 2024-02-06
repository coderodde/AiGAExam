package task4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import static task4.Utils.min;

/**
 * This class implements a semi-dynamic RMQ (range minimum query) tree.
 * 
 * @param <N> the node type.
 * @param <K> the key type.
 * @param <V> the value type.
 */
public final class SemiDynamicRMQTree<N extends AbstractRMQTreeNode<N, K, V>,
                                      K extends Comparable<? super K>,
                                      V extends Comparable<? super V>> {
    
    private final AbstractRMQTreeNode<N, K, V> root;
    private final Map<K, LeafRMQTreeNode<N, K, V>> leafMap = new HashMap<>();
    
    public SemiDynamicRMQTree(AbstractRMQTreeNode<N, K, V> root) {
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
        if (leftKey.equals(rightKey)) {
            // Handle the trivial case. This also will guarantee that splitNode
            // will not be null.
            return leafMap.get(leftKey).getValue();
        }
        
        AbstractRMQTreeNode<N, K, V> leftLeaf  = leafMap.get(leftKey);
        AbstractRMQTreeNode<N, K, V> rightLeaf = leafMap.get(rightKey);
        
        AbstractRMQTreeNode<N, K, V> splitNode =
                computeSplitNode(leftLeaf,
                                 rightLeaf);
        
        List<AbstractRMQTreeNode<N, K, V>> leftPath = getPath(splitNode,
                                                              leftLeaf);
        
        List<AbstractRMQTreeNode<N, K, V>> rightPath = getPath(splitNode,
                                                               rightLeaf);
        
        List<AbstractRMQTreeNode<N, K, V>> leftPathV = 
                computeLeftPathV(leftPath);
        
        List<AbstractRMQTreeNode<N, K, V>> rightPathV = 
                computeRightPathV(rightPath);
        
        V vl = computeMinimum(leftPathV);
        V vr = computeMinimum(rightPathV);
        
        if (vl == null) {
            vl = leftLeaf.getValue();
        }
        
        if (vr == null) {
            vr = rightLeaf.getValue();
        }
        
        vl = min(vl, leftLeaf.getValue());
        vr = min(vr, rightLeaf.getValue());
        
        return min(vl, vr);
    }
    
    private <N extends AbstractRMQTreeNode<N, K, V>, 
             K extends Comparable<? super K>, 
             V extends Comparable<? super V>> 
        V computeMinimum(List<AbstractRMQTreeNode<N, K, V>> nodes) {
        
        if (nodes.isEmpty()) {
            return null;
        }
            
        V minValue = nodes.get(0).getValue();
        
        for (int i = 1; i < nodes.size(); i++) {
            AbstractRMQTreeNode<N, K, V> node = nodes.get(i);
            
            minValue = min(minValue, node.getValue());
        }
        
        return minValue;
    }
    
    private List<AbstractRMQTreeNode<N, K, V>> 
        computeLeftPathV(List<AbstractRMQTreeNode<N, K, V>> path) {
        
        List<AbstractRMQTreeNode<N, K, V>> nodeList = 
                new ArrayList<>(path.size());
        
        for (int i = 0; i < path.size() - 1; i++) {
            InternalRMQTreeNode<N, K, V> parent = 
                    (InternalRMQTreeNode<N, K, V>) path.get(i);
            
            InternalRMQTreeNode<N, K, V> child  = 
                    (InternalRMQTreeNode<N, K, V>) path.get(i + 1);
            
            if (parent.getLeftChild().equals(child)) {
                nodeList.add(parent.getRightChild());
            }
        }
        
        return nodeList;
    }
        
    private List<AbstractRMQTreeNode<N, K, V>>
        computeRightPartV(List<AbstractRMQTreeNode<N, K, V>> path) {
        
        List<AbstractRMQTreeNode<N, K, V>> nodeList = 
                new ArrayList<>();
        
        for (int i = 0; i < path.size() - 1; i++) {
            InternalRMQTreeNode<N, K, V> parent = 
                    (InternalRMQTreeNode<N, K, V>) path.get(i);
            
            InternalRMQTreeNode<N, K, V> child = 
                    (InternalRMQTreeNode<N, K, V>) path.get(i + 1);
            
            if (parent.getRightChild().equals(child)) {
                nodeList.add(parent.getLeftChild());
            }
        }
        
        return nodeList;
    }
    
    private List<AbstractRMQTreeNode<N, K, V>> 
        computeRightPathV(List<AbstractRMQTreeNode<N, K, V>> path) {
        
        List<AbstractRMQTreeNode<N, K, V>> nodeList = new ArrayList<>();
        
        for (int i = 0; i < path.size() - 1; i++) {
            InternalRMQTreeNode<N, K, V> parent = 
                    (InternalRMQTreeNode<N, K, V>) path.get(i);
            
            InternalRMQTreeNode<N, K, V> child = 
                    (InternalRMQTreeNode<N, K, V>) path.get(i + 1);
            
            if (parent.getLeftChild().equals(child)) {
                nodeList.add(parent.getRightChild());
            }
        }
        
        return nodeList;
    }
    
    /**
     * Gets the path from {@code splitNode} to the {@code leaf}. The returned
     * path will, however, exclude {@code splitNode} and {@code leaf}.
     *
     * @param splitNode the starting path node.
     * @param leafNode  the target node.
     * @return the path from {@code splitNode} to {@code leaf};
     */
    private List<AbstractRMQTreeNode<N, K, V>> 
        getPath(AbstractRMQTreeNode<N, K, V> splitNode, 
                AbstractRMQTreeNode<N, K, V> leafNode) {
        
        List<AbstractRMQTreeNode<N, K, V>> path = new ArrayList<>();
        
        AbstractRMQTreeNode<N, K, V> node = leafNode.getParent();
        
        while (node != null && !node.equals(splitNode)) {
            path.add(node);
            node = node.getParent();
        }
        
        Collections.reverse(path);
        return path;
    }
    
    private AbstractRMQTreeNode<N, K, V> 
        computeSplitNode(AbstractRMQTreeNode<N, K, V> leftLeaf,
                         AbstractRMQTreeNode<N, K, V> rightLeaf) {
            
        Set<AbstractRMQTreeNode<N, K, V>> leftPathSet = new HashSet<>();
        
        AbstractRMQTreeNode<N, K, V> node = leftLeaf;
        
        while (node != null) {
            leftPathSet.add(node);
            node = node.getParent();
        }
        
        node = rightLeaf.getParent();
        
        while (node != null) {
            if (leftPathSet.contains(node)) {
                return node;
            }
            
            node = node.getParent();
        }
        
        throw new IllegalStateException("Should not get here.");
    }
    
    private void loadLeafMap() {
        loadLeafMapImpl(root);
    }
    
    private void loadLeafMapImpl(AbstractRMQTreeNode<N, K, V> node) {
        if (node instanceof InternalRMQTreeNode) {
            
            InternalRMQTreeNode<N, K, V> internalNode = 
                    (InternalRMQTreeNode<N, K, V>) node;
            
            loadLeafMapImpl(internalNode.getLeftChild());
            loadLeafMapImpl(internalNode.getRightChild());
            
        } else {
            LeafRMQTreeNode<N, K, V> leafNode = (LeafRMQTreeNode<N, K, V>) node;
            leafMap.put(leafNode.getKey(), leafNode);
        }
    }
}
