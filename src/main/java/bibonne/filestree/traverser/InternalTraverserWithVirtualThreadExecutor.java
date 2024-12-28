package bibonne.filestree.traverser;

import bibonne.filestree.external.FilesUtilsFromJdkFiles;
import bibonne.filestree.utils.FilesUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static bibonne.filestree.traverser.Traverser.executor;

record InternalTraverserWithVirtualThreadExecutor(BrowseResult result, FilesUtils filesUtils) implements Callable<BrowseResult> {

    public InternalTraverserWithVirtualThreadExecutor(BrowseResult browseResult){
        this(browseResult, new FilesUtilsFromJdkFiles());
    }

    @Override
    public BrowseResult call() {
        List<Callable<BrowseResult>> executeLater = new ArrayList<>();
        try(var files = listAllFiles(result.currentDirectory())){
            files.forEach(path -> {
                if (isDirectory(path)){
                    executeLater.add((new InternalTraverserWithVirtualThreadExecutor(result.child(path), filesUtils)));
                }else{
                    result.processFile(path);
                }
            });
            executor.invokeAll(executeLater);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return result.aggregate();
    }

    private boolean isDirectory(Path path) {
        return filesUtils.isDirectorySafely(path);
    }

    private Stream<Path> listAllFiles(Path directory) {
        return filesUtils.listSafely(directory);
    }
}
