package poc.java.filestree.withstructuredconcurrency.traverser;

import poc.java.filestree.withstructuredconcurrency.utils.FilesUtils;
import poc.java.filestree.withstructuredconcurrency.external.FilesUtilsFromJdkFiles;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.Stream;

record InternalTraverser(BrowseResult result, FilesUtils filesUtils) implements Callable<BrowseResult> {

    public InternalTraverser(BrowseResult browseResult){
        this(browseResult, new FilesUtilsFromJdkFiles());
    }

    @Override
    public BrowseResult call() {
        if (! Thread.currentThread().isInterrupted()) {
            processCurrentDirectory();
            return result.aggregate();
        }
        throw new RuntimeException("Process of "+result.currentDirectory()+" was interrupted");
    }

    private void processCurrentDirectory() {
        try(var scope=StructuredTaskScope.open() ; var files = listAllFiles(result.currentDirectory())){
            files.forEach(path -> {
                if (isDirectory(path)){
                    scope.fork(new InternalTraverser(result.child(path), filesUtils));
                }else{
                    result.processFile(path);
                }
            });
            scope.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private boolean isDirectory(Path path) {
        return filesUtils.isDirectorySafely(path);
    }

    private Stream<Path> listAllFiles(Path directory) {
        return filesUtils.listSafely(directory);
    }
}
