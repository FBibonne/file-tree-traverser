package bibonne.filestree.external;

import bibonne.filestree.utils.FilesUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public record FilesUtilsFromJdkFiles()implements FilesUtils {
    @Override public boolean isDirectorySafely(Path path) {
        return path!=null && Files.isDirectory(path);
    }

    @Override public Stream<Path> listSafely(Path directory) {
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
    public long sizeSafely(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public void deleteSafely(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
