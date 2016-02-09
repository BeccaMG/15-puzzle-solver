#!/bin/bash
if [ ! -d bin ] ; then mkdir bin/ ; fi
javac -cp "jars/*" -sourcepath src -d bin src/*.java
if [ "$1" == "-d" ] ; then
sudo nohup java -cp "jars/*:bin"  Publisher &
else
sudo java -cp "jars/*:bin"  Publisher
if [ $? -eq 0 ] ; then echo "Service running in the background" ; fi
fi
