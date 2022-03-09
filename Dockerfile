FROM openjdk:11-jdk
WORKDIR /app
COPY . .
RUN chmod +x gradlew
CMD sh gradlew run