package com.agudoApp.salaryApp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Cuenta;

public class EditCuenta extends Activity implements OnClickListener {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";	
	private GestionBBDD gestion = new GestionBBDD();
	private EditText editCuenta;
	private String textEdit;
	private String idCuenta;
	SharedPreferences prefs;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_cuenta);
		// Abrimos la base de datos si no esta abierta
		gestion = new GestionBBDD();
		editCuenta = (EditText) findViewById(R.id.cajaEditarCuenta);
		View botonEditCuenta = findViewById(R.id.botonEditarCuenta);
		botonEditCuenta.setOnClickListener(this);
		View botonEditCancelar = findViewById(R.id.botonEditarCancelar);
		botonEditCancelar.setOnClickListener(this);
		Cuenta cuenta = new Cuenta();

		int cuen = cuentaSeleccionada();
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			cuenta = gestion.getCuentaSeleccionada(db, cuen);
		}
		db.close();

		// Recuperar cuenta seleccionada
		idCuenta = cuenta.getIdCuenta();
		textEdit = cuenta.getDescCuenta();

		editCuenta.setText(textEdit);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.botonEditarCuenta:
			if (!"".equals(editCuenta.getText().toString())) {
				String text = "";

				for (int i = 0; i < editCuenta.getText().length(); i++) {
					if (i == 0) {
						text = text
								+ editCuenta.getText().toString().toUpperCase()
										.charAt(i);
					} else {
						text = text
								+ editCuenta.getText().toString().toLowerCase()
										.charAt(i);
					}
				}

				boolean ok = false;
				db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
				if (db != null) {
					ok = gestion.editCuenta(db, text.trim(), idCuenta);
				}
				db.close();

				if (ok) {
					Context context = getApplicationContext();
					CharSequence textMsg = getResources().getString(
							R.string.editCuentaOK);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, textMsg, duration);
					toast.show();

					finish();
				} else {
					Context context = getApplicationContext();
					CharSequence textMsg = getResources().getString(
							R.string.editCuentaKO);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, textMsg, duration);
					toast.show();

					finish();
				}
			}
			break;
		case R.id.botonEditarCancelar:
			finish();
			break;
		}
	}
	
	public int cuentaSeleccionada() {
		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);

		int idCuenta = prefs.getInt("cuenta", 0);
		return idCuenta;
	}
}
