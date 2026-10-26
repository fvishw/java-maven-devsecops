FROM eclipse-temurin:21-jre-alpine

USER root

RUN apk update && \
    apk upgrade --no-cache brotli-libs

WORKDIR /app

COPY --chown=10001:10001 target/com.java-maven-devsecops-1.0.0-SNAPSHOT.jar app.jar

USER 10001

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]