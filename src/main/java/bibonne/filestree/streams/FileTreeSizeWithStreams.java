package bibonne.filestree.streams;

import bibonne.filestree.external.FilesUtilsFromJdkFiles;
import bibonne.filestree.size.SizeResult;
import bibonne.filestree.utils.FilesUtils;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static bibonne.filestree.utils.TraverseUtils.getRootPath;

public class FileTreeSizeWithStreams {

    private static final FilesUtils filesUtils = new FilesUtilsFromJdkFiles();

    public static void main(String[] args) {
        SizeResult root = SizeResult.root(getRootPath(args), new FilesUtilsFromJdkFiles());
        Stream.of(root)
                .parallel()
                .mapMulti(FileTreeSizeWithStreams::childrenToStream)
                .filter(s-> s==root)
                .forEach(System.out::println);
    }

    static void childrenToStream(SizeResult current, Consumer<SizeResult> consumer) {
        filesUtils.listSafely(current.currentDirectory())
                .forEach(path -> {
                    if (filesUtils.isDirectorySafely(path)){
                        childrenToStream(current.child(path), consumer);
                    }else{
                        current.processFile(path);
                    }
                });
        current.aggregate();
        consumer.accept(current);
    }

}
