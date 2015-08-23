package com.agudoApp.salaryApp.adapters;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Movimiento;

public class ListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<Movimiento> listaMov = new ArrayList<Movimiento>();
	Locale locale = Locale.getDefault();
	String languaje = locale.getLanguage();

	public ListAdapter(Context context, ArrayList<Movimiento> lista) {
		listaMov = lista;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listaMov.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listaMov.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView text;
		TextView text2;
		TextView text3;
		TextView text4;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.lista, null);
		}

		text = (TextView) convertView.findViewById(R.id.textConcep);
		text2 = (TextView) convertView.findViewById(R.id.textCant);
		text3 = (TextView) convertView.findViewById(R.id.textCat);
		text4 = (TextView) convertView.findViewById(R.id.textFech);

		text.setText(listaMov.get(position).toString());
		text2.setText(listaMov.get(position).getCantidadAux());
		text4.setText(listaMov.get(position).getFecha().toString());

		if (listaMov.get(position).getCantidadAux().substring(0, 1).equals("-")) {
			text2.setTextColor(Color.RED);
		} else {
			text2.setTextColor(Color.argb(255, 98, 186, 14));
			//text2.setTextColor(Color.argb(255, 31, 107, 38));
		}

		// rellenamos el campo de las categorias
		if (!listaMov.get(position).getDescCategoria().equals("-")) {
			if (!listaMov.get(position).getDescSubcategoria().equals("-")) {
				text3.setText(listaMov.get(position).getDescCategoria()
						+ "  >  "
						+ listaMov.get(position).getDescSubcategoria());
			} else {
				text3.setText(listaMov.get(position).getDescCategoria());
			}
		} else {
			if (!listaMov.get(position).getDescSubcategoria().equals("-")) {
				text3.setText(listaMov.get(position).getDescSubcategoria());
			} else {
				if (languaje.equals("es") || languaje.equals("es-rUS")
						|| languaje.equals("ca")) {
					text3.setText("Otros");
				} else if (languaje.equals("fr")) {
					text3.setText("Autres");
				} else if (languaje.equals("de")) {
					text3.setText("Andere");
				} else if (languaje.equals("en")) {
					text3.setText("Others");
				} else if (languaje.equals("it")) {
					text3.setText("Altri");
				} else if (languaje.equals("pt")) {
					text3.setText("Outros");
				} else {
					text3.setText("Others");
				}
			}
		}
		return convertView;
	}

}
