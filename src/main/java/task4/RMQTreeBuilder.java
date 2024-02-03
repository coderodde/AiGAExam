package task4;

import java.util.Collections;
import java.util.List;

public final class RMQTreeBuilder<K extends Comparable<? super K>, V> {

    public static <K extends Comparable<? super K>, V>
            RMQTreeNode<K, V> buildRMQTree(List<RMQTreeNode<K, V>> nodes) {
        
        if (nodes == null || nodes.isEmpty()) {
            return null;
        }
        
        Collections.sort(nodes);
        return buildRMQTreeImpl(nodes);
    }

    // This algorithm seems much like in Task9, yet it differs: this one does 
    // not stored actual keys to the internal nodes, except to the leaf nodes,
    // unlike the algorithm in Task9.java.
    private static <K extends Comparable<? super K>, V>
            RMQTreeNode<K, V> buildRMQTreeImpl(List<RMQTreeNode<K, V>> nodes) {

        if (nodes.size() == 1) {
            return nodes.get(0);
        }

        int middleIndex = nodes.size() / 2; // middleIndex goes to the right.

        RMQTreeNode<K, V> leftSubTreeRoot
                = buildRMQTreeImpl(nodes.subList(0, middleIndex));

        RMQTreeNode<K, V> rightSubTreeRoot
                = buildRMQTreeImpl(nodes.subList(middleIndex, nodes.size()));

        RMQTreeNode<K, V> localRoot = 
                new RMQTreeNode<>(rightSubTreeRoot.getKey(), null);
        
        localRoot.setLeftChild(leftSubTreeRoot);
        localRoot.setRightChild(rightSubTreeRoot);

        return localRoot;
    }
}