#!/bin/bash

##########################################################################
# Script de Instalación Automatizada
# ConstrucTrack LogBook en Ubuntu Server
# GA10-220501097-AA5-EV01
##########################################################################

set -e  # Salir si hay error

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Funciones auxiliares
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_step() {
    echo -e "\n${YELLOW}════════════════════════════════════════${NC}"
    echo -e "${YELLOW}$1${NC}"
    echo -e "${YELLOW}════════════════════════════════════════${NC}\n"
}

# Verificar si se ejecuta como root
if [[ $EUID -ne 0 ]]; then
   print_error "Este script debe ejecutarse con privilegios sudo"
   exit 1
fi

##########################################################################
# PASO 1: Actualizar Sistema
##########################################################################
print_step "PASO 1: Actualizando Sistema Operativo"

apt update
apt upgrade -y

print_info "Sistema actualizado ✓"

##########################################################################
# PASO 2: Instalar Dependencias Básicas
##########################################################################
print_step "PASO 2: Instalando Dependencias Básicas"

apt install -y \
    curl \
    wget \
    git \
    vim \
    nano \
    net-tools \
    htop \
    software-properties-common \
    apt-transport-https \
    ca-certificates \
    gnupg \
    lsb-release

print_info "Dependencias instaladas ✓"

##########################################################################
# PASO 3: Instalar Java 17
##########################################################################
print_step "PASO 3: Instalando Java 17 LTS"

apt install -y openjdk-17-jdk

# Configurar JAVA_HOME
if ! grep -q "JAVA_HOME" /etc/profile; then
    echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> /etc/profile
    print_info "JAVA_HOME configurado ✓"
fi

java -version
print_info "Java 17 instalado ✓"

##########################################################################
# PASO 4: Instalar Maven
##########################################################################
print_step "PASO 4: Instalando Apache Maven"

apt install -y maven

mvn -version
print_info "Maven instalado ✓"

##########################################################################
# PASO 5: Instalar MySQL Server
##########################################################################
print_step "PASO 5: Instalando MySQL Server 8.0"

apt install -y mysql-server

# Iniciar servicio
systemctl start mysql
systemctl enable mysql

print_info "MySQL Server instalado e iniciado ✓"

# Ejecutar seguridad básica (non-interactive)
mysql -u root <<EOF
ALTER USER 'root'@'localhost' IDENTIFIED BY 'root123';
DELETE FROM mysql.user WHERE User='';
DELETE FROM mysql.user WHERE User='root' AND Host NOT IN ('localhost', '127.0.0.1', '::1');
DROP DATABASE IF EXISTS test;
DELETE FROM mysql.db WHERE Db='test' OR Db='test\\_%';
FLUSH PRIVILEGES;
EOF

print_info "Seguridad de MySQL configurada ✓"

##########################################################################
# PASO 6: Crear Base de Datos y Usuario
##########################################################################
print_step "PASO 6: Creando Base de Datos y Usuario"

mysql -u root -proot123 <<EOF
CREATE DATABASE IF NOT EXISTS constructrack_logbook;
CREATE USER IF NOT EXISTS 'constructrack'@'localhost' IDENTIFIED BY 'constructrack123';
GRANT ALL PRIVILEGES ON constructrack_logbook.* TO 'constructrack'@'localhost';
FLUSH PRIVILEGES;
EOF

print_info "Base de datos 'constructrack_logbook' creada ✓"
print_info "Usuario 'constructrack' creado ✓"

##########################################################################
# PASO 7: Instalar Apache 2
##########################################################################
print_step "PASO 7: Instalando Apache HTTP Server"

apt install -y apache2

# Habilitar módulos necesarios
a2enmod proxy
a2enmod proxy_http
a2enmod rewrite
a2enmod headers

# Iniciar Apache
systemctl start apache2
systemctl enable apache2

print_info "Apache 2 instalado e iniciado ✓"
print_info "Módulos proxy habilitados ✓"

##########################################################################
# PASO 8: Configurar Proxy Apache
##########################################################################
print_step "PASO 8: Configurando Apache como Proxy"

cat > /etc/apache2/sites-available/constructrack.conf <<'EOF'
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
EOF

# Habilitar sitio
a2ensite constructrack.conf

# Verificar configuración
apache2ctl configtest
systemctl reload apache2

print_info "Configuración de proxy creada ✓"
print_info "Sitio habilitado ✓"

##########################################################################
# PASO 9: Crear Usuario de Aplicación
##########################################################################
print_step "PASO 9: Creando Usuario de Aplicación"

if ! id "constructrack" &>/dev/null; then
    useradd -m -s /bin/bash constructrack
    echo "constructrack:ConstrucTrack2025#" | chpasswd
    print_info "Usuario 'constructrack' creado ✓"
else
    print_warning "Usuario 'constructrack' ya existe"
fi

##########################################################################
# PASO 10: Preparar Directorio de Aplicación
##########################################################################
print_step "PASO 10: Preparando Directorio de Aplicación"

mkdir -p /opt/constructrack
chown -R constructrack:constructrack /opt/constructrack

mkdir -p /var/log/constructrack
chown -R constructrack:constructrack /var/log/constructrack

print_info "Directorios creados en /opt/constructrack ✓"
print_info "Directorio de logs en /var/log/constructrack ✓"

##########################################################################
# RESUMEN FINAL
##########################################################################
print_step "✓ INSTALACIÓN COMPLETADA"

echo -e "\n${GREEN}Detalles de Configuración:${NC}\n"
echo "Sistema Operativo: $(lsb_release -ds)"
echo "Java: $(java -version 2>&1 | head -n 1)"
echo "Maven: $(mvn -version | head -n 1)"
echo "MySQL: $(mysql -u root -proot123 -e 'SELECT VERSION();' | tail -n 1)"
echo "Apache: $(apache2ctl -v | head -n 1)"

echo -e "\n${GREEN}Servicios en Ejecución:${NC}\n"
systemctl status mysql --no-pager | grep Active
systemctl status apache2 --no-pager | grep Active

echo -e "\n${GREEN}Próximos Pasos:${NC}\n"
echo "1. Transferir código fuente a /opt/constructrack"
echo "2. Compilar: cd /opt/constructrack && mvn clean package"
echo "3. Editar application.properties con valores correctos"
echo "4. Crear servicio systemd para ejecución automática"
echo "5. Iniciar aplicación: systemctl start constructrack"

echo -e "\n${GREEN}Credenciales de Acceso:${NC}\n"
echo "MySQL Root: root / root123"
echo "MySQL App: constructrack / constructrack123"
echo "Usuario Sistema: constructrack / ConstrucTrack2025#"

echo -e "\n${YELLOW}Nota:${NC} Cambia las contraseñas en producción\n"

print_info "Script finalizado exitosamente"
