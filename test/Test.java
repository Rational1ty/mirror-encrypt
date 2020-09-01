package test;

import static java.lang.System.out;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);

        while (true) {
            out.print("Enter mirrors: ");
            String inp = kb.nextLine();
            char[] field = toMirrorField(inp);

            out.println("\nLength of field: " + field.length + "\n");

            for (int i = 0; i < field.length; i++) {
                out.print(field[i]);
                if (i == 0) continue;
                if ((i + 1) % 13 == 0) {
                    out.println();
                }
            }

            out.println();
        }
    }

    // Converts a String of any length to a char array of length 169 (13^2)
    // If [mirrors] contains any integers, they will be parsed as the respective number of spaces
    // If [mirrors] contains the letter n, it will be parsed as a newline character
    static char[] toMirrorField(String mirrors) throws IllegalArgumentException {
        final int flen = 169;
        String field = "";

        // Check for any invalid characters
        // Accepted characters are: /, \, 0-9, n, and space
        if (!mirrors.matches("[\\\\/0-9n\\s]*"))
            throw new IllegalArgumentException("Invalid character entered; accepted characters are /, \\, 0-9, n, or space");

        // Put mirrors into field
        if (true) {
            // Repeat the sequence until field is full
            // Always repeat until field length > flen, then trim so that field length = flen
            field = mirrors.repeat( (int) Math.ceil( (double) flen / mirrors.length() ) );
            field = field.substring(0, flen);
        } else {
            // Otherwise, fill the rest of field with spaces
            field = mirrors += " ".repeat(flen - mirrors.length());
        }

        // If user entered any numbers in mirrors, convert them to spaces
        // Ex: /5/ becomes /     /
        // Also, if user entered the letter 'n' convert it to a newline character
        // Ex: /n/ becomes /\n/
        String r = "";
        String spaces = "";
        for (int i = 0; i < field.length(); i++) {
            char c = field.charAt(i);

            // numbers 0-9
            if ((c + "").matches("[0-9]")) {
                spaces += c;
                continue;
            }

            // anything other than a number; check if spaces need to be inserted first
            if (spaces.length() > 0) {
                int buffer = Integer.parseInt(spaces);
                r += " ".repeat(buffer);
                spaces = "";
            }

            // / or \
            if (c == '/' || c == '\\') {
                r += c;
                continue;
            }

            // n or N
            if (c == 'n' || c == 'N') {
                int distToLineEnd = 13 - (i % 13) - 1;
                r += " ".repeat(distToLineEnd);
                continue;
            }

            // space
            r += c;
        }
        
        // Trim r to the correct length if it's too long
        if (r.length() > flen) {
            r = r.substring(0, flen);
        }
        
        return r.toCharArray();
    }
}