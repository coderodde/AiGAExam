package task4;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import task4.SemiDynamicRMQTreeBuilder.RMQTreeBuilderResult;
import static task4.Utils.min;

/**
 * This class implements a semi-dynamic RMQ (range minimum query) tree.
 * 
 * @param <N> the node type.
 * @param <K> the key type.
 * @param <V> the value type.
 */
public final class SemiDynamicRMQTree<N extends AbstractRMQTreeNode<N, V>,
                                      K extends Comparable<? super K>,
                                      V extends Comparable<? super V>> {
    
    private final AbstractRMQTreeNode<N, V> root;
    private final Map<K, LeafRMQTreeNode<N, V>> leafMap;
    
    public SemiDynamicRMQTree(Set<KeyValuePair<K, V>> keyValuePairSet) {
        RMQTreeBuilderResult<N, K, V> result = 
                SemiDynamicRMQTreeBuilder.buildRMQTree(keyValuePairSet);
        
        root = result.getRoot();
        leafMap = result.getLeafMap();
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        toStringImpl(stringBuilder);
        return stringBuilder.toString();
    }
    
    public AbstractRMQTreeNode<N, V> getRoot() {
        return root;
    }
    
    public void update(K key, V newValue) {
        AbstractRMQTreeNode<N, V> node = leafMap.get(key);
        
        while (node != null) {
            node.setValue(min(node.getValue(), newValue));
            node = node.getParent();
        }
    }
    
    public V getRangeMinimum(K leftKey, K rightKey) {
        
        AbstractRMQTreeNode<N, V> leftLeaf  = leafMap.get(leftKey);
        
        Objects.requireNonNull(
                leftLeaf,
                String.format(
                        "The left key [%s] is not in this tree.",
                        leftKey));
        
        AbstractRMQTreeNode<N, V> rightLeaf = leafMap.get(rightKey);
        
        Objects.requireNonNull(
                rightLeaf,
                String.format(
                        "The right key [%s] is not in this tree.",
                        rightKey));
        
        if (leftKey.compareTo(rightKey) > 0) {
            String exceptionMessage = 
                    String.format(
                            "The specified range [%s, %s] is descending.", 
                            leftKey, 
                            rightKey);
            
            throw new IllegalArgumentException(exceptionMessage);
        }
        
        AbstractRMQTreeNode<N, V> splitNode =
                computeSplitNode(leftLeaf,
                                 rightLeaf);
        
        List<AbstractRMQTreeNode<N, V>> leftPath = getPath(splitNode,
                                                              leftLeaf);
        
        List<AbstractRMQTreeNode<N, V>> rightPath = getPath(splitNode,
                                                               rightLeaf);
        
        List<AbstractRMQTreeNode<N, V>> leftPathV = 
                computeLeftPathV(leftPath);
        
        List<AbstractRMQTreeNode<N, V>> rightPathV = 
                computeRightPartV(rightPath);
        
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
    
    private <N extends AbstractRMQTreeNode<N, V>, 
             K extends Comparable<? super K>, 
             V extends Comparable<? super V>> 
                
        V computeMinimum(List<AbstractRMQTreeNode<N, V>> nodes) {
        
        if (nodes.isEmpty()) {
            return null;
        }
            
        V minValue = nodes.get(0).getValue();
        
        for (int i = 1; i < nodes.size(); i++) {
            AbstractRMQTreeNode<N, V> node = nodes.get(i);
            
            minValue = min(minValue, node.getValue());
        }
        
        return minValue;
    }
    
    private List<AbstractRMQTreeNode<N, V>> 
        computeLeftPathV(List<AbstractRMQTreeNode<N, V>> path) {
            
        Set<AbstractRMQTreeNode<N, V>> pathSet   = new HashSet<>(path);
        List<AbstractRMQTreeNode<N, V>> nodeList = new ArrayList<>();
        
        for (int i = 0; i < path.size(); i++) {
            InternalRMQTreeNode<N, V> parent = 
                    (InternalRMQTreeNode<N, V>) path.get(i);
            
            AbstractRMQTreeNode<N, V> leftChild  = parent.getLeftChild();
            AbstractRMQTreeNode<N, V> rightChild = parent.getRightChild();
            
            if (pathSet.contains(leftChild)) {
                nodeList.add(rightChild);
            }
        }
        
        return nodeList;
    }
        
    private List<AbstractRMQTreeNode<N, V>>
        computeRightPartV(List<AbstractRMQTreeNode<N, V>> path) {
        Set<AbstractRMQTreeNode<N, V>> pathSet   = new HashSet<>(path);
        List<AbstractRMQTreeNode<N, V>> nodeList = new ArrayList<>();
        
        for (int i = 0; i < path.size() - 1; i++) {
            InternalRMQTreeNode<N, V> parent = 
                    (InternalRMQTreeNode<N, V>) path.get(i);
            
            AbstractRMQTreeNode<N, V> leftChild  = parent.getLeftChild();
            AbstractRMQTreeNode<N, V> rightChild = parent.getRightChild();
            
            if (pathSet.contains(rightChild)) {
                nodeList.add(leftChild);
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
    private List<AbstractRMQTreeNode<N, V>> 
        getPath(AbstractRMQTreeNode<N, V> splitNode, 
                AbstractRMQTreeNode<N, V> leafNode) {
        
        List<AbstractRMQTreeNode<N, V>> path = new ArrayList<>();
        
        AbstractRMQTreeNode<N, V> node = leafNode.getParent();
        
        while (node != null && !node.equals(splitNode)) {
            path.add(node);
            node = node.getParent();
        }
        
        Collections.reverse(path);
        return path;
    }
    
    private AbstractRMQTreeNode<N, V> 
        computeSplitNode(AbstractRMQTreeNode<N, V> leftLeaf,
                         AbstractRMQTreeNode<N, V> rightLeaf) {
            
        Set<AbstractRMQTreeNode<N, V>> leftPathSet = new HashSet<>();
        
        AbstractRMQTreeNode<N, V> node = leftLeaf;
        
        while (node != null) {
            leftPathSet.add(node);
            node = node.getParent();
        }
        
        node = rightLeaf;
        
        while (node != null) {
            if (leftPathSet.contains(node)) {
                return node;
            }
            
            node = node.getParent();
        }
        
        throw new IllegalStateException("Should not get here.");
    }
        
    private void toStringImpl(StringBuilder stringBuilder) {
        
        Deque<AbstractRMQTreeNode<N, V>> queue = 
                new ArrayDeque<>();
        
        queue.addLast(getRoot());
        
        AbstractRMQTreeNode<N, V> levelEnd = getRoot();
        
        while (!queue.isEmpty()) {
            AbstractRMQTreeNode<N, V> currentNode = queue.removeFirst();
            stringBuilder.append(String.format("%s ", currentNode));
            
            if (currentNode instanceof InternalRMQTreeNode) {
                
                AbstractRMQTreeNode<N, V> leftChild =
                        ((InternalRMQTreeNode<N, V>) currentNode)
                                .getLeftChild();
                
                AbstractRMQTreeNode<N, V> rightChild = 
                        ((InternalRMQTreeNode<N, V>) currentNode)
                                .getRightChild();
                
                queue.addLast(leftChild);
                queue.addLast(rightChild);
            } 
            
            if (currentNode.equals(levelEnd)) {
                if (!queue.isEmpty()) {
                    levelEnd = queue.getLast();
                }
                    
                stringBuilder.append("\n");
            }
        }
    }
}
