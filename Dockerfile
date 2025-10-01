FROM openjdk:latest
COPY ./target/DevOpsLabI-0.1.0.5-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "DevOpsLabI-0.1.0.5-jar-with-dependencies.jar"]


