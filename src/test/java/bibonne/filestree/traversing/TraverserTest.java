package bibonne.filestree.traversing;

import bibonne.filestree.traversing.external.FilesUtilsFromJavaFiles;
import bibonne.filestree.traversing.filesconvert.ConvertResult;
import bibonne.filestree.traversing.filesconvert.MusicConverter;
import bibonne.filestree.traversing.filessizes.SizeResult;
import bibonne.filestree.traversing.internal.Traverser;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
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
        FilesUtils filesUtils = new FilesUtilsMock(new FilesUtilsFromJavaFiles(), List.of());
        SizeResult sizeResult = SizeResult.root(root, filesUtils);
        var traverser= Traverser.forBrowseResult(sizeResult);
        assertThat(traverser.call()).hasToString("""
                test-classes : 6.52
                  _NEG_ : 1.86
                  wav : 1.86
                    _NEG_ : 1.86
                  flac : 1.86
                    _NEG_ : 0.00
                    mano negra : 1.86
                      _NEG_ : 1.86
                  wma : 0.93
                    _NEG_ : 0.93""");
    }

    @Test
    void call_withConvertResult() throws Exception {
        Path root = Path.of(TraverserTest.class.getClassLoader().getResource(".").toURI());
        List<ConverterCall> expectedCalls = List.of(
                new ConverterCall(root.resolve("wav"), Path.of("beatles.wav"), "beatles.mp3"),
        new ConverterCall(root.resolve("wav"), Path.of("renaud.wav"), "renaud.mp3"),
        new ConverterCall(root.resolve("opus"), Path.of("grand corps.opus"), "grand corps.mp3"),
        new ConverterCall(root.resolve("flac"), Path.of("zebda.flac"), "zebda.mp3"),
        new ConverterCall(root.resolve("opus"), Path.of("goldman.opus"), "goldman.mp3"),
        new ConverterCall(root.resolve("flac/mano negra"), Path.of("a.flac"), "a.mp3")
        );
        ConverterSpyer converterSpyer = new ConverterSpyer();
        List<Path> deletedSpy=Collections.synchronizedList(new ArrayList<>());
        FilesUtilsMock filesUtils = new FilesUtilsMock(new FilesUtilsFromJavaFiles(), deletedSpy);
        ConvertResult convertResult = new ConvertResult(root, converterSpyer, filesUtils);
        Traverser.forBrowseResult(convertResult).call();
        assertThat(converterSpyer.spy).hasSize(expectedCalls.size())
                .containsAll(expectedCalls);
        assertThat(deletedSpy).hasSize(expectedCalls.size())
                .containsAll(expectedCalls.stream().map(call->call.directory.resolve(call.srcFile)).toList());
    }

    record ConverterCall(Path directory, Path srcFile, String targetFilename) {
    }

    static class ConverterSpyer extends MusicConverter{

        List<ConverterCall> spy = Collections.synchronizedList(new ArrayList<>());

        @Override
        public boolean convert(Path directory, Path fileName, String targetFilename) {
            spy.add(new ConverterCall(directory, fileName, targetFilename));
            return true;
        }

    }
}