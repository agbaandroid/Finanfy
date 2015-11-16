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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SeguridadRepetir extends AppCompatActivity {

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
    String pass1 = "";
    TextView textSeguridad;
    boolean isPremium = false;
    boolean isSinPublicidad = false;
    private RelativeLayout layoutPubli;

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pass1 = extras.getString("pass");
            isPremium = extras.getBoolean("isPremium", false);
            isSinPublicidad = extras.getBoolean("isSinPublicidad", false);
        }

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

        layoutPubli = (RelativeLayout) findViewById(R.id.layoutPubli);

        //Se carga la publicidad
        AdView adView = (AdView) findViewById(R.id.adView);
        if (isPremium || isSinPublicidad) {
            layoutPubli.setVisibility(View.GONE);
        } else {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        pass.setHint(getResources().getString(R.string.repetirPass));
        //textSeguridad.setText(getResources().getString(R.string.repetirPass));

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
                if (pass.getText().toString().equals(pass1)) {
                    editor.putBoolean("seguridadActivada", true);
                    editor.putString("contraseña", pass.getText().toString());
                    editor.commit();
                    finish();
                    Context context = getApplicationContext();
                    CharSequence text = getResources().getString(
                            R.string.passActivada);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    Intent in = new Intent(SeguridadRepetir.this,
                            SeguridadRepetir.class);
                    in.putExtra("pass", pass1);
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
