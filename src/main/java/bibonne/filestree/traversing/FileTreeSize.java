package bibonne.filestree.traversing;

import bibonne.filestree.traversing.external.FilesUtilsFromJavaFiles;
import bibonne.filestree.traversing.filessizes.SizeResult;
import bibonne.filestree.traversing.internal.Traverser;

import static bibonne.filestree.traversing.TraverseUtils.getRootPath;

public class FileTreeSize {

    public static void main(String[] args) throws Exception {
        System.out.println(Traverser.forBrowseResult(SizeResult.root(getRootPath(args), new FilesUtilsFromJavaFiles())).call());
    }
}
