package bibonne.filestree;

import bibonne.filestree.external.FilesUtilsFromJdkFiles;
import bibonne.filestree.size.SizeResult;
import bibonne.filestree.traverser.Traverser;

import static bibonne.filestree.utils.TraverseUtils.getRootPath;

public class FileTreeSize {

    public static void main(String[] args) {
        System.out.println(Traverser.browseFor(SizeResult.root(getRootPath(args), new FilesUtilsFromJdkFiles())));
    }
}
