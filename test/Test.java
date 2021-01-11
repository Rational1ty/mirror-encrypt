package test;

import static java.lang.System.*;
import java.nio.file.*;
import java.util.*;

public class Test {
    public static void main(String[] args) throws Exception {
        Path path = Paths.get("..", "mirror.properties");
        if (Files.exists(path)) {
            out.println("File already exists");
            exit(0);
        }
        Files.createFile(path);
        Files.write(path, Arrays.asList(new String[] {
            "beam_color:\t\t\tred",
            "trace_color:\t\tred",
            "success_color:\t\tgreen",
            "create_window:\t\ttrue",
            "repeat_sequence:\ttrue",
            "delay:\t\t\t\t50"
        }));
    }
}