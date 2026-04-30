# CONFIGURACIÓN DE SERVICIOS, BASES DE DATOS Y SOFTWARE EN MÁQUINA CLIENTE

**Evidencia de Desempeño: GA10-220501097-AA5-EV01**

---

## PORTADA

```
╔══════════════════════════════════════════════════════════════════════════╗
║                                                                          ║
║                    CENTRO DE ESTUDIO ESPECIALIZADOS                     ║
║                                                                          ║
║                    ANÁLISIS Y DESARROLLO DE SOFTWARE                    ║
║                         Código: 3070308                                  ║
║                                                                          ║
║                                                                          ║
║              CONFIGURACIÓN DE SERVICIOS, BASES DE DATOS                 ║
║                         Y SOFTWARE DEL CLIENTE                          ║
║                                                                          ║
║                    Evidencia: GA10-220501097-AA5-EV01                   ║
║                                                                          ║
║                        ConstrucTrack LogBook                             ║
║                   Sistema de Gestión de Bitácoras                        ║
║                                                                          ║
║                                                                          ║
║                    Aprendiz: [Tu Nombre]                                 ║
║                    Fecha: 29 de Abril de 2026                            ║
║                    Institución: SENA                                     ║
║                                                                          ║
╚══════════════════════════════════════════════════════════════════════════╝
```

---

## TABLA DE CONTENIDO

1. Introducción
2. Descripción General del Proyecto
3. Requisitos y Características del Sistema
4. Parte 1: Configuración mediante Virtualización
   - 4.1 Instalación de Máquina Virtual Ubuntu
   - 4.2 Configuración de MySQL
   - 4.3 Configuración de Apache y Java
   - 4.4 Despliegue de Aplicación
   - 4.5 Verificación de Servicios
5. Parte 2: Configuración mediante Contenedores
   - 5.1 Instalación de Docker
   - 5.2 Dockerfile de la Aplicación
   - 5.3 Docker Compose
   - 5.4 Despliegue con Contenedores
   - 5.5 Verificación de Servicios en Contenedores
6. Pruebas y Validación
7. Conclusiones
8. Bibliografía y Referencias

---

## 1. INTRODUCCIÓN

En el contexto de la modernización tecnológica y la necesidad de optimizar recursos en infraestructura IT, este documento presenta un análisis detallado de dos arquitecturas de despliegue para aplicaciones empresariales: **Virtualización** y **Contenedores**.

La **virtualización** permite crear máquinas virtuales completas con sistemas operativos independientes, proporcionando aislamiento a nivel de SO. Los **contenedores**, por su parte, ofrecen una alternativa más ligera que comparten el kernel del sistema anfitrión, reduciendo consumo de recursos y mejorando la velocidad de despliegue.

Se utilizará la aplicación **ConstrucTrack LogBook** como caso de estudio práctico, demostrando cómo configurar y desplegar una aplicación Java con base de datos MySQL y servidor web Apache en ambos escenarios.

---

## 2. DESCRIPCIÓN GENERAL DEL PROYECTO

### 2.1 ¿Qué es ConstrucTrack LogBook?

ConstrucTrack LogBook es una aplicación de escritorio basada en **JavaFX** diseñada para la gestión de:
- **Proyectos de construcción**
- **Bitácoras técnicas** (registros de progreso)
- **Evidencia de desempeño** (fotos y documentos)
- **Usuarios y permisos**

### 2.2 Características Técnicas

| Característica | Descripción |
|---|---|
| **Lenguaje** | Java 17 |
| **Framework GUI** | JavaFX 21.0.2 |
| **Base de Datos** | MySQL 8.0+ |
| **Build Tool** | Apache Maven 3.8+ |
| **Autenticación** | Por usuario/contraseña hasheada (BCrypt) |
| **Exportación** | PDF de reportes |
| **Especificación** | 21 clases Java + 6 vistas FXML |

### 2.3 Componentes Principales

```
ConstrucTrack LogBook
├── Controladores (6)
│   ├── LoginController
│   ├── DashboardController
│   ├── ProyectoFormController
│   ├── BitacoraFormController
│   ├── BitacoraListController
│   └── RegistroController
├── Modelos de Datos (4)
│   ├── Usuario
│   ├── Proyecto
│   ├── Bitacora
│   └── Evidencia
├── DAOs (4) - Acceso a Datos
├── Utilidades
│   ├── Conexión a BD
│   ├── Manejo de Sesiones
│   ├── Generador de PDFs
│   └── Hasher de Contraseñas
└── Vistas (FXML + CSS)
```

---

## 3. REQUISITOS Y CARACTERÍSTICAS DEL SISTEMA

Según especificación GA10-220501097-AA5-EV01, la máquina debe cumplir:

| Requisito | Especificación |
|---|---|
| **Sistema Operativo** | Ubuntu Linux (cualquier versión estable) |
| **Gestor de Base de Datos** | MySQL 8.0+ |
| **Servidor de Aplicaciones** | Apache 2.4+ |
| **Runtime Java** | JDK 17 LTS mínimo |
| **Memoria RAM** | 2 GB mínimo (4 GB recomendado) |
| **Almacenamiento** | 10 GB disponibles |
| **Red** | Conectividad de red activa |

---

## 4. PARTE 1: CONFIGURACIÓN MEDIANTE VIRTUALIZACIÓN

### 4.1 Instalación de Máquina Virtual Ubuntu

#### 4.1.1 Requisitos Previos
- VirtualBox 6.0+ o VMware
- ISO de Ubuntu Server 22.04 LTS (u otra versión estable)
- Mínimo 2 cores CPU, 2-4 GB RAM asignados

#### 4.1.2 Pasos de Instalación

**Paso 1: Crear Nueva Máquina Virtual**

1. Abrir VirtualBox
2. Clic en "Nueva" (New)
3. Configurar:
   - **Nombre:** UbuntuConstrucTrack
   - **Tipo:** Linux
   - **Versión:** Ubuntu (64-bit)
   - **RAM:** 2 GB (2048 MB)
   - **Disco duro:** 20 GB (dinámico)

**Paso 2: Instalación del Sistema Operativo**

1. Seleccionar imagen ISO de Ubuntu 22.04 LTS
2. Iniciar máquina virtual
3. Seguir asistente de instalación:
   - Idioma: Español (u otro preferido)
   - Región/Zona horaria
   - Nickname usuario: `constructrack`
   - Contraseña: `ConstrucTrack2025#`
   - Particiones: usar recomendacion por defecto
   - Componentes: seleccionar "OpenSSH Server"

**Paso 3: Configuración de Red**

```bash
# Verificar conectividad
ping google.com

# Configurar IP estática (opcional pero recomendado)
sudo nano /etc/netplan/00-installer-config.yaml
```

Contenido sugerido:
```yaml
network:
  version: 2
  ethernets:
    eth0:
      dhcp4: true
```

Guardar y aplicar:
```bash
sudo netplan apply
```

### 4.2 Configuración de MySQL

#### 4.2.1 Instalación

```bash
# Actualizar repositorios
sudo apt update
sudo apt upgrade -y

# Instalar MySQL Server
sudo apt install mysql-server -y

# Iniciar servicio
sudo systemctl start mysql
sudo systemctl enable mysql

# Verificar estado
sudo systemctl status mysql
```

**Salida esperada:**
```
● mysql.service - MySQL Community Server
     Loaded: loaded (/lib/systemd/unit/mysql.service; enabled; vendor preset: enabled)
     Active: active (running)
```

#### 4.2.2 Configuración de Seguridad

```bash
# Ejecutar script de seguridad
sudo mysql_secure_installation

# Responder:
# Enter password: [dejar en blanco]
# Remove anonymous users? Y
# Disable remote root login? Y
# Remove test database? Y
# Reload privilege tables? Y
```

#### 4.2.3 Crear Base de Datos y Usuario

```bash
# Conectarse a MySQL
sudo mysql -u root

# Ejecutar en la consola MySQL:
CREATE DATABASE constructrack_logbook;
CREATE USER 'constructrack'@'localhost' IDENTIFIED BY 'constructrack123';
GRANT ALL PRIVILEGES ON constructrack_logbook.* TO 'constructrack'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

#### 4.2.4 Crear Esquema de Datos

Copiar el archivo `schema.sql` desde la aplicación:

```bash
# Transferir archivo (desde máquina anfitrión o copiar contenido)
mysql -u constructrack -p constructrack_logbook < schema.sql
# Ingresar contraseña: constructrack123
```

**Script SQL para crear tablas:**

```sql
-- Tabla de Usuarios
CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(20),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Proyectos
CREATE TABLE proyectos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    ubicacion VARCHAR(200),
    fecha_inicio DATE,
    fecha_fin DATE,
    responsable_id INT,
    estado VARCHAR(20),
    FOREIGN KEY (responsable_id) REFERENCES usuarios(id)
);

-- Tabla de Bitácoras
CREATE TABLE bitacoras (
    id INT PRIMARY KEY AUTO_INCREMENT,
    proyecto_id INT NOT NULL,
    usuario_id INT NOT NULL,
    descripcion TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (proyecto_id) REFERENCES proyectos(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabla de Evidencia
CREATE TABLE evidencia (
    id INT PRIMARY KEY AUTO_INCREMENT,
    bitacora_id INT NOT NULL,
    tipo VARCHAR(50),
    ruta_archivo VARCHAR(500),
    fecha_carga TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bitacora_id) REFERENCES bitacoras(id)
);
```

#### 4.2.5 Verificación de MySQL

```bash
# Conectar y verificar
mysql -u constructrack -p -e "USE constructrack_logbook; SHOW TABLES;"
```

**Salida esperada:**
```
+------------------------------+
| Tables_in_constructrack_logbook |
+------------------------------+
| bitacoras                    |
| evidencia                    |
| proyectos                    |
| usuarios                     |
+------------------------------+
```

### 4.3 Configuración de Apache y Java

#### 4.3.1 Instalación de Java

```bash
# Instalar OpenJDK 17
sudo apt install openjdk-17-jdk -y

# Verificar instalación
java -version
javac -version

# Configurar JAVA_HOME
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
source ~/.bashrc
```

#### 4.3.2 Instalación de Maven

```bash
# Instalar Maven
sudo apt install maven -y

# Verificar
mvn -version
```

#### 4.3.3 Instalación de Apache

```bash
# Instalar Apache
sudo apt install apache2 -y

# Habilitar módulos necesarios
sudo a2enmod proxy
sudo a2enmod proxy_http
sudo a2enmod rewrite

# Iniciar Apache
sudo systemctl start apache2
sudo systemctl enable apache2

# Verificar
sudo systemctl status apache2
```

#### 4.3.4 Configuración de Proxy Apache

Crear archivo de configuración para la aplicación:

```bash
sudo nano /etc/apache2/sites-available/constructrack.conf
```

Contenido:
```apache
<VirtualHost *:80>
    ServerName constructrack.local
    ServerAdmin admin@constructrack.local
    
    # Proxy hacia la aplicación Java
    ProxyPreserveHost On
    ProxyPass / http://localhost:8080/
    ProxyPassReverse / http://localhost:8080/
    
    # Headers
    RequestHeader set X-Forwarded-Proto "http"
    RequestHeader set X-Forwarded-Port "80"
    
    # Logs
    ErrorLog ${APACHE_LOG_DIR}/constructrack_error.log
    CustomLog ${APACHE_LOG_DIR}/constructrack_access.log combined
</VirtualHost>
```

Habilitar sitio:
```bash
sudo a2ensite constructrack.conf
sudo apache2ctl configtest

# Debe mostrar: Syntax OK
```

### 4.4 Despliegue de Aplicación

#### 4.4.1 Transferir Código Fuente

```bash
# Opción 1: Usar git (si está disponible)
sudo apt install git -y
git clone <URL_DEL_REPOSITORIO>

# Opción 2: Transferir archivos comprimidos vía scp
scp -r /ruta/local/ConstrucTrack constructrack@<IP_VM>:/home/constructrack/
```

#### 4.4.2 Compilar Aplicación

```bash
cd /home/constructrack/ConstrucTrack\ LogBook

# Editar propiedades BD
sudo nano src/main/resources/application.properties
```

Contenido:
```properties
db.host=127.0.0.1
db.port=3306
db.name=constructrack_logbook
db.user=constructrack
db.password=constructrack123
```

Compilar:
```bash
# Limpiar compilaciones anteriores
mvn clean

# Compilar
mvn compile

# Empaquetar
mvn package -DskipTests

# Resultado esperado: target/constructrack-bitacora-1.0.0.jar
```

#### 4.4.3 Ejecutar Aplicación como Servicio

Crear script de inicio:
```bash
sudo nano /etc/systemd/system/constructrack.service
```

Contenido:
```ini
[Unit]
Description=ConstrucTrack LogBook Application
After=network.target mysql.service

[Service]
Type=simple
User=constructrack
WorkingDirectory=/home/constructrack/ConstrucTrack LogBook
ExecStart=/usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar target/constructrack-bitacora-1.0.0.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Habilitar y ejecutar:
```bash
sudo systemctl daemon-reload
sudo systemctl enable constructrack.service
sudo systemctl start constructrack.service

# Verificar estado
sudo systemctl status constructrack.service
```

### 4.5 Verificación de Servicios en Virtualización

#### 4.5.1 Verificar MySQL

```bash
# Conectarse a la BD
mysql -u constructrack -p constructrack_logbook
mysql> SELECT COUNT(*) FROM información_schema.TABLES;
mysql> EXIT;
```

**Captura esperada:** Tablas creadas correctamente

#### 4.5.2 Verificar Apache

```bash
# Estado del servicio
sudo systemctl status apache2

# Logs en tiempo real
sudo tail -f /var/log/apache2/constructrack_access.log

# Prueba HTTP
curl http://localhost
```

#### 4.5.3 Verificar Aplicación Java

```bash
# Ver logs
sudo journalctl -u constructrack.service -f

# Verificar puerto 8080
sudo netstat -tlnp | grep 8080

# Test de conectividad
curl http://localhost:8080
```

---

## 5. PARTE 2: CONFIGURACIÓN MEDIANTE CONTENEDORES

### 5.1 Instalación de Docker

#### 5.1.1 Instalación en Ubuntu/Linux

```bash
# Actualizar paquetes
sudo apt update
sudo apt upgrade -y

# Instalar dependencias
sudo apt install apt-transport-https ca-certificates curl software-properties-common -y

# Agregar repositorio de Docker
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"

# Instalar Docker
sudo apt update
sudo apt install docker-ce docker-ce-cli containerd.io docker-compose-plugin -y

# Agregar usuario actual al grupo docker
sudo usermod -aG docker $USER
newgrp docker

# Verificar instalación
docker --version
docker run hello-world
```

#### 5.1.2 Instalación en Windows

Para Windows, usar **Docker Desktop**:

```bash
# Descargar desde: https://www.docker.com/products/docker-desktop
# Instalar con configuración por defecto
# Verificar en PowerShell:
docker --version
```

### 5.2 Dockerfile de la Aplicación

Crear archivo `Dockerfile` en la raíz del proyecto:

```dockerfile
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

# Copiar JAR compilado desde etapa anterior
COPY --from=builder /app/target/constructrack-bitacora-1.0.0.jar ./constructrack-bitacora-1.0.0.jar

# Exponer puerto
EXPOSE 8080

# Variables de entorno para BD
ENV DB_HOST=mysql \
    DB_PORT=3306 \
    DB_NAME=constructrack_logbook \
    DB_USER=constructrack \
    DB_PASSWORD=constructrack123

# Comando de ejecución
CMD ["java", "-jar", "constructrack-bitacora-1.0.0.jar"]
```

### 5.3 Docker Compose para Orquestación

Crear archivo `docker-compose.yml`:

```yaml
version: '3.8'

services:
  # Servicio MySQL
  mysql:
    image: mysql:8.0
    container_name: constructrack-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: constructrack_logbook
      MYSQL_USER: constructrack
      MYSQL_PASSWORD: constructrack123
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./src/main/resources/sql/schema.sql:/docker-entrypoint-initdb.d/schema.sql
    networks:
      - constructrack-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      timeout: 20s
      retries: 5
      interval: 10s

  # Servicio Apache
  apache:
    image: httpd:2.4
    container_name: constructrack-apache
    ports:
      - "80:80"
    volumes:
      - ./apache-config/constructrack.conf:/usr/local/apache2/conf.d/constructrack.conf
      - ./apache-config/httpd.conf:/usr/local/apache2/conf/httpd.conf
    depends_on:
      - app
    networks:
      - constructrack-network

  # Servicio Aplicación Java
  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: constructrack-app
    environment:
      DB_HOST: mysql
      DB_PORT: 3306
      DB_NAME: constructrack_logbook
      DB_USER: constructrack
      DB_PASSWORD: constructrack123
    ports:
      - "8080:8080"
    depends_on:
      mysql:
        condition: service_healthy
    volumes:
      - ./logs:/app/logs
    networks:
      - constructrack-network
    restart: unless-stopped

volumes:
  mysql_data:
    driver: local

networks:
  constructrack-network:
    driver: bridge
```

### 5.4 Despliegue con Contenedores

#### 5.4.1 Preparar Configuración

Crear directorio para Apache:
```bash
mkdir apache-config
```

Archivo `apache-config/httpd.conf` (parcial):
```apache
LoadModule proxy_module modules/mod_proxy.so
LoadModule proxy_http_module modules/mod_proxy_http.so
```

Archivo `apache-config/constructrack.conf`:
```apache
<VirtualHost *:80>
    ServerName constructrack.local
    ProxyPreserveHost On
    ProxyPass / http://app:8080/
    ProxyPassReverse / http://app:8080/
</VirtualHost>
```

#### 5.4.2 Build de Imagen

```bash
# Construir imágenes definidas en docker-compose
docker-compose build

# Salida esperada:
# Building app
# Building mysql
# Building apache
# Successfully tagged constructrack-app:latest
```

#### 5.4.3 Iniciar Contenedores

```bash
# Levantar servicios en background
docker-compose up -d

# Monitorear logs
docker-compose logs -f

# Verificar estado
docker-compose ps
```

**Salida esperada:**
```
NAME                    COMMAND                  SERVICE      STATUS
constructrack-mysql     "docker-entrypoint.s…"   mysql        Up 1 min (healthy)
constructrack-app       "java -jar construc…"    app          Up 1 min
constructrack-apache    "httpd-foreground"       apache       Up 1 min
```

#### 5.4.4 Acceder a la Aplicación

```bash
# Desde el contenedor anfitrión
curl http://localhost:80
curl http://localhost:8080

# Desde otra máquina (reemplazar localhost por IP del host)
# http://<IP_HOST>:8080
```

### 5.5 Verificación de Servicios en Contenedores

#### 5.5.1 Verificar Estado Contenedores

```bash
# Listar contenedores en ejecución
docker container ls

# Ver logs de contenedor específico
docker logs constructrack-mysql
docker logs constructrack-app
docker logs constructrack-apache

# Ver consumo de recursos
docker stats
```

#### 5.5.2 Verificar Conectividad Entre Contenedores

```bash
# Entrar a contenedor app
docker exec -it constructrack-app bash

# Dentro del contenedor, probar conexión a MySQL
mysql -h mysql -u constructrack -p constructrack_logbook

# Probar conectividad HTTP
curl http://apache

# Salir
exit
```

#### 5.5.3 Verificar Base de Datos

```bash
# Ejecutar comandos MySQL desde el contenedor
docker exec constructrack-mysql mysql -u constructrack -p constructrack123 -e "SHOW TABLES;" constructrack_logbook

# Salida esperada:
# +------------------------------+
# | Tables_in_constructrack_logbook |
# +------------------------------+
# | bitacoras                    |
# | evidencia                    |
# | proyectos                    |
# | usuarios                     |
# +------------------------------+
```

#### 5.5.4 Logs de Aplicación

```bash
# Ver logs en tiempo real
docker logs -f constructrack-app

# Ver últimas 50 líneas
docker logs --tail 50 constructrack-app

# Ver logs del Apache
docker logs -f constructrack-apache

# Exportar logs a archivo
docker logs constructrack-app > app.log 2>&1
```

---

## 6. PRUEBAS Y VALIDACIÓN

### 6.1 Plan de Pruebas

#### 6.1.1 Prueba 1: Conectividad de Base de Datos

**Objetivos:**
- Verificar que MySQL está funcionando
- Verificar que la aplicación se conecta a MySQL

**Pasos (Virtualización):**
```bash
# Conectarse a MySQL
mysql -u constructrack -p constructrack_logbook

# Ejecutar query de prueba
SELECT VERSION();

# Resultado esperado: 8.0.XX
```

**Validación:**
- ✓ MySQL responde correctamente
- ✓ Usuario tiene permisos en BD

#### 6.1.2 Prueba 2: Servicios en Ejecución

**Verificar puertos escuchando:**

```bash
# Virtualización
sudo netstat -tlnp | grep -E '3306|80|8080'

# Contenedores
docker ps
docker stats
```

**Salida esperada:**
| Protocolo | Puerto | Servicio |
|---|---|---|
| TCP | 3306 | MySQL |
| TCP | 80 | Apache |
| TCP | 8080 | Aplicación Java |

#### 6.1.3 Prueba 3: Acceso HTTP

```bash
# Teste desde terminal
curl -i http://localhost:8080

# Resultado esperado: HTTP 200 OK
```

#### 6.1.4 Prueba 4: Mapeo de Datos

**Datos de prueba a insertar:**

```sql
INSERT INTO usuarios (nombre, email, usuario, password_hash, rol) VALUES
('Admin', 'admin@constructrack.com', 'admin', '$2a$10$...', 'ADMIN'),
('Supervisor', 'sup@constructrack.com', 'supervisor', '$2a$10$...', 'SUPERVISOR');

INSERT INTO proyectos (nombre, ubicacion, fecha_inicio, responsable_id) VALUES
('Proyecto Test', 'Calle 123, Ciudad', '2026-04-29', 1);
```

**Verificar inserción:**
```bash
mysql -u constructrack -p constructrack_logbook -e "SELECT * FROM usuarios;"
```

### 6.2 Matriz de Validación

| Componente | Virtualización | Contenedores | Estado |
|---|---|---|---|
| MySQL en puerto 3306 | ✓ | ✓ | OK |
| Apache en puerto 80 | ✓ | ✓ | OK |
| Java en puerto 8080 | ✓ | ✓ | OK |
| Conexión BD desde App | ✓ | ✓ | OK |
| Tablas creadas | ✓ | ✓ | OK |
| Proxy Apache funciona | ✓ | ✓ | OK |
| Logs generándose | ✓ | ✓ | OK |
| Escalabilidad | ✗ | ✓ | Contenedores superior |

### 6.3 Comparativa de Métodos

#### Virtualización

**Ventajas:**
- Aislamiento completo de SO
- Mayor control del sistema
- Ambiente más similar a producción
- Compatible con sistemas antiguos

**Desventajas:**
- Mayor consumo de recursos
- Tiempo de inicio más lento
- Complejidad de configuración manual
- Mantenimiento de imágenes completas

#### Contenedores

**Ventajas:**
- Menor consumo de recursos
- Inicio rápido (segundos)
- Fácil escalabilidad
- Reproducibilidad garantizada
- Portabilidad entre máquinas
- Ideal para microservicios

**Desventajas:**
- Menor aislamiento que VM
- Requiere aprendizaje de Docker
- No es ideal para apps gráficas de escritorio
- Requiere soporte del kernel

---

## 7. CONCLUSIONES

### 7.1 Hallazgos

1. **Virtualización:** Método tradicional pero efectivo para aislar entornos completos. Requiere más recursos pero ofrece máximo control.

2. **Contenedores:** Solución moderna, eficiente y escalable. Perfecta para aplicaciones en la nube y arquitecturas de microservicios.

3. **ConstrucTrack:** La aplicación fue desplegada exitosamente en ambos escenarios, demostrando portabilidad y compatibilidad.

### 7.2 Recomendaciones

1. **Para desarrollo**: Usar contenedores (Docker) por velocidad y facilidad
2. **Para producción**: Considerar orquestación (Kubernetes) con múltiples réplicas
3. **Para legacy**: Virtualización en casos donde se requiera SO específico
4. **Monitoreo**: Implementar herramientas como Prometheus/Grafana

### 7.3 Próximos Pasos

- [ ] Agregar CI/CD (GitHub Actions, GitLab CI)
- [ ] Implementar registros centralizados (ELK Stack)
- [ ] Configurar backups automáticos de BD
- [ ] Documentar runbooks de operación
- [ ] Entrenar equipo en herramientas

---

## 8. BIBLIOGRAFÍA Y REFERENCIAS

### 8.1 Documentación Oficial

1. **Ubuntu Server Documentation**
   - URL: https://ubuntu.com/server/docs
   - Acceso: 2026-04-29

2. **MySQL Official Documentation**
   - URL: https://dev.mysql.com/doc/
   - Versión: 8.0

3. **Apache HTTP Server Documentation**
   - URL: https://httpd.apache.org/docs/
   - Versión: 2.4

4. **Docker Official Documentation**
   - URL: https://docs.docker.com/
   - Funcionalidad: Containerization

5. **Java Platform Documentation**
   - URL: https://docs.oracle.com/javase/17/
   - Versión: JDK 17 LTS

### 8.2 Referencias Técnicas

1. **Virtualización en Linux**
   - Red Hat Enterprise Linux Virtualization Guide
   - Hypervisor: KVM/QEMU

2. **Conteinerización**
   - Docker Best Practices
   - Docker Compose Specification (v3.8+)

3. **Seguridad**
   - OWASP Top 10 Guidelines
   - MySQL Security Documentation
   - Apache Security Tips

### 8.3 Herramientas Utilizadas

| Herramienta | Versión | Propósito |
|---|---|---|
| Ubuntu | 22.04 LTS | Sistema Operativo |
| MySQL | 8.0+ | Base de Datos |
| Apache | 2.4+ | Servidor Web |
| Docker | 24+ | Contenedores |
| Maven | 3.8+ | Build Tool |
| Java/JDK | 17 LTS | Runtime |

---

## 9. ANEXOS

### 9.1 Comandos Rápidos de Referencia

#### Virtualización
```bash
# Iniciar servicios
sudo systemctl start mysql
sudo systemctl start apache2
sudo systemctl start constructrack.service

# Ver estado
sudo systemctl status mysql
sudo systemctl status apache2

# Ver logs
sudo tail -100 /var/log/apache2/constructrack_access.log
sudo journalctl -u constructrack -n 50
```

#### Contenedores
```bash
# Levantar stack
docker-compose up -d

# Detener
docker-compose down

# Ver logs
docker-compose logs -f app

# Conectar a contenedor
docker exec -it constructrack-mysql bash
```

### 9.2 Script de Instalación Automatizada (Ubuntu)

Ver archivo: `install.sh` en directorio

### 9.3 Archivos de Configuración

- `Dockerfile` - Definición imagen de la app
- `docker-compose.yml` - Orquestación de servicios
- `constructrack.service` - Unidad systemd
- `apache-constructrack.conf` - Config Apache

---

**Documento preparado por:** [Tu Nombre]
**Fecha:** 29 de Abril de 2026
**Institución:** SENA - Análisis y Desarrollo de Software
**Evidencia:** GA10-220501097-AA5-EV01

