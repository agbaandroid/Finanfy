package com.agudoApp.salaryApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.util.Util;

import java.util.ArrayList;

public class ListAdapterSpinner extends ArrayAdapter<Categoria> {

	private Context contexto;
	private ArrayList<Categoria> lista;

	public ListAdapterSpinner(Context context, int textViewResourceId,
			ArrayList<Categoria> items) {
		super(context, textViewResourceId, items);
		this.contexto = context;
		this.lista = items;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getListView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflador = LayoutInflater.from(contexto);
		Categoria cat = lista.get(position);
		View fila = inflador.inflate(R.layout.spinner_iconos, parent, false);
		TextView texto = (TextView) fila.findViewById(R.id.textSpinner);

		texto.setText(cat.toString());

		ImageView icono = (ImageView) fila.findViewById(R.id.imagenSpinner);
		icono.setBackgroundDrawable(contexto.getResources().getDrawable(
				Util.obtenerIconoCategoria(cat.getIdIcon())));

		if(cat.getIdIcon() == -1){
			icono.setVisibility(View.GONE);
		}else{
			icono.setVisibility(View.VISIBLE);
		}

		return fila;
	}

	public View getListView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflador = LayoutInflater.from(contexto);
		Categoria cat = lista.get(position);
		View fila = inflador.inflate(R.layout.spinner_iconos, parent, false);
		TextView texto = (TextView) fila.findViewById(R.id.textSpinner);
		texto.setText(cat.toString());
		ImageView icono = (ImageView) fila.findViewById(R.id.imagenSpinner);
		icono.setBackgroundDrawable(contexto.getResources().getDrawable(
				Util.obtenerIconoCategoria(cat.getIdIcon())));
		return fila;
	}

}