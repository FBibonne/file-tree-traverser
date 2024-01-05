package bibonne.files.sizebrowsing;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;

public record BrowseForSizes(BrowseResult current) implements Callable<BrowseResult> {
    public static final long THREATHOLD = 1_000_000_000L;

    @Override
    public BrowseResult call() {
        try(var scope=new StructuredTaskScope.ShutdownOnFailure()){
            for(File file : listAllFiles(current.getDirectory())){
                if (isDirectory(file)){
                    scope.fork(new BrowseForSizes(current.child(file.toPath())));
                }else{
                    current.addNegligeableSize(length(file));
                }
            }
            scope.join();
            current.updateSizes();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return current;
    }

    private static long length(File file) {
        try {
            return file.length();
        } catch (Exception e) {
            return 0;
        }
    }

    private static boolean isDirectory(File file) {
        try {
            return file.isDirectory();
        } catch (Exception e) {
            return false;
        }
    }

    private File[] listAllFiles(Path directory) {
        File[] retour;
        try {
            retour=directory.toFile().listFiles();
            retour=retour==null?new File[0]:retour;
        } catch (Exception e) {
            retour=new File[0];
        }
        return retour;
    }
}
