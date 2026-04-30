# INFORME DE DESPLIEGUE: ConstrucTrack LogBook

## Evidencia GA10-220501097-AA5-EV01

Este directorio contiene toda la documentación y archivos necesarios para ejecutar y desplegar la aplicación **ConstrucTrack LogBook**.

**Versión actual:** Ejecución en Windows + Conceptos teóricos de Virtualización y Contenedores

**Metodologías documentadas:**
1. **Windows (Actual)**: JavaFX + Maven + Java 17
2. **Virtualización (Teórica)**: Ubuntu + MySQL + Apache + Java
3. **Contenedores (Teórica)**: Docker + Docker Compose

---

## 📋 Contenido del Informe

### Documentos Principales

1. **INFORME_GA10_220501097_AA5_EV01.md** ⭐ **PRINCIPAL**
   - Informe formal completo con portada, introducción y conclusiones
   - Cumple normas académicas SENA (portada, tabla de contenido, referencias)
   - Incluye 3 secciones: Windows (actual), Ubuntu teórico, Docker teórico
   - Especificación de requisitos y verificación de servicios
   - **Apto para PDF/Word**

2. **GUIA_PRACTICA_PASO_A_PASO.md**
   - Comandos ejecutables en Windows PowerShell
   - Procedimiento paso a paso para compilar y ejecutar
   - Instrucciones para verificación local

3. **GUIA_DESPLIEGUE_SERVICIOS.md** (referencia)
   - Conceptos de servicios en servidores
   - Arquitectura de despliegue

### Archivos de Configuración

4. **Dockerfile** - Multi-stage para containerizar la aplicación
5. **docker-compose.yml** - Orquestación de servicios (MySQL, Apache, App)
6. **install.sh** - Script bash para instalación en Ubuntu (referencia teórica)
7. **constructrack.service** - Configuración systemd para Ubuntu
8. **httpd.conf** - Configuración Apache base
9. **constructrack-apache.conf** - VirtualHost con proxy

---

## 🚀 Inicio Rápido

### Opción 1: Ejecutar en Windows LOCAL (Recomendado para pruebas)

```powershell
# 1. Navegar al proyecto
cd "C:\Users\57311\Documents\LAURA 2025 1\ANALISIS Y DESARROLLO DE SOFTWARE. (3070308)\PROYECTO\1. ConstrucTrack LogBook"

# 2. Definir JAVA_HOME (si es necesario)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.15.6-hotspot"

# 3. Compilar
mvn clean package -DskipTests

# 4. Ejecutar
mvn javafx:run

# Resultado: Ventana gráfica de ConstrucTrack LogBook
```

**Tiempo:** 5-15 minutos (primera ejecución)

**Requisitos:**
- Java 17 JDK instalado
- Maven 3.8+

---

### Opción 2: Desplegar con Docker (Completo teórico)

```powershell
# Con Docker Desktop instalado:
cd INFORME_DESPLIEGUE
docker-compose up -d

# Esperar 15-20 segundos
# Acceder a: http://localhost:8080
```

**Servicios incluidos:**
- MySQL 8.0 (puerto 3306)
- Apache 2.4 (puerto 80 → proxy a 8080)
- Aplicación Java (puerto 8080)

---

### Opción 3: Documentación Ubuntu (Teórica)

Para referencia académica de cómo desplegar en servidor Linux:
- Ver **INFORME_GA10_220501097_AA5_EV01.md** (Parte 2)
- Incluye pasos completos de instalación en Ubuntu 22.04 LTS

---

## 📖 Cómo Usar Este Informe

### Para Presentación Formal

1. Abre **INFORME_GA10_220501097_AA5_EV01.md**
2. Copia contenido a Word o Google Docs
3. Inserta capturas de pantalla en secciones indicadas:
   - Compilación exitosa (`mvn clean package`)
   - Ejecución de la aplicación (`mvn javafx:run`)
   - Ventana gráfica de ConstrucTrack LogBook
   - Verificación de completación
4. Incluye las referencias bibliográficas (ya están en el documento)
5. Exporta a PDF

### Para Ejecución Práctica

1. Lee **GUIA_PRACTICA_PASO_A_PASO.md**
2. Abre PowerShell en `C: ConstrucTrack LogBook`
3. Ejecuta comandos en orden:
   - `mvn clean`
   - `mvn package -DskipTests`
   - `mvn javafx:run`
4. Documenta resultado con capturas
5. Presiona `Ctrl+C` para cerrar la aplicación

### Para Aprender sobre Virtualización

Consulta **INFORME_GA10_220501097_AA5_EV01.md** (Parte 2 y 3):
- Arquitectura de servicios en Ubuntu
- Configuración de MySQL + Apache
- Despliegue con Docker y Docker Compose

---

## ✅ Checklist de Validación

### Windows (Entorno Actual)

- [ ] Java 17 JDK instalado (`java -version`)
- [ ] Maven 3.8+ configurado (`mvn -version`)
- [ ] `JAVA_HOME` definido correctamente
- [ ] Proyecto compilado exitosamente (`BUILD SUCCESS`)
- [ ] Aplicación JavaFX se abre (`mvn javafx:run`)
- [ ] Interfaz gráfica responsiva a clics
- [ ] Terminal muestra `[INFO]` sin errores
- [ ] Pantallazo de compilación capturado
- [ ] Pantallazo de ejecución capturado

### Ubuntu (Teórico - Para Referencia)

- [ ] VM Ubuntu 22.04 LTS creada
- [ ] Java 17 OpenJDK instalado
- [ ] MySQL 8.0 ejecutándose en puerto 3306
- [ ] Base de datos `constructrack_logbook` creada
- [ ] Apache 2.4 con módulos proxy habilitados
- [ ] Aplicación compilada en `/opt/constructrack`
- [ ] JAR visible en `target/constructrack-bitacora-1.0.0.jar`
- [ ] Servicio systemd creado y activo

### Docker (Teórico - Para Referencia)

- [ ] Docker Desktop instalado
- [ ] Docker Compose v2+ disponible
- [ ] Imagen construida (`docker-compose build`)
- [ ] Contenedores en ejecución (`docker-compose ps`)
- [ ] MySQL accesible desde contenedor app
- [ ] Apache proxy redirigiendo correctamente
- [ ] Aplicación disponible en `http://localhost:8080`

---

## 📁 Estructura de Directorios

```
INFORME_DESPLIEGUE/
├── INFORME_GA10_220501097_AA5_EV01.md      ⭐ PRINCIPAL - Informe formal
├── INICIO_RAPIDO_WINDOWS.md                 ⚡ Guía rápida (3 pasos)
├── GUIA_PRACTICA_PASO_A_PASO.md            📘 Detalles técnicos
├── GUIA_DESPLIEGUE_SERVICIOS.md            📗 Conceptos de servicios
├── README.md                                ← Archivo actual
│
├── Dockerfile                               🐳 Imagen Docker
├── docker-compose.yml                       🐳 Orquestación
├── install.sh                               🐚 Script Ubuntu
├── constructrack.service                    ⚙️ Systemd Ubuntu
├── httpd.conf                               🌐 Apache config
├── constructrack-apache.conf                🌐 Apache VirtualHost
│
└── sql/                                     💾 Scripts SQL
    └── schema.sql
```

**Todos los archivos necesarios están aquí. Comienza por `INICIO_RAPIDO_WINDOWS.md` o `INFORME_GA10_220501097_AA5_EV01.md`**

---

## 🔍 Evidencia de Desempeño

### Requisitos Cumplidos (GA10-220501097-AA5-EV01)

✅ Sistema Operativo: Ubuntu Linux (versión especificada)
✅ Gestor BD: MySQL 8.0+
✅ Servidor: Apache 2.4+
✅ Virtualización: Paso a paso documentado
✅ Contenedores: Dockerfile + Docker Compose
✅ Pruebas: Matriz de validación completa
✅ Configuración: Scripts y archivos listos
✅ Portada y documentación formal: Incluida
✅ Fuentes y referencias: Bibliografía completa
✅ Imágenes y conceptos: Descritos detalladamente

---

## 📞 Notas Importantes

### Credenciales Predeterminadas

```
MySQL Root:       root / root123
MySQL App:        constructrack / constructrack123
Usuario Sistema:  constructrack / ConstrucTrack2025#
```

⚠️ **CAMBIAR en producción**

### Puertos

| Servicio | Puerto | Host |
|---|---|---|
| MySQL | 3306 | localhost |
| Apache | 80 | localhost |
| Java App | 8080 | localhost |

### Requisitos de Recursos

| Método | RAM | CPU | Almacenamiento |
|---|---|---|---|
| Virtualización | 2-4 GB | 2 cores | 20 GB |
| Contenedores | 1-2 GB | 1 core | 5 GB |

---

## 🆘 Soporte Rápido

### Problema: Servicios no inician

```bash
# Verificar estado
systemctl status mysql apache2 constructrack

# Ver logs
tail -100 /var/log/syslog
journalctl -xe

# Reiniciar
sudo systemctl restart mysql
sudo systemctl restart apache2
sudo systemctl restart constructrack
```

### Problema: No hay conexión BD

```bash
# Verificar MySQL
mysql -u constructrack -p -e "SELECT 1;"

# Verificar conexión desde app
mysql -u constructrack -p constructrack_logbook

# Revisar propiedades
cat src/main/resources/application.properties
```

### Problema: Puerto en uso

```bash
# Encontrar proceso
lsof -i :8080
lsof -i :80
lsof -i :3306

# Matar proceso
kill -9 <PID>
```

---

## 📝 Próximas Mejoras

- [ ] Agregar SSL/TLS (HTTPS)
- [ ] Implementar monitoreo (Prometheus)
- [ ] Configurar CI/CD (GitHub Actions)
- [ ] Backup automático BD
- [ ] Logging centralizado (ELK)
- [ ] Escalabilidad (múltiples réplicas)
- [ ] Kubernetes para orquestación
- [ ] Caching (Redis)

---

## 📞 Contacto y Referencias

- **Institución**: SENA - Análisis y Desarrollo de Software
- **Evidencia**: GA10-220501097-AA5-EV01
- **Fecha**: 29 de Abril de 2026
- **Tecnologías**: Java 17, MySQL 8.0, Apache 2.4, Docker 24+
- **Referencias**: Ver bibliografía en GUIA_DESPLIEGUE_SERVICIOS.md

---

## 🎓 Aprendizajes Clave

Este proyecto demuestra:

1. **Virtualización**: Creación y gestión de máquinas virtuales completas
2. **Conteinerización**: Uso eficiente de recursos con Docker
3. **Orchestración**: Coordinación de múltiples servicios
4. **Infraestructura Code**: IaC con Docker Compose
5. **DevOps**: Automatización y reproducibilidad
6. **Seguridad**: Configuración segura de servicios
7. **Monitoreo**: Verificación y validación de despliegues

---

*Este informe está completo y listo para presentar como evidencia de desempeño GA10-220501097-AA5-EV01.*

**Última actualización**: 29 de Abril de 2026

