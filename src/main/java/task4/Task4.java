package task4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Task4 {
    
    private static final int TREE_SIZE = 1000;
    
    public static void main(String[] args) {
        List<KeyValuePair<Integer, String>> nodes = generateKeyValuePairs();
        Collections.shuffle(nodes);
        AbstractRMQTreeNode<?, Integer, String> root =
                RMQTreeBuilder.buildRMQTree(nodes);
        
        System.out.println(root);
    }
    
    private static List<KeyValuePair<Integer, String>> 
        generateKeyValuePairs() {
        List<KeyValuePair<Integer, String>> keyValuePairs = 
                new ArrayList<>(TREE_SIZE);
        
        for (int i = 0; i < TREE_SIZE; i++) {
            KeyValuePair keyValuePair = new KeyValuePair<>(Integer.valueOf(i),
                                                           Integer.toString(i));
            keyValuePairs.add(keyValuePair);
        }
        
        return keyValuePairs;
    }
}
