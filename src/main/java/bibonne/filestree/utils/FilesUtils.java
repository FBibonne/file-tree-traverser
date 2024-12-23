package bibonne.filestree.utils;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public interface FilesUtils {
    boolean isDirectorySafely(Path path);

    Stream<Path> listSafely(Path directory);

    long sizeSafely(Path path);

    void deleteSafely(Path path);

    default PathWithExtension pathWithExtension(Path path){
        return new PathWithExtension(path);
    }

    class PathWithExtension {
        private final Path path;
        private String filename;
        private Optional<String> extension;

        public PathWithExtension(Path path) {
            this.path = path;
        }

        public Optional<String> extension() {
            if (extension == null) {
                this.extension=computeExtension(filename());
            }
            return extension;
        }

        public String filename() {
            if (filename == null) {
                filename = path.getFileName().toString();
            }
            return filename;
        }

        private static Optional<String> computeExtension(String fileName) {
            int lastDot = fileName.lastIndexOf('.');
            if (lastDot == -1) return Optional.empty();
            if (lastDot < fileName.length() - 1) return Optional.of(fileName.substring(lastDot + 1));
            //filename ends with a dot :
            return Optional.empty();
        }

        public String filenameWithoutExtension() {
            int lastDot = filename.lastIndexOf('.');
            return filename.substring(0, lastDot);
        }
    }
}
