cd "C:\Users\57311\Documents\LAURA 2025 1\ANALISIS Y DESARROLLO DE SOFTWARE. (3070308)\PROYECTO\Constructrack API Constructrack T\javafx-bitacora"-- Crear base de datos
CREATE DATABASE IF NOT EXISTS constructrack_logbook CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE constructrack_logbook;

-- Crear usuario de aplicación (ejecutar como root, contraseña root: 0000)
CREATE USER IF NOT EXISTS 'constructrack'@'%' IDENTIFIED BY 'constructrack123';
GRANT ALL PRIVILEGES ON constructrack_logbook.* TO 'constructrack'@'%';
FLUSH PRIVILEGES;

-- Tabla usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabla proyectos
CREATE TABLE IF NOT EXISTS proyectos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    ubicacion VARCHAR(200) NOT NULL,
    constructora VARCHAR(200),
    interventoria VARCHAR(200),
    contrato VARCHAR(200),
    fecha_inicio DATE,
    fecha_fin DATE,
    estado VARCHAR(100) NOT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_proyecto_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE
);
CREATE INDEX idx_proyectos_usuario ON proyectos(id_usuario);

-- Tabla bitacoras
CREATE TABLE IF NOT EXISTS bitacoras (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_proyecto BIGINT NOT NULL,
    fecha DATE NOT NULL,
    clima VARCHAR(50),
    jornada VARCHAR(50),
    actividades TEXT,
    personal TEXT,
    equipos TEXT,
    materiales TEXT,
    porcentaje_avance INT NOT NULL DEFAULT 0,
    observaciones TEXT,
    firma VARCHAR(200),
    ruta_imagen VARCHAR(500),
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_bitacora_proyecto FOREIGN KEY (id_proyecto) REFERENCES proyectos(id) ON DELETE CASCADE
);
CREATE INDEX idx_bitacoras_proyecto_fecha ON bitacoras(id_proyecto, fecha DESC);

-- Semilla admin (si no existe)
INSERT INTO usuarios (nombre, correo, contrasena)
SELECT 'Laura Rosas', 'lrosass9@soy.sena.edu.co', '$2a$10$uTJE6fs/3Z5eZMhr4WO2IeSEGyYCFNplm2yDRPfJGSxo.JmV7mJue'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE correo = 'lrosass9@soy.sena.edu.co');
