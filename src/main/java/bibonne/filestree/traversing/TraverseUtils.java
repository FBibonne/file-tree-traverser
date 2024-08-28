package bibonne.filestree.traversing;

import java.nio.file.FileSystems;
import java.nio.file.Path;

class TraverseUtils {

    private TraverseUtils() {}

    static Path getRootPath(String[] args) {
        return args.length > 0 ? Path.of(args[0]) : fileSystemRoot();
    }

    private static Path fileSystemRoot(){
        return FileSystems.getDefault().getRootDirectories().iterator().next();
    }


}
