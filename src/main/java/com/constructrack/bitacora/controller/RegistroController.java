package com.constructrack.bitacora.controller;

import com.constructrack.bitacora.dao.UsuarioDAO;
import com.constructrack.bitacora.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistroController {
    @FXML
    private TextField nombreField;
    @FXML
    private TextField correoField;
    @FXML
    private PasswordField contrasenaField;
    @FXML
    private PasswordField confirmarField;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void handleRegistrar() {
        String nombre = nombreField.getText();
        String correo = correoField.getText();
        String pass = contrasenaField.getText();
        String confirm = confirmarField.getText();

        if (nombre == null || nombre.isBlank() || correo == null || correo.isBlank() || pass == null || pass.isBlank() || confirm == null || confirm.isBlank()) {
            AlertUtil.error("Datos requeridos", "Completa todos los campos");
            return;
        }
        if (!correo.contains("@")) {
            AlertUtil.error("Validacion", "Correo no válido");
            return;
        }
        if (pass.length() < 6) {
            AlertUtil.error("Validacion", "La contraseña debe tener al menos 6 caracteres");
            return;
        }
        if (!pass.equals(confirm)) {
            AlertUtil.error("Validacion", "Las contraseñas no coinciden");
            return;
        }
        if (usuarioDAO.existsByCorreo(correo)) {
            AlertUtil.error("Registro", "Ya existe un usuario con ese correo");
            return;
        }

        usuarioDAO.crear(nombre, correo, pass);
        AlertUtil.info("Registro", "Usuario creado correctamente");
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
