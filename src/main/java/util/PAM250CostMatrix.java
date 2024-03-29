package util;

import java.util.HashMap;
import java.util.Map;

public final class PAM250CostMatrix {

    private static PAM250CostMatrix instance;

    private final Map<Character, Map<Character, Integer>> m = new HashMap<>();

    public static PAM250CostMatrix getPAM250CostMatrix() {
        if (instance == null) {
            instance = new PAM250CostMatrix();
        }

        return instance;
    } 

    private PAM250CostMatrix() {
        AminoAcidAlphabet alphabet = AminoAcidAlphabet.getAminoAcidAlphabet();

        alphabet.getCharacterSet().stream().forEach((character) -> {
            m.put(character, new HashMap<>());
        });

        m.get('A').put('A', -2);
        m.get('R').put('A', 2);
        m.get('A').put('R', 2);
        m.get('R').put('R', -6);
        m.get('N').put('A', 0);
        m.get('A').put('N', 0);
        m.get('N').put('R', 0);
        m.get('R').put('N', 0);
        m.get('N').put('N', -2);
        m.get('D').put('A', 0);
        m.get('A').put('D', 0);
        m.get('D').put('R', 1);
        m.get('R').put('D', 1);
        m.get('D').put('N', -2);
        m.get('N').put('D', -2);
        m.get('D').put('D', -4);
        m.get('C').put('A', 2);
        m.get('A').put('C', 2);
        m.get('C').put('R', 4);
        m.get('R').put('C', 4);
        m.get('C').put('N', 4);
        m.get('N').put('C', 4);
        m.get('C').put('D', 5);
        m.get('D').put('C', 5);
        m.get('C').put('C', -4);
        m.get('Q').put('A', 0);
        m.get('A').put('Q', 0);
        m.get('Q').put('R', -1);
        m.get('R').put('Q', -1);
        m.get('Q').put('N', -1);
        m.get('N').put('Q', -1);
        m.get('Q').put('D', -2);
        m.get('D').put('Q', -2);
        m.get('Q').put('C', 5);
        m.get('C').put('Q', 5);
        m.get('Q').put('Q', -4);
        m.get('E').put('A', 0);
        m.get('A').put('E', 0);
        m.get('E').put('R', 1);
        m.get('R').put('E', 1);
        m.get('E').put('N', -1);
        m.get('N').put('E', -1);
        m.get('E').put('D', -3);
        m.get('D').put('E', -3);
        m.get('E').put('C', 5);
        m.get('C').put('E', 5);
        m.get('E').put('Q', -2);
        m.get('Q').put('E', -2);
        m.get('E').put('E', -4);
        m.get('G').put('A', -1);
        m.get('A').put('G', -1);
        m.get('G').put('R', 3);
        m.get('R').put('G', 3);
        m.get('G').put('N', 0);
        m.get('N').put('G', 0);
        m.get('G').put('D', -1);
        m.get('D').put('G', -1);
        m.get('G').put('C', 3);
        m.get('C').put('G', 3);
        m.get('G').put('Q', 1);
        m.get('Q').put('G', 1);
        m.get('G').put('E', 0);
        m.get('E').put('G', 0);
        m.get('G').put('G', -5);
        m.get('H').put('A', 1);
        m.get('A').put('H', 1);
        m.get('H').put('R', -2);
        m.get('R').put('H', -2);
        m.get('H').put('N', -2);
        m.get('N').put('H', -2);
        m.get('H').put('D', -1);
        m.get('D').put('H', -1);
        m.get('H').put('C', 3);
        m.get('C').put('H', 3);
        m.get('H').put('Q', -3);
        m.get('Q').put('H', -3);
        m.get('H').put('E', -1);
        m.get('E').put('H', -1);
        m.get('H').put('G', 2);
        m.get('G').put('H', 2);
        m.get('H').put('H', -6);
        m.get('I').put('A', 1);
        m.get('A').put('I', 1);
        m.get('I').put('R', 2);
        m.get('R').put('I', 2);
        m.get('I').put('N', 2);
        m.get('N').put('I', 2);
        m.get('I').put('D', 2);
        m.get('D').put('I', 2);
        m.get('I').put('C', 2);
        m.get('C').put('I', 2);
        m.get('I').put('Q', 2);
        m.get('Q').put('I', 2);
        m.get('I').put('E', 2);
        m.get('E').put('I', 2);
        m.get('I').put('G', 3);
        m.get('G').put('I', 3);
        m.get('I').put('H', 2);
        m.get('H').put('I', 2);
        m.get('I').put('I', -5);
        m.get('L').put('A', 2);
        m.get('A').put('L', 2);
        m.get('L').put('R', 3);
        m.get('R').put('L', 3);
        m.get('L').put('N', 3);
        m.get('N').put('L', 3);
        m.get('L').put('D', 4);
        m.get('D').put('L', 4);
        m.get('L').put('C', 6);
        m.get('C').put('L', 6);
        m.get('L').put('Q', 2);
        m.get('Q').put('L', 2);
        m.get('L').put('E', 3);
        m.get('E').put('L', 3);
        m.get('L').put('G', 4);
        m.get('G').put('L', 4);
        m.get('L').put('H', 2);
        m.get('H').put('L', 2);
        m.get('L').put('I', -2);
        m.get('I').put('L', -2);
        m.get('L').put('L', -6);
        m.get('K').put('A', 1);
        m.get('A').put('K', 1);
        m.get('K').put('R', -3);
        m.get('R').put('K', -3);
        m.get('K').put('N', -1);
        m.get('N').put('K', -1);
        m.get('K').put('D', 0);
        m.get('D').put('K', 0);
        m.get('K').put('C', 5);
        m.get('C').put('K', 5);
        m.get('K').put('Q', -1);
        m.get('Q').put('K', -1);
        m.get('K').put('E', 0);
        m.get('E').put('K', 0);
        m.get('K').put('G', 2);
        m.get('G').put('K', 2);
        m.get('K').put('H', 0);
        m.get('H').put('K', 0);
        m.get('K').put('I', 2);
        m.get('I').put('K', 2);
        m.get('K').put('L', 3);
        m.get('L').put('K', 3);
        m.get('K').put('K', -5);
        m.get('M').put('A', 1);
        m.get('A').put('M', 1);
        m.get('M').put('R', 0);
        m.get('R').put('M', 0);
        m.get('M').put('N', 2);
        m.get('N').put('M', 2);
        m.get('M').put('D', 3);
        m.get('D').put('M', 3);
        m.get('M').put('C', 5);
        m.get('C').put('M', 5);
        m.get('M').put('Q', 1);
        m.get('Q').put('M', 1);
        m.get('M').put('E', 2);
        m.get('E').put('M', 2);
        m.get('M').put('G', 3);
        m.get('G').put('M', 3);
        m.get('M').put('H', 2);
        m.get('H').put('M', 2);
        m.get('M').put('I', -2);
        m.get('I').put('M', -2);
        m.get('M').put('L', -4);
        m.get('L').put('M', -4);
        m.get('M').put('K', 0);
        m.get('K').put('M', 0);
        m.get('M').put('M', -6);
        m.get('F').put('A', 4);
        m.get('A').put('F', 4);
        m.get('F').put('R', 4);
        m.get('R').put('F', 4);
        m.get('F').put('N', 4);
        m.get('N').put('F', 4);
        m.get('F').put('D', 6);
        m.get('D').put('F', 6);
        m.get('F').put('C', 4);
        m.get('C').put('F', 4);
        m.get('F').put('Q', 5);
        m.get('Q').put('F', 5);
        m.get('F').put('E', 5);
        m.get('E').put('F', 5);
        m.get('F').put('G', 5);
        m.get('G').put('F', 5);
        m.get('F').put('H', 2);
        m.get('H').put('F', 2);
        m.get('F').put('I', -1);
        m.get('I').put('F', -1);
        m.get('F').put('L', -2);
        m.get('L').put('F', -2);
        m.get('F').put('K', 5);
        m.get('K').put('F', 5);
        m.get('F').put('M', 0);
        m.get('M').put('F', 0);
        m.get('F').put('F', -9);
        m.get('P').put('A', -1);
        m.get('A').put('P', -1);
        m.get('P').put('R', 0);
        m.get('R').put('P', 0);
        m.get('P').put('N', 1);
        m.get('N').put('P', 1);
        m.get('P').put('D', 1);
        m.get('D').put('P', 1);
        m.get('P').put('C', 3);
        m.get('C').put('P', 3);
        m.get('P').put('Q', 0);
        m.get('Q').put('P', 0);
        m.get('P').put('E', 1);
        m.get('E').put('P', 1);
        m.get('P').put('G', 1);
        m.get('G').put('P', 1);
        m.get('P').put('H', 0);
        m.get('H').put('P', 0);
        m.get('P').put('I', 2);
        m.get('I').put('P', 2);
        m.get('P').put('L', 3);
        m.get('L').put('P', 3);
        m.get('P').put('K', 1);
        m.get('K').put('P', 1);
        m.get('P').put('M', 2);
        m.get('M').put('P', 2);
        m.get('P').put('F', 5);
        m.get('F').put('P', 5);
        m.get('P').put('P', -6);
        m.get('S').put('A', -1);
        m.get('A').put('S', -1);
        m.get('S').put('R', 0);
        m.get('R').put('S', 0);
        m.get('S').put('N', -1);
        m.get('N').put('S', -1);
        m.get('S').put('D', 0);
        m.get('D').put('S', 0);
        m.get('S').put('C', 0);
        m.get('C').put('S', 0);
        m.get('S').put('Q', 1);
        m.get('Q').put('S', 1);
        m.get('S').put('E', 0);
        m.get('E').put('S', 0);
        m.get('S').put('G', -1);
        m.get('G').put('S', -1);
        m.get('S').put('H', 1);
        m.get('H').put('S', 1);
        m.get('S').put('I', 1);
        m.get('I').put('S', 1);
        m.get('S').put('L', 3);
        m.get('L').put('S', 3);
        m.get('S').put('K', 0);
        m.get('K').put('S', 0);
        m.get('S').put('M', 2);
        m.get('M').put('S', 2);
        m.get('S').put('F', 3);
        m.get('F').put('S', 3);
        m.get('S').put('P', -1);
        m.get('P').put('S', -1);
        m.get('S').put('S', -3);
        m.get('T').put('A', -1);
        m.get('A').put('T', -1);
        m.get('T').put('R', 1);
        m.get('R').put('T', 1);
        m.get('T').put('N', 0);
        m.get('N').put('T', 0);
        m.get('T').put('D', 0);
        m.get('D').put('T', 0);
        m.get('T').put('C', 2);
        m.get('C').put('T', 2);
        m.get('T').put('Q', 1);
        m.get('Q').put('T', 1);
        m.get('T').put('E', 0);
        m.get('E').put('T', 0);
        m.get('T').put('G', 0);
        m.get('G').put('T', 0);
        m.get('T').put('H', 1);
        m.get('H').put('T', 1);
        m.get('T').put('I', 0);
        m.get('I').put('T', 0);
        m.get('T').put('L', 2);
        m.get('L').put('T', 2);
        m.get('T').put('K', 0);
        m.get('K').put('T', 0);
        m.get('T').put('M', 1);
        m.get('M').put('T', 1);
        m.get('T').put('F', 2);
        m.get('F').put('T', 2);
        m.get('T').put('P', 0);
        m.get('P').put('T', 0);
        m.get('T').put('S', -1);
        m.get('S').put('T', -1);
        m.get('T').put('T', -3);
        m.get('W').put('A', 6);
        m.get('A').put('W', 6);
        m.get('W').put('R', -2);
        m.get('R').put('W', -2);
        m.get('W').put('N', 4);
        m.get('N').put('W', 4);
        m.get('W').put('D', 7);
        m.get('D').put('W', 7);
        m.get('W').put('C', 8);
        m.get('C').put('W', 8);
        m.get('W').put('Q', 5);
        m.get('Q').put('W', 5);
        m.get('W').put('E', 7);
        m.get('E').put('W', 7);
        m.get('W').put('G', 7);
        m.get('G').put('W', 7);
        m.get('W').put('H', 3);
        m.get('H').put('W', 3);
        m.get('W').put('I', 5);
        m.get('I').put('W', 5);
        m.get('W').put('L', 2);
        m.get('L').put('W', 2);
        m.get('W').put('K', 3);
        m.get('K').put('W', 3);
        m.get('W').put('M', 4);
        m.get('M').put('W', 4);
        m.get('W').put('F', 0);
        m.get('F').put('W', 0);
        m.get('W').put('P', 6);
        m.get('P').put('W', 6);
        m.get('W').put('S', 2);
        m.get('S').put('W', 2);
        m.get('W').put('T', 5);
        m.get('T').put('W', 5);
        m.get('W').put('W', -17);
        m.get('Y').put('A', 3);
        m.get('A').put('Y', 3);
        m.get('Y').put('R', 4);
        m.get('R').put('Y', 4);
        m.get('Y').put('N', 2);
        m.get('N').put('Y', 2);
        m.get('Y').put('D', 4);
        m.get('D').put('Y', 4);
        m.get('Y').put('C', 0);
        m.get('C').put('Y', 0);
        m.get('Y').put('Q', 4);
        m.get('Q').put('Y', 4);
        m.get('Y').put('E', 4);
        m.get('E').put('Y', 4);
        m.get('Y').put('G', 5);
        m.get('G').put('Y', 5);
        m.get('Y').put('H', 0);
        m.get('H').put('Y', 0);
        m.get('Y').put('I', 1);
        m.get('I').put('Y', 1);
        m.get('Y').put('L', 1);
        m.get('L').put('Y', 1);
        m.get('Y').put('K', 4);
        m.get('K').put('Y', 4);
        m.get('Y').put('M', 2);
        m.get('M').put('Y', 2);
        m.get('Y').put('F', -7);
        m.get('F').put('Y', -7);
        m.get('Y').put('P', 5);
        m.get('P').put('Y', 5);
        m.get('Y').put('S', 3);
        m.get('S').put('Y', 3);
        m.get('Y').put('T', 3);
        m.get('T').put('Y', 3);
        m.get('Y').put('W', 0);
        m.get('W').put('Y', 0);
        m.get('Y').put('Y', -10);
        m.get('V').put('A', 0);
        m.get('A').put('V', 0);
        m.get('V').put('R', 2);
        m.get('R').put('V', 2);
        m.get('V').put('N', 2);
        m.get('N').put('V', 2);
        m.get('V').put('D', 2);
        m.get('D').put('V', 2);
        m.get('V').put('C', 2);
        m.get('C').put('V', 2);
        m.get('V').put('Q', 2);
        m.get('Q').put('V', 2);
        m.get('V').put('E', 2);
        m.get('E').put('V', 2);
        m.get('V').put('G', 1);
        m.get('G').put('V', 1);
        m.get('V').put('H', 2);
        m.get('H').put('V', 2);
        m.get('V').put('I', -4);
        m.get('I').put('V', -4);
        m.get('V').put('L', -2);
        m.get('L').put('V', -2);
        m.get('V').put('K', 2);
        m.get('K').put('V', 2);
        m.get('V').put('M', -2);
        m.get('M').put('V', -2);
        m.get('V').put('F', 1);
        m.get('F').put('V', 1);
        m.get('V').put('P', 1);
        m.get('P').put('V', 1);
        m.get('V').put('S', 1);
        m.get('S').put('V', 1);
        m.get('V').put('T', 0);
        m.get('T').put('V', 0);
        m.get('V').put('W', 6);
        m.get('W').put('V', 6);
        m.get('V').put('Y', 2);
        m.get('Y').put('V', 2);
        m.get('V').put('V', -4);
    }

    public Integer getCost(Character aminoAcidChar1, Character aminoAcidChar2) {
        try {
            return m.get(aminoAcidChar1).get(aminoAcidChar2);
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("Bad arguments: (" +
                    aminoAcidChar1 + ", " + aminoAcidChar2 + ")",
                    ex);
        }
    }
}