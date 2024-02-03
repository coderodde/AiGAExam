package task4;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class Task4Test {
    
    @Test
    public void passesOnTreeWith4Nodes() {
        List<KeyValuePair<Integer, Long>> keyValuePairs = new ArrayList<>(4);
        
        keyValuePairs.add(new KeyValuePair<>(2, 2L));
        keyValuePairs.add(new KeyValuePair<>(4, 4L));
        keyValuePairs.add(new KeyValuePair<>(1, 1L));
        keyValuePairs.add(new KeyValuePair<>(3, 3L));
        
        RMQTree<?, Integer, Long> tree = 
                RMQTreeBuilder.buildRMQTree(keyValuePairs);
        
        InternalRMQTreeNode<?, Integer, Long> root = 
                (InternalRMQTreeNode<?, Integer, Long>) tree.getRoot();
        
        InternalRMQTreeNode<?, Integer, Long> leftMiddleNode  = (InternalRMQTreeNode<?, Integer, Long>) root.getLeftChild();
        InternalRMQTreeNode<?, Integer, Long> rightMiddleNode = (InternalRMQTreeNode<?, Integer, Long>) root.getRightChild();
        
        LeafRMQTreeNode<?, Integer, String> leaf1 = (LeafRMQTreeNode<?, Integer, String>) leftMiddleNode.getLeftChild();
        LeafRMQTreeNode<?, Integer, String> leaf2 = (LeafRMQTreeNode<?, Integer, String>) leftMiddleNode.getRightChild();
        LeafRMQTreeNode<?, Integer, String> leaf3 = (LeafRMQTreeNode<?, Integer, String>) rightMiddleNode.getLeftChild();
        LeafRMQTreeNode<?, Integer, String> leaf4 = (LeafRMQTreeNode<?, Integer, String>) rightMiddleNode.getRightChild();
        
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
        List<KeyValuePair<Integer, Long>> keyValuePairs = new ArrayList<>(4);
        
        keyValuePairs.add(new KeyValuePair<>(2, 2L));
        keyValuePairs.add(new KeyValuePair<>(1, 1L));
        keyValuePairs.add(new KeyValuePair<>(3, 3L));
        
        RMQTree<?, Integer, Long> tree = 
                RMQTreeBuilder.buildRMQTree(keyValuePairs);
        
        InternalRMQTreeNode<?, Integer, Long> root =
                (InternalRMQTreeNode<?, Integer, Long>) tree.getRoot();
        
        assertEquals(Integer.valueOf(3), root.getKey());
        assertEquals(Long.valueOf(1L), root.getValue());
        
        InternalRMQTreeNode<?, Integer, Long> middleInternalNode = 
                (InternalRMQTreeNode<?, Integer, Long>) root.getRightChild();
        
        assertEquals(Integer.valueOf(3), middleInternalNode.getKey());
        assertEquals(Long.valueOf(2L), middleInternalNode.getValue());
        
        LeafRMQTreeNode<?, Integer, Long> leaf1 = (LeafRMQTreeNode<?, Integer, Long>) root.getLeftChild();
        LeafRMQTreeNode<?, Integer, Long> leaf2 = (LeafRMQTreeNode<?, Integer, Long>) middleInternalNode.getLeftChild();
        LeafRMQTreeNode<?, Integer, Long> leaf3 = (LeafRMQTreeNode<?, Integer, Long>) middleInternalNode.getRightChild();
        
        assertEquals(Integer.valueOf(1), leaf1.getKey());
        assertEquals(Integer.valueOf(2), leaf2.getKey());
        assertEquals(Integer.valueOf(3), leaf3.getKey());
        
        assertEquals(Long.valueOf(1L), leaf1.getValue());
        assertEquals(Long.valueOf(2L), leaf2.getValue());
        assertEquals(Long.valueOf(3L), leaf3.getValue());
    }
}
