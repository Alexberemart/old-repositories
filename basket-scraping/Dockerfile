FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8081
ADD target/basket-scraping.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n -Djava.security.egd=file:/dev/./urandom -jar /app.jar