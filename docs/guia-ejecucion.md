# Guia de ejecucion (paso a paso)

Pensada para alguien sin experiencia técnica.

## 1. Instalar lo basico
- Java 17 (JDK). Si no lo tienes, descarga e instala Azul/Zulu u Oracle JDK 17.
- Maven 3.8+. Si no lo tienes, instala Maven y verifica con `mvn -v`.

## 2. Descargar el proyecto
- Si usas Git: `git clone https://github.com/lrossas9/ConstrucTrack-LogBook.git`
- O descarga el ZIP desde GitHub y descomprime.

## 3. Abrir la carpeta del proyecto
- Carpeta: `1. ConstrucTrack LogBook`
- Asegura que dentro veas `pom.xml`, `src/`, `docs/`.

## 4. Ejecutar en modo rapido (desarrollo)
- En la terminal (dentro de la carpeta del proyecto):
  ```
  mvn javafx:run
  ```
- Se abre la ventana de Login.

## 5. Configurar la base de datos (si aplica)
- Script SQL: `src/main/resources/sql/schema.sql`
- Configuracion: `src/main/resources/application.properties`
  - Ajusta URL, usuario, clave segun tu BD.

## 6. Construir un JAR (opcional)
- Comando: `mvn clean package`
- Archivo generado: `target/constructrack-logbook-1.0-SNAPSHOT.jar`
- Para ejecutarlo: `java -jar target/constructrack-logbook-1.0-SNAPSHOT.jar`
  - Nota: si tu entorno no trae JavaFX runtime, usa `mvn javafx:run` o prepara un jlink/jpackage.

## 7. Estructura rapida
- Vistas (pantallas): `src/main/resources/view/`
- Estilos: `src/main/resources/styles.css`
- Imagenes: `src/main/resources/images/`
- Controladores: `src/main/java/com/constructrack/bitacora/controller/`
- DAO/Modelo: `src/main/java/com/constructrack/bitacora/dao|model/`
- Documentacion: `docs/`

## 8. Problemas frecuentes
- "No se reconoce mvn": instala Maven y revisa PATH.
- "Java no se encuentra": instala JDK 17 y reinicia la terminal.
- Error de BD: revisa `application.properties` y que la BD este arriba.
- Sin estilos: confirma que `styles.css` existe y que `ViewLoader` lo carga (ya configurado).

## 9. Credenciales de prueba
- Inserta un usuario en la tabla `usuarios` (usa `PasswordUtil` para hashear).
- O ajusta tus credenciales de prueba en la BD.

## 10. Necesitas ayuda
- Copia el mensaje de error completo y el comando que ejecutaste; con eso es mas facil ayudarte.
