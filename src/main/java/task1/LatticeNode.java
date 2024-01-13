package task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class LatticeNode {
    
    /**
     * The problem instance this lattice node contributes to.
     */
    private final MultipleSequenceAlignmentInstance instance;

    /**
     * The coordinates of this node in the entire lattice.
     */
    private final int[] coordinates;

    LatticeNode(MultipleSequenceAlignmentInstance instance,
                int[] coordinates) {
        this.instance = instance;
        this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object o) {
        LatticeNode otherLatticeNode = (LatticeNode) o;
        return Arrays.equals(this.coordinates, otherLatticeNode.coordinates);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.coordinates);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        String separator = "";

        for (int coordinate : coordinates) {
            sb.append(separator).append(coordinate);
            separator = ", ";
        }

        return sb.append("]").toString();
    }

    List<LatticeNode> getChildren() {
        // Find out in how many dimension we can move forward:
        int dimensionsNotReached = 0;
        String[] sequenceArray = instance.getSequenceArray();
        boolean[] inclusionMap = new boolean[coordinates.length];

        for (int i = 0; i < sequenceArray.length; ++i) {
            if (coordinates[i] < sequenceArray[i].length()) {
                dimensionsNotReached++;
                // We can make a step forward in the direction of ith dimension:
                inclusionMap[i] = true;
            }
        }

        // Create the array of children:
        int numberOfChildren = pow2(dimensionsNotReached) - 1;
        List<LatticeNode> children = new ArrayList<>(numberOfChildren);
        loadChildren(children, inclusionMap);

        // Convert offsets to actual child coordinates:
        for (LatticeNode child : children) {
            child.addOffsets(coordinates);
        }

        return children;
    }

    List<LatticeNode> getParents() {
        // Find out in how many dimensions we can move BACKward:
        int dimensionsNotReached = 0;
        String[] sequenceArray = instance.getSequenceArray();
        boolean[] inclusionMap = new boolean[coordinates.length];

        for (int i = 0; i < sequenceArray.length; ++i) {
            if (coordinates[i] > 0) {
                dimensionsNotReached++;
                // We can make a step backwards in the direction of ith 
                // dimension:
                inclusionMap[i] = true;
            }
        }

        // Create the array of parents:
        int numberOfParents = pow2(dimensionsNotReached) - 1;
        List<LatticeNode> parents = new ArrayList(numberOfParents);
        loadParents(parents, inclusionMap);

        // Convert the offsets to actual parent coordinates:
        for (LatticeNode parent : parents) {
            parent.addOffsets(coordinates);
        }

        return parents;
    }

    int[] getCoordinates() {
        return coordinates;
    }

    private void loadChildren(List<LatticeNode> children, 
                              boolean[] inclusionMap) {
        
        int[] coords = new int[this.coordinates.length];

        for (int i = 0; i != children.size(); ++i) {
            increment(coords, inclusionMap);
            children.set(i, new LatticeNode(instance, coords.clone()));
        }
    }

    private void loadParents(List<LatticeNode> parents, 
                             boolean[] inclusionMap) {
        int[] coords = new int[this.coordinates.length];

        for (int i = 0; i != parents.size(); ++i) {
            decrement(coords, inclusionMap);
            parents.set(i, new LatticeNode(instance, coords.clone()));
        }
    }

    private static void increment(int[] coordinates, boolean[] inclusionMap) {
        for (int i = 0; i < coordinates.length; ++i) {
            if (!inclusionMap[i]) {
                continue;
            }

            if (coordinates[i] == 0) {
                coordinates[i] = 1;
                return;
            }

            coordinates[i] = 0;
        }
    }

    private static void decrement(int[] coordinates, boolean[] inclusionMap) {
        for (int i = 0; i < coordinates.length; ++i) {
            if (!inclusionMap[i]) {
                continue;
            }

            if (coordinates[i] == 0) {
                coordinates[i] = -1;
                return;
            }

            coordinates[i] = 0;
        }
    }

    private void addOffsets(int[] offsets) {
        for (int i = 0; i < coordinates.length; ++i) {
            coordinates[i] += offsets[i];
        }
    }

    private static int pow2(int exponent) {
        int ret = 1;

        for (int e = 0; e < exponent; ++e) {
            ret *= 2;
        }

        return ret;
    }
}
