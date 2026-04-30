# GUÍA DE CAPTURAS DE PANTALLA PARA INFORME

## Secciones Donde Incluir Imágenes

Para convertir el informe Markdown a PDF/Word profesional, incluye capturas en estas secciones:

---

## 1. PORTADA (Intro)

**Imagen sugerida:**
- Logo institucional SENA
- Interfaz gráfica de ConstrucTrack LogBook
- Diagrama de arquitectura simple

---

## 2. PARTE 1: VIRTUALIZACIÓN

### 2.1 Creación de Máquina Virtual

**Captura de VirtualBox:**
```
Mostrar:
- Panel de configuración de nueva VM
- Asignación de recursos (RAM, CPU)
- Selección de imagen ISO de Ubuntu
- Resultado: VM creada en VirtualBox
```

Localización en informe: **Sección 4.1.2, Paso 1**

### 2.2 Instalación de Ubuntu

**Capturas de instalación:**
```
- Pantalla inicial de instalación Ubuntu
- Selección de idioma y zona horaria
- Creación de usuario 'constructrack'
- Pantalla final: "Installation complete"
```

Localización: **Sección 4.1.2, Paso 2**

### 2.3 Verificación de MySQL

**Terminal con comandos:**

```bash

$ sudo systemctl status mysql
● mysql.service - MySQL Community Server
     Loaded: loaded (/lib/systemd/unit/mysql.service)
     Active: active (running)

# Captura 2: Conectar a MySQL
$ mysql -u constructrack -p
mysql> SELECT COUNT(*) FROM información_schema.TABLES;
+----------+
| COUNT(*) |
+----------+
|    4     |
+----------+

# Captura 3: Mostrar tablas creadas
mysql> USE constructrack_logbook;
mysql> SHOW TABLES;
+------------------------------+
| Tables_in_constructrack_logbook |
+------------------------------+
| bitacoras                    |
| evidencia                    |
| proyectos                    |
| usuarios                     |
+------------------------------+
```

Localización: **Sección 4.2.5**

### 2.4 Apache Configurado

**Terminal:**

```bash
# Captura: Módulos habilitados y sitio activo
$ apache2ctl -M | grep proxy
 proxy_module (shared)
 proxy_http_module (shared)

$ ls -la /etc/apache2/sites-enabled/
lrwxrwxrwx ... constructrack.conf -> ../sites-available/constructrack.conf

$ sudo systemctl status apache2
● apache2.service - Apache HTTP Server
     Active: active (running)
```

Localización: **Sección 4.3.4**

### 2.5 Aplicación Ejecutándose

**Capturas de terminal:**

```bash
Compilación exitosa
$ mvn package -DskipTests
...
BUILD SUCCESS
Total time: 12.5 s

# Captura 2: Jar creado
$ ls -lh target/constructrack-bitacora-1.0.0.jar
-rw-r--r-- ... 25M constructrack-bitacora-1.0.0.jar

# Captura 3: Servicio iniciado
$ sudo systemctl status constructrack
● constructrack.service - ConstrucTrack LogBook Application
     Active: active (running)

# Captura 4: Logs mostrando app iniciada
$ sudo journalctl -u constructrack -n 20
... Application started on port 8080
... Connected to database successfully
```

Localización: **Sección 4.4.3**

### 2.6 Verificación de Puertos

**Terminal con netstat:**

```bash
$ sudo netstat -tlnp | grep -E '3306|80|8080'
tcp  0 0 127.0.0.1:3306  0.0.0.0:*  LISTEN  1234/mysqld
tcp  0 0 0.0.0.0:80     0.0.0.0:*  LISTEN  5678/apache2
tcp  0 0 127.0.0.1:8080  0.0.0.0:*  LISTEN  9012/java
```

Localización: **Sección 4.5**

---

## 3. PARTE 2: CONTENEDORES

### 3.1 Instalación de Docker

**Captura de terminal:**

```bash
$ docker --version
Docker version 24.0.0, build latest

$ docker run hello-world
Hello from Docker!
This message shows that your installation appears correct.
```

Localización: **Sección 5.1**

### 3.2 Build de Imágenes

**Captura del proceso:**

```bash
$ docker-compose build
Building app
Sending build context to Docker daemon
...
Successfully built abc12345
Successfully tagged constructrack-app:latest
```

Localización: **Sección 5.4.2**

### 3.3 Contenedores Ejecutándose

**Captura 1: docker-compose ps**

```bash
$ docker-compose ps
NAME                    COMMAND                 SERVICE     STATUS
constructrack-mysql     "docker-entrypoint..."  mysql       Up 2 min
constructrack-app      "java -jar construct..." app         Up 2 min
constructrack-apache   "httpd-foreground"      apache      Up 2 min
```

Localización: **Sección 5.4.3**

**Captura 2: docker stats (opcional)**

```
CONTAINER          CPU %  MEM USAGE / LIMIT
constructrack-app  15.3%  156MiB / 2GiB
constructrack-mysql 8.2%  234MiB / 2GiB
constructrack-apache 0.1%  12MiB / 2GiB
```

### 3.4 Logs de Contenedores

**Captura: docker-compose logs**

```bash
$ docker-compose logs -f app

app_1      | 2026-04-29 12:15:30 INFO The following profiles are active: default
app_1      | 2026-04-29 12:15:31 INFO Started Application in 2.134 seconds (JVM)
app_1      | 2026-04-29 12:15:32 INFO Database connection successful
apache_1   | constructrack-app:8080 - - [29/Apr/2026] GET / HTTP/1.1
```

Localización: **Sección 5.5.4**

### 3.5 Conectividad entre Contenedores

**Captura: Conectarse a MySQL desde contenedor**

```bash
$ docker exec -it constructrack-mysql mysql -u constructrack -p constructrack_logbook

mysql> SHOW TABLES;
+------------------------------+
| Tables_in_constructrack_logbook |
+------------------------------+
| bitacoras                    |
| evidencia                    |
| proyectos                    |
| usuarios                     |
+------------------------------+
5 rows in set (0.00 sec)

mysql> SELECT COUNT(*) FROM usuarios;
+----------+
| COUNT(*) |
+----------+
|    0     |
+----------+
```

Localización: **Sección 5.5.2**

---

## 4. PRUEBAS Y VALIDACIÓN

### 4.1 Test HTTP

**Captura: curl a localhost:8080**

```bash
$ curl -i http://localhost:8080

HTTP/1.1 200 OK
Content-Type: text/html; charset=UTF-8
Content-Length: 1234
Connection: keep-alive

<!DOCTYPE html>
<html>
<head>
    <title>ConstrucTrack LogBook</title>
    ...
</head>
```

Localización: **Sección 6.1.3**

### 4.2 Test a través de Apache

**Captura: curl a localhost:80**

```bash
$ curl -i http://localhost:80

HTTP/1.1 200 OK
Server: Apache/2.4.x
Content-Type: text/html
Via: HTTP/1.1 localhost:80
X-Forwarded-For: 127.0.0.1

[Contenido de página de aplicación]
```

Localización: **Sección 6.1.3**

### 4.3 Logs de Apache

**Captura: archivo access.log**

```
127.0.0.1 - - [29/Apr/2026:12:15:30 -0500] "GET / HTTP/1.1" 200 1234 "-"
127.0.0.1 - - [29/Apr/2026:12:15:31 -0500] "POST /login HTTP/1.1" 302 0 "-"
127.0.0.1 - - [29/Apr/2026:12:15:32 -0500] "GET /dashboard HTTP/1.1" 200 5678 "-"
```

Localización: **Sección 6.1**

---

## 5. MATRIZ DE VALIDACIÓN

### 5.1 Tabla Comparativa de Despliegue

**Captura recomendada (tabla en PDF):**

| Puesto | Virtualización | Contenedores | Estado |
|---|---|---|---|
| MySQL:3306 | ✓ | ✓ | OK |
| Apache:80 | ✓ | ✓ | OK |
| Java:8080 | ✓ | ✓ | OK |
| BD Inicializada | ✓ | ✓ | OK |
| Proxy Funciona | ✓ | ✓ | OK |

Localización: **Sección 6.2**

---

## 6. COMPARATIVA VISUAL

### 6.1 Arquitectura Virtualización

**Diagrama a crear:**

```
┌─────────────────────────────────────────────┐
│            Máquina Anfitrión (Windows)      │
├─────────────────────────────────────────────┤
│                                             │
│  ┌──────────────────────────────────────┐   │
│  │    VirtualBox/VMware Hypervisor      │   │
│  ├──────────────────────────────────────┤   │
│  │  ┌──────────────────────────────┐    │   │
│  │  │  VM Ubuntu 22.04             │    │   │
│  │  ├──────────────────────────────┤    │   │
│  │  │ ┌──────┬──────────┬────────┐ │    │   │
│  │  │ │MySQL │ Apache   │  Java  │ │    │   │
│  │  │ │:3306 │   :80    │ :8080  │ │    │   │
│  │  │ └──────┴──────────┴────────┘ │    │   │
│  │  │                              │    │   │
│  │  │ Linux Kernel                 │    │   │
│  │  └──────────────────────────────┘    │   │
│  └──────────────────────────────────────┘   │
│                                             │
└─────────────────────────────────────────────┘
```

Localización: **Sección 5**

### 6.2 Arquitectura Contenedores

**Diagrama a crear:**

```
┌──────────────────────────────────────────┐
│      Máquina Anfitrión (Linux/Windows)   │
├──────────────────────────────────────────┤
│                                          │
│    ┌────────┐  ┌────────┐  ┌────────┐  │
│    │ MySQL  │  │ Apache │  │  App   │  │
│    │:3306   │  │  :80   │  │  :8080 │  │
│    └───┬────┘  └───┬────┘  └───┬────┘  │
│        │           │           │        │
│    ┌───┴───────────┴───────────┴──┐    │
│    │   Docker Engine - Red Bridge   │    │
│    │  (172.20.0.0/16)              │    │
│    └────────────────────────────────┘    │
│                                          │
│    ┌────────────────────────────────┐   │
│    │  Linux Kernel (Compartido)     │   │
│    └────────────────────────────────┘   │
│                                          │
└──────────────────────────────────────────┘
```

Localización: **Sección 5**

---

## 7. RECOMENDACIONES PARA INSERTARLAS

### En Word/Google Docs:

1. **Captura 1:** Pantalla inicial Ubuntu → Sección 4.1
2. **Captura 2:** MySQL conectado → Sección 4.2
3. **Captura 3:** Apache status → Sección 4.3
4. **Captura 4:** Aplicación compilada → Sección 4.4
5. **Captura 5:** Systemctl status → Sección 4.5
6. **Captura 6:** Docker build → Sección 5.4
7. **Captura 7:** docker-compose ps → Sección 5.4
8. **Captura 8:** Logs exitosos → Sección 5.5
9. **Captura 9:** curl HTTP 200 → Sección 6.1
10. **Diagrama 1:** Arquitectura Virtualización
11. **Diagrama 2:** Arquitectura Contenedores
12. **Tabla 1:** Matriz de validación

---

## 8. CÓMO CAPTURAR PANTALLAS

### En Linux:

```bash
# Captura completa
gnome-screenshot

# Captura de región específica
gnome-screenshot -a

# Captura con demora (5 segundos)
gnome-screenshot -d 5

# Con nombre personalizado
gnome-screenshot -f ~/Escritorio/mysql-status.png
```

### En Windows:

```powershell
# Captura con recorte
# Presionar: Win + Shift + S

# O usar Snipping Tool:
# Win + Shift + S

# Para terminal PowerShell:
# Click derecho en ventana → Seleccionar todo → Copiar
```

### En macOS:

```bash
# Captura completa
Command + Shift + 3

# Captura de región
Command + Shift + 4

# Screenshot Tool
# Command + Shift + 5
```

---

## 9. FORMATO DE INSERCIÓN EN DOCUMENTO

### Formato recomendado:

```markdown
## [Número Sección] Título Subsección

[Texto explicativo]

**Figura [N]:** Descripción de Screenshot
```
[IMAGEN AQUÍ - Insertar screenshot]
```

**Descripción técnica bajo imagen:**
- Explicar qué se ve
- Resaltar puntos clave
- Mostrar estado/resultado esperado

```

---

## 10. CHECKLIST DE IMÁGENES

- [ ] 3-4 capturas de instalación Ubuntu
- [ ] 3-4 capturas de MySQL funcionando
- [ ] 2-3 capturas de Apache
- [ ] 2-3 capturas de Aplicación Java corriendo
- [ ] 2-3 capturas de Docker/Contenedores
- [ ] 2-3 capturas de logs
- [ ] 2 diagramas de arquitectura
- [ ] 1 tabla de validación
- [ ] 2-3 capturas de pruebas HTTP

**Total recomendado:** 20-25 imágenes/diagramas

---

*Este documento sirve como guía para saber DÓNDE insertar capturas de pantalla en el informe Markdown cuando lo conviertas a PDF/Word.*

**Última actualización:** 29 de Abril de 2026

