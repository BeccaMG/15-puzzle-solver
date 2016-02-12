#!/bin/bash
JUNITPATH=jars/*
javac -cp $JUNITPATH:$PWD src/*.java
java -cp src/:$JUNITPATH org.junit.runner.JUnitCore PuzzleTest

rm src/*.class
