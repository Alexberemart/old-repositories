set -e
mvn clean
mvn package
mvn dockerfile:build
docker run --link mysql --link discovery-server -e "SPRING_PROFILES_ACTIVE=prod" --name basket-api -p 8084:8084 -p 8004:8004 -t com.alexberemart/basket-api:latest