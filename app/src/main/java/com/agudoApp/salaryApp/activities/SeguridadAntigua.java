package com.agudoApp.salaryApp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;

public class SeguridadAntigua extends Activity {

	ImageView uno;
	ImageView dos;
	ImageView tres;
	ImageView cuatro;
	ImageView cinco;
	ImageView seis;
	ImageView siete;
	ImageView ocho;
	ImageView nueve;
	ImageView cero;
	ImageView ok;
	ImageView borrar;
	EditText pass;
	String contrasenia;
	TextView textSeguridad;

	SharedPreferences prefs;
	SharedPreferences.Editor editor;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seguridad);

		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
		editor = prefs.edit();
		contrasenia = prefs.getString("contraseña", "");

		uno = (ImageView) findViewById(R.id.uno);
		dos = (ImageView) findViewById(R.id.dos);
		tres = (ImageView) findViewById(R.id.tres);
		cuatro = (ImageView) findViewById(R.id.cuatro);
		cinco = (ImageView) findViewById(R.id.cinco);
		seis = (ImageView) findViewById(R.id.seis);
		siete = (ImageView) findViewById(R.id.siete);
		ocho = (ImageView) findViewById(R.id.ocho);
		nueve = (ImageView) findViewById(R.id.nueve);
		cero = (ImageView) findViewById(R.id.cero);
		pass = (EditText) findViewById(R.id.pass);
		ok = (ImageView) findViewById(R.id.ok);
		borrar = (ImageView) findViewById(R.id.borrar);

		textSeguridad = (TextView) findViewById(R.id.textSeguridad);
		textSeguridad.setText(getResources().getString(R.string.antiguaPass));

		uno.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pass.setText(pass.getText().toString() + "1");
			}
		});

		dos.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pass.setText(pass.getText().toString() + "2");
			}
		});

		tres.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pass.setText(pass.getText().toString() + "3");
			}
		});

		cuatro.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pass.setText(pass.getText().toString() + "4");
			}
		});

		cinco.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pass.setText(pass.getText().toString() + "5");
			}
		});

		seis.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pass.setText(pass.getText().toString() + "6");
			}
		});

		siete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pass.setText(pass.getText().toString() + "7");
			}
		});

		ocho.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pass.setText(pass.getText().toString() + "8");
			}
		});

		nueve.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pass.setText(pass.getText().toString() + "9");
			}
		});

		cero.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pass.setText(pass.getText().toString() + "0");
			}
		});

		ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (contrasenia.equals(pass.getText().toString())) {
					Intent in = new Intent(SeguridadAntigua.this,
							SeguridadIntroducir.class);
					finish();
					startActivity(in);
				} else {
					Intent in = new Intent(SeguridadAntigua.this,
							SeguridadAntigua.class);
					finish();
					startActivity(in);
				}
			}
		});

		borrar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (pass.getText().toString().length() > 0) {
					String passTemp = pass
							.getText()
							.toString()
							.substring(0,
									pass.getText().toString().length() - 1);
					pass.setText(passTemp);
				}
			}
		});
	}
}
