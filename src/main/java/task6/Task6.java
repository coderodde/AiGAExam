package task6;

import java.util.List;
import java.util.Random;
import util.Utils;

public final class Task6 {
    
    public static void main(String[] args) {
        long seed = Utils.parseSeed(args);
        Random random = new Random(seed);
        System.out.printf("Seed = %d.\n", seed);
        
        HiddenMarkovModelState startState = 
                new HiddenMarkovModelState(0, HiddenMarkovModelStateType.START);
        
        HiddenMarkovModelState endState = 
                new HiddenMarkovModelState(3, HiddenMarkovModelStateType.END);
        
        HiddenMarkovModelState codingState = 
                new HiddenMarkovModelState(
                        1,
                        HiddenMarkovModelStateType.HIDDEN);
        
        HiddenMarkovModelState noncodingState = 
                new HiddenMarkovModelState(
                        2,
                        HiddenMarkovModelStateType.HIDDEN);
        
        HiddenMarkovModel hmm = new HiddenMarkovModel(startState, 
                                                      endState,
                                                      random);
        
        // BEGIN: State transitions.
        startState.addStateTransition(noncodingState, 0.49);
        startState.addStateTransition(codingState, 0.49);
        startState.addStateTransition(endState, 0.02);
        
        codingState.addStateTransition(codingState, 0.96);
        codingState.addStateTransition(endState, 0.02);
        codingState.addStateTransition(noncodingState, 0.02);
        
        noncodingState.addStateTransition(noncodingState, 0.96);
        noncodingState.addStateTransition(codingState, 0.02);
        noncodingState.addStateTransition(endState, 0.02);
        // END: State transitions.
        
        // BEGIN: Emissions.
        codingState.addEmissionTransition('A', 0.18);
        codingState.addEmissionTransition('C', 0.32);
        codingState.addEmissionTransition('G', 0.32);
        codingState.addEmissionTransition('T', 0.18);
        
        noncodingState.addEmissionTransition('A', 0.25);
        noncodingState.addEmissionTransition('C', 0.25);
        noncodingState.addEmissionTransition('G', 0.25);
        noncodingState.addEmissionTransition('T', 0.25);
        // END: Emissions.
        
        startState.normalize();
        codingState.normalize();
        noncodingState.normalize();
        endState.normalize();
        
        for (int i = 0; i < 10; i++) {
            int lineNumber = i + 1;
            System.out.printf("%2d: %s\n", lineNumber, hmm.compose());
        }
        
        long start = System.currentTimeMillis();
        
        String sequence = "AGCG";
        
        List<HiddenMarkovModelStateSequence> stateSequenceList = 
                hmm.computeAllStatePaths(sequence);
        
        long end = System.currentTimeMillis();
        
        System.out.printf("Brute-force path inference in %d milliseconds for " +
                         "sequence \"%s\".\n",
                          end - start,
                          sequence);
        
        int lineNumber = 1;
        
        for (HiddenMarkovModelStateSequence stateSequence : stateSequenceList) {
            System.out.printf("%4d: %s\n", lineNumber++, stateSequence);
        }
        
        System.out.println("--- Viterbi ---");
        
        HiddenMarkovModelStateSequence sequence1 =
                hmm.computeAllStatePaths("AT").get(0);
        
        System.out.println("Expected state sequence: " + sequence1);
        
        HiddenMarkovModelStateSequence sequence2 =
                hmm.runViterbiAlgorithm("AT");
        
        System.out.println("The comparison sequence: " + sequence2);
        
        debugViterbi1();
    }
    
    private static void debugViterbi1() {
        System.out.println("--- debugViterbi() ---");
        
        HiddenMarkovModelState startState = 
                new HiddenMarkovModelState(
                        1,
                        HiddenMarkovModelStateType.START);
        
        HiddenMarkovModelState hiddenState = 
                new HiddenMarkovModelState(
                        2,
                        HiddenMarkovModelStateType.HIDDEN);
        
        HiddenMarkovModelState endState = 
                new HiddenMarkovModelState(
                        3,
                        HiddenMarkovModelStateType.END);
        
        startState.addStateTransition(hiddenState, 1.0);
        hiddenState.addStateTransition(hiddenState, 0.9);
        hiddenState.addStateTransition(endState, 0.1);
        hiddenState.addEmissionTransition('A', 0.3);
        hiddenState.addEmissionTransition('T', 0.7);
        
        startState.normalize();
        hiddenState.normalize();
        endState.normalize();
        
        Random random = new Random(13L);
        
        HiddenMarkovModel hmm = new HiddenMarkovModel(startState, 
                                                      endState,
                                                      random);
        
        HiddenMarkovModelStateSequence sequence = hmm.runViterbiAlgorithm("A");
        
        System.out.println(sequence);
    }
}
