FROM openjdk:8-jdk-alpine
EXPOSE 8084
ADD target/basket-api.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -agentlib:jdwp=transport=dt_socket,address=8004,server=y,suspend=n -Djava.security.egd=file:/dev/./urandom -jar /app.jar