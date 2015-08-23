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
import android.widget.TextView;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.util.Util;

public class EditSubTexto extends Activity implements OnClickListener {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	private EditText editSub;
	private String id;
	private String textEdit;
	private TextView textTituloEditCat;
	private int idIconAntiguo;
	private int idIcon;
	private boolean isPremium = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
		// Abrimos la base de datos si no esta abierta
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		gestion = new GestionBBDD();
		editSub = (EditText) findViewById(R.id.cajaEditarCat);
		textTituloEditCat = (TextView) findViewById(R.id.textTituloEditCat);
		View botonEditSub = findViewById(R.id.botonEditarCat);
		botonEditSub.setOnClickListener(this);
		View botonEditCancelar = findViewById(R.id.botonEditarCancelar);
		botonEditCancelar.setOnClickListener(this);
		View imagenEdit = findViewById(R.id.imagenEdit);
		imagenEdit.setOnClickListener(this);
		
		textTituloEditCat.setText(getResources().getString(R.string.editarSubcategoria));

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id = extras.getString("id");
			textEdit = extras.getString("textEdit");
			idIconAntiguo = extras.getInt("idIcon");
			idIcon = extras.getInt("idIcon");
			isPremium = extras.getBoolean("isPremium", false);
		}

		editSub.setText(textEdit);
		imagenEdit.setBackgroundDrawable(this.getResources().getDrawable(
				Util.obtenerIconoCategoria(idIconAntiguo)));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.botonEditarCat:
			if (!"".equals(editSub.getText().toString())) {
				String text = "";

				for (int i = 0; i < editSub.getText().length(); i++) {
					if (i == 0) {
						text = text
								+ editSub.getText().toString().toUpperCase()
										.charAt(i);
					} else {
						text = text
								+ editSub.getText().toString().toLowerCase()
										.charAt(i);
					}
				}

				boolean ok = gestion.editSubcategoria(db, text.trim(), id,
						idIcon);
				if (ok) {
					Context context = getApplicationContext();
					CharSequence textMsg = getResources().getString(
							R.string.subEditOK);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, textMsg, duration);
					toast.show();
				} else {
					Context context = getApplicationContext();
					CharSequence textMsg = getResources().getString(
							R.string.subEditKO);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, textMsg, duration);
					toast.show();
				}
				finish();
			}
			break;
		case R.id.botonEditarCancelar:
			boolean ok = gestion.editSubcategoriaCancel(db, id, idIconAntiguo);
			finish();
			break;
		case R.id.imagenEdit:
			Intent intent = new Intent(this, Categorias.class);
			intent.putExtra("id", id);
			intent.putExtra("textEdit", textEdit);
			intent.putExtra("tipo", "subcategoria");
			intent.putExtra("isPremium", isPremium);			
			startActivity(intent);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Categoria cat = gestion.getSubcategoriaId(db, id);
		idIcon = cat.getIdIcon();
		ImageView imagen = (ImageView) findViewById(R.id.imagenEdit);
		imagen.setBackgroundDrawable(getResources().getDrawable(
				Util.obtenerIconoCategoria(idIcon)));
		super.onResume();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			boolean ok = gestion.editSubcategoriaCancel(db, id, idIconAntiguo);
			finish();
		}
		return true;
	}
}
