FROM openjdk:18-jdk-slim
WORKDIR /app
COPY . .
RUN chmod +x gradlew
CMD sh gradlew run