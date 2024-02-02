package task4;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Task4 {
    
    private static final int TREE_SIZE = 1000;
    
    public static void main(String[] args) {
        List<RMQTreeNode<Integer, String>> nodes = generateRMQTree();
        RMQTreeNode<Integer, String> root = RMQTreeBuilder.buildRMQTree(nodes);
        System.out.println(root);
    }
    
    private static List<RMQTreeNode<Integer, String>> 
        generateRMQTree() {
        List<RMQTreeNode<Integer, String>> nodes = new ArrayList<>(TREE_SIZE);
        
        for (int i = 0; i < TREE_SIZE; i++) {
            RMQTreeNode<Integer, String> node = 
                    new RMQTreeNode<>(Integer.valueOf(i), Integer.toString(i));
            
            node.setValue(Integer.toString(i));
            nodes.add(node);
        }
        
        return nodes;
    }
}
