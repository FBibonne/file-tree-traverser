package bibonne.filestree;

import bibonne.filestree.external.FilesUtilsFromJdkFiles;
import bibonne.filestree.size.SizeResult;
import bibonne.filestree.traverser.Traverser;

import java.util.concurrent.ExecutionException;

import static bibonne.filestree.utils.TraverseUtils.getRootPath;

public class FileTreeSizeWithExecutorService {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(Traverser.browseWithExecutorServiceFor(SizeResult.root(getRootPath(args), new FilesUtilsFromJdkFiles())));
    }
}
