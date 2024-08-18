package bibonne.filestree.traversing.internal;

import bibonne.filestree.traversing.BrowseResult;

import java.util.concurrent.Callable;

public class Traverser {

    private Traverser(){}

    public static Callable<BrowseResult> forBrowseResult(BrowseResult browseResult) {
        return new InternalTraverser(browseResult);
    }

}
