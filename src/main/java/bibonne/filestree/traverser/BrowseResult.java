package bibonne.filestree.traverser;

import java.nio.file.Path;

/// Instance which is linked to the process of one directory (which is the [`currentDirectory()`](#currentDirectory())).
/// it should aggregate the results of the process of all the linked directory and of all its subdirectories
public interface BrowseResult {

    /// Returns a new instance of BrowseResult linked to the subdirectory provided as a parameter.
    /// The implementation is reponsible for keeping a reference to the children instance of BrowseResult linked to
    /// the subdirectory if needed for aggregation of the results
    BrowseResult child(Path subdirectory);

    /// The directory to which this instance is related.
    Path currentDirectory();

    /// Process the file which is linked to this path. Must mutate this instance to take into account the file linked
    /// to the path in parameter.
    void processFile(Path path);

    /// Implementations should override this method if needed (if the need to aggregate results of processing each file)
    ///
    /// Aggregates the results of this instance related to the files of the [`currentDirectory()`](#currentDirectory())
    /// and the results of all its subdirectories
    ///
    default BrowseResult aggregate(){
        return this;
    }
}
