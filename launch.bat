@echo off

echo Launching MirrorEncrypt.java...

:: Make sure to launch from the correct location
cd %~dp0

:: Create bin if it doesn't exist
if not exist ./bin mkdir bin

:: Compile the program (lib files first, then src files)
cd lib && ^
javac -d ../bin -cp ../bin *.java && ^
cd ../src && ^
javac -d ../bin -cp ../bin *.java
if errorlevel 1 goto:fatal

:: Run the program
cd ../bin
java src.MirrorEncrypt
if errorlevel 1 goto:fatal

cmd /k
goto:eof

:fatal
echo A fatal error occurred while launching
pause