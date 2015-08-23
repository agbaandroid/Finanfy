package com.agudoApp.salaryApp.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterTarjetas;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Tarjeta;

public class EditTarjeta extends Activity {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	private ListView listaTarjetasAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_delete_tarjetas);
		gestion = new GestionBBDD();
		listaTarjetasAdapter = (ListView) findViewById(R.id.listaTarjetas);
		obtenerTarjetas();

		listaTarjetasAdapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Tarjeta tar = (Tarjeta) parent.getItemAtPosition(position);
				Intent inTarEdit = new Intent(EditTarjeta.this,
						EditTarTexto.class);
				inTarEdit.putExtra("id", tar.getId());
				inTarEdit.putExtra("textEdit", tar.toString());				
				EditTarjeta.this.startActivityForResult(inTarEdit, 0);
				EditTarjeta.this.setResult(0);
				finish();
			}
		});
	}

	public void obtenerTarjetas() {
		ArrayList<Tarjeta> listTarjetas = new ArrayList<Tarjeta>();
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			// Recuperamos el listado del spinner Tarjetas
			listTarjetas = (ArrayList<Tarjeta>) gestion.getTarjetas(db);
		}
		db.close();
		listaTarjetasAdapter.setAdapter(new ListAdapterTarjetas(this,
				listTarjetas));
	}
}