FROM openjdk:latest
COPY ./target/DevOpsLabI-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "DevOpsLabI-1.0-SNAPSHOT-jar-with-dependencies.jar"]
