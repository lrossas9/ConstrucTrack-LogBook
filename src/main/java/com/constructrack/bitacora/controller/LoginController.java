package com.constructrack.bitacora.controller;

import com.constructrack.bitacora.dao.UsuarioDAO;
import com.constructrack.bitacora.model.Usuario;
import com.constructrack.bitacora.util.AlertUtil;
import com.constructrack.bitacora.util.SessionManager;
import com.constructrack.bitacora.util.ViewLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField correoField;
    @FXML
    private PasswordField contrasenaField;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void initialize() {
        usuarioDAO.createIfNotExists("Laura Rosas", "lrosass9@soy.sena.edu.co", "laurarosas");
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String correo = correoField.getText();
        String pass = contrasenaField.getText();
        if (correo == null || correo.isBlank() || pass == null || pass.isBlank()) {
            AlertUtil.error("Datos requeridos", "Ingresa correo y contraseña");
            return;
        }
        Usuario u = usuarioDAO.findByCorreoAndPassword(correo, pass);
        if (u == null) {
            AlertUtil.error("Acceso denegado", "Credenciales incorrectas");
            return;
        }
        SessionManager.setUsuario(u);
        Stage stage = (Stage) correoField.getScene().getWindow();
        ViewLoader.show(stage, "/view/DashboardView.fxml", "Dashboard");
    }

    @FXML
    public void handleAbrirRegistro() {
        try {
            ViewLoader.modal("/view/RegistroView.fxml", "Registrar usuario");
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo abrir registro: " + e.getMessage());
        }
    }
}
