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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class NuevoEditCategoriaActivity extends AppCompatActivity {

    private final String BD_NOMBRE = "BDGestionGastos";

    static final int MENSAJE_CONFIRMACION_CATEGORIA = 1;
    static final int MENSAJE_CONFIRMACION_SUBCATEGORIA = 2;

    private SQLiteDatabase db;
    final GestionBBDD gestion = new GestionBBDD();
    final int CATEGORIA_ICON = 1;
    ImageView icon;
    LinearLayout layoutAddIcon;
    EditText descripcion;
    String descripcionAntigua;
    String idCategoria;
    private int idIconAntiguo;
    private int idIcon;
    Categoria cat;

    private RelativeLayout layoutPubli;

    boolean isPremium;
    boolean isSinPublicidad;
    boolean isCategoriaPremium;
    boolean isCategoria;

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
            idCategoria = extras.getString("idCategoria");
            isPremium = extras.getBoolean("isPremium", false);
            isSinPublicidad = extras.getBoolean("isSinPublicidad", false);
            isCategoriaPremium = extras.getBoolean("isCategoriaPremium", false);
        }

        db = openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            if (isCategoria) {
                cat = gestion.getCategoriaId(db, idCategoria);
            } else {
                cat = gestion.getSubcategoriaId(db, idCategoria);
            }
        }
        db.close();

        descripcion.setText(cat.toString());
        descripcionAntigua = cat.toString();
        idIcon = cat.getIdIcon();
        idIconAntiguo = cat.getIdIcon();
        icon.setBackgroundDrawable(getResources().getDrawable(
                Util.obtenerIconoCategoria(idIcon)));

        // Inflate the custom view and add click handlers for the buttons
        View actionBarButtons = getLayoutInflater().inflate(R.layout.edit_delete_actionbar,
                new LinearLayout(this), false);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        View deleteActionView = actionBarButtons.findViewById(R.id.action_cancel);
        deleteActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCategoria) {
                    onCreateDialog(MENSAJE_CONFIRMACION_CATEGORIA);
                } else {
                    onCreateDialog(MENSAJE_CONFIRMACION_SUBCATEGORIA);
                }
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
                    if (db != null) {
                        if (isCategoria) {
                            ok = gestion.editCategoria(db, text.trim(), idCategoria, idIcon);
                        } else {
                            ok = gestion.editSubcategoria(db, text.trim(), idCategoria, idIcon);
                        }
                    }
                    db.close();

                    if (ok) {
                        CharSequence textMsg;
                        Context context = getApplicationContext();
                        if (isCategoria) {
                            textMsg = getResources().getString(
                                    R.string.addCatOk);
                        } else {
                            textMsg = getResources().getString(
                                    R.string.catEditOK);
                        }
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, textMsg, duration);
                        toast.show();
                        setResult(RESULT_OK, getIntent());
                        finish();
                    } else {
                        CharSequence textMsg;
                        Context context = getApplicationContext();
                        if (isCategoria) {
                            textMsg = getResources().getString(R.string.addCatKO);
                        } else {
                            textMsg = getResources().getString(R.string.catEditKO);
                        }
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

        layoutAddIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuevoEditCategoriaActivity.this, Categorias.class);
                intent.putExtra("id", idCategoria);
                intent.putExtra("textEdit", descripcion.getText().toString());
                if (isCategoria) {
                    intent.putExtra("tipo", "categoria");
                }
                intent.putExtra("isPremium", isPremium);
                intent.putExtra("isCategoriaPremium", isCategoriaPremium);
                startActivityForResult(intent, CATEGORIA_ICON);
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        db = openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            if (isCategoria) {
                cat = gestion.getCategoriaId(db, idCategoria);
            } else {
                cat = gestion.getSubcategoriaId(db, idCategoria);
            }
        }
        db.close();
        idIcon = cat.getIdIcon();
        icon.setBackgroundDrawable(getResources().getDrawable(
                Util.obtenerIconoCategoria(idIcon)));

        super.onResume();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alert;
        switch (id) {
            case MENSAJE_CONFIRMACION_CATEGORIA:
                builder.setTitle(getResources().getString(R.string.eliminarCategoria));
                builder.setIcon(R.drawable.ic_delete);
                builder.setMessage(getResources().getString(R.string.msnEliminar));
                builder.setCancelable(false);
                builder.setPositiveButton(
                        getResources().getString(R.string.eliminar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean ok = false;

                                db = openOrCreateDatabase(BD_NOMBRE, 1, null);
                                if (db != null) {
                                    ok = gestion.deleteCategoria(db, idCategoria,
                                            "categoria");
                                }
                                db.close();

                                if (ok) {
                                    Context context = getApplicationContext();
                                    CharSequence textMsg = getResources().getString(R.string.catDeleteOK);
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, textMsg, duration);
                                    toast.show();
                                    setResult(RESULT_OK, getIntent());
                                    finish();
                                } else {
                                    Context context = getApplicationContext();
                                    CharSequence textMsg = getResources().getString(R.string.catDeleteKO);
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, textMsg, duration);
                                    toast.show();
                                    finish();
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
            case MENSAJE_CONFIRMACION_SUBCATEGORIA:
                builder.setTitle(getResources().getString(
                        R.string.eliminarSubcategoria));
                builder.setIcon(R.drawable.ic_delete);
                builder.setMessage(getResources().getString(R.string.msnEliminar));
                builder.setCancelable(false);
                builder.setPositiveButton(
                        getResources().getString(R.string.eliminar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean ok = false;

                                db = openOrCreateDatabase(BD_NOMBRE, 1, null);
                                if (db != null) {
                                    ok = gestion.deleteCategoria(db, idCategoria,
                                            "subcategoria");
                                }
                                db.close();

                                if (ok) {
                                    Context context = getApplicationContext();
                                    CharSequence textMsg = getResources().getString(R.string.subDeleteOK);
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, textMsg, duration);
                                    toast.show();
                                    setResult(RESULT_OK, getIntent());
                                    finish();
                                } else {
                                    Context context = getApplicationContext();
                                    CharSequence textMsg = getResources().getString(R.string.subDeleteKO);
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, textMsg, duration);
                                    toast.show();
                                    finish();
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
                db = openOrCreateDatabase(BD_NOMBRE, 1, null);
                if (db != null) {
                    if (isCategoria) {
                        boolean ok = gestion.editCategoria(db, descripcionAntigua, idCategoria, idIconAntiguo);
                    } else {
                        boolean ok = gestion.editSubcategoria(db, descripcionAntigua, idCategoria, idIconAntiguo);
                    }
                }
                db.close();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
