package task6;

import java.util.List;

public final class HiddenMarkovModelStateSequence {
    
    private final List<HiddenMarkovModelState> stateSequence;
    
    HiddenMarkovModelStateSequence(List<HiddenMarkovModelState> stateSequence) {
        this.stateSequence = stateSequence;
    }
    
    public int size() {
        return stateSequence.size();
    }
    
    public HiddenMarkovModelState getState(int stateIndex) {
        return stateSequence.get(stateIndex);
    }
}
