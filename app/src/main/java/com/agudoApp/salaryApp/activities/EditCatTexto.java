package com.agudoApp.salaryApp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.util.Util;

public class EditCatTexto extends Activity implements OnClickListener {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	static final int MENSAJE_OK = 1;
	static final int MENSAJE_ERROR = 2;
	private GestionBBDD gestion = new GestionBBDD();
	private EditText editCat;
	private String id;
	private String textEdit;
	private int idIconAntiguo;
	private int idIcon;
	private boolean isPremium = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
		// Abrimos la base de datos si no esta abierta
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		gestion = new GestionBBDD();
		editCat = (EditText) findViewById(R.id.cajaEditarCat);

		View botonEditCat = findViewById(R.id.botonEditarCat);
		botonEditCat.setOnClickListener(this);
		View botonEditCancelar = findViewById(R.id.botonEditarCancelar);
		botonEditCancelar.setOnClickListener(this);
		View imagenEdit = findViewById(R.id.imagenEdit);
		imagenEdit.setOnClickListener(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id = extras.getString("id");
			textEdit = extras.getString("textEdit");
			idIconAntiguo = extras.getInt("idIcon");
			idIcon = extras.getInt("idIcon");
			isPremium = extras.getBoolean("isPremium", false);
		}

		editCat.setText(textEdit);
		imagenEdit.setBackgroundDrawable(this.getResources().getDrawable(
				Util.obtenerIconoCategoria(idIconAntiguo)));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.botonEditarCat:
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

				boolean ok = gestion.editCategoria(db, text.trim(), id, idIcon);
				if (ok) {
					Context context = getApplicationContext();
					CharSequence textMsg = getResources().getString(
							R.string.catEditOK);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, textMsg, duration);
					toast.show();
				} else {
					Context context = getApplicationContext();
					CharSequence textMsg = getResources().getString(
							R.string.catEditKO);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, textMsg, duration);
					toast.show();
				}
				finish();
			}
			break;
		case R.id.botonEditarCancelar:
			boolean ok = gestion.editCategoriaCancel(db, id, idIconAntiguo);
			finish();
			break;
		case R.id.imagenEdit:
			Intent intent = new Intent(this, Categorias.class);
			intent.putExtra("id", id);
			intent.putExtra("textEdit", textEdit);
			intent.putExtra("tipo", "categoria");			
			intent.putExtra("isPremium", isPremium);
			startActivity(intent);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Categoria cat = gestion.getCategoriaId(db, id);
		idIcon = cat.getIdIcon();
		ImageView imagen = (ImageView) findViewById(R.id.imagenEdit);
		imagen.setBackgroundDrawable(getResources().getDrawable(
				Util.obtenerIconoCategoria(idIcon)));
		super.onResume();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			boolean ok = gestion.editCategoriaCancel(db, id, idIconAntiguo);
			finish();
		}
		return true;
	}
}
