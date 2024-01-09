package task0;

import util.Codon;
import util.MultipleGroupPermuter;
import util.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

// This class solves the problem 1.3 from the book 2nd edition of the course
// book.
// 
// NOTE: If subtask 2 fails, rerun the program to generate another random source 
// protein.
//
// The program for the subtask 2 FAILS to find the codon permutation encoding
// the same source protein. It turns out that all the codon permutations S' have
// the same f-score: f(S') = f(S) for all codon permutations S' encoding S. They
// are, however, listed for illustrative purposes.
public final class Task0 {
    
    public static void main(String[] args) {
        subTask1();
        System.out.println();
        subTask2V2();
    }
    
    static void subTask1() {
        System.out.println("--- Subtask 1.3.1 ---");
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        System.out.println("seed = " + seed);
        List<String> exons = getRandomExons(random);
        System.out.println("Exons: " + exons);
        Map<String, Double> zMap = computeZMap(exons);
        System.out.println("Z-map: " + zMap);
    }
    
    static void subTask2V2() {
        System.out.println("--- Subtask 1.3.2 ---");
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        System.out.println("seed = " +  seed);
        // Protein length between 2 and 20 codons, inclusively:
        int proteinLength = 2 + random.nextInt(19);
        
        System.out.println("Source protein length: " + proteinLength);
        
        // Create a random protein as a list of codons:
        List<Codon> proteinAsCodonList = 
                generateRandomProteinAsCodonList(
                        proteinLength, 
                        random);
        
        // Convert the protein expressed as the codon list to the string of
        // amino acids:
        String proteinAsAminoAcids = 
                convertCodonListToAminoAcidString(proteinAsCodonList);
        
        System.out.println(
                "Source protein as amino acid string: " + proteinAsAminoAcids);
        
        System.out.println(
                "Source protein as a codon list:      " + proteinAsCodonList);
        
        Map<Character, Integer> proteinFrequencyMap = 
                computeProteinFrequencyMap(proteinAsAminoAcids);
        
        System.out.println("Protein amino acid frequencies: ");
        
        double sourceProteinF = 
                f(computeZMap(Arrays.asList(proteinAsAminoAcids)));
        
        System.out.println("Source protein f: " + sourceProteinF);
        
        System.out.println("Number of permutations: " + 
                computeNumberOfPermutations(proteinFrequencyMap));
        
        // The fun part starts here:
        processProtein(proteinAsAminoAcids, proteinAsCodonList);
    }
    
    private static long 
        computeNumberOfPermutations(
                Map<Character, Integer> proteinFrequencyMap) {
        long c = 1L;
        
        for (Map.Entry<Character, Integer> e : proteinFrequencyMap.entrySet()) {
            c *= Utils.factorial(e.getValue());
        }
        
        return c;
    }
    
    private static Map<Character, Integer> 
        computeProteinFrequencyMap(String protein) {
            
        Map<Character, Integer> map = new TreeMap<>();
            
        for (char aminoAcid : protein.toCharArray()) {
            if (!map.containsKey(aminoAcid)) {
                map.put(aminoAcid, 1);
            } else {
                map.put(aminoAcid, map.get(aminoAcid) + 1);
            }
        }
        
        return map;
    }
    
    private static void processProtein(String protein, List<Codon> codonList) {
        // Map each amino acid 'A' in 'protein' to the list of indices at which 
        // codons encoding 'A' appear. For example, protein "WAW" returns 
        // {'W': <0, 2>, 'A': <1>}.
        Map<Character, List<Integer>> mapAminoAcidToIndices =
                computeMapAminoAcidsToCodonIndexList(protein);
        
        // For example, convert {'W': <0, 2>, 'A': <1>} to <<0, 2>, <1>>:
        List<List<Integer>> indexGroups = getIndexGroups(mapAminoAcidToIndices);
        
        // Produce all the possible codon group permutations:
        List<List<List<Integer>>> groupPermutations = 
                new MultipleGroupPermuter<>(indexGroups)
                        .computeGroupPermutations();
        
        // BEGIN: A hash set trick: removes duplicate permutations:
        Set<List<List<Integer>>> filteredPermutations = 
                new HashSet<>(groupPermutations);
        
        groupPermutations.clear();
        groupPermutations.addAll(filteredPermutations);
        // END: A hash set trick: removes duplicate permutations.
        
        // Compute the list of all codon permutations:
        List<List<List<Integer>>> sortedIndexGroups = sort(groupPermutations);
        
        // Convert the output of the above statement into the list of protein
        // permutations:
        List<ProteinPermutation> proteinPermutationList = 
                computeProteinPermutationList(codonList, 
                                              groupPermutations,
                                              sortedIndexGroups);
        
        // Sort the protein permutations by f-scores (lowest comes first):
        proteinPermutationList.sort(new ProteinPermutationComparator());
        
        // Print the permutations:
        proteinPermutationList.forEach(System.out::println);
    }
    
    // Computes the copy of codon list index permutations into the same data 
    // structure except that the actual group lists are sorted in ascending 
    // order.
    private static <T> List<List<List<Integer>>> 
        sort(List<List<List<Integer>>> data) {
        
        List<List<List<Integer>>> copy = new ArrayList<>(data.size());
        
        for (List<List<Integer>> list : data) {
            List<List<Integer>> sortedList = new ArrayList<>(list.size());
            
            for (List<Integer> l : list) {
                List<Integer> sorted = new ArrayList<>(l);
                Collections.sort(sorted, Integer::compareTo);
                sortedList.add(sorted);
            }
            
            copy.add(sortedList);
        }
        
        return copy;
    }
    
    // Converts the group permutation data into a list of protein permutations:
    private static List<ProteinPermutation> 
        computeProteinPermutationList(
                List<Codon> proteinAsCodonList,
                List<List<List<Integer>>> groupPermutations,
                List<List<List<Integer>>> groupPermutationsSorted) {
            
        List<ProteinPermutation> proteinPermutationList = 
                new ArrayList<>(groupPermutations.size());
        
        for (int i = 0; i < groupPermutations.size(); i++) {
            // For each group permutation, do:
            List<List<Integer>> listOfIndexGroups = groupPermutations.get(i);
            List<List<Integer>> listOfIndexGroupsSorted = 
                    groupPermutationsSorted.get(i);
            
            // Obtain a permuted codon list:
            List<Codon> permutedCodonList = 
                    computePermutedProtein(proteinAsCodonList, 
                                           listOfIndexGroups,
                                           listOfIndexGroupsSorted);
            
            // Convert the above permuted codon list into a string of amino
            // acids:
            String permutedAminoAcidString = 
                    convertCodonListToAminoAcidString(permutedCodonList);
            
            // Get the f-score of the current codon list:
            double f = computeF(permutedCodonList);
            
            ProteinPermutation proteinPermutation = 
                    new ProteinPermutation(
                            f,
                            permutedAminoAcidString,
                            permutedCodonList);
            
            proteinPermutationList.add(proteinPermutation);
        }
        
        return proteinPermutationList;
    }
        
    // Computes the f-score of a codon list <C_1, ..., C_n>. In other words, 
    // returns (Z(C_1, C_2) + ... + Z(C_{n - 1}, C_n)) / n:
    private static double computeF(List<Codon> codonList) {
        List<Double> zScoreList = computeZScoreList(codonList);
        double sum = 0.0;
        
        for (double z : zScoreList) {
            sum += z;
        }
        
        return sum / zScoreList.size();
    }
        
    // Computes the list of z-scores. For codon list <C_1, C_2, ..., C_n>, 
    // returns <Z(C_1, C_2), ..., z(C_{n - 1}, C_n)>:
    private static List<Double> computeZScoreList(List<Codon> codonList) {
        List<Double> zScoreList = new ArrayList<>(codonList.size() - 1);
        Map<String, Integer> codonPairMap = computeCodonPairMap(codonList);
        
        for (int i = 0; i < codonList.size() - 1; i++) {
            Codon codon1 = codonList.get(i);
            Codon codon2 = codonList.get(i + 1);
            
            char aminoAcid1 = Utils.getAminoAcid(codon1.toString());
            char aminoAcid2 = Utils.getAminoAcid(codon2.toString());
            
            int expectedNumberOfOccurences = 
                    getFrequencyOfCodonPair(aminoAcid1, 
                                            aminoAcid2);
            
            String aminoAcidPair = 
                    String.format(
                            "%c%c", 
                            aminoAcid1,
                            aminoAcid2);
            
            int observedNumberOfOccurences = 
                    codonPairMap.get(aminoAcidPair);
            
            double zRatio = ((double) observedNumberOfOccurences) /
                            ((double) expectedNumberOfOccurences);
            
            zScoreList.add(zRatio);
        }
        
        return zScoreList;
    }
    
    // Computes a map mapping a codon PAIR to its frequency:
    private static Map<String, Integer> 
        computeCodonPairMap(List<Codon> codonList) {
        Map<String, Integer> map = new HashMap<>();
        
        for (int i = 0; i < codonList.size() - 1; i++) {
            Codon codon1 = codonList.get(i);
            Codon codon2 = codonList.get(i + 1);
            char aminoAcid1 = Utils.getAminoAcid(codon1.toString());
            char aminoAcid2 = Utils.getAminoAcid(codon2.toString());
            String aminoAcidPair = 
                    String.format(
                            "%c%c",
                            aminoAcid1,
                            aminoAcid2);
            
            map.put(aminoAcidPair,
                    map.getOrDefault(aminoAcidPair, 0) + 1);
        }
        
        return map;
    }
        
    // Converts the codon list to the amino acid string that that codon list 
    // encodes:
    private static String convertCodonListToAminoAcidString(
            List<Codon> permutedCodonList) {
        StringBuilder stringBuilder = 
                new StringBuilder(permutedCodonList.size());
        
        for (Codon codon : permutedCodonList) {
            char aminoAcid = Utils.MAP_CODON_TO_AMINOA_ACID.get(codon);
            stringBuilder.append(aminoAcid);
        }
        
        return stringBuilder.toString();
    }
        
    // The actual method for permuting a codon list:
    private static List<Codon> computePermutedProtein(
            List<Codon> proteinAsCodonList,
            List<List<Integer>> listOfIndexGroups,
            List<List<Integer>> listOfIndexGroupsSorted) {
        
        List<Codon> outputProteinAsCodonList = 
                new ArrayList<>(proteinAsCodonList.size());
        
        for (Codon codon : proteinAsCodonList) {
            outputProteinAsCodonList.add(null);
        }
        
        // Here, outputProteinAsCodonList can allocate all the codons.
        
        // Do permute:
        for (int i = 0; i < listOfIndexGroups.size(); i++) {
            List<Integer> codonGroupIndices = listOfIndexGroups.get(i);
            List<Integer> codonGroupIndicesSorted =
                    listOfIndexGroupsSorted.get(i);
            
            for (int j = 0; j < codonGroupIndices.size(); j++) {
                // Take current sorted codon index:
                int codonIndex = codonGroupIndicesSorted.get(j);
                // Take the codon to place next:
                Codon codonToPlace = proteinAsCodonList.get(codonIndex);
                // Take the index at whic to place 'codonToPlace':
                int placeIndex = codonGroupIndices.get(j);
                // Set the 'codonToPlace' to its proper location dectated by the
                // permutation:
                outputProteinAsCodonList.set(placeIndex, codonToPlace);
            }
        }
        
        return outputProteinAsCodonList;
    }    
    
    // Converts the map mapping each amino acid to its appearance index list to
    // a simple list of lists:
    private static List<List<Integer>> 
        getIndexGroups(Map<Character, List<Integer>> map) {
        
        List<List<Integer>> indexGroups = new ArrayList<>(map.size());
        
        for (Map.Entry<Character, List<Integer>> e : map.entrySet()) {
            indexGroups.add(new ArrayList<>(e.getValue()));
        }
        
        return indexGroups;
    }

    // Computes a map mapping each amino acid to the list of indices at which it
    // it appears in the input protein:
    private static Map<Character, List<Integer>>
        computeMapAminoAcidsToCodonIndexList(String protein) {
        
        Map<Character, Set<Integer>> map = new HashMap<>();

        for (int i = 0; i < protein.length(); i++) {
            Character aminoAcid = protein.charAt(i);
            
            if (!map.containsKey(aminoAcid)) {
                map.put(aminoAcid, new HashSet<>(6));
            }

            map.get(aminoAcid).add(i);
        }

        return convertMapAminoAcidsToCodonList(map);
    }
        
    private static Map<Character, List<Integer>> 
        convertMapAminoAcidsToCodonList(Map<Character, Set<Integer>> map) {
        Map<Character, List<Integer>> resultMap = new HashMap<>(map.size());
        
        for (Map.Entry<Character, Set<Integer>> e : map.entrySet()) {
            if (!resultMap.containsKey(e.getKey())) {
                resultMap.put(e.getKey(), new ArrayList<>(e.getValue()));
            }
        }
        
        return resultMap;
    }
    
    // Computes the f-score:
    static double f(Map<String, Double> m) {
        double sum = 0.0;
        
        for (Map.Entry<String, Double> e : m.entrySet()) {
            sum += e.getValue();
        }
        
        return sum / m.size();
    }
    
    // Computes the z-map over the list of exons:
    static Map<String, Double> computeZMap(List<String> exons) {
        Map<String, Integer> expectedFrequencies = 
                getCodonPairFrequencies(exons);
        
        Map<String, Double> result = new HashMap<>();
        
        for (String exon : exons) {
            for (int i = 0; i < exon.length() - 1; i++) {
                char ch1 = exon.charAt(i);
                char ch2 = exon.charAt(i + 1);
                String codon = String.format("%c%c", ch1, ch2);
                Double value = result.getOrDefault(codon, 0.0);
                value += 1.0;
                result.put(codon, value);
            }
        }
        
        for (Map.Entry<String, Integer> e : expectedFrequencies.entrySet()) {
            String codonPair = e.getKey();
            
            if (result.containsKey(codonPair)) {
                result.put(codonPair, result.get(codonPair) / e.getValue());
            }
        }
        
        return result;
    }
    
    static Map<String, Integer> getCodonPairFrequencies(List<String> exons) {
        Map<String, Integer> m = new HashMap<>();
        
        for (String exon : exons) {
            for (int i = 0; i < exon.length() - 1; i++) {
                char x = exon.charAt(i);
                char y = exon.charAt(i + 1);
                m.put("" + x + y, getFrequencyOfCodonPair(x, y));
            }
        }
        
        return m;
    }
    
    // Returns the frequency of a codon pair (x, y). Formally, returns the 
    // number of codons encoding amino acid 'x' times the number of codons
    // encoding amino acid 'y':
    static int getFrequencyOfCodonPair(char x, char y) {
        return Utils.MAP_AMINO_ACID_TO_CODON_LISTS.get(x).size() *
               Utils.MAP_AMINO_ACID_TO_CODON_LISTS.get(y).size();
    }
    
    private static List<String> getRandomExons(Random random) {
        int exons = random.nextInt(10) + 1;
        List<String> exonList = new ArrayList<>(exons);
        
        for (int i = 0; i < exons; i++) {
            String exon = getRandomExon(random);
            exonList.add(exon);
        }
        
        return exonList;
    }
    
    private static String getRandomExon(Random random) {
        int exonLength = random.nextInt(9) + 2;
        StringBuilder exonStringBuilder = new StringBuilder(exonLength);
        List<Character> alphabet = Utils.AMINO_ACID_ALPHABET_LIST;
        
        for (int i = 0; i < exonLength; i++) {
            char aminoAcid = alphabet.get(random.nextInt(alphabet.size()));
            exonStringBuilder.append(aminoAcid);
        }
        
        return exonStringBuilder.toString();
    }
    
    private static List<Codon> 
        generateRandomProteinAsCodonList(int length, Random random) {
        
        List<Codon> codonList = new ArrayList<>(length);
 
        for (int i = 0; i < length; i++) {
            int codonIndex = random.nextInt(Utils.CODONS.size());
            codonList.add(new Codon(Utils.CODONS.get(codonIndex)));
        }
        
        return codonList;
    }
}

class ProteinPermutationComparator implements Comparator<ProteinPermutation> {

    @Override
    public int compare(ProteinPermutation o1, ProteinPermutation o2) {
        return Double.compare(o1.getF(), o2.getF());
    }
}

class ProteinPermutation {

    private final double f;
    private final String proteinAsAminoAcidString;
    private final List<Codon> proteinAsCodonList;
    
    ProteinPermutation(double f, String proteinAsAminoAcidString, List<Codon> proteinAsCodonList) {
        this.f = f;
        this.proteinAsAminoAcidString = proteinAsAminoAcidString;
        this.proteinAsCodonList = new ArrayList<>(proteinAsCodonList);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(
                String.format(
                        "%f: %s %s",
                        f,
                        proteinAsAminoAcidString, 
                        convertCodonListToString()));
        
        return sb.toString();
    }
    
    double getF() {
        return f;
    }
    
    private String convertCodonListToString() {
        StringBuilder sb = new StringBuilder();
        
        for (Codon codon : proteinAsCodonList) {
            sb.append(codon)
              .append(' ');
        }
        
        return sb.toString();
    }
}
