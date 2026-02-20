package com.constructrack.bitacora.controller;

import com.constructrack.bitacora.dao.ProyectoDAO;
import com.constructrack.bitacora.model.Proyecto;
import com.constructrack.bitacora.model.Usuario;
import com.constructrack.bitacora.util.AlertUtil;
import com.constructrack.bitacora.util.SessionManager;
import com.constructrack.bitacora.util.ViewLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class DashboardController {
    @FXML
    private TableView<Proyecto> tablaProyectos;
    @FXML
    private TableColumn<Proyecto, String> colNombre;
    @FXML
    private TableColumn<Proyecto, String> colUbicacion;
    @FXML
    private TableColumn<Proyecto, String> colEstado;

    private final ProyectoDAO proyectoDAO = new ProyectoDAO();
    private final ObservableList<Proyecto> proyectos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tablaProyectos.setItems(proyectos);
        cargarProyectos();
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
    public void handleLogout() {
        SessionManager.clear();
        Stage stage = (Stage) tablaProyectos.getScene().getWindow();
        ViewLoader.show(stage, "/view/LoginView.fxml", "Login");
    }

    private void abrirProyectoForm(Proyecto proyecto) {
        try {
            ViewLoader.modal("/view/ProyectoFormView.fxml", proyecto == null ? "Nuevo Proyecto" : "Editar Proyecto", loader -> {
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
    }
}
