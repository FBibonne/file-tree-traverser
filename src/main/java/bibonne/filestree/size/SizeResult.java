package bibonne.filestree.size;

import bibonne.filestree.traverser.BrowseResult;
import bibonne.filestree.utils.FilesUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.lang.System.lineSeparator;
import static java.util.Objects.requireNonNull;

public class SizeResult implements BrowseResult {

    public static final long THREATHOLD = 1_000_000_000L;

    private final Path directory;
    protected final FilesUtils filesUtils;
    private final List<SizeResult> children = new ArrayList<>();

    private Size totalSize= Size.ZERO;
    private Size negileableSize= Size.ZERO;



    public static SizeResult root(Path rootDirectory, FilesUtils filesUtils) {
        return new SizeResult(rootDirectory, filesUtils);
    }

    @Override
    public SizeResult child(Path subdirectory){
        var retour= newInstance(subdirectory);
        children.add(retour);
        return retour;
    }

    SizeResult newInstance(Path subdirectory) {
        return new SizeResult(subdirectory, filesUtils);
    }


    protected SizeResult(Path directory, FilesUtils filesUtils) {
        this.directory = requireNonNull(directory);
        this.filesUtils = requireNonNull(filesUtils);
    }

    public Path currentDirectory() {
        return directory;
    }

    Size totalSize() {return totalSize;}

    @Override
    public void processFile(Path path) {
        addNegligeableSize(size(path));
    }

    private long size(Path path) {
        return filesUtils.sizeSafely(path);
    }

    @Override
    public SizeResult aggregate() {
        updateSizes();
        children.sort(Comparator.comparingLong(sizeResult -> sizeResult.totalSize.value()));
        return this;
    }

    private void addNegligeableSize(long size) {
        this.negileableSize=this.negileableSize.add(size);
    }

    void updateSizes() {
        children.removeIf(this::isToBeRemovedFromChildren);
        this.totalSize=this.totalSize.add(this.negileableSize);
    }

    private boolean isToBeRemovedFromChildren(SizeResult child) {
        if (child.isNegligeable()){
            negileableSize=negileableSize.add(child.totalSize());
            return true;
        }
        this.totalSize=this.totalSize.add(child.totalSize());
        return false;
    }

    public boolean isNegligeable() {
        return totalSize.value() < THREATHOLD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SizeResult that = (SizeResult) o;
        return Objects.equals(currentDirectory(), that.currentDirectory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentDirectory());
    }


    @Override
    public String toString() {
        return toString("").toString();
    }

    private StringBuilder toString(String indent) {
        var toString=new StringBuilder("""
        %s%s : %.2f
        %s  _NEG_ : %.2f"""
                .formatted(indent, directoryName(), totalSize.toGiga(), indent, negileableSize.toGiga())
        );
        for (var browseResult:children){
            toString.append(lineSeparator()).append(browseResult.toString(indent+"  "));
        }
        return toString;
    }

    private String directoryName() {
        var fileName=this.directory.getFileName();
        return fileName==null?this.directory.toString():fileName.toString();
    }

}
