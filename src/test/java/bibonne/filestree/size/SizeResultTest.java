package bibonne.filestree.size;

import bibonne.filestree.external.FilesUtilsFromJdkFiles;
import bibonne.filestree.utils.FilesUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SizeResultTest {

    private final FilesUtils localFilesUtils = new FilesUtilsFromJdkFiles();
    private final Map<Path, Size> values = Map.of(
            Path.of("/a"), new Size(1000),
            Path.of("/b"), new Size(2000),
            Path.of("/c"), new Size(3000),
            Path.of("/a/1"), new Size(1002),
            Path.of("/a/2"), new Size(1001)
    );

    @Test
    void updateSizesTest() {
        SizeResult sizeResult = new StubbedSizeResult(Path.of("/"), new Size(100));
        values.keySet().forEach(sizeResult::child);
        assertDoesNotThrow(sizeResult::updateSizes);
    }

    private class StubbedSizeResult extends SizeResult {
        private final Size totalSize;

        public StubbedSizeResult(Path path, Size totalSize) {
            super(path, localFilesUtils);
            this.totalSize = totalSize;
        }

        @Override
        SizeResult newInstance(Path subdirectory) {
            return new StubbedSizeResult(subdirectory, values.get(subdirectory));
        }

        @Override
        Size totalSize() {
            return this.totalSize;
        }
    }
}