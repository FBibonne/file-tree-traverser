package poc.java.filestree;

import poc.java.filestree.withstructuredconcurrency.external.FilesUtilsFromJdkFiles;
import poc.java.filestree.withstructuredconcurrency.utils.FilesUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public record FilesUtilsFake(FilesUtilsFromJdkFiles delegate, List<Path> deletedSpy) implements FilesUtils {

    @Override
    public boolean isDirectorySafely(Path path) {
        return delegate.isDirectorySafely(path);
    }

    @Override
    public Stream<Path> listSafely(Path directory) {
        return delegate.listSafely(directory);
    }

    @Override
    public long sizeSafely(Path path) {
        try {
            return Long.parseLong(Files.readAllLines(path).getFirst());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
