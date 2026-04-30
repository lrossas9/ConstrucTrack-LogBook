# GUÍA PRÁCTICA: COMPILACIÓN Y EJECUCIÓN DE ConstrucTrack LogBook

## Contenido Práctico para Ejecución en Windows

### SECCIÓN A: EJECUCIÓN EN WINDOWS (Entorno Actual)

---

## A.1 COMPILACIÓN Y EJECUCIÓN SIMPLE

Si deseas compilar y ejecutar la aplicación JavaFX:

```powershell
# Navegar al directorio del proyecto
cd "C:\Users\57311\Documents\LAURA 2025 1\ANALISIS Y DESARROLLO DE SOFTWARE. (3070308)\PROYECTO\1. ConstrucTrack LogBook"

# Definir JAVA_HOME (si es necesario)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.15.6-hotspot"

# Compilar y empaquetar
mvn clean package -DskipTests

# Ejecutar la aplicación
mvn javafx:run
```

**Requisitos previos:**
- Java 17 JDK (Eclipse Adoptium o similar)
- Maven 3.8+
- Windows 10/11

---

## A.2 INSTALACIÓN MANUAL PASO A PASO

### PASO 1: Preparación Inicial

```bash
# Actualizar sistema
sudo apt update
sudo apt upgrade -y

# Instalar herramientas básicas
sudo apt install -y curl wget git nano vim net-tools
```

### PASO 2: Instalación de Java 17

```bash
# Instalar OpenJDK 17
sudo apt install -y openjdk-17-jdk

# Verificar instalación
java -version
# Salida esperada: openjdk version "17.x.x"

# Configurar JAVA_HOME permanentemente
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc

# Verificar JAVA_HOME
echo $JAVA_HOME
# Salida esperada: /usr/lib/jvm/java-17-openjdk-amd64
```

### PASO 3: Instalación de Maven

```bash
# Instalar Maven
sudo apt install -y maven

# Verificar
mvn -version
# Salida esperada: Apache Maven 3.8.x o superior
```

### PASO 4: Instalación de MySQL

```bash
# Instalar MySQL Server
sudo apt install -y mysql-server

# Iniciar servicio
sudo systemctl start mysql
sudo systemctl enable mysql

# Verificar estado
sudo systemctl status mysql
# Salida esperada: Active: active (running)
```

#### Configurar Seguridad MySQL

```bash
# Conectarse a MySQL como root
sudo mysql -u root

# Ejecutar en consola MySQL:
ALTER USER 'root'@'localhost' IDENTIFIED BY 'root123';
DELETE FROM mysql.user WHERE User='';
DELETE FROM mysql.user WHERE User='root' AND Host NOT IN ('localhost', '127.0.0.1', '::1');
DROP DATABASE IF EXISTS test;
DELETE FROM mysql.db WHERE Db='test' OR Db='test\\_%';
FLUSH PRIVILEGES;
EXIT;
```

#### Crear Base de Datos

```bash
# Conectarse con nueva contraseña
mysql -u root -p
# Contraseña: root123

# Crear BD y usuario
CREATE DATABASE constructrack_logbook;
CREATE USER 'constructrack'@'localhost' IDENTIFIED BY 'constructrack123';
GRANT ALL PRIVILEGES ON constructrack_logbook.* TO 'constructrack'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

#### Crear Tablas

```bash
# Descargar schema.sql desde tu proyecto
# Si está en máquina local, transfiere con:
# scp schema.sql usuario@servidor:/tmp/

# Aplicar schema
mysql -u constructrack -p constructrack_logbook < /ruta/a/schema.sql
# Contraseña: constructrack123

# Verificar tablas creadas
mysql -u constructrack -p -e "USE constructrack_logbook; SHOW TABLES;"
```

**Script SQL alternativo (si no tienes schema.sql):**

```bash
mysql -u constructrack -p constructrack_logbook <<EOF
CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    usuario VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(20),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE proyectos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    ubicacion VARCHAR(200),
    fecha_inicio DATE,
    fecha_fin DATE,
    responsable_id INT,
    estado VARCHAR(20),
    FOREIGN KEY (responsable_id) REFERENCES usuarios(id) ON DELETE SET NULL
);

CREATE TABLE bitacoras (
    id INT PRIMARY KEY AUTO_INCREMENT,
    proyecto_id INT NOT NULL,
    usuario_id INT NOT NULL,
    descripcion TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (proyecto_id) REFERENCES proyectos(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE evidencia (
    id INT PRIMARY KEY AUTO_INCREMENT,
    bitacora_id INT NOT NULL,
    tipo VARCHAR(50),
    ruta_archivo VARCHAR(500),
    fecha_carga TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bitacora_id) REFERENCES bitacoras(id) ON DELETE CASCADE
);
EOF
```

### PASO 5: Instalación de Apache

```bash
# Instalar Apache
sudo apt install -y apache2

# Habilitar módulos necesarios
sudo a2enmod proxy
sudo a2enmod proxy_http
sudo a2enmod rewrite
sudo a2enmod headers

# Verificar configuración
sudo apache2ctl configtest
# Salida esperada: Syntax OK

# Iniciar Apache
sudo systemctl start apache2
sudo systemctl enable apache2

# Verificar estado
sudo systemctl status apache2
```

#### Configurar Proxy en Apache

```bash
# Crear archivo de configuración
sudo nano /etc/apache2/sites-available/constructrack.conf
```

**Pegar contenido:**

```apache
<VirtualHost *:80>
    ServerName constructrack.local
    ServerAdmin admin@constructrack.local
    
    ProxyPreserveHost On
    ProxyPass / http://127.0.0.1:8080/
    ProxyPassReverse / http://127.0.0.1:8080/
    
    RequestHeader set X-Forwarded-Proto "http"
    RequestHeader set X-Forwarded-Port "80"
    
    ErrorLog ${APACHE_LOG_DIR}/constructrack_error.log
    CustomLog ${APACHE_LOG_DIR}/constructrack_access.log combined
</VirtualHost>
```

**Guardar y activar:**

```bash
# Habilitar sitio
sudo a2ensite constructrack.conf

# Verificar sintaxis
sudo apache2ctl configtest

# Recargar Apache
sudo systemctl reload apache2
```

### PASO 6: Preparar Aplicación

```bash
# Crear usuario de aplicación
sudo useradd -m -s /bin/bash constructrack
echo "constructrack:ConstrucTrack2025#" | sudo chpasswd

# Crear directorio de aplicación
sudo mkdir -p /opt/constructrack
sudo mkdir -p /var/log/constructrack
sudo chown -R constructrack:constructrack /opt/constructrack
sudo chown -R constructrack:constructrack /var/log/constructrack

# Cambiar a usuario constructrack
sudo su - constructrack

# Copiar código fuente a /opt/constructrack
# (Transferir desde máquina local o clonar repositorio)
cd /opt/constructrack
```

#### Compilar Aplicación

```bash
# Copiar pom.xml y src/ primero

# Editar archivo de propiedades
nano src/main/resources/application.properties
```

**Verificar contenido:**

```properties
db.host=127.0.0.1
db.port=3306
db.name=constructrack_logbook
db.user=constructrack
db.password=constructrack123
```

**Compilar:**

```bash
# Limpiar
mvn clean

# Compilar
mvn compile

# Empaquetar
mvn package -DskipTests

# Resultado esperado
# BUILD SUCCESS
# Created jar: target/constructrack-bitacora-1.0.0.jar

# Salir del usuario constructrack
exit
```

### PASO 7: Crear Servicio Systemd

```bash
# Crear archivo de servicio
sudo nano /etc/systemd/system/constructrack.service
```

**Pegar contenido:**

```ini
[Unit]
Description=ConstrucTrack LogBook Application
After=network.target mysql.service

[Service]
Type=simple
User=constructrack
WorkingDirectory=/opt/constructrack
ExecStart=/usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar target/constructrack-bitacora-1.0.0.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

**Habilitar y ejecutar:**

```bash
# Recargar systemd
sudo systemctl daemon-reload

# Habilitar automáticamente en boot
sudo systemctl enable constructrack.service

# Iniciar servicio
sudo systemctl start constructrack.service

# Ver estado
sudo systemctl status constructrack.service
```

### PASO 8: Verificación de Despliegue

```bash
# Verificar que MySQL está escuchando
sudo netstat -tlnp | grep 3306
# Salida esperada: tcp 0 0 127.0.0.1:3306

# Verificar que Apache está escuchando
sudo netstat -tlnp | grep 80
# Salida esperada: tcp 0 0 0.0.0.0:80

# Verificar que Java está escuchando
sudo netstat -tlnp | grep 8080
# Salida esperada: tcp 0 0 127.0.0.1:8080

# Logs de Apache
sudo tail -20 /var/log/apache2/constructrack_access.log
sudo tail -20 /var/log/apache2/constructrack_error.log

# Logs de aplicación
sudo journalctl -u constructrack.service -n 50
sudo journalctl -u constructrack.service -f  # Tiempo real

# Test de conectividad
curl -i http://localhost:8080
curl -i http://localhost:80

# Test a base de datos desde aplicación
mysql -u constructrack -p -e "USE constructrack_logbook; SELECT COUNT(*) FROM usuarios;"
```

---

### SECCIÓN B: DESPLIEGUE CON CONTENEDORES (Docker)

---

## B.1 INSTALACIÓN DE DOCKER

### En Ubuntu/Linux

```bash
# Actualizar sistema
sudo apt update
sudo apt upgrade -y

# Instalar dependencias
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common

# Agregar clave GPG de Docker
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

# Agregar repositorio
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"

# Actualizar e instalar Docker
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Verificar instalación
docker --version
docker run hello-world

# Agregar usuario actual a grupo docker (opcional)
sudo usermod -aG docker $USER
newgrp docker
```

### En Windows

```powershell
# Descargar Docker Desktop desde:
# https://www.docker.com/products/docker-desktop

# Instalar y reiniciar
# Verificar en PowerShell:
docker --version
docker run hello-world
```

---

## B.2 CONSTRUIR Y EJECUTAR CON DOCKER COMPOSE

### Preparar Archivos

```bash
# Crear estructura de proyecto
mkdir -p constructrack-docker/apache-config
mkdir -p constructrack-docker/sql
mkdir -p constructrack-docker/logs

cd constructrack-docker

# Copiar archivos del proyecto
cp ../1.\ ConstrucTrack\ LogBook/pom.xml .
cp -r ../1.\ ConstrucTrack\ LogBook/src .
cp ../1.\ ConstrucTrack\ LogBook/src/main/resources/sql/schema.sql ./sql/

# Copiar archivos de configuración
# Dockerfile
# docker-compose.yml
# apache-config/httpd.conf
# apache-config/constructrack.conf
```

### Ejecutar Stack Completo

```bash
# Navegar al directorio
cd constructrack-docker

# Construir imágenes
docker-compose build

# Iniciar servicios en background
docker-compose up -d

# Ver logs
docker-compose logs -f

# Ver estado de contenedores
docker-compose ps
```

### Acceder a Servicios

```bash
# Aplicación Java
# http://localhost:8080

# Apache (proxy)
# http://localhost:80

# MySQL desde otra aplicación
# localhost:3306
```

### Gestión de Contenedores

```bash
# Ver logs en tiempo real
docker-compose logs -f app
docker-compose logs -f mysql
docker-compose logs -f apache

# Conectarse a contenedor MySQL
docker exec -it constructrack-mysql bash
mysql -u constructrack -p constructrack_logbook

# Conectarse a contenedor app
docker exec -it constructrack-app bash

# Ver consumo de recursos
docker stats

# Parar servicios
docker-compose stop

# Reiniciar servicios
docker-compose restart

# Detener y remover contenedores
docker-compose down

# Remover incluyendo volúmenes
docker-compose down -v
```

---

## VERIFICACIÓN FINAL (AMBOS MÉTODOS)

### Test 1: Conectividad Base de Datos

```bash
# Virtualización:
mysql -u constructrack -p constructrack_logbook
> SELECT VERSION();
> SHOW TABLES;
> EXIT;

# Contenedores:
docker exec constructrack-mysql mysql -u constructrack -p constructrack_logbook -e "SHOW TABLES;"
```

### Test 2: Puertos Escuchando

```bash
# Virtualización:
sudo netstat -tlnp | grep -E '3306|80|8080'

# Contenedores:
docker port constructrack-mysql
docker port constructrack-apache
docker port constructrack-app
```

### Test 3: HTTP Request

```bash
# Virtualización y Contenedores:
curl -v http://localhost:8080
curl -v http://localhost:80

# Windows PowerShell:
Invoke-WebRequest http://localhost:8080
Invoke-WebRequest http://localhost:80
```

### Test 4: Ver Logs

```bash
# Virtualización:
sudo tail -100 /var/log/apache2/constructrack_access.log
sudo journalctl -u constructrack -n 100

# Contenedores:
docker-compose logs --tail=100 app
docker-compose logs --tail=100 mysql
```

---

## SOLUCIÓN DE PROBLEMAS COMÚN

### MySQL no inicia

```bash
# Virtualización:
sudo systemctl restart mysql
sudo systemctl status mysql

# Ver logs:
sudo grep -i error /var/log/mysql/error.log

# Contenedores:
docker-compose logs mysql
docker-compose restart mysql
```

### Puerto 8080 en uso

```bash
# Encontrar proceso usando puerto
sudo netstat -tlnp | grep 8080
sudo lsof -i :8080

# Matar proceso
sudo kill -9 <PID>

# O cambiar puerto en docker-compose.yml
# ports:
#   - "8081:8080"
```

### Conexión rechazada a aplicación

```bash
# Verificar que servicios están corriendo
# Virtualización:
sudo systemctl status constructrack
sudo systemctl status apache2

# Contenedores:
docker-compose ps

# Esperar 10-15 segundos para que inicien
# Revisar logs para errores de conexión BD
```

### Base de datos vacía

```bash
# Virtualización:
mysql -u constructrack -p constructrack_logbook < src/main/resources/sql/schema.sql

# Contenedores:
docker exec constructrack-mysql mysql -u constructrack -p constructrack_logbook < sql/schema.sql
```

---

## COMPARATIVA: VIRTUALIZACIÓN vs CONTENEDORES

| Característica | Virtualización | Contenedores |
|---|---|---|
| **Tiempo de inicio** | 30-60 segundos | 3-5 segundos |
| **Consumo RAM** | 1-2 GB por VM | 200-500 MB por contenedor |
| **Tamaño imagen** | 2-3 GB | 500 MB - 1 GB |
| **Aislamiento** | Total (SO independiente) | Parcial (kernel compartido) |
| **Curva aprendizaje** | Media | Media |
| **Escalabilidad** | Media | Alta |
| **Portabilidad** | Media | Alta |
| **Producción** | Sí (requiere hypervisor) | Sí (ideal con Kubernetes) |

---

## SIGUIENTES PASOS

1. **Monitoreo**: Implementar Prometheus + Grafana
2. **CI/CD**: GitHub Actions o GitLab CI
3. **Backups**: Automatizar backup BD diariamente
4. **Clustering**: MySQL con replicación (master-slave)
5. **Load Balancing**: Nginx o HAProxy
6. **Logs Centralizados**: ELK Stack (Elasticsearch, Logstash, Kibana)
7. **Orquestación**: Kubernetes para producción

---

**Fin de Guía Práctica**

*Versión: 1.0 | Fecha: 29 de Abril de 2026 | GA10-220501097-AA5-EV01*

