# Manual tecnico

## Arquitectura y stack
- Tipo: Aplicacion de escritorio JavaFX (MVC ligero con FXML + controladores).
- Lenguaje: Java 17.
- Build: Maven.
- UI: JavaFX (FXML + CSS).
- Datos: DAOs sencillos; esquema en `src/main/resources/sql/schema.sql`.
- Export: PDF via `PdfExporter`.

## Estructura del proyecto
- UI (FXML): `src/main/resources/view/`
- Estilos: `src/main/resources/styles.css`
- Imagenes: `src/main/resources/images/`
- Controladores: `src/main/java/com/constructrack/bitacora/controller/`
- DAO: `src/main/java/com/constructrack/bitacora/dao/`
- Modelos: `src/main/java/com/constructrack/bitacora/model/`
- Utilidades: `src/main/java/com/constructrack/bitacora/util/`
- Config/BD: `src/main/resources/application.properties`
- SQL: `src/main/resources/sql/schema.sql`
- Docs: `docs/`

## Flujo principal
- Inicio en `MainApp` -> `ViewLoader.show(LoginView.fxml)`.
- `LoginController` autentica via `UsuarioDAO` y `PasswordUtil`, guarda usuario en `SessionManager` y abre `DashboardView`.
- `DashboardController` lista proyectos con `ProyectoDAO`; abre bitacoras (`BitacoraListView`) o formularios (`ProyectoFormView`).
- `BitacoraListController` gestiona CRUD y export PDF (`PdfExporter`).

## Configuracion
- Archivo: `src/main/resources/application.properties`
  - Ajustar URL, usuario y clave de la BD.
- Esquema: ejecutar `src/main/resources/sql/schema.sql` en la BD destino.

## Build y ejecucion
- Dev (recomendado): `mvn javafx:run`
- Build JAR: `mvn clean package`
  - Salida: `target/constructrack-logbook-1.0-SNAPSHOT.jar`
  - Ejecucion: `java -jar target/constructrack-logbook-1.0-SNAPSHOT.jar` (requiere runtime JavaFX si no se usa jlink/jpackage).

## Paquetes y clases clave
- `controller`:
  - `LoginController`: login y apertura de dashboard.
  - `DashboardController`: CRUD proyectos, navegar a bitacoras.
  - `BitacoraListController` / `BitacoraFormController`: CRUD bitacoras, export PDF.
  - `ProyectoFormController`, `RegistroController`.
- `dao`: `UsuarioDAO`, `ProyectoDAO`, `BitacoraDAO` (acceso directo a BD).
- `model`: `Usuario`, `Proyecto`, `Bitacora`.
- `util`:
  - `ViewLoader`: carga vistas y aplica stylesheet.
  - `SessionManager`: sesion de usuario.
  - `PasswordUtil`: hashing de contrasenas.
  - `PdfExporter`: exportacion PDF de bitacora.
  - `AlertUtil`: dialogos.

## Estilos y recursos
- CSS global: `styles.css` (incluye fondo, botones, tablas, sidebar).
- Imagen de fondo: `images/constructrack.jpg` (referenciada en `styles.css`).
- Fuentes: usa las del sistema (Segoe UI por defecto).

## Pruebas
- Ver `docs/pruebas.md` para registrar casos, resultados y ambientes.
- Tests automatizados: no incluidos; se recomienda agregar JUnit para DAOs y utilidades.

## Despliegue / Artefactos
- Local: `mvn javafx:run`.
- Artefacto: `target/constructrack-logbook-1.0-SNAPSHOT.jar` (ver `docs/ejecutables.md`).
- URLs de despliegue: (pendiente; documentar en `docs/despliegue.md`).

## Credenciales
- Insertar usuario en tabla `usuarios`; hashear password con `PasswordUtil`.

## Problemas tipicos
- Falta JavaFX al ejecutar JAR: usar `mvn javafx:run` o empacar con jlink/jpackage.
- Error de conexion BD: revisar `application.properties` y que la BD este levantada.
- Estilos no aplican: verificar que `ViewLoader` carga `styles.css` (ya configurado) y que el recurso exista.

## Mantenimiento rapido
- Para modificar vistas: editar FXML en `view/` y ajustar controladores en `controller/`.
- Para nuevos modulos: crear FXML + controlador, registrar navegacion en el controlador que los abra usando `ViewLoader`.
