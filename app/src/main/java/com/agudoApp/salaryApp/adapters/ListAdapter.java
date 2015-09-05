package com.agudoApp.salaryApp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Movimiento;
import com.agudoApp.salaryApp.util.Util;

import java.util.ArrayList;
import java.util.Locale;

public class ListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<Movimiento> listaMov = new ArrayList<Movimiento>();
	Locale locale = Locale.getDefault();
	String languaje = locale.getLanguage();
	Context context;

	public ListAdapter(Context context, ArrayList<Movimiento> lista) {
		listaMov = lista;
		mInflater = LayoutInflater.from(context);
		this.context = context;
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
		TextView txtFecha;
		TextView txtCategoria;
		TextView txtSubcategoria;
		TextView txtDescripcion;
		TextView txtCant;
		ImageView imgView;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.lista_movimientos, null);
		}

		txtFecha = (TextView) convertView.findViewById(R.id.txtFecha);
		txtCategoria = (TextView) convertView.findViewById(R.id.txtCategoria);
		txtDescripcion = (TextView) convertView.findViewById(R.id.txtDescripcion);
		txtCant = (TextView) convertView.findViewById(R.id.txtCant);
		imgView = (ImageView) convertView.findViewById(R.id.iconCategoria);

		if(!listaMov.get(position).toString().equals("")) {
			txtDescripcion.setText(listaMov.get(position).toString());
		}else{
			txtDescripcion.setVisibility(View.GONE);
		}
		txtCant.setText(listaMov.get(position).getCantidadAux());
		txtFecha.setText(listaMov.get(position).getFecha().toString());
		imgView.setBackgroundResource(Util.obtenerIconoCategoria(listaMov.get(position).getIdIconCat()));

		if (listaMov.get(position).getCantidadAux().substring(0, 1).equals("-")) {
			txtCant.setTextColor(Color.RED);
		}else{
			txtCant.setTextColor(context.getResources().getColor(R.color.txtAzul));
		}

		Locale locale = Locale.getDefault();
		String languaje = locale.getLanguage();

		// rellenamos el campo de las categorias
		if (!listaMov.get(position).getDescCategoria().equals("-")) {
			txtCategoria.setText(listaMov.get(position).getDescCategoria());
		} else {
			if (languaje.equals("es") || languaje.equals("es-rUS")
					|| languaje.equals("ca")) {
				txtCategoria.setText("Sin categoría");
			} else if (languaje.equals("fr")) {
				txtCategoria.setText("Sans catégorie");
			} else if (languaje.equals("de")) {
				txtCategoria.setText("Keine Kategorie");
			} else if (languaje.equals("en")) {
				txtCategoria.setText("No category");
			} else if (languaje.equals("it")) {
				txtCategoria.setText("Senza categoria");
			} else if (languaje.equals("pt")) {
				txtCategoria.setText("Sem categoria");
			} else {
				txtCategoria.setText("No category");
			}
		}

		return convertView;
	}

}
