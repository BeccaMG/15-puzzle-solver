*Use "./runme.sh -d" to compile and run as daemon (run as root to use port 80)

** Compile and run manually:
* If bin directory is not created then:
mkdir bin/

* Compile the server : 
javac -cp "jars/*" -sourcepath src -d bin src/*.java

* Launch the Publisher, use sudo to allow using port 80 =)
sudo java -cp "jars/*:bin"  Publisher 

*Profit
