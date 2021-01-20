FROM openjdk:13
VOLUME /tmp
COPY target/*.jar blablatest-server.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /blablatest-server.jar
