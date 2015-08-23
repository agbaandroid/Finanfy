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

public class AddTarjeta extends Activity implements OnClickListener {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	private EditText editTar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_tarjeta);
		gestion = new GestionBBDD();
		editTar = (EditText) findViewById(R.id.cajaNombreTarjeta);
		View botonAddSub = findViewById(R.id.botonCrearTar);
		botonAddSub.setOnClickListener(this);
		View botonCancelar = findViewById(R.id.botonCancelarTar);
		botonCancelar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.botonCrearTar:
			if (!"".equals(editTar.getText().toString())) {
				String text = "";

				for (int i = 0; i < editTar.getText().length(); i++) {
					if (i == 0) {
						text = text
								+ editTar.getText().toString().toUpperCase()
										.charAt(i);
					} else {
						text = text
								+ editTar.getText().toString().toLowerCase()
										.charAt(i);
					}
				}
				boolean ok = false;
				db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
				if (db != null) {
					ok = gestion.addTarjeta(db, text.trim(), 0);
				}
				db.close();
				if (ok) {
					this.setResult(0);

					Context context = getApplicationContext();
					CharSequence textMsg = getResources().getString(
							R.string.tarjetaOK);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, textMsg, duration);
					toast.show();

					finish();
				} else {
					Context context = getApplicationContext();
					CharSequence textMsg = getResources().getString(
							R.string.addCardKO);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, textMsg, duration);
					toast.show();

					finish();
				}
			}
			break;
		case R.id.botonCancelarTar:
			finish();
			break;
		}
	}
}
