FROM openjdk:8-jdk-slim AS builder
WORKDIR /app
COPY EchoServer.java ./
RUN javac *.java


FROM openjdk:8-jre-slim
WORKDIR /app
COPY --from=builder /app/EchoServer.class .
RUN ls -al
CMD ["java", "EchoServer"]
