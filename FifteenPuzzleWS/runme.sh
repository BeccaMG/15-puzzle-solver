#!/bin/bash
echo $1
if [ ! -d bin ] ; then
mkdir bin/
fi
javac -cp "jars/*" -sourcepath src -d bin src/*.java
if [ "$1" == "-d" ] ; then
echo $1
sudo nohup java -cp "jars/*:bin"  Publisher &
else
sudo java -cp "jars/*:bin"  Publisher
fi
