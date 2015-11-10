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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class NuevoAddCategoriaActivity extends AppCompatActivity {

    private final String BD_NOMBRE = "BDGestionGastos";
    static final int DATE_DIALOG_ID = 0;
    static final int IMPORTE = 1;

    private SQLiteDatabase db;
    final GestionBBDD gestion = new GestionBBDD();
    final int CATEGORIA_ICON = 1;
    ImageView icon;
    LinearLayout layoutAddIcon;
    EditText descripcion;
    private RelativeLayout layoutPubli;

    boolean isPremium;
    boolean isSinPublicidad;
    boolean isCategoria;

    int idIcon;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_add_categoria);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);

        icon = (ImageView) findViewById(R.id.iconCategoria);
        layoutAddIcon = (LinearLayout) findViewById(R.id.layoutAddIcon);
        descripcion = (EditText) findViewById(R.id.descripcion);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isCategoria = extras.getBoolean("isCategoria");
        }

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

                    boolean ok = false;

                    db = openOrCreateDatabase(BD_NOMBRE, 1, null);
                    if (db.isOpen()) {
                        if (isCategoria) {
                            ok = gestion.addCategoria(db, text.trim(), idIcon);
                        } else {
                            ok = gestion.addSubcategoria(db, text.trim(), idIcon);
                        }
                    }
                    db.close();

                    if (ok) {
                        resetIconoId();

                        Context context = getApplicationContext();
                        CharSequence textMsg = getResources().getString(
                                R.string.addCatOk);
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, textMsg, duration);
                        toast.show();

                        finish();
                    } else {
                        resetIconoId();

                        Context context = getApplicationContext();
                        CharSequence textMsg = getResources().getString(
                                R.string.addCatKO);
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

        layoutAddIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuevoAddCategoriaActivity.this, Categorias.class);
                intent.putExtra("flujo", "add");
                intent.putExtra("isPremium", false);
                startActivityForResult(intent, CATEGORIA_ICON);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        idIcon = getIconoId();
        icon.setBackgroundDrawable(getResources().getDrawable(
                Util.obtenerIconoCategoria(idIcon)));
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
}
