package task1;

final class HeapNodeHolder<N> implements Comparable<HeapNodeHolder<N>> {

    private final int score;
    private final N node;
    
    HeapNodeHolder(int score, N node) {
        this.score = score;
        this.node = node;
    }
    
    N getNode() {
        return node;
    }
    
    @Override
    public int compareTo(HeapNodeHolder<N> o) {
        return score - o.score;
    }
}
