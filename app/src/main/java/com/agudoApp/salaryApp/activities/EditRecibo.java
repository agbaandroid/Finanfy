package com.agudoApp.salaryApp.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterRecibos;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Recibo;

public class EditRecibo extends Activity {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	private ListView listaRecibosAdapter;
	SharedPreferences prefs;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_delete_recibos);
		gestion = new GestionBBDD();
		listaRecibosAdapter = (ListView) findViewById(R.id.listaRecibos);
		obtenerRecibos();

		listaRecibosAdapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Recibo tar = (Recibo) parent.getItemAtPosition(position);
				Intent inRecEdit = new Intent(EditRecibo.this,
						EditRecTexto.class);
				inRecEdit.putExtra("id", tar.getId());
				EditRecibo.this.startActivityForResult(inRecEdit, 0);
				EditRecibo.this.setResult(0);
				finish();
			}
		});
	}

	public void obtenerRecibos() {
		ArrayList<Recibo> listRecibos = new ArrayList<Recibo>();
		int idCuenta = cuentaSeleccionada();
		
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			// Recuperamos el listado del spinner Recibos
			listRecibos = (ArrayList<Recibo>) gestion.getRecibos(db, idCuenta);
		}
		db.close();
		listaRecibosAdapter.setAdapter(new ListAdapterRecibos(this,
				listRecibos));
	}
	
	public int cuentaSeleccionada() {
		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);

		int idCuenta = prefs.getInt("cuenta", 0);
		return idCuenta;
	}
}