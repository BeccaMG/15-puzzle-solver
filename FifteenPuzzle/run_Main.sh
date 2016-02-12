#!/bin/bash
JUNITPATH=$PWD/jars/*
javac -cp $JUNITPATH:$PWD src/*.java
java -cp src/ Main

rm src/*.class
