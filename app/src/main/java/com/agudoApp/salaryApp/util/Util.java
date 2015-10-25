package com.agudoApp.salaryApp.util;

import android.content.Context;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.IconTarjeta;

import java.util.ArrayList;

public class Util {

	Context context;

	public static int obtenerIconoCategoria(int idIcon) {
		int icon = 0;

		switch (idIcon) {
		case 0:
			icon = R.drawable.defecto;
			break;
		case 1:
			icon = R.drawable.amor;
			break;
		case 2:
			icon = R.drawable.animales;
			break;
		case 3:
			icon = R.drawable.azar;
			break;
		case 4:
			icon = R.drawable.baby;
			break;
		case 5:
			icon = R.drawable.baby_boyy;
			break;
		case 6:
			icon = R.drawable.baby_girll;
			break;
		case 7:
			icon = R.drawable.basket;
			break;
		case 8:
			icon = R.drawable.bday;
			break;
		case 9:
			icon = R.drawable.beer;
			break;
		case 10:
			icon = R.drawable.bike;
			break;
		case 11:
			icon = R.drawable.boat;
			break;
		case 12:
			icon = R.drawable.bus;
			break;
		case 13:
			icon = R.drawable.cinema;
			break;
		case 14:
			icon = R.drawable.coche;
			break;
		case 15:
			icon = R.drawable.coctel1;
			break;
		case 16:
			icon = R.drawable.compris;
			break;
		case 17:
			icon = R.drawable.compra_super;
			break;
		case 18:
			icon = R.drawable.doctor;
			break;
		case 19:
			icon = R.drawable.family;
			break;
		case 20:
			icon = R.drawable.futbol;
			break;
		case 21:
			icon = R.drawable.gastos_colegio;
			break;
		case 22:
			icon = R.drawable.glasses;
			break;
		case 23:
			icon = R.drawable.graduacion;
			break;
		case 24:
			icon = R.drawable.gym;
			break;
		case 25:
			icon = R.drawable.homee;
			break;
		case 26:
			icon = R.drawable.hotel;
			break;
		case 27:
			icon = R.drawable.interesss;
			break;
		case 28:
			icon = R.drawable.invertir;
			break;
		case 29:
			icon = R.drawable.laptop;
			break;
		case 30:
			icon = R.drawable.maid;
			break;
		case 31:
			icon = R.drawable.movil;
			break;
		case 32:
			icon = R.drawable.navidad;
			break;
		case 33:
			icon = R.drawable.otras;
			break;
		case 34:
			icon = R.drawable.parq_atracciones;
			break;
		case 35:
			icon = R.drawable.premio;
			break;
		case 36:
			icon = R.drawable.prestamo;
			break;
		case 37:
			icon = R.drawable.restaurant;
			break;
		case 38:
			icon = R.drawable.restaurante;
			break;
		case 39:
			icon = R.drawable.salario;
			break;
		case 40:
			icon = R.drawable.school_bus;
			break;
		case 41:
			icon = R.drawable.taller;
			break;
		case 42:
			icon = R.drawable.taxi;
			break;
		case 43:
			icon = R.drawable.train;
			break;
		case 44:
			icon = R.drawable.venta;
			break;
		case 45:
			icon = R.drawable.viajee;
			break;
		case 46:
			icon = R.drawable.internet;
			break;
		case 47:
			icon = R.drawable.baby_boy;
			break;
		case 48:
			icon = R.drawable.baby_girl;
			break;
		case 49:
			icon = R.drawable.circo;
			break;
		case 50:
			icon = R.drawable.compris;
			break;
		case 51:
			icon = R.drawable.concierto;
			break;
		case 52:
			icon = R.drawable.cosmo;
			break;
		case 53:
			icon = R.drawable.empleado;
			break;
		case 54:
			icon = R.drawable.entretenimiento;
			break;
		case 55:
			icon = R.drawable.gasss;
			break;
		case 56:
			icon = R.drawable.hairdresser;
			break;
		case 57:
			icon = R.drawable.holidays;
			break;
		case 58:
			icon = R.drawable.moto;
			break;
		case 59:
			icon = R.drawable.music;
			break;
		case 60:
			icon = R.drawable.pipa;
			break;
		case 61:
			icon = R.drawable.poker;
			break;
		case 62:
			icon = R.drawable.regalis;
			break;
		case 63:
			icon = R.drawable.servicio_domestico;
			break;
		case 64:
			icon = R.drawable.tabaco;
			break;
		case 65:
			icon = R.drawable.tattoo;
			break;
		case 66:
			icon = R.drawable.wedding;
			break;
		default:
			icon = R.drawable.defecto;
			break;
		}
		return icon;
	}


	public static int obtenerIconoTarjeta(int idIcon) {
		int icon = 0;

		switch (idIcon) {
			case 0:
				icon = R.drawable.visa;
				break;
			case 1:
				icon = R.drawable.visa_debit;
				break;
			case 2:
				icon = R.drawable.visa_electron;
				break;
			case 3:
				icon = R.drawable.mastercard;
				break;
			case 4:
				icon = R.drawable.american;
				break;
			case 5:
				icon = R.drawable.american2;
				break;
			case 6:
				icon = R.drawable.paypal;
				break;
			case 7:
				icon = R.drawable.union;
				break;
			case 8:
				icon = R.drawable.wester;
				break;
			case 9:
				icon = R.drawable.cirrus;
				break;
			default:
				icon = R.drawable.visa;
				break;
		}
		return icon;
	}


	public static ArrayList<IconTarjeta> obtenerIconosTarjetas(){
		ArrayList<IconTarjeta> listIcon = new ArrayList<IconTarjeta>();

		for(int i=0;i<10;i++){
			IconTarjeta icon = new IconTarjeta();
			icon.setId(i);
			listIcon.add(icon);
		}

		return listIcon;
	}
}
