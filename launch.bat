@echo off

echo Launching mirror-encrypt...

:: Make sure to launch from the correct location
cd %~dp0

:: Create /bin if it doesn't exist
if not exist ./bin mkdir bin

:: Compile the program (lib files first, then src files)
javac -d bin lib/*.java src/*.java
if errorlevel 1 goto:fatal1

:: Run the program
java -cp bin src.MirrorEncrypt
if errorlevel 1 goto:fatal2

goto:eof

:fatal1
echo A fatal error occurred while launching
pause
goto:eof

:fatal2
echo(
echo A fatal error occurred
pause