package task4;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Task4 {
    
    private static final int TREE_SIZE = 5;
    
    public static void main(String[] args) {
        List<KeyValuePair<Integer, Long>> keyValuePairs = 
                generateKeyValuePairs();
        
        Collections.shuffle(keyValuePairs);
        
        SemiDynamicRMQTree<?, Integer, Long> tree = 
                SemiDynamicRMQTreeBuilder.buildRMQTree(keyValuePairs);
        
        printRMQTree(tree);
        
        tree.update(2, -1L);
        
        System.out.println("==========");
        
        printRMQTree(tree);
    }
    
    private static List<KeyValuePair<Integer, Long>> 
        generateKeyValuePairs() {
        List<KeyValuePair<Integer, Long>> keyValuePairs = 
                new ArrayList<>(TREE_SIZE);
        
        for (int i = 0; i < TREE_SIZE; i++) {
            KeyValuePair keyValuePair = new KeyValuePair<>(Integer.valueOf(i),
                                                           Long.valueOf(i));
            keyValuePairs.add(keyValuePair);
        }
        
        return keyValuePairs;
    }
        
    private static <K extends Comparable<? super K>, 
             V extends Comparable<? super V>> void printRMQTree(SemiDynamicRMQTree<?, K, V> tree) {
        
        Deque<AbstractRMQTreeNode<?, K, V>> queue = 
                new ArrayDeque<>();
        
        queue.addLast(tree.getRoot());
        
        AbstractRMQTreeNode<?, K, V> levelEnd = tree.getRoot();
        
        while (!queue.isEmpty()) {
            AbstractRMQTreeNode<?, K, V> currentNode = queue.removeFirst();
            
            System.out.printf("%s ", currentNode);
            
            if (currentNode instanceof InternalRMQTreeNode) {
                
                AbstractRMQTreeNode<?, K, V> leftChild =
                        ((InternalRMQTreeNode<?, K, V>) currentNode)
                                .getLeftChild();
                
                AbstractRMQTreeNode<?, K, V> rightChild = 
                        ((InternalRMQTreeNode<?, K, V>) currentNode)
                                .getRightChild();
                
                queue.addLast(leftChild);
                queue.addLast(rightChild);
            } 
            
            if (currentNode.equals(levelEnd)) {
                if (!queue.isEmpty()) {
                    levelEnd = queue.getLast();
                }
                    
                System.out.println();
            }
        }
    }
}
