package com.agudoApp.salaryApp.activities;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;

import java.util.Locale;

public class AddCuenta extends Activity implements OnClickListener {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	private EditText nombreCuenta;
	Locale locale = Locale.getDefault();
	String languaje = locale.getLanguage();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_cuenta);
		gestion = new GestionBBDD();
		nombreCuenta = (EditText) findViewById(R.id.cajaNombreCuenta);
		View botonAddSub = findViewById(R.id.botonCrearCuenta);
		botonAddSub.setOnClickListener(this);
		View botonCancelar = findViewById(R.id.botonCancelar);
		botonCancelar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.botonCrearCuenta:
			if (!"".equals(nombreCuenta.getText().toString())) {
				String text = "";

				for (int i = 0; i < nombreCuenta.getText().length(); i++) {
					if (i == 0) {
						text = text
								+ nombreCuenta.getText().toString()
										.toUpperCase().charAt(i);
					} else {
						text = text
								+ nombreCuenta.getText().toString()
										.toLowerCase().charAt(i);
					}
				}
				boolean ok = false;
				db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
				if (db != null) {
					//ok = gestion.addCuenta(db, text.trim());
				}
				db.close();
				if (ok) {
					this.setResult(0);

					Context context = getApplicationContext();
					CharSequence textMsg = getResources().getString(
							R.string.addCuentaOK);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, textMsg, duration);
					toast.show();

					finish();
				} else {
					Context context = getApplicationContext();
					CharSequence textMsg = getResources().getString(
							R.string.addCuentaKO);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, textMsg, duration);
					toast.show();

					finish();
				}
			}
			break;
		case R.id.botonCancelar:
			finish();
			break;
		}
	}
}
