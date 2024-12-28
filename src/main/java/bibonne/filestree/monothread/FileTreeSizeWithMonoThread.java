package bibonne.filestree.monothread;

import bibonne.filestree.external.FilesUtilsFromJdkFiles;
import bibonne.filestree.size.SizeResult;
import bibonne.filestree.utils.FilesUtils;

import static bibonne.filestree.utils.TraverseUtils.getRootPath;

public class FileTreeSizeWithMonoThread {

    private static final FilesUtils filesUtils = new FilesUtilsFromJdkFiles();

    public static void main(String[] args) {
        SizeResult root = SizeResult.root(getRootPath(args), new FilesUtilsFromJdkFiles());
        computeRecursive(root);
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
