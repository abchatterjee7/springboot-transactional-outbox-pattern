# Multi-stage build for Spring Boot Transactional Outbox Pattern
# Optimized for layer caching and minimal image size

FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy pom files first for better layer caching
COPY order-service/pom.xml ./order-service/
COPY order-poller/pom.xml ./order-poller/
COPY inventory-service/pom.xml ./inventory-service/

# Download dependencies (cached layer)
RUN cd order-service && mvn dependency:go-offline -B
RUN cd order-poller && mvn dependency:go-offline -B
RUN cd inventory-service && mvn dependency:go-offline -B

# Copy source code
COPY order-service/src ./order-service/src
COPY order-poller/src ./order-poller/src
COPY inventory-service/src ./inventory-service/src

# Build all services
RUN cd order-service && mvn clean package -DskipTests -B
RUN cd order-poller && mvn clean package -DskipTests -B
RUN cd inventory-service && mvn clean package -DskipTests -B

# Runtime base image
FROM eclipse-temurin:21-jre-alpine AS base
RUN apk add --no-cache tzdata
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+UseContainerSupport"

# Order Service
FROM base AS order-service
WORKDIR /app
COPY --from=builder /app/order-service/target/*.jar app.jar
EXPOSE 9191
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Order Poller Service  
FROM base AS order-poller
WORKDIR /app
COPY --from=builder /app/order-poller/target/*.jar app.jar
EXPOSE 9292
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Inventory Service
FROM base AS inventory-service
WORKDIR /app
COPY --from=builder /app/inventory-service/target/*.jar app.jar
EXPOSE 9192
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
