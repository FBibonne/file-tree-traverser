package bibonne.filestree.traverser;

public class Traverser {

    private Traverser(){}

    public static BrowseResult browseFor(BrowseResult browseResult) {
        return (new InternalTraverser(browseResult)).call();
    }

}
