FROM gradle:7.6.0-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:18-jdk-slim

EXPOSE 80

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/surviving-0.6.war /app/spring-boot-application.war

ENTRYPOINT ["java", "-jar","/app/spring-boot-application.war"]