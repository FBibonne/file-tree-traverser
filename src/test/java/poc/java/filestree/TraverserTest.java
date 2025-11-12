package poc.java.filestree;

import org.junit.jupiter.api.Test;
import poc.java.filestree.withstructuredconcurrency.external.FilesUtilsFromJdkFiles;
import poc.java.filestree.withstructuredconcurrency.size.SizeResult;
import poc.java.filestree.withstructuredconcurrency.traverser.Traverser;
import poc.java.filestree.withstructuredconcurrency.utils.FilesUtils;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TraverserTest {

    /*
     * src/test/resources:
     *   gainsbourg.mp3: 2000000000
     *   mozart.mp3: 11
     *   wav:
     *     beatles.wav: 2000000000
     *     renaud.wav: 13
     *     mp4:
     *   opus:
     *     grand corps.opus: 14
     *     goldman.opus: 15
     *   ogg:
     *   wma:
     *     caravan.wma: 1000000000
     *   flac:
     *     zebda.flac: 17
     *     mano negra:
     *       a.flac: 18
     *       b.mp3: 19
     *       Paris.mp3: 2000000000
     *   ...:
     *   endWithADot.:
     *   withoutExt:
     *   withShortExt.1:
     *
     */


    @Test
    void call_withSizeResult() throws Exception {
        Path root = Path.of(TraverserTest.class.getClassLoader().getResource(".").toURI());
        FilesUtils filesUtils = new FilesUtilsFake(new FilesUtilsFromJdkFiles(), List.of());
        SizeResult sizeResult = SizeResult.root(root, filesUtils);
        var traverser= Traverser.browseFor(sizeResult);
        assertThat(traverser).hasToString("""
                test-classes : 6.52
                  _NEG_ : 1.86
                  wma : 0.93
                    _NEG_ : 0.93
                  wav : 1.86
                    _NEG_ : 1.86
                  flac : 1.86
                    _NEG_ : 0.00
                    mano negra : 1.86
                      _NEG_ : 1.86""");
    }


}