#!/bin/bash
# Copyright (c) 2020. Borislav S. Sabotinov
# https://github.com/bss8
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

# Server application launch script for Linux.

java -Xms1024m -Xmx2048m -jar ../dist/server.jar

# comment out below line if you auto-build an artifact using IntelliJ
# start /min java -Xms1024m -Xmx2048m -jar ../out/artifacts/01a_server_jar/01a_server.jar