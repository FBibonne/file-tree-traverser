package poc.java.filestree.withstructuredconcurrency;

import poc.java.filestree.withstructuredconcurrency.external.FilesUtilsFromJdkFiles;
import poc.java.filestree.withstructuredconcurrency.size.SizeResult;
import poc.java.filestree.withstructuredconcurrency.traverser.Traverser;

import java.util.concurrent.ExecutionException;

import static poc.java.filestree.withstructuredconcurrency.utils.TraverseUtils.getRootPath;

public class FileTreeSize {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(Traverser.browseFor(SizeResult.root(getRootPath(args), new FilesUtilsFromJdkFiles())));
    }
}
