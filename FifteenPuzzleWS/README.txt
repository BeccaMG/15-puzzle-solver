*If bin directory is not created then:
mkdir bin/

* Compile the server : 
javac -cp "jars/*" -sourcepath src -d bin src/*.java

* Launch the Publisher, use sudo to allow using port 80 =)
sudo java -cp "jars/*:bin"  Publisher 
