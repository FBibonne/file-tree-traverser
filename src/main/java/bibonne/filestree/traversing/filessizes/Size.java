package bibonne.filestree.traversing.filessizes;

public record Size(long value) {

    public Size add(long size) {
        return new Size(value+size);
    }

    public Size add(Size totalSize) {
        return add(totalSize.value);
    }
}
