package bibonne.filestree;

import bibonne.filestree.convertmusic.ConvertResult;
import bibonne.filestree.traverser.Traverser;

import static bibonne.filestree.utils.TraverseUtils.getRootPath;

public class ConvertMusicFilesInFilesTree {
    public static void main(String[] args) throws Exception {
        Traverser.browseFor(new ConvertResult(getRootPath(args)));
    }
}
