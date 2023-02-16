package com.example.turtletorrent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class HelloController {
    public TextField txtIp;
    public TextField txtPort;
    public TableView<Archivo> tblArchivos;
    public TableColumn<Object, Object> clmNombre;
    public TableColumn<Object, Object> clmTamano;
    public Button btnDescargarTorrent;
    public Button btnConnect;
    private ObservableList<Archivo> listaArchivos;
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
        listaArchivos = FXCollections.observableArrayList();
        clmNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        clmTamano.setCellValueFactory(new PropertyValueFactory<>("tamano"));
        tblArchivos.setItems(listaArchivos);
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
            tblArchivos.getItems().clear();
            txtIp.setEditable(false);
            txtPort.setEditable(false);
            btnConnect.setDisable(true);
            socket = new Socket(txtIp.getText(), Integer.parseInt(txtPort.getText()));
            if (socket.isConnected()) {
                System.out.println("Connected");
                // sends output to the socket
                out = new DataOutputStream(socket.getOutputStream());
                //takes input from socket
                in = new DataInputStream(socket.getInputStream());
            } else {
                System.out.println("Not Connected");
                txtIp.setEditable(true);
                txtPort.setEditable(true);
                btnConnect.setDisable(false);
            }
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
        // put the String[][] to the observable list
        for (String[] strings : datosServer) {
            listaArchivos.add(new Archivo(strings[0], strings[1]));
        }


    }

    public void OnActionBtnDescargarTorrent(ActionEvent actionEvent) {
        if (tblArchivos.getSelectionModel().getSelectedItem() != null) {
            Archivo archivo = tblArchivos.getSelectionModel().getSelectedItem();
            System.out.println("Se va a descargar el archivo: " + archivo.getNombre());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Se va a descargar el archivo: " + archivo.getNombre(), ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                // send the file name to the server via datagram
                try {
                    DatagramSocket socket = new DatagramSocket();
                    byte[] mensaje = archivo.getNombre().getBytes();
                    DatagramPacket paquete = new DatagramPacket(mensaje, mensaje.length, InetAddress.getByName(txtIp.getText()), Integer.parseInt(txtPort.getText()));
                    socket.send(paquete);
                    System.out.println("Se envio el nombre del archivo");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}