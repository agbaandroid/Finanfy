package com.agudoApp.salaryApp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.util.Util;

public class AddSub extends Activity implements OnClickListener {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	static final int MENSAJE_OK = 1;
	static final int MENSAJE_KO = 2;
	private EditText editCat;
	private TextView textTituloAddCat;
	private int idIcon = 0;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	boolean isPremium = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		// Abrimos la base de datos si no esta abierta
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		gestion = new GestionBBDD();
		editCat = (EditText) findViewById(R.id.cajaNombreCat);
		textTituloAddCat = (TextView) findViewById(R.id.textTituloAddCat);
		View botonAddSub = findViewById(R.id.botonCrearCat);
		botonAddSub.setOnClickListener(this);
		View botonCancelar = findViewById(R.id.botonCancelar);
		botonCancelar.setOnClickListener(this);
		View imagenAdd = findViewById(R.id.imagenAdd);
		imagenAdd.setOnClickListener(this);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			isPremium = extras.getBoolean("isPremium", false);
		}
		
		textTituloAddCat.setText(getResources().getString(R.string.aniadirSubcategoria));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.botonCrearCat:
			if (!"".equals(editCat.getText().toString())) {
				String text = "";

				for (int i = 0; i < editCat.getText().length(); i++) {
					if (i == 0) {
						text = text
								+ editCat.getText().toString().toUpperCase()
										.charAt(i);
					} else {
						text = text
								+ editCat.getText().toString().toLowerCase()
										.charAt(i);
					}
				}

				boolean ok = gestion.addSubcategoria(db, text.trim(), idIcon);
				if (ok) {
					this.setResult(1);
					resetIconoId();

					Context context = getApplicationContext();
					CharSequence textMsg = getResources().getString(
							R.string.addSubOK);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, textMsg, duration);
					toast.show();

					finish();
				} else {
					resetIconoId();

					Context context = getApplicationContext();
					CharSequence textMsg = getResources().getString(
							R.string.addSubKO);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, textMsg, duration);
					toast.show();

					finish();
				}
			}
			break;
		case R.id.botonCancelar:
			resetIconoId();
			finish();
			break;
		case R.id.imagenAdd:
			Intent intent = new Intent(this, Categorias.class);
			intent.putExtra("flujo", "add");
			intent.putExtra("isPremium", isPremium);			
			startActivity(intent);
		}
	}

	public int getIconoId() {
		prefs = this.getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
		int idCuenta = prefs.getInt("idIcon", 0);
		return idCuenta;
	}

	public void resetIconoId() {
		prefs = this.getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
		editor = prefs.edit();
		editor.putInt("idIcon", 0);
		editor.commit();
	}

	@Override
	protected void onResume() {
		idIcon = getIconoId();
		ImageView imagen = (ImageView) findViewById(R.id.imagenAdd);
		imagen.setBackgroundDrawable(getResources().getDrawable(
				Util.obtenerIconoCategoria(idIcon)));
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		resetIconoId();
		super.onDestroy();
	}
}
