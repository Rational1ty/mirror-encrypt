package src;

import static java.lang.System.out;
import java.awt.Color;
import java.lang.Class;
import java.lang.reflect.Field;

public abstract class MirrorConstants {
    public static Color BEAM_COLOR = Color.RED;
    public static Color COLOR1 = Color.RED;
    public static Color COLOR2 = Color.GREEN;
    public static boolean CREATE_WINDOW = true;
    public static int DELAY = 50;
    public static boolean REPEAT_SEQ = true;

    public static void parseCommand(String command) {
        command = command.toLowerCase();
        String[] s = command.split("[:]");
        switch (s[0]) {
            case "beam":    // BEAM_COLOR
                try {
                    Field f = Class.forName("java.awt.Color").getField(s[1]);
                    BEAM_COLOR = (Color)f.get(null);
                } catch (Exception ex) {
                    out.printf("Unrecognized command value: \"%s\"\n", s[1]);
                    System.exit(-1);
                }
                break;
            case "color1":  // COLOR1
                try {
                    Field f = Class.forName("java.awt.Color").getField(s[1]);
                    COLOR1 = (Color)f.get(null);
                } catch (Exception ex) {
                    out.printf("Unrecognized command value: \"%s\"\n", s[1]);
                    System.exit(-1);
                }
                break;
            case "color2":  // COLOR2
                try {
                    Field f = Class.forName("java.awt.Color").getField(s[1]);
                    COLOR2 = (Color)f.get(null);
                } catch (Exception ex) {
                    out.printf("Unrecognized command value: \"%s\"\n", s[1]);
                    System.exit(-1);
                }
                break;
            case "window":  // CREATE_WINDOW
                if (s[1].equals("true") || s[1].equals("false")) {
                    CREATE_WINDOW = Boolean.parseBoolean(s[1]);
                } else {
                    out.printf("Unrecognized command value: \"%s\"\n", s[1]);
                    System.exit(-1);
                }
                break;
            case "delay":   // DELAY
                try {
                    DELAY = Integer.parseInt(s[1]);
                } catch (NumberFormatException ex) {
                    out.printf("Unrecognized command value: \"%s\"\n", s[1]);
                    System.exit(-1);
                }
                break;
            case "repeat":  // REPEAT_SEQ
                if (s[1].equals("true") || s[1].equals("false")) {
                    REPEAT_SEQ = Boolean.parseBoolean(s[1]);
                } else {
                    out.printf("Unrecognized command value: \"%s\"\n", s[1]);
                    System.exit(-1);
                }
                break;
            default:
                out.printf("Unrecognized command key: \"%s\"\n", s[0]);
                System.exit(-1);
        }
    }

    public static void parseCommands(String[] commands) {
        if (commands.length > 0)
            for (String command : commands)
                parseCommand(command);
    }
}