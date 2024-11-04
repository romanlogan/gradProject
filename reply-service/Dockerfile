FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/reply-service-1.0.jar ReplyService.jar
ENTRYPOINT ["java","-jar", "ReplyService.jar"]