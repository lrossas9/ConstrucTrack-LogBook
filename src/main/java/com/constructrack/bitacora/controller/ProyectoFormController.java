package com.constructrack.bitacora.controller;

import com.constructrack.bitacora.dao.ProyectoDAO;
import com.constructrack.bitacora.model.Proyecto;
import com.constructrack.bitacora.model.Usuario;
import com.constructrack.bitacora.util.AlertUtil;
import com.constructrack.bitacora.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProyectoFormController {
    @FXML
    private TextField nombreField;
    @FXML
    private TextField ubicacionField;
    @FXML
    private TextField constructoraField;
    @FXML
    private TextField interventoriaField;
    @FXML
    private TextField contratoField;
    @FXML
    private DatePicker fechaInicioPicker;
    @FXML
    private DatePicker fechaFinPicker;
    @FXML
    private TextField estadoField;

    private final ProyectoDAO proyectoDAO = new ProyectoDAO();
    private Proyecto proyecto;
    private Runnable onSaved;

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
        if (proyecto != null) {
            nombreField.setText(proyecto.getNombre());
            ubicacionField.setText(proyecto.getUbicacion());
            constructoraField.setText(proyecto.getConstructora());
            interventoriaField.setText(proyecto.getInterventoria());
            contratoField.setText(proyecto.getContrato());
            fechaInicioPicker.setValue(proyecto.getFechaInicio());
            fechaFinPicker.setValue(proyecto.getFechaFin());
            estadoField.setText(proyecto.getEstado());
        }
    }

    public void setOnSaved(Runnable onSaved) {
        this.onSaved = onSaved;
    }

    @FXML
    public void handleGuardar() {
        if (nombreField.getText().isBlank() || ubicacionField.getText().isBlank() || fechaInicioPicker.getValue() == null || estadoField.getText().isBlank()) {
            AlertUtil.error("Datos requeridos", "Completa nombre, ubicacion, fecha inicio y estado");
            return;
        }
        Usuario u = SessionManager.getUsuario();
        if (u == null) {
            AlertUtil.error("Sesion", "No hay usuario autenticado");
            return;
        }
        if (proyecto == null) {
            proyecto = new Proyecto();
            proyecto.setIdUsuario(u.getId());
        }
        proyecto.setNombre(nombreField.getText());
        proyecto.setUbicacion(ubicacionField.getText());
        proyecto.setConstructora(constructoraField.getText());
        proyecto.setInterventoria(interventoriaField.getText());
        proyecto.setContrato(contratoField.getText());
        proyecto.setFechaInicio(fechaInicioPicker.getValue());
        proyecto.setFechaFin(fechaFinPicker.getValue());
        proyecto.setEstado(estadoField.getText());

        try {
            if (proyecto.getId() == null) {
                proyectoDAO.guardar(proyecto);
            } else {
                proyectoDAO.actualizar(proyecto);
            }
        } catch (RuntimeException e) {
            AlertUtil.error("Error", "No se pudo guardar: " + e.getMessage());
            e.printStackTrace();
            return;
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

    private void cerrar() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}
