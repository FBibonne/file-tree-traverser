package bibonne.files.sizebrowsing;

import java.nio.file.Path;

public class BrowseFilesForSizes {

    public static void main(String[] args) {
        System.out.println(new BrowseForSizes(BrowseResult.root(Path.of("C:/"))).call());
    }


}
