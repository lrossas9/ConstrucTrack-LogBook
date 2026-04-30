# Etapa 1: Compilación (Build)
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar archivos de build
COPY pom.xml .
COPY src ./src

# Compilar
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Runtime)
FROM eclipse-temurin:17-jre

WORKDIR /app

# Instalar MySQL client (opcional pero útil para debugging)
RUN apt-get update && apt-get install -y default-mysql-client && rm -rf /var/lib/apt/lists/*

# Copiar JAR compilado desde etapa anterior
COPY --from=builder /app/target/constructrack-bitacora-1.0.0.jar ./constructrack-bitacora-1.0.0.jar

# Crear directorio de logs
RUN mkdir -p /app/logs

# Exponer puerto
EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:8080/ || exit 1

# Variables de entorno para BD
ENV DB_HOST=mysql \
    DB_PORT=3306 \
    DB_NAME=constructrack_logbook \
    DB_USER=constructrack \
    DB_PASSWORD=constructrack123 \
    JAVA_OPTS="-Xmx512m -Xms256m"

# Comando de ejecución
CMD ["sh", "-c", "exec java $JAVA_OPTS -jar constructrack-bitacora-1.0.0.jar"]
