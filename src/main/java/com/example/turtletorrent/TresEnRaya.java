package com.example.turtletorrent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class TresEnRaya {

    private static final int JUGADOR_X = 1;
    private static final int JUGADOR_O = 2;
    private static final int EMPATE = 3;
    private static final int SIN_JUGAR = 0;
    private static final int MAQUINA = JUGADOR_O;

    File player_circle = new File("src/main/resources/drawable/circle.png");
    File player_x = new File("src/main/resources/drawable/x_player.png");

    private int[][] tablero;
    private int jugadorActual;
    private int ganador;
    private boolean finDelJuego;
    private Object lock;

    public TresEnRaya() {
        this.tablero = new int[3][3];
        this.lock = new Object();
        reiniciarJuego();
    }


    public void jugar(int fila, int columna, int jugador) {
        synchronized (lock) {
            if (finDelJuego) {
                return;
            }

            if (tablero[fila][columna] != SIN_JUGAR) {
                return;
            }

            tablero[fila][columna] = jugador;

            if (hayGanador()) {

                ganador = jugador;
                panel.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
                panel.toFront();
                winnerMsg.toFront();
                winnerMsg.setText("GANADOR: " + ganador);
                finDelJuego = true;
            } else if (hayEmpate()) {
                ganador = EMPATE;
                panel.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
                panel.toFront();
                winnerMsg.toFront();
                winnerMsg.setText("EMPATE");
                finDelJuego = true;
            } else {
                jugadorActual = (jugadorActual == JUGADOR_X) ? MAQUINA : JUGADOR_X;
                if (jugadorActual == MAQUINA) {
                    jugarMaquina();
                }
            }
            lock.notifyAll();
        }
    }

    private void jugarMaquina() {
        Random random = new Random();
        int fila = random.nextInt(3);
        int columna = random.nextInt(3);

        while (tablero[fila][columna] != SIN_JUGAR) {
            fila = random.nextInt(3);
            columna = random.nextInt(3);
        }
        Image img = new Image(player_x.toURI().toString());
        if (fila == 0) {
            switch (columna) {
                case 0 -> leftTop.setImage(img);
                case 1 -> midTop.setImage(img);
                case 2 -> rightTop.setImage(img);
            }
        } else if (fila == 1) {
            switch (columna) {
                case 0 -> leftMid.setImage(img);
                case 1 -> midMid.setImage(img);
                case 2 -> rightMid.setImage(img);
            }
        } else if (fila == 2) {
            switch (columna) {
                case 0 -> leftBot.setImage(img);
                case 1 -> midBot.setImage(img);
                case 2 -> rightBot.setImage(img);
            }
        }
        jugar(fila, columna, MAQUINA);
    }

    private boolean hayGanador() {
        for (int i = 0; i < 3; i++) {
            if (tablero[i][0] != SIN_JUGAR && tablero[i][0] == tablero[i][1] && tablero[i][1] == tablero[i][2]) {
                return true;
            }
        }

        for (int j = 0; j < 3; j++) {
            if (tablero[0][j] != SIN_JUGAR && tablero[0][j] == tablero[1][j] && tablero[1][j] == tablero[2][j]) {
                return true;
            }
        }

        if (tablero[0][0] != SIN_JUGAR && tablero[0][0] == tablero[1][1] && tablero[1][1] == tablero[2][2]) {
            return true;
        }

        if (tablero[0][2] != SIN_JUGAR && tablero[0][2] == tablero[1][1] && tablero[1][1] == tablero[2][0]) {
            return true;
        }
        return false;
    }


    private boolean hayEmpate() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == SIN_JUGAR) {
                    return false;
                }
            }
        }
        return true;
    }

    public void reiniciarJuego() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = SIN_JUGAR;
            }
        }
        jugadorActual = JUGADOR_X;
        ganador = SIN_JUGAR;
        finDelJuego = false;
    }

    public int getGanador() {
        return ganador;
    }

    public int getJugadorActual() {
        return jugadorActual;
    }

    public int[][] getTablero() {
        return tablero;
    }

    @FXML
    private ImageView leftTop;
    @FXML
    private ImageView midTop;
    @FXML
    private ImageView rightTop;
    @FXML
    private ImageView leftMid;
    @FXML
    private ImageView midMid;
    @FXML
    private ImageView rightMid;
    @FXML
    private ImageView leftBot;
    @FXML
    private ImageView midBot;
    @FXML
    private ImageView rightBot;
    @FXML
    private Pane panel;
    @FXML
    private Label winnerMsg;

    public void drawBoard(MouseEvent mouseEvent) {
        Image img;
        if (getJugadorActual() == 1) {
            img = new Image(player_circle.toURI().toString());
        } else {
            img = new Image(player_x.toURI().toString());
        }

        if (mouseEvent.getSource().equals(leftTop)) {
            jugar(0,0,1);
            if (leftTop.getImage() == null) {
                leftTop.setImage(img);
            }
        } else if (mouseEvent.getSource().equals(midTop)) {
            jugar(0,1,1);
            if (midTop.getImage() == null) {
                midTop.setImage(img);
            }
        } else if (mouseEvent.getSource().equals(rightTop)) {
            jugar(0,2,1);
            if (rightTop.getImage() == null) {
                rightTop.setImage(img);
            }
        } else if (mouseEvent.getSource().equals(leftMid)) {
            jugar(1,0,1);
            if (leftMid.getImage() == null) {
                leftMid.setImage(img);
            }
        } else if (mouseEvent.getSource().equals(midMid)) {
            jugar(1,1,1);
            if (midMid.getImage() == null) {
                midMid.setImage(img);
            }
        } else if (mouseEvent.getSource().equals(rightMid)) {
            jugar(1,2,1);
            if (rightMid.getImage() == null) {
                rightMid.setImage(img);
            }
        } else if (mouseEvent.getSource().equals(leftBot)) {
            jugar(2,0,1);
            if (leftBot.getImage() == null) {
                leftBot.setImage(img);
            }
        } else if (mouseEvent.getSource().equals(midBot)) {
            jugar(2,1,1);
            if (midBot.getImage() == null) {
                midBot.setImage(img);
            }
        } else if (mouseEvent.getSource().equals(rightBot)) {
            jugar(2,2,1);
            if (rightBot.getImage() == null) {
                rightBot.setImage(img);
            }
        }
    }
}