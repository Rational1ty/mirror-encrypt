package src;

import java.awt.Color;
import java.io.IOException;
import java.lang.Class;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

public abstract class MirrorConstants {
    public static final HashMap<MKey, MValue<?>> constants = new HashMap<MKey, MValue<?>>();
    private static final Path path = Paths.get("../mirror.properties");

    static {
        try {
            if (!Files.exists(path)) {
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
            loadProperties();
        } catch (IOException ex) {
            System.err.println("Error while accessing mirror.properties");
            System.exit(0);
        }
    }

    public static MValue<?> get(MKey key) {
        return constants.get(key);
    }

    private static void loadProperties() throws IOException {
        var lines = Files.readAllLines(path);
        for (String line : lines) {
            var kv = line.split(":\\s*");
            MKey key = MKey.valueOf(kv[0].toUpperCase());
            MValue<?> val = getValue(kv[1]);
            if (val == null) {
                throw new IOException();
            }
            constants.put(key, val);
        }
    }

    private static MValue<?> getValue(String val) {
        // If val is an integer
        if (val.matches("\\b[0-9]+\\b")) {
            return new MValue<Integer>(Integer.parseInt(val));
        }

        // If val is a boolean
        if (val.matches("(?i)\\btrue|false\\b")) {
            return new MValue<Boolean>(Boolean.parseBoolean(val));
        }

        // If val is a hex string representing a color
        try {
            Color c = Color.decode(val);
            return new MValue<Color>(c);
        } catch (NumberFormatException ex) {}

        // If val is a named color (ex: red, green, blue)
        try {
            Color c = (Color) Class.forName("java.awt.Color").getField(val).get(null);
            return new MValue<Color>(c);
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }
}