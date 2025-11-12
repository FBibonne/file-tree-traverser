package poc.java.filestree.withstructuredconcurrency.utils;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class TraverseUtils {

    private TraverseUtils() {}

    public static Path getRootPath(String[] args) {
        return args.length > 0 ? Path.of(args[0]) : fileSystemRoot();
    }

    private static Path fileSystemRoot(){
        return FileSystems.getDefault().getRootDirectories().iterator().next();
    }


}
