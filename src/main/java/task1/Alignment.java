package task1;

public class Alignment {

    private final String[] alignment;

    Alignment(String[] alignment) {
        this.alignment = alignment;
    }

    public String[] getAlignemnt() {
        return alignment;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String separator = "";

        for (String row : alignment) {
            sb.append(separator).append(row);
            separator = "\n";
        }

        return sb.toString();
    }
}
