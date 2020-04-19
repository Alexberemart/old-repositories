mvn clean
mvn package
mvn dockerfile:build
docker run --link mysql --link basket-scraping --link basket-api -e "SPRING_PROFILES_ACTIVE=prod" --name basket-crons -p 8082:8082 -t com.grupoasv/basket-crons:latest