package task1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public final class DijkstrasAlgorithm<N> {

    public List<N> find(N source, N target, ChildrenExpander<N> expander) {
        
        Queue<LatticeNodeHeapHolder> open       = new PriorityQueue<>();
        Map<LatticeNode, Integer> distanceMap   = new HashMap<>();
        Map<LatticeNode, LatticeNode> parentMap = new HashMap<>();
        
        open.add(new LatticeNodeHeapHolder<>(0, source));
    }
}
