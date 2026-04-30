# Plan y evidencia de pruebas

## 2. Introduccion
Este plan define como se verificara que el sistema cumple los requerimientos funcionales y no funcionales definidos. Incluye estrategias, criterios de aceptacion, entornos y artefactos de seguimiento.

## 3. Analisis de requerimientos
- Requerimientos funcionales a cubrir: RF1 (autenticacion y gestion usuarios), RF2 (gestion proyectos), RF3 (bitacora diaria y exportar PDF), RF4 (evidencias/archivos), RF5 (reportes y metricas).
- Requerimientos no funcionales a cubrir: RNF1 (hash passwords), RNF2 (respuesta < 2s en consultas tipicas), RNF3 (disponibilidad segun SLA), RNF4 (portabilidad Java 17+, JavaFX).
- Reglas: roles admin/usuario, validaciones basicas de formularios, integridad referencial proyecto-bitacora-usuario.

## 4. Funcionalidades existentes a regredir
- Login y manejo de sesion/logout.
- CRUD de proyectos.
- CRUD de bitacoras.
- Exportacion a PDF de bitacoras.
- Carga y consulta de evidencias/archivos (si aplica en version actual).

## 5. Funcionalidades nuevas a validar
- Nuevos reportes/metricas (RF5).
- Nuevos flujos de recuperacion de contrasena (si se implementan).
- Integraciones o nuevos modulos UI añadidos en la iteracion.

## 6. Estrategia y criterios de prueba
- Tipos: pruebas funcionales, integracion, sistema, aceptacion.
- Criterios de aceptacion: resultado obtenido = esperado, sin errores criticos, cumplimiento de requerimientos y reglas de rol/validaciones.
- Priorizacion: alta para autenticacion, roles, CRUD principales y exportacion; media para reportes; alta para seguridad de contrasenas y tiempo de respuesta.

## 7. Entornos de trabajo
- Hardware: CPU i5 equivalente, 8 GB RAM, internet.
- Software: SO Windows/Linux, JDK 17+, JavaFX, Maven, base de datos MySQL/PostgreSQL/SQL Server (instancia de pruebas separada o datos de prueba), navegador (para revisar PDF exportado).

## 8. Metodologia y planificacion
1. Analisis de requerimientos.
2. Diseno de casos de prueba.
3. Preparacion de datos y entorno.
4. Ejecucion de pruebas.
5. Registro de resultados y evidencias.
6. Reporte y priorizacion de defectos.
7. Verificacion de correcciones.

Cronograma referencial
- Planeacion: 1 dia
- Diseno casos: 2 dias
- Ejecucion: 3 dias
- Reporte: 1 dia

## 9. Artefactos de prueba
- Matriz de cobertura requerimientos-pruebas (abajo).
- Casos de prueba (CPxx) con ID, funcionalidad, pasos, datos, resultado esperado/obtenido, estado.
- Registro de incidencias con severidad/prioridad.
- Bitacora de pruebas con observaciones y entorno usado.

## 10. Herramientas
- JUnit para pruebas unitarias/integracion.
- Selenium o TestFX para flujos UI JavaFX (si se automatiza).
- Postman para APIs (si aplica).
- Jira/Trello/Sheets para gestion de incidencias y casos.
- PDF viewer para validar exportes.

## 11. Riesgos y contingencias
- Errores criticos en produccion: correccion inmediata y re-ejecucion de regresion critica.
- Fallas de entorno de pruebas: uso de entorno alterno o restaurar respaldo de BD.
- Falta de tiempo: priorizar casos criticos (login, roles, CRUD, PDF, seguridad).

## 12. Matriz de cobertura
| Req | Pruebas claves | Estado |
| --- | --- | --- |
| RF1 Autenticacion/usuarios | CP-login-valido, CP-login-invalido, CP-roles-acceso, CP-logout | Pendiente |
| RF2 Proyectos | CP-proyecto-crear/editar/eliminar/listar | Pendiente |
| RF3 Bitacora + PDF | CP-bitacora-crear/editar/eliminar/listar, CP-bitacora-export-pdf | Pendiente |
| RF4 Evidencias/archivos | CP-evidencia-subir/ver/eliminar | Pendiente |
| RF5 Reportes/metricas | CP-reportes-generar/filtrar | Pendiente |
| RNF1 Hash passwords | CP-password-hash-verificacion (unit), CP-login-no-texto-plano | Pendiente |
| RNF2 Rendimiento | CP-consulta-proyectos-tiempo, CP-bitacora-lista-tiempo (<2s datos tipicos) | Pendiente |
| RNF3 Disponibilidad | CP-reintentos/caida-BD (simulada), chequeos smoke post-deploy | Pendiente |
| RNF4 Portabilidad | CP-ejecucion-win, CP-ejecucion-linux (smoke) | Pendiente |
| Reglas roles/validaciones | CP-form-validaciones, CP-rol-admin-vs-usuario | Pendiente |
| Integridad referencial | CP-eliminar-proyecto-sin/ con-bitacoras, CP-eliminar-usuario-asociado | Pendiente |

## 12.1 Casos de prueba detallados (RF1-RF5, RNF, reglas)
- CP-login-valido (RF1): pre: usuario activo con password hash. Pasos: abrir login, ingresar credenciales validas, enviar. Esperado: acceso permitido, crea sesion y redirige a dashboard.
- CP-login-invalido (RF1): usar usuario o password incorrectos. Esperado: mensaje de error sin crear sesion.
- CP-roles-acceso (RF1): con usuario admin y usuario regular, intentar acceder a pantalla de administracion. Esperado: admin accede, usuario ve mensaje de permiso denegado.
- CP-logout (RF1): estando autenticado, ejecutar logout. Esperado: sesion invalidada, redirige a login.

- CP-proyecto-crear (RF2): pre: admin autenticado. Pasos: abrir formulario proyecto, ingresar nombre/estado/descripcion, guardar. Esperado: registro creado y visible en listado.
- CP-proyecto-editar (RF2): editar proyecto existente cambiando estado. Esperado: cambios persistidos y visibles al listar.
- CP-proyecto-eliminar (RF2): eliminar proyecto sin bitacoras asociadas. Esperado: borrado exitoso; si hay bitacoras, debe bloquearse y mostrar mensaje.
- CP-proyecto-listar-filtrar (RF2): listar y filtrar por estado. Esperado: resultados coinciden con filtros y paginan/ordenan segun UI.

- CP-bitacora-crear (RF3): pre: proyecto existente. Pasos: abrir formulario bitacora, ingresar fecha, descripcion, horas, guardar. Esperado: registro asociado al proyecto.
- CP-bitacora-editar (RF3): modificar descripcion/horas. Esperado: cambios guardados.
- CP-bitacora-eliminar (RF3): borrar bitacora. Esperado: eliminacion confirmada y no aparece en listado.
- CP-bitacora-listar-filtrar (RF3): listar por rango de fechas/proyecto. Esperado: datos correctos y en <2s con dataset tipico.
- CP-bitacora-export-pdf (RF3): seleccionar bitacora/listado y exportar PDF. Esperado: archivo generado, contenido correcto (datos y formatos) y descargable/abrible.

- CP-evidencia-subir (RF4): adjuntar archivo permitido a una bitacora. Esperado: archivo almacenado, referencia visible y consultable.
- CP-evidencia-ver (RF4): abrir/descargar archivo cargado. Esperado: se abre/descarga sin corrupcion.
- CP-evidencia-eliminar (RF4): eliminar evidencia de bitacora. Esperado: archivo y referencia removidos; bitacora permanece.

- CP-reportes-generar (RF5): generar reporte/metricas con filtros (fecha, proyecto). Esperado: datos correctos y agregados consistentes con BD.
- CP-reportes-filtrar-exportar (RF5): aplicar filtros, actualizar vista y exportar (si aplica). Esperado: resultados coinciden y exporte respeta filtros.

- CP-password-hash-verificacion (RNF1): inspeccionar almacenamiento y comprobar que contrasenas se guardan con hash y sal. Esperado: no texto plano.
- CP-login-no-texto-plano (RNF1): verificar que flujo de login no expone password en logs/errores. Esperado: sin filtraciones.

- CP-consulta-proyectos-tiempo (RNF2): medir listar proyectos con dataset tipico. Esperado: <2s.
- CP-bitacora-lista-tiempo (RNF2): medir listar bitacoras con filtros tipicos. Esperado: <2s.

- CP-reintentos-caida-BD (RNF3): simular caida de BD y reintentar. Esperado: manejo controlado de error, mensaje claro y recuperacion al restablecer.
- CP-smoke-post-deploy (RNF3): checklist rapido tras despliegue (login, listar proyectos, exportar PDF). Esperado: pasa sin bloqueantes.

- CP-ejecucion-win (RNF4): ejecutar app en Windows con JDK 17+, JavaFX. Esperado: arranca y flujos basicos OK.
- CP-ejecucion-linux (RNF4): ejecutar app en Linux con JDK 17+, JavaFX. Esperado: arranca y flujos basicos OK.

- CP-form-validaciones (Reglas): dejar campos obligatorios vacios o datos invalidos en formularios (login, proyecto, bitacora). Esperado: mensajes de validacion y bloqueo de guardado.
- CP-rol-admin-vs-usuario (Reglas): confirmar que usuario no admin no puede crear/editar/eliminar proyectos/bitacoras si asi se define. Esperado: restricciones aplicadas.
- CP-integridad-proyecto-bitacoras (Integridad): intentar eliminar proyecto con bitacoras asociadas. Esperado: bloqueo y mensaje.
- CP-integridad-usuario-bitacoras (Integridad): intentar eliminar usuario con bitacoras asignadas. Esperado: bloqueo y mensaje.

## 13. Ejecucion y evidencia
| Caso | Responsable | Fecha | Estado | Evidencia |
| --- | --- | --- | --- | --- |
| CP-login-valido | QA | 2026-03-12 | Pendiente | |
| CP-login-invalido | QA | 2026-03-12 | Pendiente | |
| CP-roles-acceso | QA | 2026-03-12 | Pendiente | |
| CP-logout | QA | 2026-03-12 | Pendiente | |
| CP-proyecto-crear | QA | 2026-03-12 | Pendiente | |
| CP-proyecto-editar | QA | 2026-03-12 | Pendiente | |
| CP-proyecto-eliminar | QA | 2026-03-12 | Pendiente | |
| CP-proyecto-listar-filtrar | QA | 2026-03-12 | Pendiente | |
| CP-bitacora-crear | QA | 2026-03-13 | Pendiente | |
| CP-bitacora-editar | QA | 2026-03-13 | Pendiente | |
| CP-bitacora-eliminar | QA | 2026-03-13 | Pendiente | |
| CP-bitacora-listar-filtrar | QA | 2026-03-13 | Pendiente | |
| CP-bitacora-export-pdf | QA | 2026-03-13 | Pendiente | |
| CP-evidencia-subir | QA | 2026-03-13 | Pendiente | |
| CP-evidencia-ver | QA | 2026-03-13 | Pendiente | |
| CP-evidencia-eliminar | QA | 2026-03-13 | Pendiente | |
| CP-reportes-generar | QA | 2026-03-14 | Pendiente | |
| CP-reportes-filtrar-exportar | QA | 2026-03-14 | Pendiente | |
| CP-password-hash-verificacion | QA | 2026-03-14 | Pendiente | |
| CP-login-no-texto-plano | QA | 2026-03-14 | Pendiente | |
| CP-consulta-proyectos-tiempo | QA | 2026-03-14 | Pendiente | |
| CP-bitacora-lista-tiempo | QA | 2026-03-14 | Pendiente | |
| CP-reintentos-caida-BD | QA | 2026-03-15 | Pendiente | |
| CP-smoke-post-deploy | QA | 2026-03-15 | Pendiente | |
| CP-ejecucion-win | QA | 2026-03-15 | Pendiente | |
| CP-ejecucion-linux | QA | 2026-03-15 | Pendiente | |
| CP-form-validaciones | QA | 2026-03-12 | Pendiente | |
| CP-rol-admin-vs-usuario | QA | 2026-03-12 | Pendiente | |
| CP-integridad-proyecto-bitacoras | QA | 2026-03-13 | Pendiente | |
| CP-integridad-usuario-bitacoras | QA | 2026-03-13 | Pendiente | |

Leyenda de estado: Pendiente, En ejecucion, Aprobado, Rechazado.

Leyenda de estado: Pendiente, En ejecucion, Aprobado, Rechazado.

## 14. Configuracion del entorno
- Base de datos de pruebas: nombre, usuario, puerto, datos de prueba cargados via schema.sql.
- Variables de entorno/propiedades: archivo application.properties apuntando a BD de pruebas.
- Servidor/host: (si aplica) indicar puerto y ruta.

## 15. Observaciones
- |
