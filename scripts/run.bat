rem Author: Borislav S. Sabotinov
rem this script assumes that the server JAR exists under the dist directory created at the root level
rem by the build script.
rem If you use IntelliJ, this will be the default auto-generated directory structure
rem Otherwise, you may need to modify the paths in this file for your setup.


start /min java -Xms1024m -Xmx2048m -jar ..\dist\server.jar

rem comment out below line if you auto-build an artifact using IntelliJ
rem start /min java -Xms1024m -Xmx2048m -jar ..\out\artifacts\01a_server_jar\01a_server.jar
