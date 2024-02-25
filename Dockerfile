FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY . /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /home/app/target/store-api-*.jar /usr/local/lib/store-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/store-api.jar"]