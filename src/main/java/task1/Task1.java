package task1;

import java.util.List;
import java.util.Map;
import java.util.Random;
import util.PAM250CostMatrix;
import util.Utils;

public final class Task1 {
    
    private static final int GAP_PENALTY = 4;
    
    public static void main(String[] args) {
        long seed = Utils.parseSeed(args);
        Random random = new Random(seed);
        System.out.println("Seed = " + seed);
        
        MultipleSequenceAlignmentInstance msai = 
                getRandomMSAInstance(random, 5);
        
        long start = System.currentTimeMillis();
        DijkstrasAlgorithm dijkstrasAlgorithm = new DijkstrasAlgorithm();
        
        List<LatticeNode> path1 =
                dijkstrasAlgorithm.findShortestPath(
                        msai.getSourceNode(), 
                        msai.getTargetNode(),
                        new LatticeNodeChildrenNodeExpander(), 
                        new LatticeWeightFunction(msai));
        
        System.out.printf(
                "Dijkstra's algorithm in %d milliseconds.\n", 
                System.currentTimeMillis() - start);
        
        start = System.currentTimeMillis();
        BidirectionalDijkstrasAlgorithm bidirectionalDijkstrasAlgorithm = 
                new BidirectionalDijkstrasAlgorithm();
        
        List<LatticeNode> path2 = 
                bidirectionalDijkstrasAlgorithm.findShortestPath(
                        msai.getSourceNode(), 
                        msai.getTargetNode(), 
                        new LatticeNodeChildrenNodeExpander(), 
                        new LatticeNodeParentsNodeExpander(), 
                        new LatticeWeightFunction(msai));
        
        System.out.printf(
                "Bidirectional Dijsktra's algorithm in %d milliseconds.\n",
                System.currentTimeMillis() - start);
    }
    
    private static MultipleSequenceAlignmentInstance 
        getRandomMSAInstance(
                Random random, 
                int numberOfSequences) {
        
        ProbabilityDistribution<Character> probabilityDistribution = 
                getAlphabetProbabilityDistribution(random);
        
        String[] sequences = new String[numberOfSequences];
        
        for (int i = 0; i < sequences.length; i++) {
            String sequence = getRandomSequence(random, 
                                                probabilityDistribution);
            
            sequences[i] = sequence;
        }
        
        MultipleSequenceAlignmentInstance instance =
                new MultipleSequenceAlignmentInstance(
                        PAM250CostMatrix.getPAM250CostMatrix(), 
                        GAP_PENALTY, 
                        sequences);
        
        return instance;
    }
    
    private static String 
        getRandomSequence(
                Random random, 
                ProbabilityDistribution<Character> probabilityDistribution) {
            
        int length = 4 + random.nextInt(10); // Between 10 and 40.
        StringBuilder sb = new StringBuilder(length);
        
        for (int i = 0; i < length;) {
            char aminoAcid = probabilityDistribution.sampleElement();
            
            if (aminoAcid == '<' || aminoAcid == '>') {
                // Omit starting/concluding amino acids:
                continue;
            }
            
            sb.append(aminoAcid);
            i++;
        }
        
        return sb.toString();
    }
    
    private static ProbabilityDistribution<Character> 
        getAlphabetProbabilityDistribution(Random random) {
            
        ProbabilityDistribution probabilityDistribution = 
                new ProbabilityDistribution(random);
        
        for (Map.Entry<Character, List<String>> e 
                : Utils.MAP_AMINO_ACID_TO_CODON_LISTS.entrySet()) {
            
            probabilityDistribution.addElement(e.getKey(), e.getValue().size());
        }
        
        return probabilityDistribution;
    }
}
