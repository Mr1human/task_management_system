FROM openjdk:21-ea-20-slim
RUN apt-get update && apt-get install -y curl
WORKDIR /app
COPY /target/taskmanagement-0.0.1-SNAPSHOT.jar /app/taskmanagement.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","taskmanagement.jar"]