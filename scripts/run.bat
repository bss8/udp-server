rem Copyright (c) 2020. Borislav S. Sabotinov
rem https://github.com/bss8
rem This program is free software: you can redistribute it and/or modify
rem it under the terms of the GNU Affero General Public License as published by
rem the Free Software Foundation, either version 3 of the License, or
rem (at your option) any later version.
rem
rem This program is distributed in the hope that it will be useful,
rem but WITHOUT ANY WARRANTY; without even the implied warranty of
rem MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
rem GNU Affero General Public License for more details.
rem
rem You should have received a copy of the GNU Affero General Public License
rem along with this program.  If not, see <http://www.gnu.org/licenses/>.

rem this script assumes that the server JAR exists under the dist directory created at the root level
rem by the build script.
rem If you use IntelliJ, this will be the default auto-generated directory structure
rem Otherwise, you may need to modify the paths in this file for your setup.


start /min java -Xms1024m -Xmx2048m -jar ..\dist\server.jar

rem comment out below line if you auto-build an artifact using IntelliJ
rem start /min java -Xms1024m -Xmx2048m -jar ..\out\artifacts\01a_server_jar\01a_server.jar
