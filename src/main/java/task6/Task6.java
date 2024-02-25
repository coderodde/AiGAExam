package task6;

import java.util.List;
import java.util.Random;
import static task6.HiddenMarkovModel.sumPathProbabilities;

/**
 * This class implements the demonstration of the hidden Markov model.
 */
public final class Task6 {
    
    public static void main(String[] args) {
        
        Random random = new Random(13L);
        
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
        
        codingState.addStateTransition(codingState, 0.4);
        codingState.addStateTransition(endState, 0.3);
        codingState.addStateTransition(noncodingState, 0.3);
        
        noncodingState.addStateTransition(noncodingState, 0.3);
        noncodingState.addStateTransition(codingState, 0.35);
        noncodingState.addStateTransition(endState, 0.35);
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
        
        System.out.println("--- Composing random walks ---");
        
        for (int i = 0; i < 10; i++) {
            int lineNumber = i + 1;
            System.out.printf("%2d: %s\n", lineNumber, hmm.compose());
        }
        
        String sequence = "AGCG";
        
        List<HiddenMarkovModelStatePath> statePathSequences = 
                hmm.computeAllStatePaths(sequence);
        
        System.out.printf("Brute-force path inference for sequence \"%s\", " + 
                          "total probability = %f.\n",
                          sequence,
                          HiddenMarkovModel.sumPathProbabilities(
                                  statePathSequences));
        
        HiddenMarkovModelStatePath pathBruteForce = statePathSequences.get(0);
        HiddenMarkovModelStatePath pathEfficient = 
                hmm.runViterbiAlgorithm(sequence);
        
        System.out.printf("Brute-force most probable state path: %s.\n",
                          pathBruteForce);
        
        System.out.printf("Efficient most probable state path:   %s.\n",
                           pathEfficient);
        
        double hmmProbabilitySum = hmm.runForwardAlgorithm(sequence);
        
        System.out.printf("HMM total probability: %f.\n", hmmProbabilitySum);
        System.out.println("Brute-force state paths:");
        
        int lineNumber = 1;
        
        for (HiddenMarkovModelStatePath stateSequence : statePathSequences) {
            System.out.printf("%4d: %s\n", lineNumber++, stateSequence);
        }
        
        System.out.println("--- TEST ---");
        
        for (int i = 0; i < 5; i++) {
            String sequence2 = buildRandomSequence(random);
            
            System.out.printf("Sequence: \"%s\":", sequence2);
            
            long start = System.nanoTime();
            
            List<HiddenMarkovModelStatePath> dfsPaths =
                    hmm.computeAllStatePaths(sequence2);
            
            long end = System.nanoTime();
            
            System.out.printf("  All paths in %,d nanoseconds:\n", end - start);
            
            for (HiddenMarkovModelStatePath path : dfsPaths) {
                System.out.printf("    %s.\n", path);
            }
            
            System.out.printf("  Total probability:     %.3f.\n", 
                              sumPathProbabilities(dfsPaths));
            
            start = System.nanoTime();
            
            double p = hmm.runForwardAlgorithm(sequence2);
            
            end = System.nanoTime();
            
            System.out.printf("  HMM total probability: %.3f, duration: %,d nanoseconds.\n",
                              p,
                              end - start);
            
            System.out.printf("  Most probable brute-force path:  %s.\n",
                              dfsPaths.get(0));
            
            start = System.nanoTime();
            
            HiddenMarkovModelStatePath viterbiPath =
                    hmm.runViterbiAlgorithm(sequence2);
            
            end = System.nanoTime();
            
            System.out.printf("  Viterbi path %s in %,d nanoseconds.\n", 
                              viterbiPath,
                              end - start);
        }
    }
    
    private static String buildRandomSequence(Random random) {
        int sequenceLength = 1 + random.nextInt(10);
        StringBuilder sb = new StringBuilder(sequenceLength);
        
        for (int i = 0; i < sequenceLength; i++) {
            sb.append(getRandomCharacter(random));
        }
        
        return sb.toString();
    }
    
    private static char getRandomCharacter(Random random) {
        double coin = random.nextDouble();
        
        if (coin < 0.25) {
            return 'A';
        }
        
        if (coin < 0.5) {
            return 'T';
        }
        
        if (coin < 0.75) {
            return 'C';
        }
        
        return 'G';
    }
}
