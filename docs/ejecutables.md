# Ejecutables

## Como generar
- `mvn clean package`
- Salida: `target/constructrack-logbook-1.0-SNAPSHOT.jar`

## Como entregar
- Subir el JAR generado a una carpeta `entrega/` o a un release en GitHub.
- Incluir instrucciones de ejecucion (JavaFX requiere runtime; considerar jlink o jpackage si se quiere instalador).

## Requisitos de ejecucion
- JDK/JRE 17+
- JavaFX runtime (si no se usa jlink/jpackage)
