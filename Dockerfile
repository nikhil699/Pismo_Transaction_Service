# ---------- Build stage (has Maven) ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# 1) Cache deps
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests dependency:go-offline

# 2) Build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests clean package

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Non-root
RUN useradd -ms /bin/bash spring
USER spring

# Copy fat jar
COPY --from=build /app/target/*.jar /app/app.jar

ENV SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=docker

EXPOSE 8080
ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} -jar /app/app.jar"]
