package task4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class Task4Test {
    
    @Test
    public void passesOnTreeWith4Nodes() {
        Set<KeyValuePair<Integer, Long>> keyValuePairs = new HashSet<>(4);
        
        keyValuePairs.add(new KeyValuePair<>(2, 2L));
        keyValuePairs.add(new KeyValuePair<>(4, 4L));
        keyValuePairs.add(new KeyValuePair<>(1, 1L));
        keyValuePairs.add(new KeyValuePair<>(3, 3L));
        
        SemiDynamicRMQTree<?, Integer, Long> tree = 
                SemiDynamicRMQTreeBuilder.buildRMQTree(keyValuePairs);
        
        InternalRMQTreeNode<?, Integer, Long> root = 
                (InternalRMQTreeNode<?, Integer, Long>) tree.getRoot();
        
        InternalRMQTreeNode<?, Integer, Long> leftMiddleNode  = 
                (InternalRMQTreeNode<?, Integer, Long>) root.getLeftChild();
        
        InternalRMQTreeNode<?, Integer, Long> rightMiddleNode = 
                (InternalRMQTreeNode<?, Integer, Long>) root.getRightChild();
        
        LeafRMQTreeNode<?, Integer, Long> leaf1 = (LeafRMQTreeNode<?, Integer, Long>) leftMiddleNode.getLeftChild();
        LeafRMQTreeNode<?, Integer, Long> leaf2 = (LeafRMQTreeNode<?, Integer, Long>) leftMiddleNode.getRightChild();
        LeafRMQTreeNode<?, Integer, Long> leaf3 = (LeafRMQTreeNode<?, Integer, Long>) rightMiddleNode.getLeftChild();
        LeafRMQTreeNode<?, Integer, Long> leaf4 = (LeafRMQTreeNode<?, Integer, Long>) rightMiddleNode.getRightChild();
        
        assertEquals(Long.valueOf(1L), root.getValue());
        
        assertEquals(Long.valueOf(1L), leftMiddleNode.getValue());
        
        assertEquals(Long.valueOf(3L), rightMiddleNode.getValue());
        
        assertEquals(Integer.valueOf(1), leaf1.getKey());
        assertEquals(Integer.valueOf(2), leaf2.getKey());
        assertEquals(Integer.valueOf(3), leaf3.getKey());
        assertEquals(Integer.valueOf(4), leaf4.getKey());
        
        assertEquals(Long.valueOf(1L), leaf1.getValue());
        assertEquals(Long.valueOf(2L), leaf2.getValue());
        assertEquals(Long.valueOf(3L), leaf3.getValue());
        assertEquals(Long.valueOf(4L), leaf4.getValue());
        
        assertEquals(Long.valueOf(1L), 
                     tree.getRangeMinimum(Integer.valueOf(1),
                                          Integer.valueOf(2)));
        
        assertEquals(Long.valueOf(3L),
                     tree.getRangeMinimum(Integer.valueOf(3), 
                                          Integer.valueOf(4)));
        
        assertEquals(Long.valueOf(2L),
                     tree.getRangeMinimum(Integer.valueOf(2), 
                                          Integer.valueOf(4)));
        
        assertEquals(Long.valueOf(1L),
                     tree.getRangeMinimum(Integer.valueOf(1), 
                                          Integer.valueOf(4)));
        tree.update(4, -1L);
        
        assertEquals(Long.valueOf(-1L), leaf4.getValue());
        assertEquals(Long.valueOf(-1L), rightMiddleNode.getValue());
        assertEquals(Long.valueOf(-1L), root.getValue());
    }
    
    @Test
    public void passesOnTreeWith3Nodes() {
        Set<KeyValuePair<Integer, Long>> keyValuePairs = new HashSet<>(4);
        
        keyValuePairs.add(new KeyValuePair<>(2, 2L));
        keyValuePairs.add(new KeyValuePair<>(1, 1L));
        keyValuePairs.add(new KeyValuePair<>(3, 3L));
        
        SemiDynamicRMQTree<?, Integer, Long> tree = 
                SemiDynamicRMQTreeBuilder.buildRMQTree(keyValuePairs);
        
        InternalRMQTreeNode<?, Integer, Long> root =
                (InternalRMQTreeNode<?, Integer, Long>) tree.getRoot();
        
        assertEquals(Long.valueOf(1L), root.getValue());
        
        InternalRMQTreeNode<?, Integer, Long> middleInternalNode = 
                (InternalRMQTreeNode<?, Integer, Long>) root.getRightChild();
        
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
