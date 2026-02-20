# ConstrucTrack LogBook (Guia rapida)

Guia corta para que cualquiera pueda ejecutar la app sin saber mucho de Java.

## Que es
- Aplicacion de escritorio JavaFX para gestionar proyectos y bitacoras de obra.
- Base de datos y esquema: `src/main/resources/sql/schema.sql`

## Requisitos
- Java 17 (JDK). Si no lo tienes, instala Azul/Zulu u Oracle JDK 17.
- Maven 3.8+ (si no lo tienes, instala Maven y agrega `mvn` al PATH).
- Windows: la app ya incluye JavaFX via Maven, no necesitas instalarlo aparte.

## Como correr en modo rapido (dev)
1. Abre una terminal en la carpeta del proyecto (`1. ConstrucTrack LogBook`).
2. Ejecuta: `mvn javafx:run`
3. Se abre la ventana de Login. Usa las credenciales que tengas configuradas en la BD.

## Como construir un JAR
1. `mvn clean package`
2. Salida: `target/constructrack-logbook-1.0-SNAPSHOT.jar`
	 - Para ejecutarlo: `java -jar target/constructrack-logbook-1.0-SNAPSHOT.jar`
	 - Nota: si tu entorno no trae JavaFX, podrías necesitar jlink/jpackage; en dev, `mvn javafx:run` es suficiente.

## Configuracion de BD
- Script: `src/main/resources/sql/schema.sql`
- Config: `src/main/resources/application.properties`
	- Ajusta URL, usuario, clave segun tu BD local.

## Estructura clave
- Vistas (FXML): `src/main/resources/view/`
- Estilos: `src/main/resources/styles.css`
- Imagenes: `src/main/resources/images/`
- Controladores: `src/main/java/com/constructrack/bitacora/controller/`
- DAOs/Modelo: `src/main/java/com/constructrack/bitacora/dao|model/`

## Documentacion
- Requerimientos y acta: `docs/requerimientos.md`, `docs/acta-aprobacion.md`
- Modulos y flujos: `docs/modulos.md`
- Pruebas: `docs/pruebas.md`
- Despliegue/artefactos: `docs/despliegue.md`, `docs/ejecutables.md`

## Problemas tipicos
- "No se encuentra Java": instala JDK 17 y reinicia la terminal.
- "No se encuentra mvn": instala Maven y revisa la variable PATH.
- Error de conexion BD: revisa `application.properties` y que la BD este levantada.
- Ventana sin estilos: asegura que `styles.css` exista y que `ViewLoader` lo carga (ya viene configurado).

## Credenciales de prueba
- Ajusta en tu BD de pruebas o inserta un usuario en la tabla `usuarios` con password hasheado (ver `PasswordUtil`).

---
Si algo no corre, comparte el mensaje de error exacto y el comando que usaste.
