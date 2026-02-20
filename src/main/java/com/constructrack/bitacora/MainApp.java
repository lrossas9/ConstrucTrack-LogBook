package com.constructrack.bitacora;

import com.constructrack.bitacora.util.ViewLoader;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        ViewLoader.show(primaryStage, "/view/LoginView.fxml", "Login");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
