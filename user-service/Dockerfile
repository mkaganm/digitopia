# ---------- build stage ----------
FROM gradle:8.10.0-jdk21-alpine AS build
WORKDIR /app
# cache için önce wrapper ve gradle dosyaları
COPY gradlew ./
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
RUN ./gradlew --no-daemon dependencies || true
# sonra kaynak kod
COPY . .
RUN ./gradlew --no-daemon clean bootJar

# ---------- runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
# Spring Boot 3 jar adı genelde *SNAPSHOT*.jar olur
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8081
# zarif shutdown & düşük bellek footprint
ENV JAVA_OPTS="-XX:+ExitOnOutOfMemoryError -XX:MaxRAMPercentage=75"
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
