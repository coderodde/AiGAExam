package task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public final class BidirectionalDijkstrasAlgorithm<N> {
    
    public List<N> findShortestPath(N source,
                                    N target,
                                    NodeExpander<N> childrenExpander,
                                    NodeExpander<N> parentsExpander,
                                    IntWeightFunction weightFunction) {
        if (source.equals(target)) {
            return Arrays.asList(target);
        }
        
        Queue<HeapNodeWrapper<N>> queueF = new PriorityQueue<>();
        Queue<HeapNodeWrapper<N>> queueB = new PriorityQueue<>();
        Map<N, Integer> distancesF = new HashMap<>();
        Map<N, Integer> distancesB = new HashMap<>();
        Map<N, N> parentsF = new HashMap<>();
        Map<N, N> parentsB = new HashMap<>();
        Set<N> settledF = new HashSet<>();
        Set<N> settledB = new HashSet<>();
        
        queueF.add(new HeapNodeWrapper<>(source, 0));
        queueB.add(new HeapNodeWrapper<>(target, 0));
        
        distancesF.put(source, 0);
        distancesB.put(target, 0);
        
        parentsF.put(source, null);
        parentsB.put(target, null);
        
        int mu = Integer.MAX_VALUE;
        N touchNodeF = null;
        N touchNodeB = null;
        
        while (!queueF.isEmpty() && !queueB.isEmpty()) {
            N currentNodeF = queueF.remove().getNode();
            N currentNodeB = queueB.remove().getNode();
            
            settledF.add(currentNodeF);
            settledB.add(currentNodeB);
            
            for (N childNode : childrenExpander.expand(currentNodeF)) {
                if (settledF.contains(childNode)) {
                    continue;
                }
                
                if (!distancesF.containsKey(childNode)
                        || distancesF.get(childNode) > 
                           distancesF.get(currentNodeF) +
                           weightFunction.getWeight(currentNodeF, childNode)) {
                    
                    int tentativeDistance = 
                            distancesF.get(currentNodeF) + 
                            weightFunction.getWeight(currentNodeF, childNode);
                    
                    distancesF.put(childNode, tentativeDistance);
                    parentsF.put(childNode, currentNodeF);
                    queueF.add(new HeapNodeWrapper<>(childNode, 
                                                     tentativeDistance));
                } 
                
                if (settledB.contains(childNode)) {
                    int shortestPathUpperBound = 
                            distancesF.get(currentNodeF) +
                            weightFunction.getWeight(currentNodeF, childNode) +
                            distancesB.get(childNode);
                    
                    if (mu > shortestPathUpperBound) {
                        mu = shortestPathUpperBound;
                        touchNodeF = currentNodeF;
                        touchNodeB = childNode;
                    }
                }
            }
            
            for (N parentNode : parentsExpander.expand(currentNodeB)) {
                if (settledB.contains(parentNode)) {
                    continue;
                }
                
                if (!distancesB.containsKey(parentNode)
                        || distancesB.get(parentNode) >
                           distancesB.get(currentNodeB) + 
                           weightFunction.getWeight(parentNode, currentNodeB)) {
                    
                    int tentativeDistance = 
                            distancesB.get(currentNodeB) +
                            weightFunction.getWeight(parentNode, currentNodeB);
                    
                    distancesB.put(parentNode, tentativeDistance);
                    parentsB.put(parentNode, currentNodeB);
                    queueB.add(new HeapNodeWrapper<>(parentNode, 
                                                     tentativeDistance));
                }
                
                if (settledF.contains(parentNode)) {
                    int shortestPathUpperBound = 
                            distancesF.get(parentNode) +
                            weightFunction.getWeight(parentNode, currentNodeB) +
                            distancesB.get(currentNodeB);
                    
                    if (mu > shortestPathUpperBound) {
                        mu = shortestPathUpperBound;
                        touchNodeF = parentNode;
                        touchNodeB = currentNodeB;
                    }
                }
            }
            
            if (distancesF.get(currentNodeF) + 
                distancesB.get(currentNodeB) >= mu) {
                return tracebackPath(touchNodeF,
                                     touchNodeB,
                                     parentsF,
                                     parentsB);
            }
        }
        
        throw new IllegalStateException(
                "The target node is not reachable from the source node.");
    }
    
    private static <N> List<N> tracebackPath(N touchNodeF,
                                             N touchNodeB,
                                             Map<N, N> parentsF,
                                             Map<N, N> parentsB) {
        List<N> path = new ArrayList<>();
        
        N node = touchNodeF;
        
        while (node != null) {
            path.add(node);
            node = parentsF.get(node);
        }
        
        Collections.reverse(path);
        node = touchNodeB;
        
        while (node != null) {
            path.add(node);
            node = parentsB.get(node);
        }
        
        return path;
    }
    
    private static final class HeapNodeWrapper<N> 
         implements Comparable<HeapNodeWrapper<N>> {
        
        private final N node;
        private final int distance;
        
        HeapNodeWrapper(N node, int distance) {
            this.node = node;
            this.distance = distance;
        }
        
        N getNode() {
            return node;
        }

        @Override
        public int compareTo(HeapNodeWrapper<N> other) {
            return distance - other.distance;
        }
    }
}
