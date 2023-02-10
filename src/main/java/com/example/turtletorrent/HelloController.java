package com.example.turtletorrent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Button btnCargarTorrent;

    @FXML
    private ImageView imgSelected;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Ya se como funciona");
    }



    @FXML
    protected void onAddTorrentClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Buscar Imagen");

        // Agregar filtros para facilitar la busqueda
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        // Obtener la imagen seleccionada
        File file = fileChooser.showOpenDialog(HelloApplication.g_stage);
        welcomeText.setText(file.getName());
    }
    @FXML
    protected void onCloseButton() {
        System.exit(0);
    }
}