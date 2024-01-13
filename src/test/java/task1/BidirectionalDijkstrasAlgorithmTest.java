package task1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BidirectionalDijkstrasAlgorithmTest {
    
    private static final BidirectionalDijkstrasAlgorithm<DirectedGraphNode> 
            pathfinder = new BidirectionalDijkstrasAlgorithm<>();
    
    private static final DirectedGraphNodeChildrenExpander childrenExpander = 
            new DirectedGraphNodeChildrenExpander();
    
    private static final DirectedGraphNodeParentsExpander parentsExpander = 
            new DirectedGraphNodeParentsExpander();
    
    private static final DirectedGraphWeightFunction weightFunction = 
            new DirectedGraphWeightFunction();
    
    @Test
    public void singleNodeGraphNoSelfLoop() {
        DirectedGraphNode node = new DirectedGraphNode();
        List<DirectedGraphNode> path = 
                pathfinder.findShortestPath(
                        node, 
                        node,
                        childrenExpander, 
                        parentsExpander,
                        weightFunction);
        
        assertEquals(1, path.size());
        assertEquals(node, path.get(0));
    }
    
    @Test
    public void twoNodeGraph() {
        DirectedGraphNode source = new DirectedGraphNode();
        DirectedGraphNode target = new DirectedGraphNode();
        source.addChild(target, 2);
        
        List<DirectedGraphNode> path = 
                pathfinder.findShortestPath(
                        source, 
                        target,
                        childrenExpander, 
                        parentsExpander,
                        weightFunction);
        
        assertEquals(2, path.size());
        assertEquals(source, path.get(0));
        assertEquals(target, path.get(1));
    }
    
    @Test
    public void threeNodeGraph() {
        DirectedGraphNode source = new DirectedGraphNode();
        DirectedGraphNode middle = new DirectedGraphNode();
        DirectedGraphNode target = new DirectedGraphNode();
        
        source.addChild(middle, -1);
        middle.addChild(target, -2);
        source.addChild(target, -4);
        target.addChild(source, -10);
        
        List<DirectedGraphNode> path = 
                pathfinder.findShortestPath(
                        source, 
                        target,
                        childrenExpander, 
                        parentsExpander,
                        weightFunction);
        
        assertEquals(2, path.size());
        assertEquals(source, path.get(0));
        assertEquals(target, path.get(1));
    }
}

final class DirectedGraphNode {
    private static int nodeIdCounter = 0;
    private final int id;
    private final Map<DirectedGraphNode, Integer> outgoingArcs =
          new HashMap<>();
    
    private final Map<DirectedGraphNode, Integer> incomingArcs =
          new HashMap<>();
    
    DirectedGraphNode() {
        this.id = nodeIdCounter++;
    }
    
    void addChild(DirectedGraphNode child, int weight) {
        outgoingArcs.put(child, weight);
        child.incomingArcs.put(this, weight);
    }
    
    List<DirectedGraphNode> getChildren() {
        return new ArrayList<>(outgoingArcs.keySet());
    }
    
    List<DirectedGraphNode> getParents() {
        return new ArrayList<>(incomingArcs.keySet());
    }
    
    Integer getWeightTo(DirectedGraphNode headNode) {
        return outgoingArcs.get(headNode);
    }
    
    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        DirectedGraphNode other = (DirectedGraphNode) obj;
        return this.id == other.id;
    }
}

class DirectedGraphWeightFunction
        implements IntWeightFunction<DirectedGraphNode> {

    @Override
    public int getWeight(DirectedGraphNode tail, DirectedGraphNode head) {
        return tail.getWeightTo(head);
    }
}

class DirectedGraphNodeChildrenExpander 
        implements NodeExpander<DirectedGraphNode> {

    @Override
    public List<DirectedGraphNode> expand(DirectedGraphNode node) {
        return node.getChildren();
    }
}

class DirectedGraphNodeParentsExpander
        implements NodeExpander<DirectedGraphNode> {

    @Override
    public List<DirectedGraphNode> expand(DirectedGraphNode node) {
        return node.getParents();
    }
}