@echo off

echo Launching mirror-encrypt...

:: Make sure to launch from the correct location
cd %~dp0

:: Create bin if it doesn't exist
if not exist ./bin mkdir bin

:: Compile the program (lib files first, then src files)
cd lib && ^
javac -d ../bin -cp ../bin *.java && ^
cd ../src && ^
javac -d ../bin -cp ../bin *.java
if errorlevel 1 goto:fatal1

:: Run the program
cd ../bin
java src.MirrorEncrypt
if errorlevel 1 goto:fatal2

cd ..
cmd /k
goto:eof

:fatal1
echo A fatal error occurred while launching
pause
goto:eof

:fatal2
echo(
echo A fatal error occurred
pause