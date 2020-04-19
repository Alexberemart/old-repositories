set -e
mvn clean
mvn package
mvn dockerfile:build
docker run --link mysql --link discovery-server -e "SPRING_PROFILES_ACTIVE=prod" --name basket-scraping -p 8081:8081 -p 8000:8000 -v /Users/aberenguer/temp:/tmp -t com.grupoasv/basket-scraping:latest