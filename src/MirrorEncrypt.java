package src;

import static java.lang.System.out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MirrorEncrypt {
	static Scanner kb = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		// Setting up beam/mirrors menu
		Beam beam = createBeam();
		
		// Main menu, handles encryption, decoding, and displaying the mirror field
		// After printing the menu, this section will repeat until 0 is entered
		String message;

		displayMainMenu();

		// Main menu loop
		while (true) {
			int choice = getChoice(0, 6);
			boolean createWindow = (int) MirrorConstants.get("create_window") != 0;

			switch (choice) {
				case 0:
					kb.close();
					return;

				case 1:	 // Encrypt message
					out.print("Enter message to encrypt: ");
					message = kb.nextLine();

					if (message.isEmpty()) {
						message = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
					}

					out.print("Encrypted message: ");
					out.println(encrypt(message, beam));

					sleep(250);

					if (createWindow) {
						show(message, beam, "encrypt");
					}

					break;

				case 2:	 // Decrypt message
					out.print("Enter message to decrypt: ");
					message = kb.nextLine();

					out.print("Decrypted message: ");
					out.println(encrypt(message, beam));

					sleep(250);

					if (createWindow) {
						show(message, beam, "decrypt");
					}

					break;

				case 3:	 // Display mirror field
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

				case 4:	 // Export mirror field
					out.print("Enter file name (will be saved in assets/user/): ");
					String fileName = kb.nextLine();

					if (!fileName.matches(".+\\.txt")) {
						fileName += ".txt";
					}

					FileWriter fw = new FileWriter("../assets/user/" + fileName);

					for (int y = 0; y < beam.field.length; y++) {
						for (int x = 0; x < beam.field[y].length; x++)
							fw.write(beam.field[y][x]);
						fw.write("\n");
					}

					fw.close();
					break;

				case 5:	 // Change mirror field
					beam = createBeam();
					displayMainMenu();
					break;

				case 6:  // Enter command(s)
					out.print("Enter command(s): ");
					String[] commands = kb.nextLine().split("\\s+");

					for (String cmd : commands) {
						var kv = cmd.split("[:=]");
						MirrorConstants.set(kv[0], kv[1]);
					}

					out.println("Command" + (commands.length > 1 ? "s" : "") + " executed");
					break;
			}
		}
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

	static int getChoice(int min, int max) {
		while (true) {
			out.print("\nEnter choice: ");

			try {
				int choice = kb.nextInt();
				kb.nextLine();
				if (choice >= min && choice <= max) return choice;
				out.println(choice + " is not a valid choice");
			} catch (InputMismatchException ex) {
				out.println("Input does not match expected type");
			}
		}
	}

	static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ex) {
			// do nothing
		}
	}

	// Passes [text] through the [beam]'s mirror field and returns the resulting String
	static String encrypt(String text, Beam beam) {
		String r = "";

		for (char c : text.toCharArray()) {
			boolean encryptable = (c + "").matches("[A-Z|a-z]");
			r += encryptable ? beam.tracePath(c) : c;
		}

		return r;
	}

	// Passes [text] through the [beam]'s mirror field, displays a window that shows the animated process, and returns the resulting String
	// The window will close after the encryption/decoding is complete and can be closed at any time
	// [op] should either be "encrypt" or "decode"
	static void show(String text, Beam beam, String op) {
		int delay = (int) MirrorConstants.get("delay");
		new Master(text, beam, op, delay);
	}

	// Creates a new mirror field and returns a Beam initialized with that field
	static Beam createBeam() throws IOException {
		out.println("\n1) Enter mirror field from keyboard");
		out.println("2) Create mirror field from file");
		out.println("3) Generate random mirror field");

		int choice = getChoice(1, 3);

		switch (choice) {
			case 1: {
				out.print("Enter mirrors: ");
				String mirrors = kb.nextLine();

				try {
					return new Beam(toMirrorField(mirrors));
				} catch (IllegalArgumentException ex) {
					out.println(ex.getMessage());
					System.exit(-1);
				}

				break;
			}
			case 2:
				while (true) {
					out.print("Enter file name (will search in /assets/user): ");
					String path = "../assets/user/" + kb.nextLine();

					try {
						return new Beam(new File(path));
					} catch (FileNotFoundException ex) {
						out.println("File not found: " + ex.getMessage());
					}
				}
				// no need for break here since it would be unreachable and the compiler complains about it
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
		}

		return new Beam();
	}

	// Converts a String of any length to a char array of length 169 (13^2)
	// If [mirrors] contains any integers, they will be parsed as the respective number of spaces
	static char[] toMirrorField(String mirrors) throws IllegalArgumentException {
		final int flen = 169;

		if (mirrors.equals("")) {
			return new char[flen];
		}

		String field = "";

		// Check for any invalid characters
		// Accepted characters are: /, \, 0-9, and space
		if (!mirrors.matches("[\\\\/0-9\\s]*"))
			throw new IllegalArgumentException("Invalid character entered; accepted characters are /, \\, 0-9, n, or space");

		// Put mirrors into field
		boolean repeatSeq = (int) MirrorConstants.get("repeat_sequence") != 0;

		if (repeatSeq) {
			// Repeat the sequence until field is full
			// Always repeat until field length > flen, then trim so that field length = flen
			field = mirrors.repeat((int) Math.ceil((double) flen / mirrors.length()));
			field = field.substring(0, flen);
		} else {
			// Otherwise, fill the rest of field with spaces
			field = mirrors += " ".repeat(flen - mirrors.length());
		}

		// If user entered any numbers in mirrors, convert them to spaces
		// Ex: /5/ becomes /	 /
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

			// space, /, or \
			r += c;
		}
		
		// Trim r to the correct length if it's too long
		if (r.length() > flen) {
			r = r.substring(0, flen);
		}
		
		return r.toCharArray();
	}
}