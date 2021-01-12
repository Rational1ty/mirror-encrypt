package src;

import java.awt.Color;
import java.io.IOException;
import java.lang.Class;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public final class MirrorConstants {
    private static final Properties constants = new Properties();
    private static final Path path = Paths.get("../mirror.properties");

    static {
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                Files.write(path, Arrays.asList(new String[] {
                    "beam_color:\t\t\tred",
                    "trace_color:\t\tred",
                    "success_color:\t\tgreen",
                    "create_window:\t\t1",
                    "repeat_sequence:\t1",
                    "delay:\t\t\t\t50"
                }));
            }
            constants.load(Files.newInputStream(path));
        } catch (IOException ex) {
            System.err.println("Error while accessing file \"mirror.properties\".");
            System.exit(0);
        }

        // Register hook to save constants to mirror.properties on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println(constants.propertyNames());
            } catch (Exception ex) {    // TODO: change to IOException when possible
                System.err.println("Error while accessing file \"mirror.properties\". Some properties may not have been saved.");
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

    private static void store() throws IOException {
        
    }
}