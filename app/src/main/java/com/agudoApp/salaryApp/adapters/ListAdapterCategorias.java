package com.agudoApp.salaryApp.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Categoria;

public class ListAdapterCategorias extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<Categoria> listaCat = new ArrayList<Categoria>();

	public ListAdapterCategorias(Context context, ArrayList<Categoria> lista) {
		listaCat = lista;
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
		TextView text;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.lista_cat, null);
		}

		text = (TextView) convertView.findViewById(R.id.textListaCategoria);
		text.setText(listaCat.get(position).toString());
		return convertView;
	}

}
