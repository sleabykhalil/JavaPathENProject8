# FROM openjdk:8-jdk-alpine
# # MAINTAINER baeldung.com
# COPY build/libs/gps-api-1.0-SNAPSHOT.jar gps-api-1.0-SNAPSHOT.jar
# ENTRYPOINT ["java","-jar","/gps-api-1.0-SNAPSHOT.jar"]

# For Java 8, try this# FROM openjdk:8-jdk-alpine# For Java 11, try this
# FROM adoptopenjdk/openjdk11:alpine-jre
FROM gradle:7-jdk11-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle test --no-daemon
