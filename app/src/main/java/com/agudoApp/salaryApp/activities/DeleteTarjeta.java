package com.agudoApp.salaryApp.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterTarjetas;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Tarjeta;

public class DeleteTarjeta extends Activity {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	static final int MENSAJE_OK = 1;
	static final int MENSAJE_ERROR = 2;
	static final int MENSAJE_CONFIRMACION = 3;
	static final int MENSAJE_ERROR_TAR_PRINCIPAL = 4;
	private ListView listaTarjetasAdapter;
	private Tarjeta tar;

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
				tar = (Tarjeta) parent.getItemAtPosition(position);
				onCreateDialog(MENSAJE_CONFIRMACION);
			}
		});
	}

	public void obtenerTarjetas() {
		ArrayList<Tarjeta> listTarjetas = new ArrayList<Tarjeta>();
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			// Recuperamos el listado del spinner Categorias
			listTarjetas = (ArrayList<Tarjeta>) gestion.getTarjetas(db);
		}
		db.close();
		listaTarjetasAdapter.setAdapter(new ListAdapterTarjetas(this,
				listTarjetas));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog alert;
		switch (id) {
		case MENSAJE_OK:
			builder.setMessage(getResources().getString(R.string.tarjetaDelete))
					.setTitle(getResources().getString(R.string.info))
					.setIcon(R.drawable.ic_alert)
					.setPositiveButton(
							getResources().getString(R.string.aceptar),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									DeleteTarjeta.this.setResult(0);
									finish();
								}
							});
			alert = builder.create();
			alert.show();
			break;
		case MENSAJE_ERROR:
			builder.setMessage(getResources().getString(R.string.deleteKO))
					.setTitle(getResources().getString(R.string.atencion))
					.setIcon(R.drawable.ic_alert)
					.setPositiveButton(
							getResources().getString(R.string.aceptar),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									DeleteTarjeta.this.setResult(0);
									finish();
								}
							});
			alert = builder.create();
			alert.show();
			break;
		case MENSAJE_ERROR_TAR_PRINCIPAL:
			builder.setMessage(
					getResources().getString(R.string.tarjetaPrincipal))
					.setTitle(getResources().getString(R.string.atencion))
					.setIcon(R.drawable.ic_alert)
					.setPositiveButton(
							getResources().getString(R.string.aceptar),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									DeleteTarjeta.this.setResult(0);
									finish();
								}
							});
			alert = builder.create();
			alert.show();
			break;
		case MENSAJE_CONFIRMACION:
			builder.setTitle("Eliminar tarjeta");
			builder.setIcon(R.drawable.ic_delete);
			builder.setMessage(getResources().getString(R.string.mnsTarjeta));
			builder.setCancelable(false);
			builder.setPositiveButton(
					getResources().getString(R.string.eliminar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							if (tar.getId().equals("0")) {
								onCreateDialog(MENSAJE_ERROR_TAR_PRINCIPAL);
							} else {
								boolean ok = false;
								db = openOrCreateDatabase(BD_NOMBRE, 1, null);
								if (db != null) {
									ok = gestion.deleteTarjeta(db, tar.getId());
								}
								db.close();
								if (ok) {
									Context context = getApplicationContext();
									CharSequence textMsg = getResources()
											.getString(R.string.tarjetaDelete);
									int duration = Toast.LENGTH_SHORT;
									Toast toast = Toast.makeText(context,
											textMsg, duration);
									toast.show();

									finish();
								} else {
									Context context = getApplicationContext();
									CharSequence textMsg = getResources()
											.getString(R.string.deleteKO);
									int duration = Toast.LENGTH_SHORT;
									Toast toast = Toast.makeText(context,
											textMsg, duration);
									toast.show();

									finish();
								}
							}
							dialog.cancel();
						}
					}).setNegativeButton(
					getResources().getString(R.string.cancelar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
							DeleteTarjeta.this.setResult(0);
							finish();
						}
					});
			alert = builder.create();
			alert.show();
			break;
		}
		return null;
	}
}