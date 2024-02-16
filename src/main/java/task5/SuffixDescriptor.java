package task5;

public final class SuffixDescriptor implements Comparable<SuffixDescriptor> {
    
    private final String text;
    private final int startIndex;
    
    public SuffixDescriptor(String text, int startIndex) {
        this.text = text;
        this.startIndex = startIndex;
    }
    
    public String getText() {
        return text;
    }
    
    public int getStartIndex() {
        return startIndex;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(text.length() - startIndex);
        
        for (int i = startIndex; i < text.length(); i++) {
            sb.append(text.charAt(i));
        }
        
        return sb.toString();
    }

    @Override
    public int compareTo(SuffixDescriptor o) {
        return Character.compare(getFirstCharacter(), o.getFirstCharacter());
    }
    
    char getFirstCharacter() {
        return text.charAt(startIndex);
    }
}
