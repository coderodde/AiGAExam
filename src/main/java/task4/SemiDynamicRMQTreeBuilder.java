package task4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static task4.Utils.min;

public final class SemiDynamicRMQTreeBuilder<N extends AbstractRMQTreeNode<N, V>,
                                             K extends Comparable<? super K>,
                                             V extends Comparable<? super V>> {

    public static <N extends AbstractRMQTreeNode<N, V>,
                   K extends Comparable<? super K>,
                   V extends Comparable<? super V>>
                    
    SemiDynamicRMQTree<N, K, V> 
        buildRMQTree(Set<KeyValuePair<K, V>> keyValuePairs) {
        
        if (keyValuePairs == null || keyValuePairs.isEmpty()) {
            return null;
        }
        
        List<KeyValuePair<K, V>> keyValuePairList = 
                new ArrayList<>(keyValuePairs);
        
        Collections.sort(keyValuePairList);
        
        Map<K, LeafRMQTreeNode<N, V>> mapKeyToLeafNode = 
                computeMapKeyToLeafNode(keyValuePairList);
        
        return new SemiDynamicRMQTree<>(
                buildRMQTreeImpl(keyValuePairList, mapKeyToLeafNode));
    }
        
    private static <K extends Comparable<? super K>, 
                    V extends Comparable<? super V>,
                    N extends AbstractRMQTreeNode<N, V>> 
                
        Map<K, LeafRMQTreeNode<N, V>> 
            computeMapKeyToLeafNode(List<KeyValuePair<K, V>> keyValuePairList) {
            
        Map<K, LeafRMQTreeNode<N, V>> map = 
                new HashMap<>(keyValuePairList.size());
        
        for (KeyValuePair<K, V> keyValuePair : keyValuePairList ) {
            LeafRMQTreeNode<N, V> leafNode = new LeafRMQTreeNode<>();
            map.put(keyValuePair.getKey(), leafNode);
        }
        
        return map;
    }

    // This algorithm seems much like in Task9, yet it differs: this one does 
    // not stored actual keys to the internal nodes, except to the leaf nodes,
    // unlike the algorithm in Task9.java.
    private static <N extends AbstractRMQTreeNode<N, V>,
                    K extends Comparable<? super K>,
                    V extends Comparable<? super V>>
                    
    AbstractRMQTreeNode<N, V> 
        buildRMQTreeImpl(List<KeyValuePair<K, V>> keyValuePairs, 
                         Map<K, LeafRMQTreeNode<N, V>> mapKeyToLeafNodes) {
            
        if (keyValuePairs.size() == 1) {
            return mapKeyToLeafNodes.get(keyValuePairs.get(0).getKey());
//            KeyValuePair<K, V> keyValuePair = keyValuePairs.get(0);
//            
//            LeafRMQTreeNode<N, V> leaf = 
//                    mapKeyToLeafNodes.get(keyValuePair.getKey());
//            
//            mapKeyToLeafNodes.put(keyValuePair.getKey(), leaf);
//            leaf.setValue(keyValuePair.getValue());
//            
//            return leaf;
        }
        
        // middleIndex goes to the right:
        int middleIndex = keyValuePairs.size() / 2;

        AbstractRMQTreeNode<N, V> leftSubTreeRoot
                = buildRMQTreeImpl(
                        keyValuePairs.subList(0, middleIndex),
                        mapKeyToLeafNodes);

        AbstractRMQTreeNode<N, V> rightSubTreeRoot
                = buildRMQTreeImpl(
                        keyValuePairs.subList(
                                middleIndex,
                                keyValuePairs.size()),
                        mapKeyToLeafNodes);

        InternalRMQTreeNode<N, V> localRoot = new InternalRMQTreeNode<>();
        
        // Link the children and their parent:
        localRoot.setLeftChild(leftSubTreeRoot);
        localRoot.setRightChild(rightSubTreeRoot);
        
        leftSubTreeRoot.setParent(localRoot);
        rightSubTreeRoot.setParent(localRoot);
        
        localRoot.setValue(min(leftSubTreeRoot.getValue(), // Important step!
                               rightSubTreeRoot.getValue()));

        return localRoot;
    }
}