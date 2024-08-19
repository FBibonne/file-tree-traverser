package bibonne.filestree.traversing;

import bibonne.filestree.traversing.external.FilesUtilsFromJavaFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public record FilesUtilsMock(FilesUtilsFromJavaFiles delegate, List<Path> deletedSpy) implements FilesUtils {

    @Override
    public boolean isDirectory(Path path) {
        return delegate.isDirectory(path);
    }

    @Override
    public Stream<Path> list(Path directory) {
        return delegate.list(directory);
    }

    @Override
    public long size(Path path) {
        try {
            return Long.parseLong(Files.readAllLines(path).getFirst());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Path path) {
        deletedSpy.add(path);
    }


}
