FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace
COPY pom.xml ./
COPY src ./src

RUN mvn -B -DskipTests package -f pom.xml
RUN set -eux; \
    JAR_FILE="$(find target -maxdepth 1 -name '*.jar' ! -name '*.original' -print -quit)"; \
    cp "$JAR_FILE" /workspace/app.jar

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/app.jar ./app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]