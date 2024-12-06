package bibonne.filestree.traverser;

import java.nio.file.Path;

public interface BrowseResult {
    BrowseResult child(Path childDirectory);

    Path currentDirectory();

    void addFilePath(Path path);

    BrowseResult afterTraverse();
}
