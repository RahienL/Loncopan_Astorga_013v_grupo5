FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./
COPY src ./src

RUN chmod +x mvnw && ./mvnw -q -DskipTests package
RUN set -eux; \
    JAR_FILE="$(find target -maxdepth 1 -name '*.jar' ! -name '*.original' -print -quit)"; \
    cp "$JAR_FILE" /workspace/app.jar

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/app.jar ./app.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "/app/app.jar"]