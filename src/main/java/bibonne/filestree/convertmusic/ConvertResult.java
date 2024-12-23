package bibonne.filestree.convertmusic;

import bibonne.filestree.external.FilesUtilsFromJdkFiles;
import bibonne.filestree.traverser.BrowseResult;
import bibonne.filestree.utils.FilesUtils;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

public record ConvertResult(Path currentDirectory, MusicConverter musicConverter, FilesUtils filesUtils) implements BrowseResult {
    private static final Set<String> MUSIC_EXTENSION = Set.of("mp3", "wma", "wav", "flac", "opus", "ogg");

    public ConvertResult(Path currentDirectory){
        this(currentDirectory, new MusicConverter(), new FilesUtilsFromJdkFiles());
    }

    @Override
    public BrowseResult child(Path subdirectory) {
        return new ConvertResult(subdirectory, musicConverter, filesUtils);
    }

    @Override
    public void processFile(Path path) {
        var pathWithExtension = filesUtils.pathWithExtension(path);
        if (isMusicFileToBeConverted(pathWithExtension.extension())){
            convertToMp3(path, pathWithExtension);
        }
    }

    private void convertToMp3(Path path, FilesUtils.PathWithExtension pathWithExtension) {
        if (this.musicConverter.convert(path.getParent(), pathWithExtension.filename(), newFilenameWithMp3(pathWithExtension))){
            filesUtils.deleteSafely(path);
        }else {
            System.err.println("ERROR while converting file "+path+" to mp3 file");
        }
    }

    private String newFilenameWithMp3(FilesUtils.PathWithExtension pathWithExtension) {
       return pathWithExtension.filenameWithoutExtension()+".mp3";
    }

    private boolean isMusicFileToBeConverted(Optional<String> extension) {
        var lowerCaseExtension = extension.map(String::toLowerCase);
        return isMusicFile(lowerCaseExtension) && isToBeConverted(lowerCaseExtension.get());
    }

    private static boolean isToBeConverted(String extension) {
        return !isMp3(extension) && !isWma(extension);
    }

    private static boolean isMusicFile(Optional<String> extension) {
        return extension.isPresent() && isMusicFile(extension.get());
    }

    private static boolean isWma(String extensionToLowerCase) {
        return "wma".equals(extensionToLowerCase);
    }

    private static boolean isMp3(String extensionToLowerCase) {
        return "mp3".equals(extensionToLowerCase);
    }

    private static boolean isMusicFile(String extensionToLowerCase) {
        return MUSIC_EXTENSION.contains(extensionToLowerCase);
    }
    
}
