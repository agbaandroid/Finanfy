package com.agudoApp.salaryApp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.AddReciboActivity;
import com.agudoApp.salaryApp.activities.NuevoEditRecibosActivity;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Recibo;
import com.agudoApp.salaryApp.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Locale;

public class RegistrosFijosFragment extends Fragment {

	private static final String KEY_CONTENT = "RegistrosFijosFragment:Content";
	private final String BD_NOMBRE = "BDGestionGastos";
	final GestionBBDD gestion = new GestionBBDD();
	private SQLiteDatabase db;

	private final int RECIBOS = 1;

	ListView listaRecibos;
	LinearLayout layoutSinRegistro;
	LinearLayout layoutRecibos;
	private RelativeLayout layoutPubli;
	ListAdapterRecibos adapterRecibos;

	private int idCuenta;

	SharedPreferences prefs;
	SharedPreferences.Editor editor;

	// Productos que posee el usuario
	boolean isPremium = false;
	boolean isCategoriaPremium = false;
	boolean isSinPublicidad = false;

	public RegistrosFijosFragment(boolean isUserPremium, boolean isUserSinpublicidad,
								  boolean isUserCategoriaPremium) {
		isPremium = isUserPremium;
		isCategoriaPremium = isUserCategoriaPremium;
		isSinPublicidad = isUserSinpublicidad;
	}

	private String mContent = "???";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getString(KEY_CONTENT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.nuevo_recibos, container, false);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		//((FinanfyActivity)getActivity()).mostrarPublicidad(true, false);

		layoutRecibos = (LinearLayout) getView().findViewById(R.id.layoutRecibos);
		layoutSinRegistro = (LinearLayout) getView().findViewById(R.id.layoutSinRegistro);
		listaRecibos = (ListView) getView().findViewById(R.id.listaRecibos);

		layoutPubli = (RelativeLayout) getView().findViewById(R.id.layoutPubli);

		//Se carga la publicidad
		AdView adView = (AdView) getView().findViewById(R.id.adView);
		if (isPremium || isSinPublicidad) {
			layoutPubli.setVisibility(View.GONE);
		} else {
			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);
		}

		idCuenta = cuentaSeleccionada();
		obtenerRecibos();
	}

	public void obtenerRecibos() {
		ArrayList<Recibo> listRecibos = new ArrayList<Recibo>();
		db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			// Recuperamos el listado del spinner Categorias
			listRecibos = (ArrayList<Recibo>) gestion.getRecibos(db, idCuenta);
		}
		db.close();

		if(listRecibos.size()>0){
			adapterRecibos = new ListAdapterRecibos(getActivity(), listRecibos);

			listaRecibos.setAdapter(adapterRecibos);
			layoutSinRegistro.setVisibility(View.GONE);
			layoutRecibos.setVisibility(View.VISIBLE);
		}else{
			layoutSinRegistro.setVisibility(View.VISIBLE);
			layoutRecibos.setVisibility(View.GONE);
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_setting_mas, menu);
	}

	// Aadiendo funcionalidad a las opciones de men
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_add:
				Intent intent = new Intent(getActivity(), AddReciboActivity.class);
				intent.putExtra("isPremium", isPremium);
				intent.putExtra("isSinPublicidad", isSinPublicidad);
				startActivityForResult(intent, RECIBOS);
				return true;
			case android.R.id.home:
				getActivity().finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

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
			LinearLayout layoutRecibos;

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
			layoutRecibos = (LinearLayout) convertView.findViewById(R.id.layoutRecibos);

			cant = Float.parseFloat(listaRecibo.get(position).getCantidad());

			if(!listaRecibo.get(position).toString().equals("")) {
				txtDescripcion.setText(listaRecibo.get(position).toString());
			}else{
				txtDescripcion.setVisibility(View.GONE);
			}
			txtCant.setText(Util.formatear(cant, prefs));
			String fechaDesde = Util.formatearFecha(listaRecibo.get(position).getFechaIni().toString(), prefs);

			txtFechaDesde.setText("Desde: " + fechaDesde);

			if(listaRecibo.get(position).getnVeces() == 0){
				txtFechaHasta.setText("Hasta: " + getResources().getString(R.string.sinLimite));
				nVeces.setText("Meses: " + String.valueOf(listaRecibo.get(position).getnVeces()));
				nVeces.setVisibility(View.GONE);
			}else{
				String fechaHasta = Util.formatearFecha(listaRecibo.get(position).getFechaFin().toString(), prefs);
				txtFechaHasta.setText("Hasta: " + fechaHasta);
				nVeces.setText("Meses: " + String.valueOf(listaRecibo.get(position).getnVeces()));
				nVeces.setVisibility(View.VISIBLE);
			}

			imgView.setBackgroundResource(Util.obtenerIconoCategoria(listaRecibo.get(position).getIdIcon()));

			if (cant < 0) {
				txtCant.setTextColor(Color.RED);
			}else{
				txtCant.setTextColor(context.getResources().getColor(R.color.txtAzul));
			}

			// rellenamos el campo de las categorias
			if (!listaRecibo.get(position).getDescCategoria().equals("-")) {
				if(!listaRecibo.get(position).getDescSubcategoria().equals("-")){
					txtCategoria.setText(listaRecibo.get(position).getDescCategoria() +
					 " - " + listaRecibo.get(position).getDescSubcategoria());
				}else{
					txtCategoria.setText(listaRecibo.get(position).getDescCategoria());
				}
			} else {
				txtCategoria.setText(context.getResources().getString(R.string.otros));
			}

			if (listaRecibo.get(position).isTarjeta()) {
				layoutTarjeta.setVisibility(View.VISIBLE);
			}else{
				layoutTarjeta.setVisibility(View.GONE);
			}

			layoutRecibos.setTag(position);

			layoutRecibos.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					int posiSel = (int) v.getTag();
					Recibo rec = listaRecibo.get(posiSel);
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, NuevoEditRecibosActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("idRecibo", rec.getId());
					intent.putExtras(bundle);
					intent.putExtra("isPremium", isPremium);
					intent.putExtra("isSinPublicidad", isSinPublicidad);
					startActivityForResult(intent, RECIBOS);
				}
			});

			return convertView;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == getActivity().RESULT_OK) {
			switch (requestCode) {
				case RECIBOS :
					//((FinanfyActivity)getActivity()).mostrarPublicidad(true, false);
					break;
			}
		}
		obtenerRecibos();
	}

	public int cuentaSeleccionada() {
		prefs = getActivity().getSharedPreferences("ficheroConf",
				Context.MODE_PRIVATE);

		int idCuenta = prefs.getInt("cuenta", 0);
		return idCuenta;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(adapterRecibos != null) {
			adapterRecibos.notifyDataSetChanged();
		}
	}

}
