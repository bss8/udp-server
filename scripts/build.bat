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
