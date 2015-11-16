package com.agudoApp.salaryApp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.AddCuenta;
import com.agudoApp.salaryApp.activities.DeleteCuenta;
import com.agudoApp.salaryApp.activities.EditCuenta;
import com.agudoApp.salaryApp.activities.SelectCuenta;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Cuenta;

public class CuentasFragment extends Fragment {
	private static final String KEY_CONTENT = "CuentasFragment:Content";
	private final String BD_NOMBRE = "BDGestionGastos";
	final GestionBBDD gestion = new GestionBBDD();
	private SQLiteDatabase db;

	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	private LinearLayout layoutAddCuenta;
	private LinearLayout layoutSelectCuenta;
	private LinearLayout layoutDeleteCuenta;
	private RelativeLayout layoutUser1;
	private TextView textCuenta;
	private TextView cuentaSeleccionada;
	private TextView txtMasOpciones;
	private TextView txtEditUser;
	private TextView textAddUser;
	private TextView txtDeleteUser;

	public static CuentasFragment newInstance(String content) {
		CuentasFragment fragment = new CuentasFragment();

		return fragment;
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

		return inflater.inflate(R.layout.cuentas, container, false);
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

		db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);

		layoutAddCuenta = (LinearLayout) this.getView().findViewById(
				R.id.layoutAddUser);
		layoutSelectCuenta = (LinearLayout) this.getView().findViewById(
				R.id.layoutEditUser);
		layoutDeleteCuenta = (LinearLayout) this.getView().findViewById(
				R.id.layoutDeleteUser);
		layoutUser1 = (RelativeLayout) this.getView().findViewById(
				R.id.layoutUser1);
		textCuenta = (TextView) this.getView().findViewById(
				R.id.txtCuentaSeleccionada);
		cuentaSeleccionada = (TextView) this.getView().findViewById(
				R.id.cuentaSeleccionada);
		txtMasOpciones = (TextView) this.getView().findViewById(
				R.id.txtMasOpciones);
		txtEditUser = (TextView) this.getView().findViewById(R.id.txtEditUser);
		textAddUser = (TextView) this.getView().findViewById(R.id.txtAddUser);
		txtDeleteUser = (TextView) this.getView().findViewById(
				R.id.txtDeleteUser);


		cargarCuenta();

		layoutAddCuenta.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent in = new Intent(getActivity(), AddCuenta.class);
				startActivity(in);
			}
		});

		layoutSelectCuenta.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent in = new Intent(getActivity(), SelectCuenta.class);
				startActivity(in);
			}
		});

		layoutDeleteCuenta.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent in = new Intent(getActivity(), DeleteCuenta.class);
				startActivity(in);
			}
		});

		layoutUser1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent in = new Intent(getActivity(), EditCuenta.class);
				startActivity(in);
			}
		});
	}

	// Aadiendo las opciones de men
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_setting, menu);
	}

	public void cargarCuenta() {
		int cuent = cuentaSeleccionada();
		db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			Cuenta cuenta = gestion.getCuentaSeleccionada(db, cuent);
			textCuenta.setText(cuenta.getDescCuenta());
		}
		db.close();
	}

	@Override
	public void onResume() {
		cargarCuenta();
		super.onResume();
	}
	
	public int cuentaSeleccionada() {
		prefs = getActivity().getSharedPreferences("ficheroConf",
				Context.MODE_PRIVATE);

		int idCuenta = prefs.getInt("cuenta", 0);
		return idCuenta;
	}
}
