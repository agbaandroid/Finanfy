package com.agudoApp.salaryApp.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Recibo;

public class ListAdapterRecibos extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<Recibo> listaRec = new ArrayList<Recibo>();

	public ListAdapterRecibos(Context context, ArrayList<Recibo> lista) {
		listaRec = lista;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listaRec.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listaRec.get(position);
	}

	public int getPositionById(String id) {
		int posi = 0;
		for (int i = 0; i < listaRec.size(); i++) {
			Recibo rec = listaRec.get(i);
			if (rec.getId().equals(id)) {
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
			convertView = mInflater.inflate(R.layout.lista_rec, null);
		}

		text = (TextView) convertView.findViewById(R.id.textListaRecibos);
		text.setText(listaRec.get(position).toString());
		return convertView;
	}

}
