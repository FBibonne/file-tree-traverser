package bibonne.filestree.traversing;

import bibonne.filestree.traversing.filesconvert.ConvertResult;
import bibonne.filestree.traversing.internal.Traverser;

import static bibonne.filestree.traversing.TraverseUtils.getRootPath;

public class ConvertMusicFilesInFilesTree {
    public static void main(String[] args) throws Exception {
        Traverser.forBrowseResult(new ConvertResult(getRootPath(args))).call();
    }
}
