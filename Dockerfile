# Stage 1: build the WAR
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q clean package -DskipTests

# Stage 2: run it on Tomcat 10.1
FROM tomcat:10.1-jdk17-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/sugandha_sansaar-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/sugandha_sansaar.war
EXPOSE 8080
CMD ["catalina.sh", "run"]