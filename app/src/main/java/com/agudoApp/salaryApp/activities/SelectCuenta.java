package com.agudoApp.salaryApp.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterCuentas;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Cuenta;

public class SelectCuenta extends Activity {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	private ListView listaCuentasAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_delete_cuentas);
		gestion = new GestionBBDD();
		listaCuentasAdapter = (ListView) findViewById(R.id.listaCuentas);
		obtenerCuentas();

		listaCuentasAdapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cuenta cuenta = (Cuenta) parent.getItemAtPosition(position);
				seleccionarCuenta(cuenta.getIdCuenta());
				// inCuentaEdit.putExtra("id", cuenta.getIdCuenta());
				// inCuentaEdit.putExtra("textEdit", cuenta.toString());

				finish();
			}
		});
	}

	public void obtenerCuentas() {
		ArrayList<Cuenta> listCuentas = new ArrayList<Cuenta>();
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			// Recuperamos el listado del spinner Tarjetas
			listCuentas = (ArrayList<Cuenta>) gestion.getCuentas(db);
		}
		db.close();
		listaCuentasAdapter
				.setAdapter(new ListAdapterCuentas(this, listCuentas));
	}

	public void seleccionarCuenta(String idCuenta) {
		SharedPreferences prefs;
		SharedPreferences.Editor editor;

		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);

		editor = prefs.edit();

		editor.putInt("cuenta", Integer.parseInt(idCuenta));
		editor.commit();
	}
}