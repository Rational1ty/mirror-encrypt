@echo off

:: ------------------------------------- COMMANDS GO BELOW ----------------------------------------

set commands="window:true"

:: --------------------------------------COMMANDS GO ABOVE ----------------------------------------

echo Launching MirrorEncrypt.java...

:: Make sure to launch from the correct location
cd %~dp0

:: Create bin
mkdir bin 2>&1 nul

:: Compile the program (lib files first, then src files)
cd lib
javac -d ../bin -cp ../bin *.java
cd ../src
javac -d ../bin -cp ../bin *.java

:: Run the program
cd ../bin
java src.MirrorEncrypt %commands%

cmd /k