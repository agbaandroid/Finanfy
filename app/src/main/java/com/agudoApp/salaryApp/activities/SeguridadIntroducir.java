package com.agudoApp.salaryApp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;

public class SeguridadIntroducir extends AppCompatActivity {

	TextView uno;
	TextView dos;
	TextView tres;
	TextView cuatro;
	TextView cinco;
	TextView seis;
	TextView siete;
	TextView ocho;
	TextView nueve;
	TextView cero;
	LinearLayout ok;
	LinearLayout borrar;
	EditText pass;
	TextView textSeguridad;

	SharedPreferences prefs;
	SharedPreferences.Editor editor;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seguridad);

		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
		editor = prefs.edit();

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setContentInsetsAbsolute(0, 0);
		setSupportActionBar(toolbar);

		getSupportActionBar().setTitle(
				getResources().getString(R.string.tituloSeguridad));


		uno = (TextView) findViewById(R.id.uno);
		dos = (TextView) findViewById(R.id.dos);
		tres = (TextView) findViewById(R.id.tres);
		cuatro = (TextView) findViewById(R.id.cuatro);
		cinco = (TextView) findViewById(R.id.cinco);
		seis = (TextView) findViewById(R.id.seis);
		siete = (TextView) findViewById(R.id.siete);
		ocho = (TextView) findViewById(R.id.ocho);
		nueve = (TextView) findViewById(R.id.nueve);
		cero = (TextView) findViewById(R.id.cero);
		pass = (EditText) findViewById(R.id.pass);
		ok = (LinearLayout) findViewById(R.id.ok);
		borrar = (LinearLayout) findViewById(R.id.borrar);

		pass.setHint(getResources().getString(R.string.nuevaPass));
		//textSeguridad.setText(getResources().getString(R.string.nuevaPass));

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
				if (pass.getText().toString().length() >= 4) {
					Intent intent = new Intent(SeguridadIntroducir.this,
							SeguridadRepetir.class);
					intent.putExtra("pass", pass.getText().toString());
					startActivity(intent);
					finish();
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
