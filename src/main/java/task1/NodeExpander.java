package task1;

import java.util.List;

public interface NodeExpander<N> {
    
    List<N> expand(N node);
}
