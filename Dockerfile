# 1 - Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Otimização: Copia apenas o pom.xml primeiro
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests

# 2 - Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Instala o curl para Healthcheck (Alpine não vem com ele por padrão)
RUN apk add --no-cache curl

# Copia o JAR gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta e define o comando de entrada
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]