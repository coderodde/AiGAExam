package task6;

import java.util.List;

public final class HiddenMarkovModelStateSequence 
    implements Comparable<HiddenMarkovModelStateSequence> {
    
    private final List<HiddenMarkovModelState> stateSequence;
    private final double probability;
    
    HiddenMarkovModelStateSequence(List<HiddenMarkovModelState> stateSequence,
                                   String observedSymbols,
                                   HiddenMarkovModel hiddenMarkovModel) {
        
        this.stateSequence = stateSequence;
        this.probability = computeProbability(observedSymbols);
    }
    
    public int size() {
        return stateSequence.size();
    }
    
    public HiddenMarkovModelState getState(int stateIndex) {
        return stateSequence.get(stateIndex);
    }
    
    public double getProbability() {
        return probability;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        
        boolean first = true;
        
        for (HiddenMarkovModelState state : stateSequence) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            
            sb.append(state.getId());
        }
        
        sb.append("| p = ");
        sb.append(probability);
        sb.append("]");
        return sb.toString();
    }
    
    private double computeProbability(String observedSymbols) {
        double probability = 1.0;
        
        for (int i = 1, j = 0; i < stateSequence.size() - 1; i++, j++) {
            HiddenMarkovModelState state = stateSequence.get(i);
            double tentativeProbability = 
                    state.getEmissions()
                         .get(observedSymbols.charAt(j));
            
            probability *= tentativeProbability;
        }
        
        return probability;
    }

    @Override
    public int compareTo(HiddenMarkovModelStateSequence o) {
        return Double.compare(probability, o.probability);
    }
}
