package poc.java.filestree.monothread;

import poc.java.filestree.withstructuredconcurrency.external.FilesUtilsFromJdkFiles;
import poc.java.filestree.withstructuredconcurrency.size.SizeResult;
import poc.java.filestree.withstructuredconcurrency.utils.FilesUtils;

import static poc.java.filestree.withstructuredconcurrency.utils.TraverseUtils.getRootPath;

public class FileTreeSizeWithMonoThread {

    private static final FilesUtils filesUtils = new FilesUtilsFromJdkFiles();

    public static void main(String[] args) {
        SizeResult root = SizeResult.root(getRootPath(args), new FilesUtilsFromJdkFiles());
        Thread.startVirtualThread(()->computeRecursive(root));
        System.out.println(root);
    }

    static void computeRecursive(SizeResult current) {

        try(var files = filesUtils.listSafely(current.currentDirectory())){
            files.forEach(path -> {
                if (filesUtils.isDirectorySafely(path)){
                    computeRecursive(current.child(path));
                }else{
                    current.processFile(path);
                }
            });
        }
        current.aggregate();
    }

}
