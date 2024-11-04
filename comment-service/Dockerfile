FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/comment-service-1.0.jar CommentService.jar
ENTRYPOINT ["java","-jar", "CommentService.jar"]