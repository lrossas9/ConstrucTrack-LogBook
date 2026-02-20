package com.constructrack.bitacora.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.function.Consumer;

public class ViewLoader {
    public static void show(Stage stage, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(ViewLoader.class.getResource(fxmlPath));
            Scene scene = new Scene(root);
            addStyles(scene);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar la vista " + fxmlPath, e);
        }
    }

    public static FXMLLoader modal(String fxmlPath, String title, Consumer<FXMLLoader> beforeShow) throws IOException {
        FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(fxmlPath));
        Parent root = loader.load();
        if (beforeShow != null) {
            beforeShow.accept(loader);
        }
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        Window owner = Window.getWindows().stream().filter(Window::isFocused).findFirst().orElse(null);
        if (owner != null) {
            stage.initOwner(owner);
        }
        Scene scene = new Scene(root);
        addStyles(scene);
        stage.setScene(scene);
        stage.showAndWait();
        return loader;
    }

    public static FXMLLoader modal(String fxmlPath, String title) throws IOException {
        return modal(fxmlPath, title, null);
    }

    private static void addStyles(Scene scene) {
        try {
            String css = ViewLoader.class.getResource("/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception ignored) {
            // If the stylesheet is missing, do nothing to avoid breaking the UI.
        }
    }
}
