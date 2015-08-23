package com.agudoApp.salaryApp.model;

import java.sql.Date;

public class Movimiento {
	String id = "";
	String tipo = "";
	String cantidad = "";
	String descripcion = "";
	String idCategoria = "";
	String idSubcategoria = "";
	Date fecha;
	String mes = "";
	String anio = "";
	boolean recibo;
	boolean tarjeta;
	String descCategoria = "";
	String descSubcategoria = "";
	String cantidadAux = "";
	String idTarjeta = "";
	String descTarjeta = "";
	String idCuenta = "";
	int idIconCat = 0;
	int idIconSub = 0;	

	public String getIdTarjeta() {
		return idTarjeta;
	}

	public void setIdTarjeta(String idTarjeta) {
		this.idTarjeta = idTarjeta;
	}

	public String getCantidadAux() {
		return cantidadAux;
	}

	public void setCantidadAux(String cantidadAux) {
		this.cantidadAux = cantidadAux;
	}

	public String getDescCategoria() {
		return descCategoria;
	}

	public void setDescCategoria(String descCategoria) {
		this.descCategoria = descCategoria;
	}

	public String getDescSubcategoria() {
		return descSubcategoria;
	}

	public void setDescSubcategoria(String descSubcategoria) {
		this.descSubcategoria = descSubcategoria;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String toString() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(String idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getIdSubcategoria() {
		return idSubcategoria;
	}

	public void setIdSubcategoria(String idSubcategoria) {
		this.idSubcategoria = idSubcategoria;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

	public boolean isRecibo() {
		return recibo;
	}

	public void setRecibo(boolean recibo) {
		this.recibo = recibo;
	}

	public boolean isTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(boolean tarjeta) {
		this.tarjeta = tarjeta;
	}

	public String getDescTarjeta() {
		return descTarjeta;
	}

	public void setDescTarjeta(String descTarjeta) {
		this.descTarjeta = descTarjeta;
	}

	public String getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	public int getIdIconCat() {
		return idIconCat;
	}

	public void setIdIconCat(int idIconCat) {
		this.idIconCat = idIconCat;
	}

	public int getIdIconSub() {
		return idIconSub;
	}

	public void setIdIconSub(int idIconSub) {
		this.idIconSub = idIconSub;
	}	
}
