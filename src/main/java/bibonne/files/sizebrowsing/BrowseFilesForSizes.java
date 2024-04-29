package bibonne.files.sizebrowsing;

import java.nio.file.Path;
import java.nio.FileSystems;

public class BrowseFilesForSizes {

    public static void main(String[] args) {
        Path rootPath = args.length > 0 ? Path.of(args[0]) : fileSystemRoot();
        System.out.println(new BrowseForSizes(BrowseResult.root(rootPath)).call());
    }

    private static Path fileSystemRoot(){
        return FileSystems.getDefault().getRootDirectories().iterator().next();
    }


}
