#!/bin/bash
# Author: Borislav S. Sabotinov
# Linux build script from source code

PKG_PATH=edu/txstate/bss64

find -name ../"*.java" > sources_nix.txt
javac @sources.txt

mkdir -p ../dist/${PKG_PATH}
mv ../src/${PKG_PATH}/*.class ../dist/${PKG_PATH}
copy ../src/META-INF ../dist

cd ../dist
jar cmvf MANIFEST.MF server.jar edu/*
cd ../scripts
