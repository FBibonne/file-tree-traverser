package bibonne.filestree.traversing;

import bibonne.filestree.traversing.filesconvert.ConvertResult;
import bibonne.filestree.traversing.internal.Traverser;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Traverse {

    public static void main(String[] args) throws Exception {
        Path rootPath = args.length > 0 ? Path.of(args[0]) : fileSystemRoot();
        //System.out.println(Traverser.forBrowseResult(SizeResult.root(rootPath, new FilesUtilsFromJavaFiles())).call());
        Traverser.forBrowseResult(new ConvertResult(rootPath)).call();
    }

    private static Path fileSystemRoot(){
        return FileSystems.getDefault().getRootDirectories().iterator().next();
    }


}
