package com.constructrack.bitacora.controller;

import com.constructrack.bitacora.dao.BitacoraDAO;
import com.constructrack.bitacora.dao.ProyectoDAO;
import com.constructrack.bitacora.dao.EvidenciaDAO;
import com.constructrack.bitacora.model.Bitacora;
import com.constructrack.bitacora.model.Proyecto;
import com.constructrack.bitacora.model.Usuario;
import com.constructrack.bitacora.model.Evidencia;
import com.constructrack.bitacora.util.AlertUtil;
import com.constructrack.bitacora.util.PdfExporter;
import com.constructrack.bitacora.util.SessionManager;
import com.constructrack.bitacora.util.ViewLoader;
import com.lowagie.text.DocumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DashboardController {
    @FXML
    private TableView<Proyecto> tablaProyectos;
    @FXML
    private TableColumn<Proyecto, String> colNombre;
    @FXML
    private TableColumn<Proyecto, String> colUbicacion;
    @FXML
    private TableColumn<Proyecto, String> colEstado;
    @FXML
    private Button reportesButton;
    @FXML
    private Label lblProyectosActivos;
    @FXML
    private Label lblReportesPendientes;

    private final BitacoraDAO bitacoraDAO = new BitacoraDAO();
    private final EvidenciaDAO evidenciaDAO = new EvidenciaDAO();
    private final ProyectoDAO proyectoDAO = new ProyectoDAO();
    private final ObservableList<Proyecto> proyectos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tablaProyectos.setItems(proyectos);
        tablaProyectos.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, n) -> actualizarContadorBitacoras());
        cargarProyectos();
        actualizarContadorBitacoras();
        actualizarMetricas();
    }

    @FXML
    public void handleNuevoProyecto() {
        try {
            abrirProyectoForm(null);
        } catch (RuntimeException e) {
            AlertUtil.error("Error", "No se pudo abrir el formulario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleEditarProyecto() {
        Proyecto seleccionado = tablaProyectos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertUtil.error("Seleccion requerida", "Elige un proyecto");
            return;
        }
        try {
            abrirProyectoForm(seleccionado);
        } catch (RuntimeException e) {
            AlertUtil.error("Error", "No se pudo abrir el formulario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleEliminarProyecto() {
        Proyecto seleccionado = tablaProyectos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertUtil.error("Seleccion requerida", "Elige un proyecto");
            return;
        }
        if (AlertUtil.confirm("Confirmar", "¿Eliminar proyecto y sus bitacoras?")) {
            proyectoDAO.eliminar(seleccionado.getId());
            cargarProyectos();
        }
    }

    @FXML
    public void handleVerBitacora() {
        Proyecto seleccionado = tablaProyectos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertUtil.error("Seleccion requerida", "Elige un proyecto");
            return;
        }
        try {
            ViewLoader.modal("/view/BitacoraListView.fxml", "Bitacora", loader -> {
                BitacoraListController controller = loader.getController();
                controller.setProyecto(seleccionado);
            });
        } catch (IOException e) {
            AlertUtil.error("Error", "No se pudo abrir bitácora: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSeguimiento() {
        // Reutiliza la vista de bitácoras como pantalla de seguimiento.
        handleVerBitacora();
    }

    @FXML
    public void handleReportes() {
        Proyecto seleccionado = tablaProyectos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertUtil.error("Seleccion requerida", "Elige un proyecto para ver reportes");
            return;
        }

        List<Bitacora> bitacoras = bitacoraDAO.listarPorProyecto(seleccionado.getId());
        mostrarPanelMetricas(seleccionado, bitacoras);
    }

    @FXML
    public void handleLogout() {
        SessionManager.clear();
        Stage stage = (Stage) tablaProyectos.getScene().getWindow();
        ViewLoader.show(stage, "/view/LoginView.fxml", "Login");
    }

    private void abrirProyectoForm(Proyecto proyecto) {
        try {
            ViewLoader.modal("/view/ProyectoFormView.fxml", proyecto == null ? "Nuevo Proyecto" : "Editar Proyecto",
                    loader -> {
                        ProyectoFormController controller = loader.getController();
                        controller.setProyecto(proyecto);
                        controller.setOnSaved(this::cargarProyectos);
                    });
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar el formulario", e);
        }
    }

    private void cargarProyectos() {
        Usuario u = SessionManager.getUsuario();
        if (u == null) {
            AlertUtil.error("Sesion", "No hay usuario autenticado");
            return;
        }
        List<Proyecto> lista = proyectoDAO.listarPorUsuario(u.getId());
        proyectos.setAll(lista);
        actualizarContadorBitacoras();
    }

    private void actualizarContadorBitacoras() {
        if (reportesButton == null) {
            return;
        }
        Proyecto seleccionado = tablaProyectos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            reportesButton.setText("Reportes (0)");
            actualizarMetricas();
            return;
        }
        int total = bitacoraDAO.listarPorProyecto(seleccionado.getId()).size();
        reportesButton.setText("Reportes (" + total + ")");
        actualizarMetricas();
    }

    private void actualizarMetricas() {
        if (lblProyectosActivos != null) {
            lblProyectosActivos.setText(String.valueOf(proyectos.size()));
        }
        Proyecto seleccionado = tablaProyectos != null ? tablaProyectos.getSelectionModel().getSelectedItem() : null;
        int totalBitacoras = 0;
        if (seleccionado != null) {
            totalBitacoras = bitacoraDAO.listarPorProyecto(seleccionado.getId()).size();
        }
        if (lblReportesPendientes != null) {
            lblReportesPendientes.setText(String.valueOf(totalBitacoras));
        }
    }

    private void mostrarPanelMetricas(Proyecto proyecto, List<Bitacora> bitacoras) {
        Stage owner = (Stage) tablaProyectos.getScene().getWindow();
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Reportes y métricas");

        int total = bitacoras.size();
        String ultimaFecha = bitacoras.stream()
                .map(Bitacora::getFecha)
                .filter(java.util.Objects::nonNull)
                .max(java.time.LocalDate::compareTo)
                .map(java.time.LocalDate::toString)
                .orElse("-");
        double avgAvance = bitacoras.stream()
                .map(Bitacora::getPorcentajeAvance)
                .filter(java.util.Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        Label lblProyecto = new Label("Proyecto: " + safe(proyecto.getNombre()));
        Label lblTotal = new Label("Bitácoras: " + total);
        Label lblUltima = new Label("Última fecha: " + ultimaFecha);
        Label lblAvance = new Label("Avance promedio: " + String.format("%.1f%%", avgAvance));

        Button btnExportar = new Button("Exportar PDF");
        btnExportar.setOnAction(e -> {
            exportarBitacoras(proyecto, bitacoras, dialog);
        });

        VBox content = new VBox(12, lblProyecto, lblTotal, lblUltima, lblAvance, btnExportar);
        content.setStyle("-fx-padding: 16;");

        dialog.setScene(new Scene(content));
        dialog.setWidth(320);
        dialog.setHeight(240);
        dialog.showAndWait();
    }

    @FXML
    public void handleRegistrarReporteRapido() {
        Proyecto seleccionado = tablaProyectos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertUtil.error("Seleccion requerida", "Elige un proyecto para registrar un reporte");
            return;
        }
        Bitacora draft = new Bitacora();
        draft.setIdProyecto(seleccionado.getId());
        draft.setFecha(LocalDate.now());
        draft.setJornada("Mañana");
        abrirBitacoraForm(seleccionado, draft, "Nuevo reporte diario");
    }

    @FXML
    public void handleSubirEvidencia() {
        Proyecto seleccionado = tablaProyectos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertUtil.error("Seleccion requerida", "Elige un proyecto para subir evidencia");
            return;
        }
        List<Bitacora> bitacoras = bitacoraDAO.listarPorProyecto(seleccionado.getId());
        if (bitacoras.isEmpty()) {
            AlertUtil.info("Sin bitacoras", "Primero crea una bitacora para adjuntar evidencias");
            return;
        }

        Bitacora elegido = elegirBitacora(bitacoras);
        if (elegido == null) {
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecciona evidencia");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagen", "*.png", "*.jpg", "*.jpeg"));
        java.io.File file = chooser.showOpenDialog(tablaProyectos.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path target = copiarEvidencia(file.toPath());
        Evidencia evidencia = new Evidencia();
        evidencia.setIdBitacora(elegido.getId());
        evidencia.setNombreArchivo(file.getName());
        evidencia.setRutaArchivo(target.toString());
        evidencia.setContentType(detectContentType(target));
        evidenciaDAO.guardar(evidencia);

        AlertUtil.info("Listo", "Evidencia guardada y vinculada a la bitacora");
        actualizarContadorBitacoras();
        actualizarMetricas();
    }

    private void abrirBitacoraForm(Proyecto proyecto, Bitacora bitacora, String titulo) {
        try {
            ViewLoader.modal("/view/BitacoraFormView.fxml", titulo, loader -> {
                BitacoraFormController controller = loader.getController();
                controller.setProyecto(proyecto);
                controller.setBitacora(bitacora);
                controller.setOnSaved(() -> {
                    actualizarContadorBitacoras();
                    actualizarMetricas();
                });
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void exportarBitacoras(Proyecto proyecto, List<Bitacora> bitacoras, Stage dialog) {
        if (bitacoras.isEmpty()) {
            AlertUtil.info("Sin datos", "El proyecto no tiene bitácoras para exportar");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Exportar bitácora a PDF");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        String safeName = proyecto.getNombre() == null ? "proyecto"
                : proyecto.getNombre().replaceAll("[^a-zA-Z0-9_-]", "_");
        chooser.setInitialFileName("bitacora-" + safeName + ".pdf");

        Stage stage = dialog != null ? dialog : (Stage) tablaProyectos.getScene().getWindow();
        java.io.File file = chooser.showSaveDialog(stage);
        if (file == null) {
            return; // Usuario canceló
        }

        try {
            PdfExporter.exportProyecto(proyecto, bitacoras, file.getAbsolutePath());
            AlertUtil.info("Listo", "PDF generado en:\n" + file.getAbsolutePath());
        } catch (IOException | DocumentException e) {
            AlertUtil.error("Error", "No se pudo generar el PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Bitacora elegirBitacora(List<Bitacora> bitacoras) {
        if (bitacoras.isEmpty()) {
            return null;
        }
        if (bitacoras.size() == 1) {
            return bitacoras.get(0);
        }
        List<String> labels = bitacoras.stream().map(this::formatBitacoraLabel).toList();
        ChoiceDialog<String> dialog = new ChoiceDialog<>(labels.get(0), labels);
        dialog.setTitle("Elegir bitacora");
        dialog.setHeaderText("Selecciona la bitacora a la que adjuntar evidencia");
        dialog.setContentText("Bitacora:");
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            return null;
        }
        int idx = labels.indexOf(result.get());
        if (idx < 0 || idx >= bitacoras.size()) {
            return null;
        }
        return bitacoras.get(idx);
    }

    private String formatBitacoraLabel(Bitacora b) {
        String fecha = b.getFecha() != null ? b.getFecha().toString() : "(sin fecha)";
        String actividad = b.getActividades() != null ? b.getActividades() : "";
        return fecha + " - " + actividad;
    }

    private Path copiarEvidencia(Path source) {
        try {
            Path uploadsDir = Paths.get(System.getProperty("user.home"), "constructrack", "uploads");
            Files.createDirectories(uploadsDir);
            String baseName = source.getFileName().toString();
            Path target = uploadsDir.resolve(System.currentTimeMillis() + "-" + baseName);
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            return target;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo copiar la evidencia", e);
        }
    }

    private String detectContentType(Path path) {
        try {
            String type = Files.probeContentType(path);
            return type != null ? type : "application/octet-stream";
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
