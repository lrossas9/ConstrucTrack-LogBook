package com.constructrack.bitacora.controller;

import com.constructrack.bitacora.dao.BitacoraDAO;
import com.constructrack.bitacora.model.Bitacora;
import com.constructrack.bitacora.model.Proyecto;
import com.constructrack.bitacora.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDateTime;

public class BitacoraFormController {
    @FXML
    private DatePicker fechaPicker;
    @FXML
    private ComboBox<String> climaCombo;
    @FXML
    private ComboBox<String> jornadaCombo;
    @FXML
    private TextArea actividadesArea;
    @FXML
    private TextArea personalArea;
    @FXML
    private TextArea equiposArea;
    @FXML
    private TextArea materialesArea;
    @FXML
    private TextField avanceField;
    @FXML
    private TextArea observacionesArea;
    @FXML
    private TextField firmaField;
    @FXML
    private ImageView imagenPreview;

    private final BitacoraDAO bitacoraDAO = new BitacoraDAO();
    private Proyecto proyecto;
    private Bitacora bitacora;
    private Runnable onSaved;

    @FXML
    public void initialize() {
        climaCombo.getItems().addAll("Soleado", "Nublado", "Lluvioso");
        jornadaCombo.getItems().addAll("Mañana", "Tarde", "Noche");
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public void setBitacora(Bitacora b) {
        this.bitacora = b;
        if (b != null) {
            fechaPicker.setValue(b.getFecha());
            climaCombo.setValue(b.getClima());
            jornadaCombo.setValue(b.getJornada());
            actividadesArea.setText(b.getActividades());
            personalArea.setText(b.getPersonal());
            equiposArea.setText(b.getEquipos());
            materialesArea.setText(b.getMateriales());
            avanceField.setText(b.getPorcentajeAvance() != null ? b.getPorcentajeAvance().toString() : "");
            observacionesArea.setText(b.getObservaciones());
            firmaField.setText(b.getFirma());
            if (b.getRutaImagen() != null) {
                File f = new File(b.getRutaImagen());
                if (f.exists()) {
                    imagenPreview.setImage(new Image(f.toURI().toString()));
                }
            }
        }
    }

    public void setOnSaved(Runnable onSaved) {
        this.onSaved = onSaved;
    }

    @FXML
    public void handleGuardar() {
        if (proyecto == null) {
            AlertUtil.error("Sesion", "No hay proyecto asignado");
            return;
        }
        if (fechaPicker.getValue() == null) {
            AlertUtil.error("Datos requeridos", "La fecha es obligatoria");
            return;
        }
        int avance = parseAvance();
        if (avance < 0 || avance > 100) {
            AlertUtil.error("Validacion", "El porcentaje de avance debe estar entre 0 y 100");
            return;
        }
        if (bitacora == null) {
            bitacora = new Bitacora();
            bitacora.setIdProyecto(proyecto.getId());
            bitacora.setFechaRegistro(LocalDateTime.now());
        }
        bitacora.setFecha(fechaPicker.getValue());
        bitacora.setClima(climaCombo.getValue());
        bitacora.setJornada(jornadaCombo.getValue());
        bitacora.setActividades(actividadesArea.getText());
        bitacora.setPersonal(personalArea.getText());
        bitacora.setEquipos(equiposArea.getText());
        bitacora.setMateriales(materialesArea.getText());
        bitacora.setPorcentajeAvance(avance);
        bitacora.setObservaciones(observacionesArea.getText());
        bitacora.setFirma(firmaField.getText());
        bitacora.setRutaImagen(imagenPreview.getImage() != null && imagenPreview.getImage().getUrl() != null ? new File(imagenPreview.getImage().getUrl()).getPath() : bitacora.getRutaImagen());

        if (bitacora.getId() == null) {
            bitacoraDAO.guardar(bitacora);
        } else {
            bitacoraDAO.actualizar(bitacora);
        }
        if (onSaved != null) {
            onSaved.run();
        }
        cerrar();
    }

    @FXML
    public void handleCancelar() {
        cerrar();
    }

    @FXML
    public void handleCargarImagen() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecciona imagen");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagen", "*.png", "*.jpg", "*.jpeg"));
        File file = chooser.showOpenDialog(imagenPreview.getScene().getWindow());
        if (file != null) {
            imagenPreview.setImage(new Image(file.toURI().toString()));
            if (bitacora != null) {
                bitacora.setRutaImagen(file.getAbsolutePath());
            }
        }
    }

    private int parseAvance() {
        try {
            if (avanceField.getText() == null || avanceField.getText().isBlank()) {
                return 0;
            }
            return Integer.parseInt(avanceField.getText());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void cerrar() {
        Stage stage = (Stage) fechaPicker.getScene().getWindow();
        stage.close();
    }
}
