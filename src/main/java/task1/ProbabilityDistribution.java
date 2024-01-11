package task1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
 
/**
 * This class implements a probability distribution data structure that
 * maintains an accumulated sum of weights and thus allows sampling the elements
 * in worst-case logarithmic time.
 */
public class ProbabilityDistribution<E> {
 
    /**
     * This class implements the actual entry in the distribution.
     *
     * @param <E> the actual element type.
     */
    private static final class Entry<E> {
 
        private final E element;
 
        private double weight;
 
        private double accumulatedWeight;
 
        Entry(E element, double weight, double accumulatedWeight) {
            this.element = element;
            this.weight = weight;
            this.accumulatedWeight = accumulatedWeight;
        }
 
        E getElement() {
            return element;
        }
 
        double getWeight() {
            return weight;
        }
 
        void setWeight(double weight) {
            this.weight = weight;
        }
 
        double getAccumulatedWeight() {
            return accumulatedWeight;
        }
 
        void addAccumulatedWeight(double delta) {
            accumulatedWeight += delta;
        }
    }
    
    private double totalWeight;
    private final Random random;
    
    /**
     * This map maps each element stored in this probability distribution to its
     * respective entry.
     */
    private final Map<E, Entry<E>> map = new HashMap<>();
 
    /**
     * Holds the actual distribution entries.
     */
    private final List<Entry<E>> storage = new ArrayList<>();
 
    /**
     * Constructs this probability distribution with given random number
     * generator.
     *
     * @param random the random number generator.
     */
    public ProbabilityDistribution(Random random) {
        this.random = random;
    }
    
    public boolean addElement(E element, double weight) {
        checkWeight(weight);
        Entry<E> entry = map.get(element);
 
        if (entry == null) {
            entry = new Entry<>(element, weight, totalWeight);
            map.put(element, entry);
            storage.add(entry);
        } else {
            for (int i = storage.indexOf(entry); i < storage.size(); ++i) {
                storage.get(i).addAccumulatedWeight(weight);
            }
        }
 
        totalWeight += weight;
        return true;
    }
    
    public E sampleElement() {
        checkNotEmpty();
        double value = totalWeight * random.nextDouble();
 
        int left = 0;
        int right = storage.size() - 1;
 
        while (left < right) {             int middle = left + ((right - left) >> 1);
            Entry<E> middleEntry = storage.get(middle);
            double lowerBound = middleEntry.getAccumulatedWeight();
            double upperBound = lowerBound + middleEntry.getWeight();
 
            if (lowerBound <= value && value < upperBound) {
                return middleEntry.getElement();
            }
 
            if (value < lowerBound) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }
 
        return storage.get(left).getElement();
    }
 
    private void checkNotEmpty() {
        checkNotEmpty(storage.size());
    }
    
    protected void checkWeight(double weight) {
        if (Double.isNaN(weight)) {
            throw new IllegalArgumentException("The element weight is NaN.");
        }
 
        if (weight <= 0.0) {
            throw new IllegalArgumentException(
                    "The element weight must be positive. Received " + weight);
        }
 
        if (Double.isInfinite(weight)) {
            // Once here, 'weight' is positive infinity.
            throw new IllegalArgumentException(
                    "The element weight is infinite.");
        }
    }
 
    private void checkNotEmpty(int size) {
        if (size == 0) {
            throw new IllegalStateException(
                    "This probability distribution is empty.");
        }
    }
}