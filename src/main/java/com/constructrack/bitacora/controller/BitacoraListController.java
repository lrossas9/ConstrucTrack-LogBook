package com.constructrack.bitacora.controller;

import com.constructrack.bitacora.dao.BitacoraDAO;
import com.constructrack.bitacora.model.Bitacora;
import com.constructrack.bitacora.model.Proyecto;
import com.constructrack.bitacora.util.AlertUtil;
import com.constructrack.bitacora.util.PdfExporter;
import com.constructrack.bitacora.util.ViewLoader;
import com.lowagie.text.DocumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BitacoraListController {
    @FXML
    private TableView<Bitacora> tablaBitacoras;
    @FXML
    private TableColumn<Bitacora, java.time.LocalDate> colFecha;
    @FXML
    private TableColumn<Bitacora, String> colClima;
    @FXML
    private TableColumn<Bitacora, Integer> colAvance;

    private final BitacoraDAO bitacoraDAO = new BitacoraDAO();
    private final ObservableList<Bitacora> bitacoras = FXCollections.observableArrayList();
    private Proyecto proyecto;

    @FXML
    public void initialize() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colClima.setCellValueFactory(new PropertyValueFactory<>("clima"));
        colAvance.setCellValueFactory(new PropertyValueFactory<>("porcentajeAvance"));
        tablaBitacoras.setItems(bitacoras);
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
        recargar();
    }

    @FXML
    public void handleNueva() {
        abrirForm(null);
    }

    @FXML
    public void handleEditar() {
        Bitacora b = tablaBitacoras.getSelectionModel().getSelectedItem();
        if (b == null) {
            AlertUtil.error("Seleccion requerida", "Elige un registro");
            return;
        }
        abrirForm(b);
    }

    @FXML
    public void handleEliminar() {
        Bitacora b = tablaBitacoras.getSelectionModel().getSelectedItem();
        if (b == null) {
            AlertUtil.error("Seleccion requerida", "Elige un registro");
            return;
        }
        if (AlertUtil.confirm("Confirmar", "¿Eliminar registro de bitacora?")) {
            bitacoraDAO.eliminar(b.getId());
            recargar();
        }
    }

    @FXML
    public void handleExportarPdf() {
        if (proyecto == null) {
            return;
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Guardar PDF");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        chooser.setInitialFileName("bitacora-" + proyecto.getNombre() + ".pdf");
        File file = chooser.showSaveDialog(tablaBitacoras.getScene().getWindow());
        if (file != null) {
            try {
                PdfExporter.exportProyecto(proyecto, bitacoras, file.getAbsolutePath());
                AlertUtil.info("Exportado", "PDF generado");
            } catch (IOException | DocumentException e) {
                AlertUtil.error("Error", "No se pudo exportar: " + e.getMessage());
            }
        }
    }

    private void abrirForm(Bitacora bitacora) {
        try {
            ViewLoader.modal("/view/BitacoraFormView.fxml", bitacora == null ? "Nueva bitacora" : "Editar bitacora", loader -> {
                BitacoraFormController controller = loader.getController();
                controller.setProyecto(proyecto);
                controller.setBitacora(bitacora);
                controller.setOnSaved(this::recargar);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void recargar() {
        if (proyecto == null) {
            return;
        }
        List<Bitacora> lista = bitacoraDAO.listarPorProyecto(proyecto.getId());
        bitacoras.setAll(lista);
    }
}
