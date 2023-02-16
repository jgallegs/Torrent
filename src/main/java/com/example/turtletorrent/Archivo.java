package com.example.turtletorrent;

public class Archivo {
    private String nombre;
    private String tamano;

    public Archivo(String nombre, String tamano) {
        this.nombre = nombre;
        this.tamano = calcularTamano(tamano);
    }

    private String calcularTamano(String tamano) {
        int tamanoInt = Integer.parseInt(tamano);
        if (tamanoInt < 1024) {
            return tamanoInt + " B";
        } else if (tamanoInt < 1024 * 1024) {
            return tamanoInt / 1024 + " KB";
        } else if (tamanoInt < 1024 * 1024 * 1024) {
            return tamanoInt / (1024 * 1024) + " MB";
        } else {
            return tamanoInt / (1024 * 1024 * 1024) + " GB";
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }
}
