package com.agudoApp.salaryApp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterIconTarjeta;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Icon;
import com.agudoApp.salaryApp.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class NuevoAddTarjetaActivity extends AppCompatActivity {

    private final String BD_NOMBRE = "BDGestionGastos";
    private SQLiteDatabase db;
    final GestionBBDD gestion = new GestionBBDD();

    static final int IMPORTE = 1;

    EditText descripcion;
    Spinner spinnerTipo;
    Spinner spinnerIconTarjeta;
    TextView cantMax;
    private RelativeLayout layoutPubli;

    // Productos que posee el usuario
    boolean isPremium = false;
    boolean isSinPublicidad = false;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_add_tarjeta);

        prefs = getSharedPreferences("ficheroConf",
                Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isPremium = extras.getBoolean("isPremium", false);
            isSinPublicidad = extras.getBoolean("isSinPublicidad", false);
        }

        descripcion = (EditText) findViewById(R.id.descripcion);
        spinnerTipo = (Spinner) findViewById(R.id.spinnerTipo);
        spinnerIconTarjeta = (Spinner) findViewById(R.id.spinnerIconTarjeta);
        cantMax = (TextView) findViewById(R.id.cantMax);

        cargarSpinners();

        // Inflate the custom view and add click handlers for the buttons
        View actionBarButtons = getLayoutInflater().inflate(R.layout.accept_cancel_actionbar,
                new LinearLayout(this), false);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        View cancelActionView = actionBarButtons.findViewById(R.id.action_cancel);
        cancelActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View doneActionView = actionBarButtons.findViewById(R.id.action_done);
        doneActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(descripcion.getText().toString())) {
                    String text = "";

                    for (int i = 0; i < descripcion.getText().length(); i++) {
                        if (i == 0) {
                            text = text
                                    + descripcion.getText().toString().toUpperCase()
                                    .charAt(i);
                        } else {
                            text = text
                                    + descripcion.getText().toString().toLowerCase()
                                    .charAt(i);
                        }
                    }
                    int tipo = spinnerTipo.getSelectedItemPosition();
                    int idIcon = spinnerIconTarjeta.getSelectedItemPosition();

                    boolean ok = false;
                    db = openOrCreateDatabase(BD_NOMBRE, 1, null);
                    if (db != null) {
                        ok = gestion.addTarjeta(db, text.trim(), Util.formatearFloat(cantMax.getText().toString(), prefs), tipo, idIcon);
                    }
                    db.close();
                    if (ok) {
                        Context context = getApplicationContext();
                        CharSequence textMsg = getResources().getString(R.string.tarjetaOK);
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, textMsg, duration);
                        toast.show();
                        setResult(RESULT_OK, getIntent());
                        finish();
                    } else {
                        Context context = getApplicationContext();
                        CharSequence textMsg = getResources().getString(R.string.addCardKO);
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, textMsg, duration);
                        toast.show();

                        finish();
                    }
                }
            }
        });

        // Hide the icon, title and home/up button
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        // Set the custom view and allow the bar to show it
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL;
        getSupportActionBar().setCustomView(actionBarButtons, layoutParams);

        layoutPubli = (RelativeLayout) findViewById(R.id.layoutPubli);

        //Se carga la publicidad
        AdView adView = (AdView) findViewById(R.id.adView);
        if (isPremium || isSinPublicidad) {
            layoutPubli.setVisibility(View.GONE);
        } else {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        cantMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(NuevoAddTarjetaActivity.this, CantidadActivity.class);
                startActivityForResult(intent, IMPORTE);
            }
        });
    }

    public void cargarSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.tipoTarjeta,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner);
        spinnerTipo.setAdapter(adapter);

        spinnerTipo.setSelection(0);

        ArrayList<Icon> listIcon = Util.obtenerIconosTarjetas();
        // Creamos el adaptador
        ListAdapterIconTarjeta spinner_adapterIcont = new ListAdapterIconTarjeta(
                this, listIcon);
        spinnerIconTarjeta.setAdapter(spinner_adapterIcont);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMPORTE:
                    String importe = data.getExtras().getString("importe");
                    cantMax.setText(Util.formatear(Float.parseFloat(importe), prefs));
                    break;
            }
        }
    }
}
