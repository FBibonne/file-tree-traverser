package bibonne.filestree.traversing.filesconvert;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class MusicConverter {
    public void convert(Path directory, Path fileName, String targetFilename) {
        //ffmpeg -i "$sansext.opus" "$sansext.flac"
        List<String> command = List.of("ffmpeg", "-i", fileName.toString(), targetFilename);
        ProcessBuilder pb = new ProcessBuilder(command);
        System.out.println(STR."*** LAUNCH \{command} ***");
        pb.directory(directory.toFile());
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            (new BufferedReader(new InputStreamReader(process.getInputStream()))).lines().forEach(System.out::println);
        } catch (IOException e) {
            System.err.println(STR."Error while starting process : \{e.getMessage()}");
        }
    }
}
