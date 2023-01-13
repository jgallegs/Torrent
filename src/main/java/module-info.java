module com.example.turtletorrent {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens com.example.turtletorrent to javafx.fxml;
    exports com.example.turtletorrent;
}