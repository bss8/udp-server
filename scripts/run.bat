rem Author: Borislav S. Sabotinov
rem this script assumes that the server JAR is under out\artifacts\01a_server_jar
rem If you use IntelliJ, this will be the default auto-generated directory structure
rem Otherwise, you may need to modify the paths in this file for your setup.

rem from scripts dir, go to the jar
cd ..\out\artifacts\01a_server_jar
rem execute it as a process in a minimized window
start /min java -Xms1024m -Xmx2048m -jar 01a_server.jar
rem return to root project dir
cd ..\..\..\