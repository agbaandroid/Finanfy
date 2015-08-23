package com.agudoApp.salaryApp.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;

public class ListAdapterOpciones extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<String> listaOp = new ArrayList<String>();
	Context contextG;

	public ListAdapterOpciones(Context context, ArrayList<String> lista) {
		listaOp = lista;
		mInflater = LayoutInflater.from(context);
		contextG = context;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listaOp.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listaOp.get(position);
	}

	public int getPositionById(String id) {
		int posi = 0;
		// for (int i = 0; i < listaTar.size(); i++) {
		// String cat = listaTar.get(i);
		// if (cat.getId().equals(id)) {
		// posi = i;
		// break;
		// }
		// }
		return posi;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView text;
		ImageView icon;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.lista_opciones, null);
		}

		text = (TextView) convertView.findViewById(R.id.textOpciones);
		icon = (ImageView) convertView.findViewById(R.id.icon);
		text.setText(listaOp.get(position).toString());

		
		// Asignamos el tipo de fuente
		AssetManager am = contextG.getAssets();
		Typeface miPropiaTypeFace = Typeface.createFromAsset(am, "fonts/Berlin.ttf");
		text.setTypeface(miPropiaTypeFace);

		switch (position) {
		case 0:
			icon.setBackgroundResource(R.drawable.usuario);
			break;
		case 1:
			icon.setBackgroundResource(R.drawable.candado);
			break;
		case 2:
			icon.setBackgroundResource(R.drawable.database);
			break;
		case 3:
			icon.setBackgroundResource(R.drawable.valoracion);
			break;
		case 4:
			icon.setBackgroundResource(R.drawable.tarjetas);
			break;
		case 5:
			icon.setBackgroundResource(R.drawable.recibos);
			break;
		case 6:
			icon.setBackgroundResource(R.drawable.twitter);
			break;
		case 7:
			icon.setBackgroundResource(R.drawable.estadisticas);
			break;		
		}
		return convertView;
	}

}
