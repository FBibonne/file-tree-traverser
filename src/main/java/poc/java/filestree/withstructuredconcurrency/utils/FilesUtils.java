package poc.java.filestree.withstructuredconcurrency.utils;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesUtils {
    boolean isDirectorySafely(Path path);

    Stream<Path> listSafely(Path directory);

    long sizeSafely(Path path);

}
