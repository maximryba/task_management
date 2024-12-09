FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/task-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

EXPOSE 8081
