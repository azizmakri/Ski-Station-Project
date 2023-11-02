FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/skiStation.jar
EXPOSE 5052
CMD ["java","-jar","/app/skiStation.jar"]
