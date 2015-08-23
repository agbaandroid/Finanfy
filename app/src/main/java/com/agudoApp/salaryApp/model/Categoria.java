package com.agudoApp.salaryApp.model;

import java.util.ArrayList;

public class Categoria implements Comparable<Object> {
	String id;
	String descripcion = "";
	int idIcon;
	float total;
	
	ArrayList<Movimiento> listaMovimientos = new ArrayList<Movimiento>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String toString() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public ArrayList<Movimiento> getListaMovimientos() {
		return listaMovimientos;
	}

	public void setListaMovimientos(ArrayList<Movimiento> listaMovimientos) {
		this.listaMovimientos = listaMovimientos;
	}

	public int getIdIcon() {
		return idIcon;
	}

	public void setIdIcon(int idIcon) {
		this.idIcon = idIcon;
	}

	@Override
	public int compareTo(Object o) {
		Categoria cat = (Categoria) o;
		return this.descripcion.compareToIgnoreCase(cat.toString());
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}
	
	
}
