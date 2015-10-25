package com.agudoApp.salaryApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Recibo;

import java.util.ArrayList;
import java.util.Locale;

public class ListAdapterRecibos extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<Recibo> listaRecibo = new ArrayList<Recibo>();
	Locale locale = Locale.getDefault();
	String languaje = locale.getLanguage();
	Context context;

	public ListAdapterRecibos(Context context, ArrayList<Recibo> lista) {
		listaRecibo = lista;
		mInflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listaRecibo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listaRecibo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView txtNombre;
		TextView txtTipo;
		TextView txtMaximo;
		ImageView iconTarjeta;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.lista_tarjetas, null);
		}

		txtNombre = (TextView) convertView.findViewById(R.id.txtNombre);
		txtTipo = (TextView) convertView.findViewById(R.id.txtTipo);
		txtMaximo = (TextView) convertView.findViewById(R.id.txtMaximo);
		iconTarjeta = (ImageView) convertView.findViewById(R.id.iconTarjeta);

		txtNombre.setText(listaRecibo.get(position).toString());
		txtTipo.setText("ES_Débito");
		txtMaximo.setText("2000 €");

		return convertView;
	}

}
