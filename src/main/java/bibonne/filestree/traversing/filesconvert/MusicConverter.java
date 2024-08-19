package bibonne.filestree.traversing.filesconvert;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class MusicConverter {
    public boolean convert(Path directory, Path fileName, String targetFilename) {
        //ffmpeg -y -i "$sansext.opus" "$sansext.flac"
        List<String> command = List.of("ffmpeg", "-y", "-i", fileName.toString(), targetFilename);
        ProcessBuilder pb = new ProcessBuilder(command);
        System.out.println(STR."*** LAUNCH \{command} ***");
        pb.directory(directory.toFile());
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            (new BufferedReader(new InputStreamReader(process.getInputStream()))).lines().forEach(System.out::println);
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            System.err.println(STR."Error \{e.getClass()} while processing \{fileName} : \{e.getMessage()}");
            return false;
        }
    }
}
