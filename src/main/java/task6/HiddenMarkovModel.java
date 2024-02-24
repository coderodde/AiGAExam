package task6;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public final class HiddenMarkovModel {
    
    private static final double UNSET_PROBABILITY = -1.0;
    
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
        
    public HiddenMarkovModelStateSequence runViterbiAlgorithm(String sequence) {
        
        Set<HiddenMarkovModelState> graph = computeAllStates();
        
        if (!graph.contains(endState)) {
            throw new IllegalStateException("End state is unreachable.");
        }
        
        Map<Integer, HiddenMarkovModelState> stateMap = 
                new HashMap<>(graph.size());
        
        Map<HiddenMarkovModelState, Integer> inverseStateMap = 
                new HashMap<>(graph.size());
        
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
        
        double[][] viterbiMatrix =
                computeViterbiMatrix(
                        sequence,
                        stateMap,
                        inverseStateMap);
        
        return tracebackStateSequence(viterbiMatrix,
                                      sequence,
                                      stateMap,
                                      inverseStateMap);
    }
    
    private double[][] computeViterbiMatrix(
            String sequence,
            Map<Integer, HiddenMarkovModelState> stateMap,
            Map<HiddenMarkovModelState, Integer> inverseStateMap) {
        
        double[][] viterbiMatrix = new double[sequence.length() + 1]
                                             [stateMap.size()];
        
        for (int rowIndex = 1; rowIndex < viterbiMatrix.length; rowIndex++) {
            Arrays.fill(viterbiMatrix[rowIndex], UNSET_PROBABILITY);
        }
        
        viterbiMatrix[0][0] = 1.0;
        
        for (int columnIndex = 1; 
                 columnIndex < viterbiMatrix[0].length;
                 columnIndex++) {
            
            viterbiMatrix[0][columnIndex] = 0.0;
        }
        
        for (int h = 1; h < viterbiMatrix[0].length - 1; h++) {
            viterbiMatrix[sequence.length()][h] = 
                computeViterbiMatrixImpl(
                        sequence.length(),
                        h,
                        viterbiMatrix, 
                        sequence, 
                        stateMap,
                        inverseStateMap);
        }
        
        return viterbiMatrix;
    }
    
    /**
     * Computes the actual Viterbi matrix.
     * 
     * @param i             the {@code i} variable; the length of the sequence 
     *                      prefix.
     * @param h             the state index.
     * @param viterbiMatrix the actual Viterbi matrix under construction.
     * @param sequence      the symbol sequence.
     * @param stateMap      the map mapping state IDs to states.
     * @return 
     */
    private double computeViterbiMatrixImpl(
            int i,
            int h,
            double[][] viterbiMatrix,
            String sequence,
            Map<Integer, HiddenMarkovModelState> stateMap,
            Map<HiddenMarkovModelState, Integer> inverseStateMap) {
        
        if (viterbiMatrix[i][h] != UNSET_PROBABILITY) {
            return viterbiMatrix[i][h];
        }
        
        final int NUMBER_OF_STATES = stateMap.size();
        
        if (h >= NUMBER_OF_STATES - 1) {
            return UNSET_PROBABILITY;
        }
        
        if (h == 0) {
            return i == 0 ? 1.0 : -1.0;
        }
        
        char symbol = sequence.charAt(i - 1);
        
        HiddenMarkovModelState state = stateMap.get(h);
        double psih = state.getEmissions().get(symbol);
                
        Set<HiddenMarkovModelState> parentStates = state.getIncomingStates();
        
        double maximumProbability = Double.NEGATIVE_INFINITY;
        
        for (HiddenMarkovModelState parent : parentStates) {
            double v = 
                    computeViterbiMatrixImpl(
                            i - 1, 
                            inverseStateMap.get(parent),
                            viterbiMatrix, 
                            sequence,
                            stateMap, 
                            inverseStateMap);
            
            v *= parent.getFollowingStates().get(state);
            maximumProbability = Math.max(maximumProbability, v);
        }
        
        viterbiMatrix[i][h] = maximumProbability * psih;
        return viterbiMatrix[i][h];
    }
    
    private HiddenMarkovModelStateSequence 
        tracebackStateSequence(
                double[][] viterbiMatrix,
                String sequence,
                Map<Integer, HiddenMarkovModelState> stateMap,
                Map<HiddenMarkovModelState, Integer> inverseStateMap) {
            
        int bottomMaximumIndex = computeBottomMaximumIndex(viterbiMatrix);
        
        HiddenMarkovModelState bottomState = stateMap.get(bottomMaximumIndex);
        
        List<HiddenMarkovModelState> stateList = 
                new ArrayList<>(viterbiMatrix[0].length);
        
        stateList.add(endState);
        
        final int HIGHEST_I = viterbiMatrix.length - 1;
        
        tracebackStateSequenceImpl(viterbiMatrix,
                                   HIGHEST_I, 
                                   bottomState, 
                                   stateList, 
                                   inverseStateMap);
        
        stateList.add(startState);
        
        Collections.reverse(stateList);
        
        return new HiddenMarkovModelStateSequence(stateList, sequence, this);
    }
        
    private void tracebackStateSequenceImpl(
            double[][] viterbiMatrix,
            int i,
            HiddenMarkovModelState state,
            List<HiddenMarkovModelState> stateList,
            Map<HiddenMarkovModelState, Integer> inverseStateMap) {
        
        if (state.equals(startState)) {
            return;
        }
        
        stateList.add(state);
        
        HiddenMarkovModelState nextState = 
                computeNextState(viterbiMatrix, 
                                 i,
                                 state,
                                 inverseStateMap);
        
        tracebackStateSequenceImpl(viterbiMatrix, 
                                   i - 1,
                                   nextState,
                                   stateList,
                                   inverseStateMap);
    }
    
    private HiddenMarkovModelState
         computeNextState(
                 double[][] viterbiMatrix, 
                 int i, 
                 HiddenMarkovModelState state,
                 Map<HiddenMarkovModelState, Integer> inverseStateMap) {
             
        Set<HiddenMarkovModelState> parents = state.getIncomingStates();
        HiddenMarkovModelState nextState = null;
        
        double maximumProbability = Double.NEGATIVE_INFINITY;
        
        for (HiddenMarkovModelState parent : parents) {
            int parentIndex = inverseStateMap.get(parent);
            
            double probability = parent.getFollowingStates().get(state);
            probability *= viterbiMatrix[i - 1][parentIndex];
            
            if (maximumProbability < probability) {
                maximumProbability = probability;
                nextState = parent;
            }
        }
        
        return nextState;
    }
        
    private int computeBottomMaximumIndex(double[][] viterbiMatrix) {
        int maximumIndex = -1;
        double maximumProbability = Double.NEGATIVE_INFINITY;
        final int ROW_INDEX = viterbiMatrix.length - 1;
        
        for (int i = 1; i < viterbiMatrix[0].length; i++) {
            double currentProbability = viterbiMatrix[ROW_INDEX][i];
            
            if (maximumProbability < currentProbability) {
                maximumProbability = currentProbability;
                maximumIndex = i;
            }
        }
        
        return maximumIndex;
    }
    
//    private void tracebackStateSequenceImpl(
//            double[][] v,
//            int i,
//            int h,
//            Map<Integer, HiddenMarkovModelState> startMap,
//            Map<HiddenMarkovModelState, Integer> inverseStateMap,
//            List<HiddenMarkovModelState> tentativeStateList) {
//        
//        
//    }
//
//    private HiddenMarkovModelStateSequence 
//        tracebackStateSequence(
//                double[][] v,
//                String sequence,
//                Map<Integer, HiddenMarkovModelState> stateMap,
//                Map<HiddenMarkovModelState, Integer> inverseStateMap) {
//        
//        List<HiddenMarkovModelState> stateList = new ArrayList<>(v[0].length);
//        stateList.add(endState);
//        
//        for (int i = v.length - 1; i > 0; i--) {
//            for (int h = 1; h < v[0].length - 1; h++) {
//                int index = getIndexOfMaximumProbability(
//                        i,
//                        h,
//                        v,
//                        stateMap, 
//                        inverseStateMap);
//                
//                stateList.add(stateMap.get(index));
//            }
//        }
//            
//        stateList.add(startState);
//        Collections.reverse(stateList);
//        
//        return new HiddenMarkovModelStateSequence(stateList, sequence, this);
//    }
        
    /**
     * This method scans the row {@code v[i]} and returns the index of the entry
     * with maximum probability.
     * 
     * @param viterbiMatrix  the Viterbi matrix.
     * @param targetRowIndex the target row index
     * @return the index of the most probable entry in row {@code i - 1}.
     */
    private int getIndexOfMaximumProbability(
            int i,
            int h,
            double[][] viterbiMatrix, 
            Map<Integer, HiddenMarkovModelState> stateMap,
            Map<HiddenMarkovModelState, Integer> inverseStateMap) {
        
        HiddenMarkovModelState targetState = stateMap.get(h);
        Set<HiddenMarkovModelState> parents = targetState.getIncomingStates();
        double maximumProbability = Double.NEGATIVE_INFINITY;
        int maximumProbabilityIndex = -1;
        
        for (HiddenMarkovModelState parent : parents) {
            int parentIndex = inverseStateMap.get(parent);
            
            double p = parent.getFollowingStates().get(targetState);
            p *= viterbiMatrix[i - 1][parentIndex];
            
            if (maximumProbability < p) {
                maximumProbability = p;
                maximumProbabilityIndex = parentIndex;
            }
        }
        
        return maximumProbabilityIndex;
    }
    
    public String compose() {
        StringBuilder sb = new StringBuilder();
        
        HiddenMarkovModelState currentState = startState;
        
        while (true) {
            currentState = doStateTransition(currentState);
            
            if (currentState.equals(endState)) {
                return sb.toString();
            }
            
            sb.append(doEmit(currentState));
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
    
    private char doEmit(HiddenMarkovModelState currentState) {
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
