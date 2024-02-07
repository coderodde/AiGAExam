package task4;

import java.util.Objects;

public final class LeafRMQTreeNode<N extends AbstractRMQTreeNode<N, V>, 
//                                   K extends Comparable<? super K>,
                                   V extends Comparable<? super V>>
extends AbstractRMQTreeNode<N, V> {
    
    @Override
    public String toString() {
        return String.format("[LEAF: value = \"%s\"]",
                             Objects.toString(getValue()));
    }

//    @Override
//    public int compareTo(AbstractRMQTreeNode<N, K, V> o) {
//        throw new UnsupportedOperationException();
//    }

    @Override
    public int compareTo(AbstractRMQTreeNode<N, V> o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
