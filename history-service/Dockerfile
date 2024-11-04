FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/history-service-1.0.jar HistoryService.jar
ENTRYPOINT ["java","-jar","HistoryService.jar"]