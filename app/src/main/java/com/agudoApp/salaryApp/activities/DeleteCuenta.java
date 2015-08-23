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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterCuentas;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Cuenta;

public class DeleteCuenta extends Activity {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	static final int MENSAJE_CONFIRMACION = 3;
	static final int MENSAJE_ERROR_CUENTA_PRINCIPAL = 4;
	private ListView listaCuentasAdapter;
	private Cuenta cuenta;

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
				cuenta = (Cuenta) parent.getItemAtPosition(position);
				onCreateDialog(MENSAJE_CONFIRMACION);
			}
		});
	}

	public void obtenerCuentas() {
		ArrayList<Cuenta> listCuentas = new ArrayList<Cuenta>();
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			// Recuperamos el listado del spinner Categorias
			listCuentas = (ArrayList<Cuenta>) gestion.getCuentas(db);
		}
		db.close();
		listaCuentasAdapter
				.setAdapter(new ListAdapterCuentas(this, listCuentas));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog alert;
		switch (id) {
		case MENSAJE_ERROR_CUENTA_PRINCIPAL:
			builder.setMessage(
					getResources().getString(R.string.cuentaPrincipal))
					.setTitle(getResources().getString(R.string.atencion))
					.setIcon(R.drawable.ic_alert)
					.setPositiveButton(
							getResources().getString(R.string.aceptar),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									DeleteCuenta.this.setResult(0);
									finish();
								}
							});
			alert = builder.create();
			alert.show();
			break;
		case MENSAJE_CONFIRMACION:
			builder.setTitle(R.string.deleteCuenta);
			builder.setIcon(R.drawable.ic_delete);
			builder.setMessage(getResources().getString(
					R.string.deleteCuentaMns));
			builder.setCancelable(false);
			builder.setPositiveButton(
					getResources().getString(R.string.eliminar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							if (cuenta.getIdCuenta().equals("0")) {
								onCreateDialog(MENSAJE_ERROR_CUENTA_PRINCIPAL);
							} else {
								boolean ok = false;
								db = openOrCreateDatabase(BD_NOMBRE, 1, null);
								if (db != null) {
									ok = gestion.deleteCuenta(db,
											cuenta.getIdCuenta());
								}
								db.close();
								if (ok) {
									cuentaDefecto();

									Context context = getApplicationContext();
									CharSequence textMsg = getResources()
											.getString(R.string.deleteCuentaOK);
									int duration = Toast.LENGTH_SHORT;
									Toast toast = Toast.makeText(context,
											textMsg, duration);
									toast.show();

									finish();
								} else {
									Context context = getApplicationContext();
									CharSequence textMsg = getResources()
											.getString(R.string.deleteCuentaKO);
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
							DeleteCuenta.this.setResult(0);
							finish();
						}
					});
			alert = builder.create();
			alert.show();
			break;
		}
		return null;
	}

	public void cuentaDefecto() {
		SharedPreferences prefs;
		SharedPreferences.Editor editor;

		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);

		editor = prefs.edit();

		editor.putInt("cuenta", 0);
		editor.commit();
	}
}