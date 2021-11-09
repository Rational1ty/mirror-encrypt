package src;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public final class MirrorConstants {
	private static final Properties constants = new Properties();
	private static final Path path = Paths.get("../mirror.properties");

	private static final List<String> keys = List.of(
		"delay", "create_window", "repeat_sequence",
		"beam_color", "trace_color", "success_color"
	);
	private static final List<String> defaults = List.of(
		"50", "1", "1",
		"red", "red", "green"
	);

	static {
		try {
			if (!Files.exists(path)) {
				// Pair each key with its default value, formatted and separated by a colon
				List<String> lines = IntStream.range(0, keys.size())
					.mapToObj(i -> String.format("%-16s %s", keys.get(i) + ":", defaults.get(i)))
					.toList();

				// Create mirror.properties in root directory and write default k:v pairs
				Files.createFile(path);
				Files.write(path, lines);
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
		UnaryOperator<String> formatKey = (String key) -> 
			String.format("%-16s %s", key + ":", constants.getProperty(key));

		var lines = keys.stream()
			.map(k -> k.transform(formatKey))
			.toList();

		Files.write(path, lines);
	}
}