# Documentacion por modulo

## Login
- Controlador: LoginController
- Vista: view/LoginView.fxml
- Entrada: usuario, password
- Salida: sesion iniciada / error
- Notas: valida contra UsuarioDAO y PasswordUtil

## Dashboard (Proyectos)
- Controlador: DashboardController
- Vista: view/DashboardView.fxml
- Entrada: usuario autenticado
- Salida: lista de proyectos, acciones CRUD, acceso a bitacora
- Notas: usa ProyectoDAO; abre BitacoraListView via ViewLoader

## Bitacora
- Controladores: BitacoraListController, BitacoraFormController
- Vistas: view/BitacoraListView.fxml, view/BitacoraFormView.fxml
- Entrada: proyecto seleccionado, datos de bitacora
- Salida: registros de bitacora, export PDF
- Notas: PdfExporter, BitacoraDAO

## Usuarios / Registro
- Controlador: RegistroController
- Vista: view/RegistroView.fxml
- Entrada: datos de usuario
- Salida: usuario creado
- Notas: PasswordUtil hash

## Proyectos
- Controlador: ProyectoFormController
- Vista: view/ProyectoFormView.fxml
- Entrada: datos de proyecto
- Salida: proyecto creado/actualizado

## Utilidades
- SessionManager: maneja usuario en sesion
- ViewLoader: carga modales y aplica stylesheet
- AlertUtil: dialogs
- PdfExporter: exporta bitacoras

## Datos (DAO)
- DAO: UsuarioDAO, ProyectoDAO, BitacoraDAO
- BD: ver resources/sql/schema.sql
