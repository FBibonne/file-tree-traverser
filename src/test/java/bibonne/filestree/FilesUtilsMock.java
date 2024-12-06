package bibonne.filestree;

import bibonne.filestree.external.FilesUtilsFromJdkFiles;
import bibonne.filestree.utils.FilesUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public record FilesUtilsMock(FilesUtilsFromJdkFiles delegate, List<Path> deletedSpy) implements FilesUtils {

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

    @Override
    public void deleteSafely(Path path) {
        deletedSpy.add(path);
    }


}
