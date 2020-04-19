FROM openjdk:8-jdk-alpine
EXPOSE 8761
ADD target/discovery-server.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar