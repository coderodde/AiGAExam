package task1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public final class DijkstrasAlgorithm<N> {

    public List<N> findShortestPath(N source,
                                    N target, 
                                    NodeExpander<N> childrenExpander,
                                    IntWeightFunction<N> weightFunction) {
        
        Queue<HeapNodeHolder<N>> open = new PriorityQueue<>();
        Map<N, Integer> distanceMap   = new HashMap<>();
        Map<N, N> parentMap           = new HashMap<>();
        Set<N> closed                 = new HashSet<>();
        
        open.add(new HeapNodeHolder<>(0, source));
        distanceMap.put(source, 0);
        parentMap.put(source, null);
        
        while (!open.isEmpty()) {
            N currentNode = open.remove().getNode();
            
            if (currentNode.equals(target)) {
                return tracebackSolution(target, parentMap);
            }
            
            if (closed.contains(currentNode)) {
                throw new IllegalStateException("Fds");
            }
            
            closed.add(currentNode);
            
            for (N childNode : childrenExpander.expand(currentNode)) {
                if (closed.contains(childNode)) {
                    continue;
                }
                
                if (!distanceMap.containsKey(childNode)) {
                    int tentativeDistance = 
                            distanceMap.get(currentNode) +
                            weightFunction.getWeight(currentNode, childNode);
                    
                    distanceMap.put(childNode, tentativeDistance);
                    parentMap.put(childNode, currentNode);
                    open.add(new HeapNodeHolder<>(tentativeDistance, 
                                                  childNode));
                } else {
                    int tentativeDistance = 
                            distanceMap.get(currentNode) +
                            weightFunction.getWeight(currentNode, childNode);

                    if (distanceMap.get(childNode) > tentativeDistance) {
                        distanceMap.put(childNode, tentativeDistance);
                        parentMap.put(childNode, currentNode);
                        open.add(new HeapNodeHolder<>(tentativeDistance,
                                                      childNode));
                    }
                }
            }
        }
        
        throw new IllegalStateException(
                "Target not reachable from the source.");
    }
    
    private static <N> List<N> tracebackSolution(N target, Map<N, N> parentMap) {
        List<N> path = new ArrayList<>();
        N node = target;
        
        while (node != null) {
            path.add(node);
            node = parentMap.get(node);
        }
        
        Collections.reverse(path);
        return path;
    }
}
