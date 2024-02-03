package task4;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Task4 {
    
    private static final int TREE_SIZE = 5;
    
    public static void main(String[] args) {
        List<KeyValuePair<Integer, String>> keyValuePairs = 
                generateKeyValuePairs();
        
        Collections.shuffle(keyValuePairs);
        
        RMQTree<?, Integer, String> tree = 
                RMQTreeBuilder.buildRMQTree(keyValuePairs);
        
        printRMQTree(tree);
        
        tree.update(2, "0");
        
        System.out.println("==========");
        
        printRMQTree(tree);
    }
    
    private static List<KeyValuePair<Integer, String>> 
        generateKeyValuePairs() {
        List<KeyValuePair<Integer, String>> keyValuePairs = 
                new ArrayList<>(TREE_SIZE);
        
        for (int i = 0; i < TREE_SIZE; i++) {
            KeyValuePair keyValuePair = new KeyValuePair<>(Integer.valueOf(i),
                                                           Integer.toString(i + 1));
            keyValuePairs.add(keyValuePair);
        }
        
        return keyValuePairs;
    }
        
    private static void printRMQTree(RMQTree<?, Integer, String> tree) {
        
        Deque<AbstractRMQTreeNode<?, Integer, String>> queue = 
                new ArrayDeque<>();
        
        queue.addLast(tree.getRoot());
        
        AbstractRMQTreeNode<?, Integer, String> levelEnd = tree.getRoot();
        
        while (!queue.isEmpty()) {
            AbstractRMQTreeNode<?, Integer, String> currentNode = 
                    queue.removeFirst();
            
            System.out.printf("%s ", currentNode);
            
            if (currentNode instanceof InternalRMQTreeNode) {
                AbstractRMQTreeNode<?, Integer, String> leftChild =
                        ((InternalRMQTreeNode<?, Integer, String>) currentNode)
                                .getLeftChild();
                
                AbstractRMQTreeNode<?, Integer, String> rightChild = 
                        ((InternalRMQTreeNode<?, Integer, String>) currentNode)
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
