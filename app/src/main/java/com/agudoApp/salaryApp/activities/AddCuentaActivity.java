package com.agudoApp.salaryApp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterIconCuenta;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Icon;
import com.agudoApp.salaryApp.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class AddCuentaActivity extends AppCompatActivity {

    private final String BD_NOMBRE = "BDGestionGastos";
    private SQLiteDatabase db;
    final GestionBBDD gestion = new GestionBBDD();

    EditText nombre;
    Spinner spinnerIconUser;

    boolean isPremium;
    boolean isSinPublicidad;
    private RelativeLayout layoutPubli;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_add_cuenta);

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

        nombre = (EditText) findViewById(R.id.nombre);
        spinnerIconUser = (Spinner) findViewById(R.id.spinnerIconCuenta);

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
                if (!"".equals(nombre.getText().toString())) {
                    String text = "";

                    for (int i = 0; i < nombre.getText().length(); i++) {
                        if (i == 0) {
                            text = text
                                    + nombre.getText().toString()
                                    .toUpperCase().charAt(i);
                        } else {
                            text = text
                                    + nombre.getText().toString()
                                    .toLowerCase().charAt(i);
                        }
                    }

                    int idIcon = 0;
                    idIcon = spinnerIconUser.getSelectedItemPosition();

                    boolean ok = false;
                    db = openOrCreateDatabase(BD_NOMBRE, 1, null);
                    if (db != null) {
                        ok = gestion.addCuenta(db, text.trim(), idIcon);
                    }
                    db.close();
                    if (ok) {
                        Context context = getApplicationContext();
                        CharSequence textMsg = getResources().getString(
                                R.string.addCuentaOK);
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, textMsg, duration);
                        toast.show();

                        finish();
                    } else {
                        Context context = getApplicationContext();
                        CharSequence textMsg = getResources().getString(
                                R.string.addCuentaKO);
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

    }

    public void cargarSpinners() {
        ArrayList<Icon> listIcon = Util.obtenerIconosCuenta();
        // Creamos el adaptador
        ListAdapterIconCuenta spinner_adapterIcont = new ListAdapterIconCuenta(
                this, listIcon);
        spinnerIconUser.setAdapter(spinner_adapterIcont);
    }

}
