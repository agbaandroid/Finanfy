package com.agudoApp.salaryApp.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Tarjeta;

public class ListAdapterTarjetas extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<Tarjeta> listaTar = new ArrayList<Tarjeta>();

	public ListAdapterTarjetas(Context context, ArrayList<Tarjeta> lista) {
		listaTar = lista;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listaTar.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listaTar.get(position);
	}

	public int getPositionById(String id) {
		int posi = 0;
		for (int i = 0; i < listaTar.size(); i++) {
			Tarjeta tar = listaTar.get(i);
			if (tar.getId().equals(id)) {
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
		TextView text;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.lista_tar, null);
		}

		text = (TextView) convertView.findViewById(R.id.textListaTarjetas);
		text.setText(listaTar.get(position).toString());
		return convertView;
	}

}
