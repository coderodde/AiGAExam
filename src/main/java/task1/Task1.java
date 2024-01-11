package task1;

import java.util.List;
import java.util.Random;
import util.Utils;

public final class Task1 {
    
    private static String getRandomSequence(int sequence, Random random) {
        int length = 10 + random.nextInt(31); // Between 10 and 40.
        
        StringBuilder sb = new StringBuilder(length);
        List<Character> alphabet = Utils.AMINO_ACID_ALPHABET_LIST;
        
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.get(random.nextInt(alphabet.size())));
        }
        
        return sb.toString();
    }
}
