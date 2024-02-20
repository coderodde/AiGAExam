package task6;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
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
    
    public List<HiddenMarkovModelStateSequence> 
        computeAllStatePaths(String sequence) {
            
        int expectedStatePathSize = sequence.length() + 2;
        
        List<List<HiddenMarkovModelState>> statePaths = new ArrayList<>();
        List<HiddenMarkovModelState> currentPath = new ArrayList<>();
        
        currentPath.add(startState);
        
        depthFirstSearchImpl(statePaths, 
                             currentPath,
                             expectedStatePathSize, 
                             startState);
        
        List<HiddenMarkovModelStateSequence> sequenceList = 
                new ArrayList<>(statePaths.size());
        
        for (List<HiddenMarkovModelState> statePath : statePaths) {
            sequenceList.add(
                    new HiddenMarkovModelStateSequence(
                            statePath, 
                            sequence,
                            this));
        }
        
        Collections.sort(sequenceList);
        Collections.reverse(sequenceList);
        
        return sequenceList;
    }
        
    public HiddenMarkovModelStateSequence runViterbi(String sequence) {
        Set<HiddenMarkovModelState> graph = computeAllStates();
        
        if (!graph.contains(endState)) {
            throw new IllegalStateException("End state is unreachable.");
        }
        
        Map<Integer, HiddenMarkovModelState> stateMap = 
                new HashMap<>(graph.size());
        
        Map<HiddenMarkovModelState, Integer> inverseStateMap = new HashMap<>();
        
        Map<Integer, Character> characterMap = new HashMap<>(sequence.length());
        
        stateMap.put(0, startState);
        stateMap.put(graph.size() - 1, endState);
        
        inverseStateMap.put(startState, 0);
        inverseStateMap.put(endState, graph.size() - 1);
        
        int stateId = 1;
        
        for (HiddenMarkovModelState state : graph) {
            if (!state.equals(startState) && !state.equals(endState)) {
                stateMap.put(stateId, state);
                inverseStateMap.put(state, stateId);
                stateId++;
            }
        }
        
        for (int i = 0; i < sequence.length(); i++) {
            characterMap.put(i, sequence.charAt(i));
        }
        
        double[][] v = new double[sequence.length() + 1]
                                 [graph.size()];
        
        v[0][0] = 1.0;
        
        for (int i = 1; i < graph.size(); i++) {
            v[0][i] = 0.0;
        }
        
        v(sequence.length(), 
          graph.size(),
          characterMap, 
          stateMap, 
          inverseStateMap);
        
        return tracebackStateSequence(v, sequence, stateMap);
    }
    
    private double v(int i,
                     int h,
                     Map<Integer, Character> sequenceMap,
                     Map<Integer, HiddenMarkovModelState> stateMap,
                     Map<HiddenMarkovModelState, Integer> inverseStateMap) {
        
        char symbol = sequenceMap.get(i);
        HiddenMarkovModelState state = stateMap.get(h - 1);
        
        double psih = state.getEmissions().get(symbol);
        double pmax = Double.NEGATIVE_INFINITY;
        
        Set<HiddenMarkovModelState> parentsOfH = state.getIncomingStates();
        
        for (HiddenMarkovModelState parent : parentsOfH) {
            double ptmp = 
                    v(i - 1,
                      inverseStateMap.get(parent), 
                      sequenceMap,
                      stateMap,
                      inverseStateMap);
            
            ptmp *= parent.getFollowingStates().get(state);
            pmax = Math.max(pmax, ptmp);
        }
        
        return psih * pmax;
    }
    
    private HiddenMarkovModelStateSequence 
        tracebackStateSequence(
                double[][] v,
                String sequence,
                Map<Integer, HiddenMarkovModelState> stateMap) {
        
        List<HiddenMarkovModelState> stateList = new ArrayList<>(v.length);
        
        for (int i = v.length - 1; i > 0; i--) {
            int index = getArgMaxIndex(v, i);
            stateList.add(stateMap.get(index));
        }
            
        return new HiddenMarkovModelStateSequence(stateList, sequence, this);
    }
        
    /**
     * This method scans the row {@code v[i - 1]} and returns the index of the
     * entry with maximum probability.
     * 
     * @param v the matrix.
     * @param i the target row index
     * @return the index of the most probable entry in row {@code i - 1}.
     */
    private int getArgMaxIndex(double[][] v, int i) {
        int maxIndex = -1;
        double maxProbability = Double.NEGATIVE_INFINITY;
        
        for (int j = 0; j != v[0].length; j++) {
            double currentProbability = v[i - 1][j];
            
            if (maxProbability < currentProbability) {
                maxProbability = currentProbability;
                maxIndex = j;
            }
        }
        
        return maxIndex;
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
    
    private Set<HiddenMarkovModelState> computeAllStates() {
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
        
        return visited;
    }
    
    private void depthFirstSearchImpl(
            List<List<HiddenMarkovModelState>> statePaths,
            List<HiddenMarkovModelState> currentPath,
            int expectedStatePathSize,
            HiddenMarkovModelState currentState) {
        
        if (currentPath.size() == expectedStatePathSize) {
            
            if (currentState.equals(endState)) {
                statePaths.add(new ArrayList<>(currentPath));
            }
            
            return;
        }
        
        for (HiddenMarkovModelState followerState 
                : currentState.getFollowingStates().keySet()) {
            
            currentPath.add(followerState);
            
            depthFirstSearchImpl(statePaths,
                                 currentPath,
                                 expectedStatePathSize, 
                                 followerState);
            
            currentPath.remove(currentPath.size() - 1);
        }
    }
}
