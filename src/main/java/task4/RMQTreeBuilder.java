package task4;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static task4.Utils.min;

public final class RMQTreeBuilder<K extends Comparable<? super K>,
                                  V extends Comparable<? super V>> {

    public static <N extends AbstractRMQTreeNode<N, K, V>,
                   K extends Comparable<? super K>,
                   V extends Comparable<? super V>>
                    
    RMQTree<N, K, V> buildRMQTree(List<KeyValuePair<K, V>> keyValuePairs) {
        
        if (keyValuePairs == null || keyValuePairs.isEmpty()) {
            return null;
        }
        
        // Filter duplicates:
        Set<KeyValuePair<K, V>> filter = new HashSet<>(keyValuePairs);
        keyValuePairs.clear();
        
        for (KeyValuePair<K, V> keyValuePair : filter) {
            keyValuePairs.add(keyValuePair);
        }
        
        Collections.sort(keyValuePairs);
        
        return new RMQTree<N, K, V>(buildRMQTreeImpl(keyValuePairs));
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
        
        leftSubTreeRoot.setParent(localRoot);
        rightSubTreeRoot.setParent(localRoot);
        
        localRoot.setKey(rightSubTreeRoot.getKey()); // Important steps!
        localRoot.setValue(min(leftSubTreeRoot.getValue(),
                               rightSubTreeRoot.getValue()));

        return localRoot;
    }
}