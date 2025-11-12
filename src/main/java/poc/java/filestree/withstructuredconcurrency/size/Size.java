package poc.java.filestree.withstructuredconcurrency.size;

public record Size(long value) {

    public static final Size ZERO = new Size(0);
    private static final double TO_GIGA_COEFF = Math.pow(1024, 3);

    public Size add(long size) {
        return new Size(value+size);
    }

    public Size add(Size totalSize) {
        return add(totalSize.value);
    }

    public double toGiga() {
        return value() / TO_GIGA_COEFF;
    }
}
