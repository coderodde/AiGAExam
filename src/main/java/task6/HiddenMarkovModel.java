package task6;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public final class HiddenMarkovModel {
    
    /**
     * The start state of the process.
     */
    private final HiddenMarkovModelState startState;
    
    /**
     * The end state of the process.
     */
    private final HiddenMarkovModelState endState;
    
    /**
     * Maps each state ID to the HMM state having that ID.
     */
    private final Map<Integer, HiddenMarkovModelState> stateMap = 
          new HashMap<>();
    
    private final Set<HiddenMarkovModelState> hiddenStateSet = new HashSet<>();
    
    private final Random random;
    
    public HiddenMarkovModel(HiddenMarkovModelState startState,
                             HiddenMarkovModelState endState,
                             Random random) {
        
        checkAlreadyStored(startState);
        checkAlreadyStored(endState);
        this.random = random;
        this.startState = startState;
        this.endState = endState;
    }
    
    public List<List<HiddenMarkovModelState>> 
        computeAllStatePaths(String sequence) {
        int expectedStatePathSize = sequence.length() + 2;
        
        List<List<HiddenMarkovModelState>> statePaths = new ArrayList<>();
        List<HiddenMarkovModelState> currentPath = new ArrayList<>();
        Set<HiddenMarkovModelState> visited = new HashSet<>();
        
        depthFirstSearchImpl(statePaths, 
                             currentPath,
                             visited,
                             expectedStatePathSize, 
                             startState);
        
        return statePaths;
    }
    
    public String compose() {
        StringBuilder sb = new StringBuilder();
        
        HiddenMarkovModelState currentState = startState;
        
        while (true) {
            currentState = doStateTransition(currentState);
            
            if (currentState.equals(endState)) {
                return sb.toString();
            }
            
            sb.append(emit(currentState));
        }
    }
    
    private HiddenMarkovModelState 
        doStateTransition(HiddenMarkovModelState currentState) {
            
        double coin = random.nextDouble();
        
        for (Map.Entry<HiddenMarkovModelState, Double> e 
                : currentState.getFollowingStates().entrySet()) {
            
            if (coin >= e.getValue()) {
                coin -= e.getValue();
            } else {
                return e.getKey();
            }
        }
        
        throw new IllegalStateException("Should not get here.");
    }
    
    private char emit(HiddenMarkovModelState currentState) {
        double coin = random.nextDouble();
        
        for (Map.Entry<Character, Double> e 
                : currentState.getEmissions().entrySet()) {
            
            if (coin >= e.getValue()) {
                coin -= e.getValue();
            } else {
                return e.getKey();
            }
        }
        
        throw new IllegalStateException("Should not get here.");
    }
    
    private void checkAlreadyStored(HiddenMarkovModelState state) {
        if (hiddenStateSet.contains(state)) {
            throw new IllegalStateException(
                    "The input state already in this HMM.");
        }
    }
    
    private int computeNumberOfNodes() {
        Deque<HiddenMarkovModelState> queue = new ArrayDeque<>();
        Set<HiddenMarkovModelState> visited = new HashSet<>();
        
        queue.addLast(startState);
        visited.add(startState);
        
        while (!queue.isEmpty()) {
            HiddenMarkovModelState currentState = queue.removeFirst();
            
            for (HiddenMarkovModelState followerState 
                    : currentState.getFollowingStates().keySet()) {
                
                if (!visited.contains(followerState)) {
                    visited.add(followerState);
                    queue.addLast(followerState);
                }
            }
        }
        
        if (!visited.contains(endState)) {
            throw new IllegalStateException(
                    "There is no path from the start state to the end state.");
        }
        
        return visited.size();
    }
    
    private void depthFirstSearchImpl(
            List<List<HiddenMarkovModelState>> statePaths,
            List<HiddenMarkovModelState> currentPath,
            Set<HiddenMarkovModelState> visited,
            int expectedStatePathSize,
            HiddenMarkovModelState currentState) {
        
        if (currentPath.size() == expectedStatePathSize 
                && currentState.equals(endState)) {
            
            statePaths.add(new ArrayList<>(currentPath));
            currentPath.remove(currentPath.size() - 1);
            return;
        }
        
        visited.add(currentState);
        currentPath.add(currentState);
        
        for (HiddenMarkovModelState followerState 
                : currentState.getFollowingStates().keySet()) {
            
            if (visited.contains(followerState)) {
                continue;
            }
            
            depthFirstSearchImpl(statePaths,
                                 currentPath,
                                 visited,
                                 expectedStatePathSize, 
                                 followerState);    
        }
        
        visited.remove(currentState);
        currentPath.remove(currentPath.size() - 1);
    }
}
