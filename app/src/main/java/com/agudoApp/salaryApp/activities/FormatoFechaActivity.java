package com.agudoApp.salaryApp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;

public class FormatoFechaActivity extends Activity {

	private RadioGroup radioGroupFormato;
	private RadioButton radioFormat1;
	private RadioButton radioFormat2;

	TextView botonCancelar;
	TextView botonAceptar;

	int formato;

	SharedPreferences prefs;
	SharedPreferences.Editor editor;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formato_fecha);

		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);

		formato = prefs.getInt("formatoFecha", 1);

		botonAceptar = (TextView) this.findViewById(R.id.botonAceptar);
		botonCancelar = (TextView) this.findViewById(R.id.botonCancelar);

		radioGroupFormato = (RadioGroup) findViewById(R.id.radioGroupFormato);
		radioFormat1 = (RadioButton) findViewById(R.id.radioFormat1);
		radioFormat2 = (RadioButton) findViewById(R.id.radioFormat2);

		if(formato == 1){
			radioFormat1.setChecked(true);
			radioFormat2.setChecked(false);
		}else if(formato == 2){
			radioFormat2.setChecked(true);
			radioFormat1.setChecked(false);
		}

		botonAceptar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editor = prefs.edit();
				editor.putInt("formatoFecha", formato);
				editor.commit();
				finish();
			}
		});

		radioGroupFormato.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radioFormat1) {
					formato = 1;
				} else if (checkedId == R.id.radioFormat2) {
					formato = 2;
				}
			}
		});

		botonCancelar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
