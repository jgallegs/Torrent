package com.example.turtletorrent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class HelloController {
    public TextField txtIp;
    public TextField txtPort;
    public TableView<ObservableList<String>> tblArchivos;
    public TableColumn clmNombre;
    public TableColumn clmTamano;
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
    private void initialize() {
    }

    private Socket socket = null;
    private OutputStream out = null;
    private InputStream in = null;


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

    public void onConnectClick(MouseEvent mouseEvent) throws IOException {
        try {
            socket = new Socket(txtIp.getText(), Integer.parseInt(txtPort.getText()));
            if (socket.isConnected()) {
                System.out.println("Connected");
            }
            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
            //takes input from socket
            in = new DataInputStream(socket.getInputStream());
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }

        byte[] recibidos = new byte[1024];

        DatagramSocket socket = new DatagramSocket(Integer.parseInt((txtPort.getText())));

        System.out.println("Esperando Datagrama'......... ");

        DatagramPacket paqRecibido = new DatagramPacket(recibidos, recibidos.length);
        socket.receive(paqRecibido); //recibo el datagrama
        System.out.println(Arrays.toString(paqRecibido.getData()));
        System.out.println(paqRecibido.getLength());
        //convierto el datagrama a String obmitiendo los bytes vacios
        String mensaje = new String(paqRecibido.getData(), 0, paqRecibido.getLength());
        System.out.println(mensaje);
        // put the mensaje content to a String[][]
        String[] parts = mensaje.split(";");
        String[][] datosServer = new String[parts.length][];
        for (int i = 0; i < parts.length; i++) {
            datosServer[i] = parts[i].split(":");
        }
        // put the String[][] to the table
        ObservableList<ObservableList<String>> data = tblArchivos.getItems();
        for (String[] strings : datosServer) {
            ObservableList<String> row = FXCollections.observableArrayList();
            row.addAll(Arrays.asList(strings));
            data.add(row);
        }
        tblArchivos.setItems(data);


    }
}