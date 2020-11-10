run: compile test
	java NetworkFrontEnd_1

compile:
	javac NetworkBackend.java
	javac NetworkFrontEnd_1.java
	javac CS400Graph.java
	javac NetworkDataWrangler.java
	javac GraphADT.java

test:
	javac -cp .:junit5.jar TestEngineer1_suite.java
	java -jar junit5.jar --class-path . --scan-classpath --details tree

clean:
	$(RM) *.class
