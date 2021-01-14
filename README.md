# Mirror encrypt

Encrypt or decode messages using a grid of mirrors and a laser

**Overview:**
- This program provides an interesting way to encrypt/decrypt messages using mirrors and LASER BEAMS!
- It includes various ways to create your own unique sets of mirrors (mirror fields), load fields that you've already made,
  generate mirrors automatically, or save some of your favorite combinations
- The program also animates the process in real time, unless it is run in text-only mode (see commands for more info)
- To run the program, double-click on the file "launcher.bat"
- Additionally, you can customize many parts of the program using commands. A full list of commands, what they do,
  and how to use them can be found below

**Info:**
- Author: Matthew Davidson (mdavidson390@outlook.com)
- Version: 2.2.0
- Languages: Java (JDK 13)
- Offline
- LOC: 1503

---

## Commands

* All commands are case-insensitive
* Commands are made of 2 parts: the "key" (the 1st part) and the "value" (the 2nd part), separated by either a `:` or a `=`
  Ex: `key:value` or `key=value`

Commands are added to the program's "launcher.bat" file, in the "commands" variable, seperated by a space
1. To edit the program's start-up commands, right click on the "launcher.bat" file and click "edit"
2. Go to the variable named "commands" and add the commands that you'd like to run
3. Ex: `set commands="window:true delay:50 beam:blue"`
4. Save your changes by pressing "ctrl + s", then close the file. It is now ready to run

A few things to keep in mind when using commands:
- If you enter a command that is invalid (the key, value, or both key and value are wrong) the program will display an error
  and then safely shut down
- In the list of commands below, the value of each command is replaced with a placeholder that represents a value (tf for
  true/false, ms for # of milliseconds, etc.)

**List of commands**
    Command     |                               Description                               |      Accepted values     |                        Notes
--------------- | ----------------------------------------------------------------------- | ------------------------ | --------------------------------------------------
delay           | changes the amount of time between each frame of animation              | Any non-negative integer | Values are measured in milliseconds
create_window   | determines whether or not the encryption/decryption process is animated | 0 or 1                   | 
repeat_sequence | toggles repeat when entering a mirror field from the keyboard           | 0 or 1                   | Will repeat input sequence until field is full
beam_color      | changes the color of the beam                                           | Named* or hex color      | Only certain color names are valid; see list below
trace_color     | changes color 1 (the "input" color)                                     | Named* or hex color      | 
success_color   | changes color 2 (the "output" color)                                    | Named* or hex color      |



- `beam:color`: changes the color of the beam
    > Acceptable values: black, blue, cyan, gray, green, magenta, orange, pink, red, white, yellow  
    > Ex: "beam:cyan", "beam:green"  
    > Default value: red  
    > This command is especially useful if you are colorblind and cannot see red/green very well, as both the beam and the two
    main colors (color1/color2) are either red or green  
    > It is not recommended to set either the beam or main colors to black or white, as this may make some aspects of the program
    harder to see

- `color1:color`: changes the color of color1 (the "input" color)
    > Acceptable values: black, blue, cyan, gray, green, magenta, orange, pink, red, white, yellow  
    > Ex: `color1:gray`, `color1:magenta`  
    > Default value: red  

- `color2:color`: changes the color of color2 (the "output" color)
    > Acceptable values: black, blue, cyan, gray, green, magenta, orange, pink, red, white, yellow  
    > Ex: `color2:orange`, `color1:yellow`  
    > Default value: green  

- `delay:ms`: changes the amount of time (in milliseconds) between each frame of animation (assuming window is true)
    > Accepted values: any non>negative integer (ms >= 0)  
    > Ex: `delay:25` would make the animation faster, while `delay:100` would make it slower  
    > Default value: 50  
    > It is not recommended to use values greater than 500 or values that are not an even factor of 1000  
    > Ex: `delay:2000` (just extrememly slow), or `delay:37` (may cause animation timing issues)  

- `repeat:tf`: toggles the program's repeat functionality when creating a mirror field from keyboard (see tips & tricks section)
    > Accepted values: boolean (true, false)  
    > Ex: `repeat:true` or `repeat:false`  
    > Default value: true  

- `window:tf`: determines whether or not the program creates a window to animate the encryption/decryption process
    > Accepted values: boolean (true, false)  
    > Ex: `window:true` would cause a window to be created; `window:false` would cause the program to run in text-only mode  
    > Default value: true  

---

## Tips and Tricks

**Creating/loading mirror field**

*From the keyboard:*
- When creating a new mirror field from the keyboard, you can enter numbers (1-9) to represent a group of spaces or the
  letter "n" to represent a line break (same as hitting the "enter" key, or entering the amount of spaces to the next line)
- Ex: `/\/7/\` would be interpreted as `/\/       /\`
- Any non-single digit number will be interpreted as two seperate numbers
- Ex: `/\/43/\` -> `/\/       /\`
- If you enter mirrors from the keyboard, the program will automatically repeat the sequence that you entered until the
    entire board is filled up, as long as "repeat" is true. You can use this to make some really interesting patterns
    (hint: try entering a sequence that is exactly 13 characters long)

*From a file:*
- You can create your own mirror fields by using notepad or any other simple text editor - get creative!
- If you make your own, make sure that the field is exactly 13 x 13 characters (you can check by pressing "ctrl + a";
  it should highlight a blue rectangle. If there are any areas that aren't highlighted, fill them in with spaces)
- Save it as a ".txt" file in the program's "assets" folder for easy access
- When running the program, you can load saved mirror fields by entering "../assets/fileName.txt" when prompted
- When exporting a set of mirrors using the "Export mirror field" option, save it as "../assets/fileName.txt" when asked

*From random generation:*
- When generating a random mirror field by entering a % value, any number between 0 and 100 will work, so experiment with
  different values! Higher mirror density usually results in more secure encryption, but more mirrors is not always
  better. Try to find the perfect amount of mirrors to create the most unpredictable encryption!

**Main menu**
- Although there are seperate options for encrypting and decrypting, the process is acutally the same for both. This means
  that you can easily trace a path through the mirrors by yourself to create an encrypted message, then check the result
  using the program. Try creating some messages on your own!
- Sometimes, the animated window will be created in the background whenever you start to encrypt/decrypt a message. If you
  don't see the window show up, try looking at the taskbar (bottom of the screen) for a picture of a red laser bouncing off
  a mirror. This is the window; simply click it to bring it to the front

- If you really like the mirrors that you have set up, you can save them to a .txt file by choosing option 4: "Export mirror
  field"
    - Enter a file path (Ex: "../assets/mirrors-1.txt") when prompted to save the mirrors
    - It is recommended to save all of your mirror field files to the "assets" folder, so that they are all in the same place
    - Don't worry about creating the file first; the file will be created for you if it doesn't already exist
    - Also, you can enter the name of a file that already exists to replace it

---

## How to install Java

In order to run a Java program, you must first install Java on your computer. If Java is already installed on this device, 
then you should be ready to go. Otherwise, follow the steps in section 2: "Installing Java". If you're not sure whether you 
have Java installed or not, follow the brief steps in section 1: "Checking if Java is already installed"

- Checking if Java is already installed:
    1. Open Run by pressing "Windows key + r" or by searching for "run" in the Windows search bar
    2. In Run, type "cmd" and press "enter" or click "ok" to open the Windows command line (cmd.exe)
    3. In the command line, type "javac -version"
    4. If Java is installed, you should get a result similar to this:

        > javac 13

       If not, you will need to install Java before you can run any Java programs


- Installing Java:
    1. Go to the Oracle website at "[MISSING]"
    2. Click "[MISSING]" to download the current version of the JDK (Java Development Kit, it's Java + extra Java stuff)
    3. Go to your "downloads" folder, right click on the JDK folder, and click "extract all"
    4. Double-click on the extracted folder
