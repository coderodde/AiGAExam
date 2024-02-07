package task4;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Task4 {
    
    private static final int INITIAL_TREE_SIZE = 4;
             
    private static SemiDynamicRMQTree<?, Integer, Long> tree = 
            constructREPLTree(INITIAL_TREE_SIZE);
    
    public static void main(String[] args) {
        replInterface();
    }
             
    private static SemiDynamicRMQTree<?, Integer, Long> 
        constructREPLTree(int size) {
            
        Set<KeyValuePair<Integer, Long>> keyValuePairSet = new HashSet<>();
        
        for (int i = 0; i < size; i++) {
            Integer key = i + 1;
            Long value = Long.valueOf(i + 1);
            KeyValuePair<Integer, Long> keyValuePair = new KeyValuePair<>(key, value);
            keyValuePairSet.add(keyValuePair);
        }
        
        return SemiDynamicRMQTreeBuilder.buildRMQTree(keyValuePairSet);
    }
             
    private static void replInterface() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("> ");
            
            String commandLine = scanner.nextLine().trim();
            
            try {
                String[] commandLineParts = commandLine.split(" ");
                String command = commandLineParts[0].trim();

                switch (command) {
                    case "update":
                        runUpdate(commandLineParts[1],
                                  commandLineParts[2]);
                        break;

                    case "rmq":
                        Long value = runRMQ(commandLineParts[1],
                                            commandLineParts[2]);

                        System.out.println(value);
                        break;

                    case "print":
                        System.out.println(tree);
                        break;

                    case "new":
                        int size = Integer.parseInt(commandLineParts[1]);
                        tree = constructREPLTree(size);
                        break;

                    case "help":
                        printHelp();
                        break;

                    case "quit":
                    case "exit":
                        System.out.println("Bye!");
                        return;
                }
            } catch (Exception ex) {
                System.out.printf(
                        "> ERROR: Could not parse command \"%s\".\n",
                        commandLine);
            }
        }
    }
    
    private static void printHelp() {
        final String help = "update KEY VALUE\n" + 
                            "rmq KEY1 KEY2\n" + 
                            "print\n" +
                            "new TREE_SIZE\n" +
                            "help";
        
        System.out.println(help);
    }
    
    private static void runUpdate(String keyString, String newValueString) {
        Integer key = Integer.valueOf(keyString);
        Long value = Long.valueOf(newValueString);
        tree.update(key, value);
    }
    
    private static Long runRMQ(String leftKeyString, String rightKeyString) {
        Integer leftKey = Integer.valueOf(leftKeyString);
        Integer rightKey = Integer.valueOf(rightKeyString);
        return tree.getRangeMinimum(leftKey, rightKey);
    }
}
