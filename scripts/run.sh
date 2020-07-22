#!/bin/bash
# Author: Borislav S. Sabotinov
# Server application launch script.

RUN_OPTION=${1}

java -Xms1024m -Xmx2048m -jar 01a_server.jar $RUN_OPTION