package com.agudoApp.salaryApp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Recibo;
import com.agudoApp.salaryApp.util.Util;

import java.util.ArrayList;
import java.util.Locale;

public class ListAdapterRecibos extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<Recibo> listaRecibo = new ArrayList<Recibo>();
	Locale locale = Locale.getDefault();
	Context context;
	SharedPreferences prefs;

	public ListAdapterRecibos(Context context, ArrayList<Recibo> lista) {
		listaRecibo = lista;
		mInflater = LayoutInflater.from(context);
		this.context = context;
		prefs = context.getSharedPreferences("ficheroConf",
				Context.MODE_PRIVATE);
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
		TextView txtFechaDesde;
		TextView txtFechaHasta;
		TextView txtCategoria;
		TextView txtDescripcion;
		TextView txtCant;
		TextView nVeces;
		ImageView imgView;
		LinearLayout layoutTarjeta;

		float cant = 0;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.lista_recibos, null);
		}

		txtFechaDesde = (TextView) convertView.findViewById(R.id.txtFechaDesde);
		txtFechaHasta = (TextView) convertView.findViewById(R.id.txtFechaHasta);
		txtCategoria = (TextView) convertView.findViewById(R.id.txtCategoria);
		txtDescripcion = (TextView) convertView.findViewById(R.id.txtDescripcion);
		txtCant = (TextView) convertView.findViewById(R.id.txtCant);
		nVeces = (TextView) convertView.findViewById(R.id.txtNVeces);
		imgView = (ImageView) convertView.findViewById(R.id.iconCategoria);
		layoutTarjeta = (LinearLayout) convertView.findViewById(R.id.layoutTarjeta);

		cant = Float.parseFloat(listaRecibo.get(position).getCantidad());

		if(!listaRecibo.get(position).toString().equals("")) {
			txtDescripcion.setText(listaRecibo.get(position).toString());
		}else{
			txtDescripcion.setVisibility(View.GONE);
		}
		txtCant.setText(Util.formatear(cant, prefs));
		txtFechaDesde.setText("Desde: " + listaRecibo.get(position).getFechaIni().toString());
		txtFechaHasta.setText("Hasta: " + listaRecibo.get(position).getFechaFin().toString());
		imgView.setBackgroundResource(Util.obtenerIconoCategoria(listaRecibo.get(position).getIdIcon()));
		nVeces.setText("Meses: " + String.valueOf(listaRecibo.get(position).getnVeces()));

		if (cant < 0) {
			txtCant.setTextColor(Color.RED);
		}else{
			txtCant.setTextColor(context.getResources().getColor(R.color.txtAzul));
		}

		// rellenamos el campo de las categorias
		if (!listaRecibo.get(position).getDescCategoria().equals("-")) {
			txtCategoria.setText(listaRecibo.get(position).getDescCategoria());
		} else {
			txtCategoria.setText(context.getResources().getString(R.string.otros));
		}

		if (listaRecibo.get(position).isTarjeta()) {
			layoutTarjeta.setVisibility(View.VISIBLE);
		}else{
			layoutTarjeta.setVisibility(View.GONE);
		}

		return convertView;
	}

}
