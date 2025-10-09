# ---------- build stage ----------
FROM gradle:8.10.0-jdk21-alpine AS build
WORKDIR /app

COPY gradlew ./
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
RUN ./gradlew --no-daemon dependencies || true

COPY . .
RUN ./gradlew --no-daemon clean bootJar

# ---------- runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8083

ENV JAVA_OPTS="-XX:+ExitOnOutOfMemoryError -XX:MaxRAMPercentage=75"
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
