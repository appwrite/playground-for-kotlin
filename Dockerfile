FROM openjdk:11-jdk-slim
WORKDIR /app
COPY . .
RUN chmod +x gradlew
CMD sh gradlew run