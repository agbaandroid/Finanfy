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

public class EditTarTexto extends Activity implements OnClickListener {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	static final int MENSAJE_OK = 1;
	static final int MENSAJE_ERROR = 2;
	private GestionBBDD gestion = new GestionBBDD();
	private EditText editTar;
	private String id;
	private String textEdit;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_tarjeta);
		gestion = new GestionBBDD();
		editTar = (EditText) findViewById(R.id.cajaEditarTar);
		View botonEditTar = findViewById(R.id.botonEditarTar);
		botonEditTar.setOnClickListener(this);
		View botonEditCancelarTar = findViewById(R.id.botonEditarCancelarTar);
		botonEditCancelarTar.setOnClickListener(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id = extras.getString("id");
			textEdit = extras.getString("textEdit");
		}

		editTar.setText(textEdit);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.botonEditarTar:
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
					//ok = gestion.editTarjeta(db, text.trim(), id);
				}
				db.close();
				if (ok) {
					Context context = getApplicationContext();
					CharSequence textMsg = getResources().getString(
							R.string.tarjetaEdit);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, textMsg, duration);
					toast.show();

					finish();
				} else {
					Context context = getApplicationContext();
					CharSequence textMsg = getResources().getString(
							R.string.editCardKO);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, textMsg, duration);
					toast.show();

					finish();
				}
			}
			break;
		case R.id.botonEditarCancelarTar:
			finish();
			break;
		}
	}
}
