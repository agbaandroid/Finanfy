package com.agudoApp.salaryApp.model;

public class Tarjeta implements Comparable<Object> {
    String id;
    String nombre = "";
    int tipo;
    int idIcon;
    float cantMax;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return nombre;
    }

    public void setNombre(String descripcion) {
        this.nombre = descripcion;
    }

    public int getIdIcon() {
        return idIcon;
    }

    public void setIdIcon(int idIcon) {
        this.idIcon = idIcon;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public float getCantMax() {
        return cantMax;
    }

    public void setCantMax(float cantMax) {
        this.cantMax = cantMax;
    }

    @Override
    public int compareTo(Object o) {
        Tarjeta tar = (Tarjeta) o;
        return this.nombre.compareToIgnoreCase(tar.toString());
    }
}
