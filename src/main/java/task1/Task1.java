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
        LatticeWeightFunction weightFunction = new LatticeWeightFunction(msai);
        
        List<LatticeNode> path1 =
                dijkstrasAlgorithm.findShortestPath(
                        msai.getSourceNode(), 
                        msai.getTargetNode(),
                        new LatticeNodeChildrenNodeExpander(), 
                        weightFunction);
        
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
                        weightFunction);
        
        System.out.printf(
                "Bidirectional Dijsktra's algorithm in %d milliseconds.\n\n",
                System.currentTimeMillis() - start);
        
        System.out.println("Dijkstra's algorithm returned:");
        printPath(path1);
        System.out.println();
        
        System.out.println("Bidirectional Dijkstra's algorithm returned:");
        printPath(path2);
        
        System.out.println("Dijkstra's algorithm path cost: " + 
                getLatticePathCost(path1, weightFunction));
        
        System.out.println("Bidirectional Dijkstra's algorithm path cost: " +
                getLatticePathCost(path2, weightFunction));
        
        System.out.println();
        
        System.out.printf("Dijkstra's alignment:\n%s\n", 
                          msai.computeAlignment(path1));
        
        System.out.println();
        
        System.out.printf("Bidirectional Dijkstra's alignment:\n%s", 
                msai.computeAlignment(path2));
    }
    
    private static void printPath(List<LatticeNode> path) {
        String maximumLineNumberString = Integer.toString(path.size());
        String format = "%" + maximumLineNumberString.length() + "d: %s\n";
        int lineNumber = 1;
        
        for (LatticeNode latticeNode : path) {
            System.out.printf(format, lineNumber++, latticeNode);
        }
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
            
        int length = random.nextInt(10) + 5;
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
        
    private static int getLatticePathCost(
            List<LatticeNode> path, 
            LatticeWeightFunction weightFunction) {
        int cost = 0;
        
        for (int i = 0; i < path.size() - 1; i++) {
            cost += weightFunction.getWeight(path.get(i),
                                             path.get(i + 1));
        }
        
        return cost;
    }
}
