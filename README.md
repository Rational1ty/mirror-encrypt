# Mirror encrypt

Encrypt or decode messages using a grid of mirrors and a laser

### Overview
- This program provides an interesting way to encrypt/decrypt messages using mirrors and LASER BEAMS!
- It includes various ways to create your own unique sets of mirrors (mirror fields), load fields that you've already made,
  generate mirrors automatically, or save some of your favorite combinations
- The program also animates the process in real time, unless it is run in text-only mode (see commands for more info)
- To run the program, double-click on the file `launch.bat`
- Additionally, you can customize many parts of the program using commands. A full list of commands, what they do,
  and how to use them can be found below

### Info
- Author: Matthew Davidson (mdavidson390@outlook.com)
- Version: 2.2.0
- Languages: Java, Batch
- Offline
- LOC: 1503

---

## Commands

- All commands are case-insensitive
- Commands are made of 2 parts: the "key" (the 1st part) and the "value" (the 2nd part), separated by either a `:` or a `=`
  - Ex: `key:value` or `key=value`
- When entering commands from the command line (using *option 6: enter commands*), there should be no space between the key, value, and `:`/`=`
- Settings are saved in the `mirror.properties` file
  - Each property should be on its own line
  - Keys/values can be separated by either `:` or `=`; space *is allowed* in this file
- If you enter a command that is invalid (the key, value, or both key and value are wrong) the program will display an error
  and then safely shut down

### List of commands
|     Command     |                               Description                               |      Accepted values     |                        Notes                       |
| --------------- | ----------------------------------------------------------------------- | ------------------------ | -------------------------------------------------- |
| delay           | Changes the amount of time between each frame of animation              | Non-negative integers¹   | Values are measured in milliseconds                |
| create_window   | Determines whether or not the encryption/decryption process is animated | 0 or 1                   |                                                    |
| repeat_sequence | Toggles repeat when entering a mirror field from the keyboard           | 0 or 1                   | Will repeat input sequence until field is full     |
| beam_color      | Changes the color of the beam                                           | Named² or hex color      | Only certain color names are valid; see list below |
| trace_color     | Changes color 1 (the "input" color)                                     | Named² or hex color      |                                                    |
| success_color   | Changes color 2 (the "output" color)                                    | Named² or hex color      |                                                    |

### Command notes
¹ The delay, *n*, must be in the range 0 ≤ *n* ≤ 2,147,483,647 \
² Named colors are limited to the following values: black, blue, cyan, gray, green, magenta, orange, pink, red, white, yellow 

---

## Tips and Tricks

### Creating/loading mirror field
1. From the keyboard
  - If `repeat_sequence` is enabled, whatever you enter will be repeated top to bottom, left to right until the sequence reaches the end of the grid.
    You can use this to make some really interesting patterns (hint: try entering a sequence that is exactly 13 characters long).
  - You can enter numbers (1-9) to represent a group of spaces
    - Ex: `/\/7/\` translates to `/\/       /\`
    
2. From a file
  - You can create your own mirror fields by using notepad or any other simple text editor
  - If you make your own, make sure that the field is exactly 13 x 13 characters (you can check by pressing "ctrl + a" —
    it should highlight a blue rectangle. If there are any areas that aren't highlighted, fill them in with spaces)
  - Save it as a `.txt` file in the `assets/user/` folder
  - When running the program, you can load saved mirror fields by entering the filename when prompted
  
3. Randomly generated
  - When generating a random mirror field by entering a % value, any number between 0 and 100 will work, so experiment with
    different values! Higher mirror density usually results in more secure encryption, but more mirrors is not always
    better. Try to find the perfect amount of mirrors to create the most unpredictable code!

**Main menu**
- Sometimes, the animation window will be created in the background whenever you start to encrypt/decrypt a message. If you
  don't see the window show up, look at the taskbar (bottom of the screen) for a picture of a red laser bouncing off
  a mirror. This is the window; simply click it to bring it to the front
- If you like the mirrors that you have set up, you can save them by choosing *option 4: export mirror field*
  - The field will be saved as a `.txt` file in `assets/user/`
  - Don't worry about creating the file first; the file will be created for you if it doesn't already exist
  - You can enter the name of a file that already exists to replace it

---

## How to install Java

In order to run a Java program, you must first install Java on your computer. If Java is already installed on this device, 
then you should be ready to go. Otherwise, you'll need to follow these steps in order to download and install Java. If you're
not sure whether you have Java installed or not, follow the brief steps below.

### Checking if Java is already installed
1. Open the windows search bar (bottom right on the desktop) and search for "java"
2. If the first result is `java`, then Java is already installed. Otherwise, you will need to install Java before you can run any Java programs

### Installing Java
1. Go to the [Java download page](https://java.com/en/download/)
2. Click **"Agree and Start Free Download"** to download the current version of Java
3. Go to your `Downloads` folder and double-click on the Java installer — it should be named something like `jre-8u271-windows-x64.exe`
4. Follow the steps in the installer to complete the installation
