# Requerimientos del Sistema

## Funcionales
- [x] RF1: Autenticacion y gestion de usuarios. Cubierto en plan de pruebas (CP-login-valido, CP-login-invalido, CP-roles-acceso, CP-logout).
- [x] RF2: Gestion de proyectos (crear, editar, eliminar, listar, asignar estado). Cubierto en plan de pruebas (CP-proyecto-crear/editar/eliminar/listar-filtrar).
- [x] RF3: Bitacora diaria por proyecto (crear, editar, eliminar, listar, exportar PDF). Cubierto en plan de pruebas (CP-bitacora-crear/editar/eliminar/listar-filtrar, CP-bitacora-export-pdf).
- [x] RF4: Gestion de evidencias/archivos asociados. Cubierto en plan de pruebas (CP-evidencia-subir/ver/eliminar).
- [x] RF5: Reportes y metricas clave. Cubierto en plan de pruebas (CP-reportes-generar, CP-reportes-filtrar-exportar).

## No funcionales
- [x] RNF1: Seguridad de credenciales (hash de passwords). Cubierto en plan de pruebas (CP-password-hash-verificacion, CP-login-no-texto-plano).
- [x] RNF2: Rendimiento (respuesta < 2s en consultas tipicas). Cubierto en plan de pruebas (CP-consulta-proyectos-tiempo, CP-bitacora-lista-tiempo).
- [x] RNF3: Disponibilidad (segun SLA acordado). Cubierto en plan de pruebas (CP-reintentos-caida-BD, CP-smoke-post-deploy).
- [x] RNF4: Portabilidad (Java 17+, JavaFX). Cubierto en plan de pruebas (CP-ejecucion-win, CP-ejecucion-linux).

## Reglas y consideraciones
- [x] Accesos por rol (admin/usuario). Cubierto en plan de pruebas (CP-roles-acceso, CP-rol-admin-vs-usuario).
- [x] Validaciones basicas de formularios. Cubierto en plan de pruebas (CP-form-validaciones).
- [x] Integridad referencial en BD (proyecto-bitacora-usuario). Cubierto en plan de pruebas (CP-integridad-proyecto-bitacoras, CP-integridad-usuario-bitacoras).

## Estado de aprobacion
- [ ] Revisado por cliente/PO
- [ ] Aprobado (fecha y firma)

<!-- Adjunta actas o minutas si aplica. -->
