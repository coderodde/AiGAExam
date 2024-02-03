package task4;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class Task4Test {
    
    @Test
    public void passesOnTreeWith4Nodes() {
        
        RMQTreeBuilder<Integer, String> rmqTreeBuilder = new RMQTreeBuilder<>();
        
        List<RMQTreeNode<Integer, String>> nodes = new ArrayList<>();
        
        nodes.add(new RMQTreeNode<>(2, "2"));
        nodes.add(new RMQTreeNode<>(4, "4"));
        nodes.add(new RMQTreeNode<>(1, "1"));
        nodes.add(new RMQTreeNode<>(3, "3"));
        
        RMQTreeNode<Integer, String> root = rmqTreeBuilder.buildRMQTree(nodes);
        
        RMQTreeNode<Integer, String> leftMiddleNode  = root.getLeftChild();
        RMQTreeNode<Integer, String> rightMiddleNode = root.getRightChild();
        
        RMQTreeNode<Integer, String> leaf1 = leftMiddleNode.getLeftChild();
        RMQTreeNode<Integer, String> leaf2 = leftMiddleNode.getRightChild();
        RMQTreeNode<Integer, String> leaf3 = rightMiddleNode.getLeftChild();
        RMQTreeNode<Integer, String> leaf4 = rightMiddleNode.getRightChild();
        
        assertEquals(Integer.valueOf(4), root.getKey());
        assertNull(root.getValue());
        
        assertEquals(Integer.valueOf(2), leftMiddleNode.getKey());
        assertNull(leftMiddleNode.getValue());
        
        assertEquals(Integer.valueOf(4), rightMiddleNode.getKey());
        assertNull(rightMiddleNode.getValue());
        
        assertEquals(Integer.valueOf(1), leaf1.getKey());
        assertEquals(Integer.valueOf(2), leaf2.getKey());
        assertEquals(Integer.valueOf(3), leaf3.getKey());
        assertEquals(Integer.valueOf(4), leaf4.getKey());
        
        assertEquals("1", leaf1.getValue());
        assertEquals("2", leaf2.getValue());
        assertEquals("3", leaf3.getValue());
        assertEquals("4", leaf4.getValue());
    }
    
    @Test
    public void passesOnTreeWith3Nodes() {
        RMQTreeBuilder<Integer, String> rmqTreeBuilder = new RMQTreeBuilder<>();
        
        List<RMQTreeNode<Integer, String>> nodes = new ArrayList<>();
        
        nodes.add(new RMQTreeNode<>(2, "2"));
        nodes.add(new RMQTreeNode<>(1, "1"));
        nodes.add(new RMQTreeNode<>(3, "3"));
        
        RMQTreeNode<Integer, String> root = rmqTreeBuilder.buildRMQTree(nodes);
        assertEquals(Integer.valueOf(3), root.getKey());
        assertNull(root.getValue());
        
        RMQTreeNode<Integer, String> middleInternalNode = root.getRightChild();
        assertEquals(Integer.valueOf(3), middleInternalNode.getKey());
        assertNull(middleInternalNode.getValue());
        
        RMQTreeNode<Integer, String> leaf1 = root.getLeftChild();
        RMQTreeNode<Integer, String> leaf2 = middleInternalNode.getLeftChild();
        RMQTreeNode<Integer, String> leaf3 = middleInternalNode.getRightChild();
        
        assertEquals(Integer.valueOf(1), leaf1.getKey());
        assertEquals(Integer.valueOf(2), leaf2.getKey());
        assertEquals(Integer.valueOf(3), leaf3.getKey());
        
        assertEquals("1", leaf1.getValue());
        assertEquals("2", leaf2.getValue());
        assertEquals("3", leaf3.getValue());
    }
}
