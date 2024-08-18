package bibonne.filestree.traversing;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesUtils {
    boolean isDirectory(Path path);

    Stream<Path> list(Path directory);

    long size(Path path);
}
