rem Author: Borislav S. Sabotinov
rem Windows build script from source code

set PKG_PATH=edu\txstate\bss64

dir /s /B ..\*.java > sources_win.txt

javac @sources_win.txt
mkdir ..\dist\%PKG_PATH%
move ..\src\%PKG_PATH%\*.class ..\dist\%PKG_PATH%
copy ..\src\META-INF ..\dist

cd ..\dist
jar cmvf MANIFEST.MF server.jar edu\*
cd ..\scripts
