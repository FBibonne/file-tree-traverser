package bibonne.filestree.traverser;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Traverser {

    static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    private Traverser(){}

    public static BrowseResult browseFor(BrowseResult browseResult) throws InterruptedException, ExecutionException {
        return executor.invokeAll(List.of(new InternalTraverser(browseResult))).getFirst().get();
    }

    public static BrowseResult browseWithExecutorServiceFor(BrowseResult browseResult) throws InterruptedException, ExecutionException {
        return executor.invokeAll(List.of(new InternalTraverser(browseResult))).getFirst().get();
    }

}
