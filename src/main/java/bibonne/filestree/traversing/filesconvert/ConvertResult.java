package bibonne.filestree.traversing.filesconvert;

import bibonne.filestree.traversing.BrowseResult;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

public record ConvertResult(Path currentDirectory, MusicConverter musicConverter) implements BrowseResult {
    private static final Set<String> MUSIC_EXTENSION = Set.of("mp3", "wma", "wav", "flac", "opus", "ogg");

    public ConvertResult(Path currentDirectory){
        this(currentDirectory, new MusicConverter());
    }

    @Override
    public BrowseResult child(Path childDirectory) {
        return new ConvertResult(childDirectory, musicConverter);
    }

    @Override
    public void addFilePath(Path path) {
        if (isNotWmaorMp3(path)){
            convertToMp3(path);
        }
    }

    private void convertToMp3(Path path) {
        this.musicConverter.convert(path.getParent(), path.getFileName(), newFilenameWithMp3(path));
    }

    private String newFilenameWithMp3(Path path) {
        String filename = path.getFileName().toString();
        int lastDot = filename.lastIndexOf('.');
        return STR."\{filename.substring(0, lastDot)}.mp3";
    }

    private boolean isNotWmaorMp3(Path path) {
        var fileName = path.getFileName().toString();
        var extension = extension(fileName).map(String::toLowerCase);
        return extension.isPresent() && isMusicFile(extension.get()) && ! isMp3(extension.get()) && ! isWma(extension.get());
    }

    private boolean isWma(String extensionToLowerCase) {
        return "wma".equals(extensionToLowerCase);
    }

    private boolean isMp3(String extensionToLowerCase) {
        return "mp3".equals(extensionToLowerCase);
    }

    private static Optional<String> extension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot == -1) return Optional.empty();
        if (lastDot < fileName.length() - 1) return Optional.of(fileName.substring(lastDot + 1));
        //filename ends with a dot :
        return Optional.empty();
    }

    private boolean isMusicFile(String extensionToLowerCase) {
        return MUSIC_EXTENSION.contains(extensionToLowerCase);
    }

    @Override
    public BrowseResult afterTraverse() {
        return this;
    }
}
