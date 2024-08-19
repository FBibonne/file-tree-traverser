package bibonne.filestree.traversing.external;

import bibonne.filestree.traversing.FilesUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public record FilesUtilsFromJavaFiles()implements FilesUtils {
    @Override public boolean isDirectory(Path path) {
        return path!=null && Files.isDirectory(path);
    }

    @Override public Stream<Path> list(Path directory) {
        if (directory == null) {
            return Stream.empty();
        }
        try {
            return Files.list(directory);
        } catch (Exception e) {
            return Stream.empty();
        }

    }

    @Override
    public long size(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public void delete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
