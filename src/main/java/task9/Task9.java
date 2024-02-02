package task9;

// Additional fun: the task 3.2 from the course book; convert sorted kay array 
// to a balanced binary search tree in lenear time. (Uses logarithmic time for
// stack recursion.)

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import util.Utils;

public class Task9 {
    
    public static void main(String[] args) {
        long seed = Utils.parseSeed(args);
        System.out.println("Seed = " + seed);
        List<Integer> keys = getKeyList(seed);
        System.out.printf("Original keys: %s\n", keys);
        keys.sort(Integer::compare);
        System.out.printf("Sorted keys:   %s\n", keys);
        
        BinarySearchTreeNode root = buildBalancedBinaryTreeImpl(keys);
        
        System.out.printf("Tree:\n%s\n", root.convertTreeToString() );
    }
    
    private static List<Integer> getKeyList(long seed) {
        Random random = new Random();
        
        List<Integer> keys = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {
            keys.add(i);
        }
        
        Collections.shuffle(keys, random);
        return keys;
    }
    
    private static BinarySearchTreeNode 
        buildBalancedBinaryTreeImpl(List<Integer> keys) {
            
        if (keys.isEmpty()) {
            return null;
        }
        
        if (keys.size() == 1) {
            return new BinarySearchTreeNode(keys.get(0));
        }
        
        int middleIndex = keys.size() / 2;
        
        // middleIndex is EXCLUSIVE!
        BinarySearchTreeNode leftSubTree = 
                buildBalancedBinaryTreeImpl(
                        keys.subList(0, middleIndex)); 
        
        BinarySearchTreeNode rightSubTree = 
                buildBalancedBinaryTreeImpl(
                        keys.subList(middleIndex + 1, keys.size()));
        
        BinarySearchTreeNode localRoot = 
                new BinarySearchTreeNode(keys.get(middleIndex));
        
        localRoot.setLeftChild(leftSubTree);
        localRoot.setRightChild(rightSubTree);
        
        return localRoot;
    }
}

final class BinarySearchTreeNode {
    private int key;
    private BinarySearchTreeNode leftChild;
    private BinarySearchTreeNode rightChild;
    
    BinarySearchTreeNode(int key) {
        this.key = key;
    }
    
    @Override
    public String toString() {
        return String.format("[%d]", key);
    }
    
    int getKey() {
        return key;
    }
    
    BinarySearchTreeNode getLeftChild() {
        return leftChild;
    }
    
    BinarySearchTreeNode getRightChild() {
        return rightChild;
    }
    
    void setLeftChild(BinarySearchTreeNode leftChild) {
        this.leftChild = leftChild;
    }
    
    void setRightChild(BinarySearchTreeNode rightChild) {
        this.rightChild = rightChild;
    }
    
    public String convertTreeToString() {
        StringBuilder stringBuilder = new StringBuilder();
        toString(stringBuilder, this, 0);
        return stringBuilder.toString();
    }
    
    private void toString(StringBuilder stringBuilder, 
                          BinarySearchTreeNode node,
                          int depth) {
        
        if (node == null) {
            // Hit the bottom. End recursion.
            return;
        }
        
        stringBuilder.append(
                String.format(
                        "[depth = %d, key = %d]\n", 
                        depth, 
                        node.key));
        
        toString(stringBuilder, node.getLeftChild(), depth + 1);
        toString(stringBuilder, node.getRightChild(), depth + 1);
    }
}
