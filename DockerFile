FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/skiStation.jar
EXPOSE 9000
CMD ["java","-jar","/app/skiStation.jar"]