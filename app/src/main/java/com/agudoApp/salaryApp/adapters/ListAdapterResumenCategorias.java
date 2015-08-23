package com.agudoApp.salaryApp.adapters;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Movimiento;

public class ListAdapterResumenCategorias extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<Categoria> listaCat = new ArrayList<Categoria>();
	boolean aniadir = true;
	private ArrayList<Movimiento> listaMov;
	Locale locale = Locale.getDefault();
	String languaje = locale.getLanguage();

	public ListAdapterResumenCategorias(Context context,
			ArrayList<Movimiento> lista) {
		listaMov = lista;
		for (int i = 0; i < lista.size(); i++) {
			aniadir = true;
			Movimiento mov = lista.get(i);
			Categoria cat = new Categoria();
			cat.setId(mov.getIdCategoria());
			cat.setDescripcion(mov.getDescCategoria());
			if (listaCat.size() == 0) {
				if (cat.getId().equals("0")) {					
					Locale locale = Locale.getDefault();
					String languaje = locale.getLanguage();
					if (languaje.equals("es") || languaje.equals("es-rUS")
							|| languaje.equals("ca")) {
						cat.setDescripcion("Sin categoria");
					} else if (languaje.equals("fr")) {
						cat.setDescripcion("Sans catégorie");
					} else if (languaje.equals("de")) {
						cat.setDescripcion("Keine Kategorie");
					} else if (languaje.equals("en")) {
						cat.setDescripcion("No category");
					} else if (languaje.equals("it")) {
						cat.setDescripcion("Senza categoria");
					} else if (languaje.equals("pt")) {
						cat.setDescripcion("Sem categoria");
					} else {
						cat.setDescripcion("No category");
					}
				}
				listaCat.add(cat);
			} else {
				for (int j = 0; j < listaCat.size(); j++) {
					Categoria cat2 = listaCat.get(j);
					if (cat.getId().equals(cat2.getId())) {
						aniadir = false;
						break;
					} else {
						if (cat.getId().equals("0")) {
							Locale locale = Locale.getDefault();
							String languaje = locale.getLanguage();
							if (languaje.equals("es") || languaje.equals("es-rUS")
									|| languaje.equals("ca")) {
								cat.setDescripcion("Sin categoria");
							} else if (languaje.equals("fr")) {
								cat.setDescripcion("Sans catégorie");
							} else if (languaje.equals("de")) {
								cat.setDescripcion("Keine Kategorie");
							} else if (languaje.equals("en")) {
								cat.setDescripcion("No category");
							} else if (languaje.equals("it")) {
								cat.setDescripcion("Senza categoria");
							} else if (languaje.equals("pt")) {
								cat.setDescripcion("Sem categoria");
							} else {
								cat.setDescripcion("No category");
							}
						}
					}
				}
				if (aniadir) {
					listaCat.add(cat);
				}
			}
		}

		Collections.sort(listaCat);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listaCat.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listaCat.get(position);
	}

	public int getPositionById(String id) {
		int posi = 0;
		for (int i = 0; i < listaCat.size(); i++) {
			Categoria cat = listaCat.get(i);
			if (cat.getId().equals(id)) {
				posi = i;
				break;
			}
		}
		return posi;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView nomCategoria;
		TextView cantidad;
		float total = 0;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.lista_resumen_cat, null);
		}

		for (int i = 0; i < listaMov.size(); i++) {
			Movimiento mov = listaMov.get(i);
			if (mov.getIdCategoria().equals(listaCat.get(position).getId())) {
				total = total + Float.parseFloat(mov.getCantidad());
			}
		}

		nomCategoria = (TextView) convertView
				.findViewById(R.id.textNomCategoria);
		cantidad = (TextView) convertView
				.findViewById(R.id.textResumenCategoriaCant);

		nomCategoria.setText(listaCat.get(position).toString());
		DecimalFormat df = new DecimalFormat("0.00");
		cantidad.setText(String.valueOf(df.format(total)));

		if (total < 0) {
			cantidad.setTextColor(Color.RED);
		} else {
			cantidad.setTextColor(Color.argb(255, 98, 186, 14));
		}
		return convertView;
	}
}
