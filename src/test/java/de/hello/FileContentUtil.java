package de.hello;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public final class FileContentUtil {

    public static String getContentAsString(String file) {
        try {
            Path path = Paths.get(FileContentUtil.class.getClassLoader().getResource(file).toURI());
            StringBuilder data = new StringBuilder();
            Stream<String> lines = Files.lines(path);
            lines.forEach(line -> data.append(line).append("\n"));
            lines.close();
            return data.toString();
        } catch (URISyntaxException | IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
