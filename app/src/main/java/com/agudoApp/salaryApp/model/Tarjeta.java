package com.agudoApp.salaryApp.model;

public class Tarjeta implements Comparable<Object> {
	String id;
	String nombre = "";

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

	@Override
	public int compareTo(Object o) {
		Tarjeta tar = (Tarjeta) o;
		return this.nombre.compareToIgnoreCase(tar.toString());
	}
}
