# FROM openjdk:8-jdk-alpine
# # MAINTAINER baeldung.com
# COPY build/libs/gps-api-1.0-SNAPSHOT.jar gps-api-1.0-SNAPSHOT.jar
# ENTRYPOINT ["java","-jar","/gps-api-1.0-SNAPSHOT.jar"]

# For Java 8, try this# FROM openjdk:8-jdk-alpine# For Java 11, try this
# FROM adoptopenjdk/openjdk11:alpine-jre
FROM openjdk:8-jdk-alpine

# Refer to Maven build -> finalName
ARG JAR_FILE=build/libs/user-api-0.0.1-SNAPSHOT.jar

# cd /opt/app
WORKDIR /opt/app

# cp target/spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} app.jar

# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]