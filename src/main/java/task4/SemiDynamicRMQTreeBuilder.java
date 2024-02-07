package task4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import static task4.Utils.min;

public final class SemiDynamicRMQTreeBuilder<N extends AbstractRMQTreeNode<N, V>,
                                             K extends Comparable<? super K>,
                                             V extends Comparable<? super V>> {

    static <N extends AbstractRMQTreeNode<N, V>,
            K extends Comparable<? super K>,
            V extends Comparable<? super V>>
                    
    RMQTreeBuilderResult<N, K, V> 
        buildRMQTree(Set<KeyValuePair<K, V>> keyValuePairSet) {
        
        Map<K, LeafRMQTreeNode<N, V>> leafMap = new HashMap<>();
        
        loadLeafMap(leafMap, keyValuePairSet);
        
        Objects.requireNonNull(
                keyValuePairSet,
                "The input KeyValuePair set is null.");
        
        if (keyValuePairSet.isEmpty()) {
            throw new IllegalArgumentException(
                    "No key/value pairs to process.");
        }
        
        List<KeyValuePair<K, V>> keyValuePairList = 
                new ArrayList<>(keyValuePairSet);
        
        Collections.sort(keyValuePairList);
        
        Map<K, LeafRMQTreeNode<N, V>> mapKeyToLeafNode = new HashMap<>();
        RMQTreeBuilderResult<N, K, V> result = new RMQTreeBuilderResult();
        AbstractRMQTreeNode<N, V> root = 
                buildRMQTreeImpl(keyValuePairList, 
                                 mapKeyToLeafNode);
        
        result.setLeafMap(mapKeyToLeafNode);
        result.setRoot(root);
        
        return result;
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
            KeyValuePair<K, V> keyValuePair = keyValuePairs.get(0);
            LeafRMQTreeNode<N, V> leaf = new LeafRMQTreeNode<>();
            leaf.setValue(keyValuePair.getValue());
            mapKeyToLeafNodes.put(keyValuePair.getKey(), leaf);
            return leaf;
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
        
    private static <K extends Comparable<? super K>,
                    V extends Comparable<? super V>, 
                    N extends AbstractRMQTreeNode<N, V>>
                
    void loadLeafMap(Map<K, LeafRMQTreeNode<N, V>> leafMap, 
                         Set<KeyValuePair<K, V>> keyValuePairSet) {
        
        for (KeyValuePair<K, V> keyValuePair : keyValuePairSet) {
            LeafRMQTreeNode<N, V> leaf = new LeafRMQTreeNode<>();
            leaf.setValue(keyValuePair.getValue());
            leafMap.put(keyValuePair.getKey(), leaf);
        }
    }
    
    static final 
            class RMQTreeBuilderResult<N extends AbstractRMQTreeNode<N, V>,
                                       K extends Comparable<? super K>,
                                       V extends Comparable<? super V>> {
        
        private Map<K, LeafRMQTreeNode<N, V>> leafMap;
        private AbstractRMQTreeNode<N, V> root;

        public void setLeafMap(Map<K, LeafRMQTreeNode<N, V>> leafMap) {
            this.leafMap = leafMap;
        }

        public void setRoot(AbstractRMQTreeNode<N, V> root) {
            this.root = root;
        }
        
        Map<K, LeafRMQTreeNode<N, V>> getLeafMap() {
            return leafMap;
        }
        
        AbstractRMQTreeNode<N, V> getRoot() {
            return root;
        }
    }
}