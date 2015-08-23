package com.agudoApp.salaryApp.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterRecibos;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Recibo;

public class DeleteRecibo extends Activity {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	static final int MENSAJE_CONFIRMACION = 3;
	static final int MENSAJE_ERROR_TAR_PRINCIPAL = 4;
	private ListView listaRecibosAdapter;
	private Recibo rec = new Recibo();
	SharedPreferences prefs;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_delete_recibos);
		gestion = new GestionBBDD();
		listaRecibosAdapter = (ListView) findViewById(R.id.listaRecibos);
		obtenerTarjetas();

		listaRecibosAdapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				rec = (Recibo) parent.getItemAtPosition(position);
				onCreateDialog(MENSAJE_CONFIRMACION);
			}
		});
	}

	public void obtenerTarjetas() {
		ArrayList<Recibo> listRecibos = new ArrayList<Recibo>();
		int idCuenta = cuentaSeleccionada();

		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			// Recuperamos el listado del spinner Recibos
			listRecibos = (ArrayList<Recibo>) gestion.getRecibos(db, idCuenta);
		}
		db.close();
		listaRecibosAdapter
				.setAdapter(new ListAdapterRecibos(this, listRecibos));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog alert;
		switch (id) {
		case MENSAJE_CONFIRMACION:
			builder.setTitle(getResources().getString(R.string.deleteRecibo));
			builder.setIcon(R.drawable.ic_delete);
			builder.setMessage(getResources().getString(
					R.string.eliminarReciboPreg));
			builder.setCancelable(false);
			builder.setPositiveButton(
					getResources().getString(R.string.eliminar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							boolean ok = false;
							db = openOrCreateDatabase(BD_NOMBRE, 1, null);
							if (db != null) {
								ok = gestion.deleteRecibo(db, rec.getId());
							}
							db.close();
							if (ok) {
								Context context = getApplicationContext();
								CharSequence textMsg = getResources()
										.getString(R.string.deleteReciboOK);
								int duration = Toast.LENGTH_SHORT;
								Toast toast = Toast.makeText(context, textMsg,
										duration);
								toast.show();

								finish();
							} else {
								Context context = getApplicationContext();
								CharSequence textMsg = getResources()
										.getString(R.string.deleteReciboKO);
								int duration = Toast.LENGTH_SHORT;
								Toast toast = Toast.makeText(context, textMsg,
										duration);
								toast.show();

								finish();
							}
							dialog.cancel();
						}
					}).setNegativeButton(
					getResources().getString(R.string.cancelar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
							DeleteRecibo.this.setResult(0);
							finish();
						}
					});
			alert = builder.create();
			alert.show();
			break;
		}
		return null;
	}

	public int cuentaSeleccionada() {
		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);

		int idCuenta = prefs.getInt("cuenta", 0);
		return idCuenta;
	}
}