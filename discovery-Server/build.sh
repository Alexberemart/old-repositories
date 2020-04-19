set -e
mvn clean
mvn package
mvn dockerfile:build
docker run --link mysql -e "SPRING_PROFILES_ACTIVE=prod" --name discovery-server -p 8761:8761 -t com.grupoasv/discovery-server:latest