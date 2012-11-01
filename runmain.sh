export MAVEN_OPTS="-javaagent:/Users/ae589/dev/tools/jrebel/jrebel.jar"
mvn -P \!scalac-dev package exec:java -Dexec.mainClass="Main"
