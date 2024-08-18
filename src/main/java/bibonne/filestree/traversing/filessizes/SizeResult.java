package bibonne.filestree.traversing.filessizes;

import bibonne.filestree.traversing.BrowseResult;
import bibonne.filestree.traversing.FilesUtils;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.FormatProcessor.FMT;
import static java.util.Objects.requireNonNull;

public class SizeResult implements BrowseResult {

    private static final double TO_GIGA_COEFF = Math.pow(1024, 3);
    public static final long THREATHOLD = 1_000_000_000L;
    private final Path directory;
    private final FilesUtils filesUtils;

    private Size totalSize=new Size(0);

    private Size negileableSize=new Size(0);

    private Set<SizeResult> children;


    public static SizeResult root(Path rootDirectory, FilesUtils filesUtils) {
        return new SizeResult(rootDirectory,  new HashSet<>(), filesUtils);
    }

    @Override
    public SizeResult child(Path childDirectory){
        var retour= new SizeResult(childDirectory,  new HashSet<>(), filesUtils);
        children.add(retour);
        return retour;
    }


    private SizeResult(Path directory, Set<SizeResult> children, FilesUtils filesUtils) {
        this.directory = requireNonNull(directory);
        this.children = requireNonNull(children);
        this.filesUtils = requireNonNull(filesUtils);
    }

    public Path currentDirectory() {
        return directory;
    }

    @Override
    public void addFilePath(Path path) {
        addNegligeableSize(size(path));
    }

    private long size(Path path) {
        return filesUtils.size(path);
    }

    @Override
    public SizeResult afterTraverse() {
        updateSizes();
        return this;
    }

    private void addNegligeableSize(long size) {
        this.negileableSize=this.negileableSize.add(size);
    }

    private void updateSizes() {
        children=children.stream()
                .filter(browseResult -> {
                    if(browseResult.isNegligeable()){
                        this.negileableSize=negileableSize.add(browseResult.totalSize);
                        return false;
                    }
                    this.totalSize=this.totalSize.add(browseResult.totalSize);
                    return true;
                })
                .collect(Collectors.toSet());
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
        var toString=new StringBuilder(STR."""
        \{indent}\{directoryName()} : \{toGigaString(totalSize)}
        \{indent}  _NEG_ : \{toGigaString(negileableSize)}""");
        for (var browseResult:children){
            toString.append('\n').append(browseResult.toString(STR."\{indent}  "));
        }
        return toString;
    }

    private String toGigaString(Size size) {
        return FMT."%.2f\{toGiga(size)}";
    }

    private String directoryName() {
        var fileName=this.directory.getFileName();
        return fileName==null?this.directory.toString():fileName.toString();
    }

    private double toGiga(Size totalSize) {
        return totalSize.value() / TO_GIGA_COEFF;
    }
}
