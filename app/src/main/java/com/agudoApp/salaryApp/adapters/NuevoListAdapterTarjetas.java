package com.agudoApp.salaryApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Tarjeta;
import com.agudoApp.salaryApp.util.Util;

import java.util.ArrayList;
import java.util.Locale;

public class NuevoListAdapterTarjetas extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<Tarjeta> listaTar = new ArrayList<Tarjeta>();
	Locale locale = Locale.getDefault();
	String languaje = locale.getLanguage();
	Context context;

	public NuevoListAdapterTarjetas(Context context, ArrayList<Tarjeta> lista) {
		listaTar = lista;
		mInflater = LayoutInflater.from(context);
		this.context = context;
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

		txtNombre.setText(listaTar.get(position).toString());

		Tarjeta tar = listaTar.get(position);

		if(tar.getTipo() == 0){
			txtTipo.setText(context.getResources().getString(R.string.credito));
		}else{
			txtTipo.setText(context.getResources().getString(R.string.debito));
		}

		txtMaximo.setText(String.valueOf(tar.getCantMax()));
		iconTarjeta.setBackgroundResource(Util.obtenerIconoTarjeta(tar.getIdIcon()));

		return convertView;
	}

}
