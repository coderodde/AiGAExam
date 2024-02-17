package task6;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This class implements an iterator over all possible directed paths between
 * two argument nodes.
 *
 * @author Rodion "rodde" Efremov
 * @version 1.6
 */
public class HMMStatePathEnumerator
        implements Iterable<List<HiddenMarkovModelState>>, 
                   Iterator<List<HiddenMarkovModelState>> {

    private final HiddenMarkovModelState source;
    private final HiddenMarkovModelState target;
    private final Set<HiddenMarkovModelState> visitedSet;
    private final Deque<HiddenMarkovModelState> nodeStack;
    private final Deque<Iterator<HiddenMarkovModelState>> iteratorStack;
    private List<HiddenMarkovModelState> nextPath;

    public HMMStatePathEnumerator(HiddenMarkovModelState source, 
                                  HiddenMarkovModelState target) {
        this.source = source;
        this.target = target;
        this.visitedSet = new HashSet<>();
        this.nodeStack = new LinkedList<>();
        this.iteratorStack = new LinkedList<>();

        computeNextPath();
    }

    @Override
    public Iterator<List<HiddenMarkovModelState>> iterator() {
        return this;
    }

    private void computeNextPath() {
        if (nodeStack.isEmpty()) {
            nodeStack.addLast(source);
            iteratorStack.addLast(
                    source.getFollowingStates()
                          .keySet()
                          .iterator());
            
            visitedSet.add(source);
        } else {
            visitedSet.remove(nodeStack.removeLast());
            iteratorStack.removeLast();
        }

        while (nodeStack.size() > 0) {
            HiddenMarkovModelState top = nodeStack.getLast();

            if (top.equals(target)) {
                nextPath = new ArrayList<>(nodeStack);
                return;
            }

            if (iteratorStack.getLast().hasNext()) {
                HiddenMarkovModelState next = iteratorStack.getLast().next();

                if (visitedSet.contains(next)) {
                    continue;
                }

                nodeStack.addLast(next);
                visitedSet.add(next);
                iteratorStack.addLast(
                        next.getFollowingStates()
                            .keySet()
                            .iterator());
            } else {
                iteratorStack.removeLast();
                visitedSet.remove(nodeStack.removeLast());
            }
        }
    }

    @Override
    public boolean hasNext() {
        return nextPath != null;
    }

    @Override
    public List<HiddenMarkovModelState> next() {
        if (nextPath == null) {
            throw new NoSuchElementException("No more paths available.");
        }

        List<HiddenMarkovModelState> path = nextPath;
        nextPath = null;
        computeNextPath();
        return path;
    }
}
