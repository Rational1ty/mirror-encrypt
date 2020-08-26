package src;

import static java.lang.System.out;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class MirrorEncrypt {
    public static void main(String[] args) throws IOException {
        Scanner kb = new Scanner(System.in);

        MirrorConstants.parseCommands(args);

        // Setting up beam/mirrors
        Beam beam = createBeam(kb);
        
        // Main menu, handles encryption, decoding, and displaying the mirror field
        // After printing the menu, this section will repeat indefinitely until the user enters the number 5
        byte choice = -1;
        String message;

        displayMainMenu();

        while (choice != 0) {
            do {
                out.print("\nEnter choice: ");
                try {
                    choice = kb.nextByte();
                    kb.nextLine();
                } catch (InputMismatchException ex) {
                    out.println("Input does not match expected type");
                    kb.nextLine();
                    continue;
                }

                switch (choice) {
                    case 0: 
                        break;
                    case 1: // Encrypt message
                        out.print("Enter message to encrypt: ");
                        message = kb.nextLine();

                        out.print("Encrypted message: ");
                        out.println(encrypt(message, beam));

                        try { Thread.sleep(250); }
                        catch (InterruptedException ex) {}

                        if (MirrorConstants.CREATE_WINDOW) show(message, beam, "encrypt");
                        break;
                    case 2: // Decrypt message
                        out.print("Enter message to decrypt: ");
                        message = kb.nextLine();

                        out.print("Decrypted message: ");
                        out.println(encrypt(message, beam));

                        try { Thread.sleep(250); }
                        catch (InterruptedException ex) {}

                        if (MirrorConstants.CREATE_WINDOW) show(message, beam, "decrypt");
                        break;
                    case 3: // Display mirror field
                        out.println("\n  abcdefghijklm  ");
                        out.println("  -------------  ");
                        for (int y = 0; y < beam.field.length; y++) {
                            out.print((char)(y + 65) + "|");
                            for (int x = 0; x < beam.field[y].length; x++)
                                out.print(beam.field[y][x]);
                            out.print("|" + (char)(y + 110) + "\n");
                        }
                        out.println("  -------------  ");
                        out.println("  NOPQRSTUVWXYZ  ");
                        break;
                    case 4: // Export mirror field
                        out.print("Enter file to export to: ");
                        String path = kb.nextLine();
                        FileWriter fw = new FileWriter(path);
                        for (int y = 0; y < beam.field.length; y++) {
                            for (int x = 0; x < beam.field[y].length; x++)
                                fw.write(beam.field[y][x]);
                            fw.write("\n");
                        }
                        fw.close();
                        break;
                    case 5: // Change mirror field
                        beam = createBeam(kb);
                        displayMainMenu();
                        break;
                    case 6:
                        out.print("Command(s): ");
                        String[] c = kb.nextLine().split("\\s+");
                        MirrorConstants.parseCommands(c);
                        out.println((c.length > 1 ? "Commands" : "Command") + " executed");
                        break;
                    default:
                        out.println(choice + " is not a valid choice");
                }
            } while (choice > 0 && choice <= 5);
        }

        kb.close();
    }

    // Prints out the main menu
    static void displayMainMenu() {
        out.println("\n1) Encrypt message");
        out.println("2) Decode message");
        out.println("3) Display mirror field");
        out.println("4) Export mirror field");
        out.println("5) Change mirror field");
        out.println("6) Enter command(s)");
        out.println("0) Exit program");
    }

    // Passes [text] through the [beam]'s mirror field and returns the resulting String
    static String encrypt(String text, Beam beam) {
        String r = "";
        for (int i = 0; i < text.length(); i++) {
            if (!(text.charAt(i) + "").matches("[A-Z|a-z]")) {
                r += text.charAt(i);
            } else {
                r += beam.tracePath(text.charAt(i));
            }
        }
        return r;
    }

    // Passes [text] through the [beam]'s mirror field, displays a window that shows the animated process, and returns the resulting String
    // The window will close after the encryption/decoding is complete and can be closed at any time
    // [op] should either be "encrypt" or "decode"
    static void show(String text, Beam beam, String op) {
        new Master(text, beam, op, MirrorConstants.DELAY);
    }

    // Creates a new mirror field and returns a Beam initialized with that field
    static Beam createBeam(Scanner kb) throws IOException {
        out.println("\n1) Enter mirror field from keyboard");
        out.println("2) Create mirror field from file");
        out.println("3) Generate random mirror field");

        byte choice = -1;

        do {
            out.print("\nEnter choice: ");
            try {
                choice = kb.nextByte();
                kb.nextLine();
            } catch (InputMismatchException ex) {
                out.println("Input does not match expected type");
                kb.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    {
                        out.print("Enter mirrors: ");
                        String mirrors = kb.nextLine();
                        try {
                            return new Beam(toMirrorField(mirrors));
                        } catch (IllegalArgumentException ex) {
                            out.println(ex.getMessage());
                            System.exit(-1);
                        }
                    }
                    break;
                case 2:
                    out.print("Enter file path or name: ");
                    String path = kb.nextLine();

                    try {
                        return new Beam(new File(path));
                    } catch (FileNotFoundException ex) {
                        out.println("File not found: " + ex.getMessage());
                        System.exit(-1);
                    }
                    break;
                case 3:
                    out.print("Enter % of field spaces that are mirrors: ");
                    double pct = kb.nextDouble();

                    if (pct > 100) pct = 100;
                    if (pct < 0) pct = 0;
                    pct /= 2;

                    char[][] mirrors = new char[13][13];
                    for (int y = 0; y < mirrors.length; y++) {
                        for (int x = 0; x < mirrors[y].length; x++) {
                            double r = Math.random() * 100;
                            mirrors[y][x] = r < pct ? '\\' : r > 100 - pct ? '/' : ' ';
                        }
                    }
                    return new Beam(mirrors);
                default:
                    out.println(choice + " is not a valid choice");
            }
        } while (choice < 1 || choice > 3);

        return new Beam();
    }

    // TODO: update comments on/in this method
    // Converts a String of any length to a char array of length 169 (13^2)
    // If [mirrors] contains any integers, they will be parsed as the respective number of spaces
    // If [mirrors] contains the letter n, it will be parsed as a newline character
    static char[] toMirrorField(String mirrors) throws IllegalArgumentException {
        //char[] field = new char[13 * 13];
        final int flen = 13 * 13;
        String fstr = "";

        // Check for any invalid characters
        if (!mirrors.matches("[\\\\/0-9n\\s]*"))
            throw new IllegalArgumentException("Invalid character entered; accepted characters are /, \\, 0-9, n, or space");

        if (MirrorConstants.REPEAT_SEQ) {
            for (int i = 0; i < flen; i += mirrors.length())
                mirrors += mirrors;
        } else {
            for (int i = mirrors.length(); i < flen; i++)
                mirrors += " ";
        }

        // If user entered any numbers in the [mirrors], convert them to spaces
        // Ex: /5/ becomes /     /
        // Also, if user entered the letter 'n' convert it to a newline character
        int pos = 0;
        for (char c : mirrors.toCharArray()) {
            if (c == '/' || c == '\\' || c == ' ') {
                fstr += c;
                pos++;
                continue;
            }

            if (c == 'n') {
                int dist = 13 - (pos % 13);
                while (dist > 0) {
                    fstr += " ";
                    pos++;
                    dist--;
                }
                continue;
            }

            try {
                int spaces = Integer.parseInt(c + "");
                while (spaces > 0) {
                    fstr += " ";
                    pos++;
                    spaces--;
                }
            } catch (NumberFormatException ex) {
                out.println("Fatal internal error: " + ex);
                System.exit(-1);
            }
        }
        
        // Trim fstr to the correct length if it's too long
        if (fstr.length() > flen) {
            fstr = fstr.substring(0, flen + 1);
        }
        
        return fstr.toCharArray();
    }
}