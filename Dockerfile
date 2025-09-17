FROM openjdk:latest
COPY ./target/classes /app
WORKDIR /app
ENTRYPOINT ["java", "imc.com.Ixacise"]
