package bibonne.files.sizebrowsing;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.FormatProcessor.FMT;
import static java.util.Objects.requireNonNull;

public class BrowseResult{

    private static final double TO_GIGA_COEFF = Math.pow(1024, 3);
    private final Path directory;

    private Size totalSize=new Size(0);

    private Size negileableSize=new Size(0);

    private Set<BrowseResult> children;


    public static BrowseResult root(Path rootDirectory){
        return new BrowseResult(rootDirectory,  new HashSet<>());
    }

    public BrowseResult child(Path childDirectory){
        var retour= new BrowseResult(childDirectory,  new HashSet<>());
        children.add(retour);
        return retour;
    }


    private BrowseResult(Path directory, Set<BrowseResult> children) {
        this.directory = requireNonNull(directory);
        this.children = requireNonNull(children);
    }

    public Path getDirectory() {
        return directory;
    }

    public void addNegligeableSize(long size) {
        this.negileableSize=this.negileableSize.add(size);
    }

    public void updateSizes() {
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
        return totalSize.value() < BrowseForSizes.THREATHOLD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrowseResult that = (BrowseResult) o;
        return Objects.equals(getDirectory(), that.getDirectory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDirectory());
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
