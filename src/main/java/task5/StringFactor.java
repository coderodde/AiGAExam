package task5;

final class StringFactor implements Comparable<StringFactor> {
        
    private final String text;
    private final int startIndex;
    private final int length;
    
    StringFactor(String text, int startIndex, int length) {
        this.text = text;
        this.startIndex = startIndex;
        
        int remaining = text.length() - startIndex - length;
        this.length = Math.max(0, remaining);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(length);
        
        for (int i = startIndex, j = 0; j < length; j++, i++) {
            sb.append(text.charAt(i));
        }
        
        return sb.toString();
    }

    @Override
    public int compareTo(StringFactor o) {
        return Character.compare(text.charAt(0), o.text.charAt(0));
    }
}
