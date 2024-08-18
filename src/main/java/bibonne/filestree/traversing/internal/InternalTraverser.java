package bibonne.filestree.traversing.internal;

import bibonne.filestree.traversing.BrowseResult;
import bibonne.filestree.traversing.FilesUtils;
import bibonne.filestree.traversing.external.FilesUtilsFromJavaFiles;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.Stream;

record InternalTraverser(BrowseResult result, FilesUtils filesUtils) implements Callable<BrowseResult> {

    public InternalTraverser(BrowseResult browseResult){
        this(browseResult, new FilesUtilsFromJavaFiles());
    }

    @Override
    public BrowseResult call() {
        try(var scope=new StructuredTaskScope.ShutdownOnFailure() ; var files = listAllFiles(result.currentDirectory())){
            files.forEach(path -> {
                if (isDirectory(path)){
                    scope.fork(new InternalTraverser(result.child(path), filesUtils));
                }else{
                    result.addFilePath(path);
                }
            });
            scope.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result.afterTraverse();
    }

    private boolean isDirectory(Path path) {
        return filesUtils.isDirectory(path);
    }

    private Stream<Path> listAllFiles(Path directory) {
        try {
            return filesUtils.list(directory);
        } catch (Exception e) {
            return Stream.empty();
        }
    }
}
