package task6;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class defines the hidden states of a hidden Markov model.
 */
public final class HiddenMarkovModelState {
    
    /**
     * The ID of this state. Used to differentiating between the states.
     */
    private final int id;
    
    /**
     * The state type of this state.
     */
    private final HiddenMarkovModelStateType type;
    
    /**
     * Maps each transition target to the transition probability.
     */
    private final Map<HiddenMarkovModelState, Double> transitionMap =
            new HashMap<>();
    
    /**
     * Maps each emission character to its probability. 
     */
    private final Map<Character, Double> emissionMap = new HashMap<>();
    
    public HiddenMarkovModelState(int id, HiddenMarkovModelStateType type) {
        this.id = id;
        this.type = type;
    }
    
    public Map<HiddenMarkovModelState, Double> getFollowingStates() {
        return Collections.unmodifiableMap(transitionMap);
    }
    
    public Map<Character, Double> getEmissions() {
        return Collections.unmodifiableMap(emissionMap);
    }
    
    public void addStateTransition(HiddenMarkovModelState followerState, 
                                   Double probability) {
        if (type.equals(HiddenMarkovModelStateType.END)) {
            throw new IllegalArgumentException(
                    "End HMM states may not have outgoing state transitions.");
        }
        
        transitionMap.put(followerState, probability);
        normalizeTransitionMap();
    }
    
    public void addEmissionTransition(Character character, Double probability) {
        switch (type) {
            case START:
            case END:
                throw new IllegalArgumentException(
                        "Start and end HMM states may not have emissions.");
        }
        
        emissionMap.put(character, probability);
        normalizeEmissionMap();
    }
    
    @Override
    public boolean equals(Object o) {
        return id == ((HiddenMarkovModelState) o).id;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public String toString() {
        return String.format("[HMM state, ID = %d]", id);
    }
    
    private void normalizeTransitionMap() {
        double sumOfProbabilities = computeTransitionProbabilitySum();
        
        for (Map.Entry<HiddenMarkovModelState, Double> e 
                : transitionMap.entrySet()) {
            e.setValue(e.getValue() / sumOfProbabilities);
        }
    }
    
    private void normalizeEmissionMap() {
        double sumOfProbabilities = computeEmissionProbabilitySum();
        
        for (Map.Entry<Character, Double> e : emissionMap.entrySet()) {
            e.setValue(e.getValue() / sumOfProbabilities);
        }
    }
    
    private double computeTransitionProbabilitySum() {
        double sumOfProbabilities = 0.0;
        
        for (Double probability : transitionMap.values()) {
            sumOfProbabilities += probability;
        }
        
        return sumOfProbabilities;
    }
    
    private double computeEmissionProbabilitySum() {
        double sumOfProbabilities = 0.0;
        
        for (Double probability : emissionMap.values()) {
            sumOfProbabilities += probability;
        }
        
        return sumOfProbabilities;
    }
}
