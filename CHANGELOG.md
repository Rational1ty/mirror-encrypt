# CHANGELOG // Mirror encrypt

### Current version: v2.5.0

---

## v2.5.0 | 01-14-2021
***The Enduring***
- Added `mirror.properties` file to keep track of preferences
- Overhauled the command system to work better for both developer and user
- Launcher improvements
- Updated README
- Added MIT license
- Fixed several issues
- Finishing touches

---

## v2.2.0 | 07-03-2020
***The Robust***
- Fixed several bugs with *option 3: create mirror field from keyboard*
    - Added a check for empty input
    - Removed the ability to enter `n` or `N` as a valid character
    - Two-digit numbers will now produce the correct amount of spaces. For example:
      `13 => 4 spaces` (old) vs `13 => 13 spaces` (new)
- Changed the behavior of *option 1: encrypt message*
    - If no message is entered, the entire alphabet will be passed through the field
    - This is equivalent to entering `abcdefghijklmnoparstuvwxyzABCDEFGHIJKLMNOPARSTUVWXYZ`
    - This is a quick and easy way to show the all of the different paths that the beam can take through the field
- Fixed an issue which could cause the application to crash when entering a relative file path
- Other bug fixes
- Internal improvements to help increase performance and development speed

---

## v2.0.0 | 07-01-2020
***The Colorful***
- Added start-up commands option
- Added a new menu option (*6: Enter commands*) to edit commands while running
- The app can now be launched with `launcher.bat` instead of launching manually

---

## v1.0.0 | 04-20-2020
***The Beginning***
- All 3 input methods are now up and running. You can:
    - Create a mirror field by typing in characters,
    - Load a text file which contains a mirror field pre-made, or
    - Generate a random mirror field with a certain amount of mirrors
- Main menu now has support for basic operations, including the following:
    - Encrypt message
    - Decode message
    - Display mirror field
    - Import mirror field
    - Export mirror field
