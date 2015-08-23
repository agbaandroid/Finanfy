package com.agudoApp.salaryApp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;

public class DeleteSub extends Activity {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	static final int MENSAJE_CONFIRMACION = 3;
	private String idSub;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_delete);
		// Abrimos la base de datos si no esta abierta
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		gestion = new GestionBBDD();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			idSub = extras.getString("id");
		}

		onCreateDialog(MENSAJE_CONFIRMACION);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog alert;
		switch (id) {
		case MENSAJE_CONFIRMACION:
			builder.setTitle(getResources().getString(
					R.string.eliminarSubcategoria));
			builder.setIcon(R.drawable.ic_delete);
			builder.setMessage(getResources().getString(R.string.msnEliminar));
			builder.setCancelable(false);
			builder.setPositiveButton(
					getResources().getString(R.string.eliminar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							boolean ok = gestion.deleteCategoria(db, idSub,
									"subcategoria");
							if (ok) {
								Context context = getApplicationContext();
								CharSequence textMsg = getResources()
										.getString(R.string.subDeleteOK);
								int duration = Toast.LENGTH_SHORT;
								Toast toast = Toast.makeText(context, textMsg,
										duration);
								toast.show();
								finish();
							} else {
								Context context = getApplicationContext();
								CharSequence textMsg = getResources()
										.getString(R.string.subDeleteKO);
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
							DeleteSub.this.setResult(1);
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