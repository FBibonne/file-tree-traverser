package bibonne.filestree.onlyvirtualthreads;

import bibonne.filestree.external.FilesUtilsFromJdkFiles;
import bibonne.filestree.size.SizeResult;
import bibonne.filestree.utils.FilesUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;

import static bibonne.filestree.utils.TraverseUtils.getRootPath;

public class FileTreeSizeWithVirtualThreadOnly {

    private static final FilesUtils filesUtils = new FilesUtilsFromJdkFiles();

    private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public static void main(String[] args) throws InterruptedException {

        SizeResult root = SizeResult.root(getRootPath(args), new FilesUtilsFromJdkFiles());
        executor.invokeAll(List.of(()->computeRecursive(root)));
        System.out.println(root);
    }

    static SizeResult computeRecursive(SizeResult current) throws InterruptedException {
        //List<Callable<SizeResult>> executeLater = new ArrayList<>();
        try(var scope=new StructuredTaskScope.ShutdownOnFailure() ;var files = filesUtils.listSafely(current.currentDirectory())){
            files.forEach(path -> {
                if (filesUtils.isDirectorySafely(path)){
                    scope.fork(()->computeRecursive(current.child(path)));
                }else{
                    current.processFile(path);
                }
            });
            scope.join();
        }
        //executor.invokeAll(executeLater);
        return current.aggregate();
    }

}
