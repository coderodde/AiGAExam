package task4;

import java.util.Collections;
import java.util.List;

public final class RMQTreeBuilder<K extends Comparable<? super K>,
                                  V extends Comparable<? super V>> {

    public static <N extends AbstractRMQTreeNode<N, K, V>,
                   K extends Comparable<? super K>,
                   V extends Comparable<? super V>>
                    
    AbstractRMQTreeNode<N, K, V> 
        buildRMQTree(List<KeyValuePair<K, V>> keyValuePairs) {
        
        if (keyValuePairs == null || keyValuePairs.isEmpty()) {
            return null;
        }
        
        Collections.sort(keyValuePairs);
        return buildRMQTreeImpl(keyValuePairs);
    }

    // This algorithm seems much like in Task9, yet it differs: this one does 
    // not stored actual keys to the internal nodes, except to the leaf nodes,
    // unlike the algorithm in Task9.java.
    private static <N extends AbstractRMQTreeNode<N, K, V>,
                    K extends Comparable<? super K>,
                    V extends Comparable<? super V>>
                    
    AbstractRMQTreeNode<N, K, V> 
        buildRMQTreeImpl(List<KeyValuePair<K, V>> keyValuePairs) {

        if (keyValuePairs.size() == 1) {
            KeyValuePair<K, V> keyValuePair = keyValuePairs.get(0);
            
            LeafRMQTreeNode<N, K, V> leaf = new LeafRMQTreeNode<>();
            leaf.setKey(keyValuePair.getKey());
            leaf.setValue(keyValuePair.getValue());
            return leaf;
        }
        
        // middleIndex goes to the right:
        int middleIndex = keyValuePairs.size() / 2;

        AbstractRMQTreeNode<N, K, V> leftSubTreeRoot
                = buildRMQTreeImpl(keyValuePairs.subList(0, middleIndex));

        AbstractRMQTreeNode<N, K, V> rightSubTreeRoot
                = buildRMQTreeImpl(
                        keyValuePairs.subList(
                                middleIndex,
                                keyValuePairs.size()));

        InternalRMQTreeNode<N, K, V> localRoot = new InternalRMQTreeNode<>();
        
        localRoot.setLeftChild(leftSubTreeRoot);
        localRoot.setRightChild(rightSubTreeRoot);
        
        localRoot.setKey(rightSubTreeRoot.getKey()); // Important steps!
        localRoot.setValue(min(leftSubTreeRoot.getValue(),
                               rightSubTreeRoot.getValue()));

        return localRoot;
    }
        
    /**
     * Returns the smaller of the two given values.
     * 
     * @param <V>    the value of type. Must be {@link java.lang.Comparable}.
     * @param value1 the first value.
     * @param value2 the second value.
     * @return the smaller of the two input values.
     */
    private static <V extends Comparable<? super V>> V min(V value1, V value2) {
        return value1.compareTo(value2) < 0 ? value1 : value2;
    }
}