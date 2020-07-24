#!/bin/bash
# Author: Borislav S. Sabotinov
# Server application launch script for Linux.

RUN_OPTION=${1}

java -Xms1024m -Xmx2048m -jar ../dist/server.jar

# comment out below line if you auto-build an artifact using IntelliJ
# start /min java -Xms1024m -Xmx2048m -jar ../out/artifacts/01a_server_jar/01a_server.jar