package bibonne.filestree.size;

import bibonne.filestree.traverser.BrowseResult;
import bibonne.filestree.utils.FilesUtils;

import java.nio.file.Path;
import java.util.*;

import static java.lang.System.lineSeparator;
import static java.util.Objects.requireNonNull;

public class SizeResult implements BrowseResult {

    private static final double TO_GIGA_COEFF = Math.pow(1024, 3);
    public static final long THREATHOLD = 1_000_000_000L;
    private final Path directory;
    protected final FilesUtils filesUtils;

    private Size totalSize=new Size(0);

    private Size negileableSize=new Size(0);

    private final List<SizeResult> children;


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
        this.children = new ArrayList<>();
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
        return this;
    }

    private void addNegligeableSize(long size) {
        this.negileableSize=this.negileableSize.add(size);
    }

    void updateSizes() {
        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            if (child.isNegligeable()){
                negileableSize=negileableSize.add(child.totalSize());
                children.remove(i);
                i=i-1;
            }else{
                this.totalSize=this.totalSize.add(child.totalSize());
            }
        }
        this.totalSize=this.totalSize.add(this.negileableSize);
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
                .formatted(indent, directoryName(), toGiga(totalSize), indent, toGiga(negileableSize))
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

    private double toGiga(Size totalSize) {
        return totalSize.value() / TO_GIGA_COEFF;
    }
}
