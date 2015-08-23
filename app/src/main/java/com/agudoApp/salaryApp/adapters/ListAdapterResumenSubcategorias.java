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

public class ListAdapterResumenSubcategorias extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<Categoria> listaSub = new ArrayList<Categoria>();
	boolean aniadir = true;
	private ArrayList<Movimiento> listaMov;
	Locale locale = Locale.getDefault();
	String languaje = locale.getLanguage();

	public ListAdapterResumenSubcategorias(Context context,
			ArrayList<Movimiento> lista) {
		listaMov = lista;
		for (int i = 0; i < lista.size(); i++) {
			aniadir = true;
			Movimiento mov = lista.get(i);
			Categoria subcat = new Categoria();
			subcat.setId(mov.getIdSubcategoria());
			subcat.setDescripcion(mov.getDescSubcategoria());
			if (listaSub.size() == 0) {
				if (subcat.getId().equals("0")) {
					Locale locale = Locale.getDefault();
					String languaje = locale.getLanguage();
					if (languaje.equals("es") || languaje.equals("es-rUS")
							|| languaje.equals("ca")) {
						subcat.setDescripcion("Sin subcategoria");
					} else if (languaje.equals("fr")) {
						subcat.setDescripcion("Sans sous-catégorie");
					} else if (languaje.equals("de")) {
						subcat.setDescripcion("Keine Unterkategorie");
					} else if (languaje.equals("en")) {
						subcat.setDescripcion("No subcategory");
					} else if (languaje.equals("it")) {
						subcat.setDescripcion("Senza sottocategoria");
					} else if (languaje.equals("pt")) {
						subcat.setDescripcion("Sem subcategoria");
					} else {
						subcat.setDescripcion("No subcategory");
					}
				}
				listaSub.add(subcat);
			} else {
				for (int j = 0; j < listaSub.size(); j++) {
					Categoria subcat2 = listaSub.get(j);
					if (subcat.getId().equals(subcat2.getId())) {
						aniadir = false;
						break;
					} else {
						if (subcat.getId().equals("0")) {
							if (languaje.equals("es") || languaje.equals("es-rUS")
									|| languaje.equals("ca")) {
								subcat.setDescripcion("Sin subcategoria");
							} else if (languaje.equals("fr")) {
								subcat.setDescripcion("Sans sous-catégorie");
							} else if (languaje.equals("de")) {
								subcat.setDescripcion("Keine Unterkategorie");
							} else if (languaje.equals("en")) {
								subcat.setDescripcion("No subcategory");
							} else if (languaje.equals("it")) {
								subcat.setDescripcion("Senza sottocategoria");
							} else if (languaje.equals("pt")) {
								subcat.setDescripcion("Sem subcategoria");
							} else {
								subcat.setDescripcion("No subcategory");
							}
						}
					}
				}
				if (aniadir) {
					listaSub.add(subcat);
				}
			}
		}

		Collections.sort(listaSub);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listaSub.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listaSub.get(position);
	}

	public int getPositionById(String id) {
		int posi = 0;
		for (int i = 0; i < listaSub.size(); i++) {
			Categoria cat = listaSub.get(i);
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
			convertView = mInflater
					.inflate(R.layout.lista_resumen_subcat, null);
		}

		for (int i = 0; i < listaMov.size(); i++) {
			Movimiento mov = listaMov.get(i);
			if (mov.getIdSubcategoria().equals(listaSub.get(position).getId())) {
				total = total + Float.parseFloat(mov.getCantidad());
			}
		}

		nomCategoria = (TextView) convertView
				.findViewById(R.id.textNomSubcategoria);
		cantidad = (TextView) convertView
				.findViewById(R.id.textResumenSubcategoriaCant);

		nomCategoria.setText(listaSub.get(position).toString());
		DecimalFormat df = new DecimalFormat("0.00");
		cantidad.setText(String.valueOf(df.format(total)));

		if (total < 0) {
			cantidad.setTextColor(Color.RED);
		} else {
			cantidad.setTextColor(Color.argb(255, 31, 107, 38));
		}
		return convertView;
	}
}
