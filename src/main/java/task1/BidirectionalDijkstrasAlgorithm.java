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
        
        Queue<HeapNodeHolder<N>> openA = new PriorityQueue<>();
        Queue<HeapNodeHolder<N>> openB = new PriorityQueue<>();
        Set<N> closedA = new HashSet<>();
        Set<N> closedB = new HashSet<>();
        Map<N, Integer> distanceMapA = new HashMap<>();
        Map<N, Integer> distanceMapB = new HashMap<>();
        Map<N, N> parentsMapA = new HashMap<>();
        Map<N, N> parentsMapB = new HashMap<>();
        int upperCostBound; 
        N touchNode;
        
        // queueA - the open list in the forward direction.
        // queueB - the open list in the backward direction.
        // closedA - the closed list in the forward direction.
        // closedB - the closed list in the backward direction.
        // distanceMapA - the distance map in the forward direction.
        // distanceMapB - the distance map in the backward direction.
        // parentsMapA - the parent map in the forward direction.
        // parentsMapB - the parent map in the backward direction.
        
        // Initializing state:
        openA.add(new HeapNodeHolder<>(0, source));
        openB.add(new HeapNodeHolder<>(0, target));
        
        distanceMapA.put(source, 0);
        distanceMapB.put(target, 0);
        
        parentsMapA.put(source, null);
        parentsMapB.put(target, null);
        
        upperCostBound = Integer.MAX_VALUE;
        touchNode = null;
        
        while (!openA.isEmpty() && !openB.isEmpty()) {
            int deltaA = distanceMapA.get(openA.peek().getNode());
            int deltaB = distanceMapB.get(openB.peek().getNode());
            
            if (touchNode != null && upperCostBound < deltaA + deltaB) {
                return tracebackPath(touchNode, 
                                     parentsMapA, 
                                     parentsMapB);
            }
            
            int searchFrontierSizeA = openA.size() + closedA.size();
            int searchFrontierSizeB = openB.size() + closedB.size();
            
            if (searchFrontierSizeA < searchFrontierSizeB) {
                N currentNode = openA.remove().getNode();
                
                closedA.add(currentNode);
                
                for (N childNode : childrenExpander.expand(currentNode)) {
                    if (closedA.contains(childNode)) {
                        continue;
                    }
                    
                    int tentativeScoreA = 
                            distanceMapA.get(currentNode) + 
                            weightFunction.getWeight(currentNode, childNode);
                    
                    if (!distanceMapA.containsKey(childNode) ||
                            distanceMapA.get(childNode) > tentativeScoreA) {
                        distanceMapA.put(childNode, tentativeScoreA);
                        parentsMapA.put(childNode, currentNode);
                        openA.add(new HeapNodeHolder<>(tentativeScoreA, 
                                                       childNode));
                        
                        if (closedB.contains(childNode)) {
                            int pathLength = 
                                    tentativeScoreA + 
                                    distanceMapB.get(childNode);
                            
                            if (upperCostBound > pathLength) {
                                upperCostBound = pathLength;
                                touchNode = childNode;
                            }
                        }
                    }
                }
            } else {
                N currentNode = openB.remove().getNode();
                closedB.add(currentNode);
                
                for (N parentNode : parentsExpander.expand(currentNode)) {
                    if (closedB.contains(parentNode)) {
                        continue;
                    }
                    
                    int tentativeScoreB = 
                            distanceMapB.get(currentNode) + 
                            weightFunction.getWeight(parentNode, currentNode);
                    
                    if (!distanceMapB.containsKey(parentNode) ||
                            distanceMapB.get(parentNode) > tentativeScoreB) {
                        distanceMapB.put(parentNode, tentativeScoreB);
                        parentsMapB.put(parentNode, currentNode);
                        openB.add(new HeapNodeHolder<>(tentativeScoreB, 
                                                       parentNode));
                        
                        if (closedA.contains(parentNode)) {
                            int pathLength = tentativeScoreB + 
                                             distanceMapA.get(parentNode);
                            
                            if (upperCostBound > pathLength) {
                                upperCostBound = pathLength;
                                touchNode = parentNode;
                            }
                        }
                    }
                }
            }
        }
        
        throw new IllegalStateException(
                "Target node not reachable from the soruce node.");
    }
    
    private static <N> List<N> tracebackPath(N touchNode,
                                             Map<N, N> parentsMapA,
                                             Map<N, N> parentsMapB) {
        List<N> path = new ArrayList<>();
        N currentNode = touchNode;
        
        while (currentNode != null) {
            path.add(currentNode);
            currentNode = parentsMapA.get(currentNode);
        }
        
        Collections.reverse(path);
        
        currentNode = parentsMapB.get(touchNode);
        
        while (currentNode != null) {
            path.add(currentNode);
            currentNode = parentsMapB.get(currentNode);
        }
        
        return path;
    }
}
