* Compile the server : 
javac -cp "jars/*" -sourcepath src -d bin src/Server/*.java

* Launch the Publisher, use sudo to allow using port 80 =)
sudo java -cp "jars/*:bin"  Server.Publisher 
