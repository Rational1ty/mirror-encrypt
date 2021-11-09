package src;

import java.awt.Color;
import java.io.IOException;
import java.lang.Class;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

public final class MirrorConstants {
    private static final Properties constants = new Properties();
    private static final Path path = Paths.get("../mirror.properties");

    private static final String[] keys = {
        "delay", "create_window", "repeat_sequence",
        "beam_color", "trace_color", "success_color"
    };
    private static final String[] defaults = {
        "50", "1", "1",
        "red", "red", "green"
    };

    static {
        try {
            if (!Files.exists(path)) {
                // Pair each key with its default value, formatted and separated by a colon
                String[] lines = new String[keys.length];
                
                for (int i = 0; i < lines.length; i++) {
                    lines[i] = String.format("%-16s %s", keys[i] + ":", defaults[i]);
                }

                // Create mirror.properties in root directory and write default k:v pairs
                Files.createFile(path);
                Files.write(path, Arrays.asList(lines));
            }
            constants.load(Files.newInputStream(path));
        } catch (IOException ex) {
            System.err.println("Error while accessing file \"mirror.properties\".");
            System.exit(0);
        }

        // Register hook to save constants to mirror.properties on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                storeProperties();
            } catch (IOException ex) {
                System.err.println(
                    "Error while accessing file \"mirror.properties\". Some properties may not have been saved.");
            }
        }));
    }

    private MirrorConstants() {}

    public static Object get(String key) {
        Object res;
        
        if (constants.stringPropertyNames().contains(key)) {
            String value = constants.getProperty(key);
            res = getTypedValue(value);
        } else {
            res = null;
        }

        if (res == null) {
            System.err.println("Error while accessing application constants. Check file \"mirror.properties\" for invalid keys/values.");
            System.exit(0);
        }
        return res;
    }

    private static Object getTypedValue(String val) {
        if (val == null) return null;

        // If val is an integer
        if (val.matches("\\b[0-9]+\\b")) {
            return Math.abs(Integer.parseInt(val));
        }

        // If val is a hex string representing a color
        try {
            return Color.decode(val);
        } catch (NumberFormatException ex) {}

        // If val is a named color (ex: red, green, blue)
        try {
            return (Color) Class.forName("java.awt.Color").getField(val).get(null);
        } catch (ReflectiveOperationException ex) {}

        return null;
    }

    public static void set(String key, String value) {
        constants.setProperty(key, value);
    }

    private static void storeProperties() throws IOException {
        String[] lines = new String[keys.length];

        for (int i = 0; i < lines.length; i++) {
            lines[i] = String.format(
                "%-16s %s",
                keys[i] + ":",
                constants.getProperty(keys[i])
            );
        }

        Files.write(path, Arrays.asList(lines));
    }
}