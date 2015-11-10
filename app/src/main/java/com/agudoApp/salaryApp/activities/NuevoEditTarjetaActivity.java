package com.agudoApp.salaryApp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.agudoApp.salaryApp.model.Tarjeta;
import com.agudoApp.salaryApp.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class NuevoEditTarjetaActivity extends AppCompatActivity {

    static final int MENSAJE_CONFIRMACION = 1;
    static final int MENSAJE_ERROR_TAR_PRINCIPAL = 2;
    static final int IMPORTE = 1;
    private final String BD_NOMBRE = "BDGestionGastos";
    private SQLiteDatabase db;
    final GestionBBDD gestion = new GestionBBDD();

    EditText descripcion;
    Spinner spinnerTipo;
    Spinner spinnerIconTarjeta;
    TextView cantMax;

    String idTarjeta;
    Tarjeta tar = new Tarjeta();

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

        descripcion = (EditText) findViewById(R.id.descripcion);
        spinnerTipo = (Spinner) findViewById(R.id.spinnerTipo);
        spinnerIconTarjeta = (Spinner) findViewById(R.id.spinnerIconTarjeta);
        cantMax = (TextView) findViewById(R.id.cantMax);

        cargarSpinners();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idTarjeta = extras.getString("idTarjeta");
        }

        db = openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            tar = gestion.getTarjetaId(db, Integer.parseInt(idTarjeta));
        }
        db.close();

        descripcion.setText(tar.toString());
        spinnerTipo.setSelection(tar.getTipo());
        spinnerIconTarjeta.setSelection(tar.getIdIcon());

        cantMax.setText(Util.formatear(tar.getCantMax(), prefs));

        // Inflate the custom view and add click handlers for the buttons
        View actionBarButtons = getLayoutInflater().inflate(R.layout.edit_delete_actionbar,
                new LinearLayout(this), false);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        View deleteActionView = actionBarButtons.findViewById(R.id.action_cancel);
        deleteActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(MENSAJE_CONFIRMACION);
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
                    float limite = Util.formatearFloat(cantMax.getText().toString(), prefs);

                    boolean ok = false;
                    db = openOrCreateDatabase(BD_NOMBRE, 1, null);
                    if (db != null) {
                        ok = gestion.editTarjeta(db, text.trim(), idTarjeta, tipo, idIcon, limite);
                    }
                    db.close();
                    if (ok) {
                        Context context = getApplicationContext();
                        CharSequence textMsg = getResources().getString(R.string.tarjetaEdit);
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, textMsg, duration);
                        toast.show();

                        finish();
                    } else {
                        Context context = getApplicationContext();
                        CharSequence textMsg = getResources().getString(R.string.editCardKO);
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
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
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
                Intent intent = new Intent(NuevoEditTarjetaActivity.this, CantidadActivity.class);
                startActivityForResult(intent, IMPORTE);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alert;
        switch (id) {
            case MENSAJE_ERROR_TAR_PRINCIPAL:
                builder.setMessage(
                        getResources().getString(R.string.tarjetaPrincipal))
                        .setTitle(getResources().getString(R.string.atencion))
                        .setIcon(R.drawable.ic_alert)
                        .setPositiveButton(
                                getResources().getString(R.string.aceptar),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        finish();
                                    }
                                });
                alert = builder.create();
                alert.show();
                break;
            case MENSAJE_CONFIRMACION:
                builder.setTitle(getResources().getString(R.string.deleteCard));
                builder.setIcon(R.drawable.ic_delete);
                builder.setMessage(getResources().getString(R.string.mnsTarjeta));
                builder.setCancelable(false);
                builder.setPositiveButton(
                        getResources().getString(R.string.eliminar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (tar.getId().equals("0")) {
                                    onCreateDialog(MENSAJE_ERROR_TAR_PRINCIPAL);
                                } else {
                                    boolean ok = false;
                                    db = openOrCreateDatabase(BD_NOMBRE, 1, null);
                                    if (db != null) {
                                        ok = gestion.deleteTarjeta(db, tar.getId());
                                    }
                                    db.close();
                                    if (ok) {
                                        Context context = getApplicationContext();
                                        CharSequence textMsg = getResources()
                                                .getString(R.string.tarjetaDelete);
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, textMsg, duration);
                                        toast.show();

                                        finish();
                                    } else {
                                        Context context = getApplicationContext();
                                        CharSequence textMsg = getResources()
                                                .getString(R.string.deleteKO);
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, textMsg, duration);
                                        toast.show();

                                        finish();
                                    }
                                }
                                dialog.cancel();
                            }
                        }).setNegativeButton(
                        getResources().getString(R.string.cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });
                alert = builder.create();
                alert.show();
                break;
        }
        return null;
    }

    // Anadiendo funcionalidad a las opciones de menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LayoutInflater li = LayoutInflater.from(this);
        View view = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alert;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void cargarSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.tipoTarjeta,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner);
        spinnerTipo.setAdapter(adapter);

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
