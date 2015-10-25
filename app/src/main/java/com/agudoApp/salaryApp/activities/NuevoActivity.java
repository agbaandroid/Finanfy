package com.agudoApp.salaryApp.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterSpinner;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Tarjeta;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class NuevoActivity extends AppCompatActivity {

    private final String BD_NOMBRE = "BDGestionGastos";
    static final int DATE_DIALOG_ID = 0;
    static final int IMPORTE = 1;

    private LinearLayout btnGasto;
    private LinearLayout btnIngreso;
    private TextView txtGasto;
    private TextView txtIngreso;
    int tipoRegistro = 0;

    private LinearLayout layoutFecha;
    private LinearLayout layoutCant;
    private TextView txtFecha;
    private TextView txtCant;
    private EditText descripcion;

    private int mYear;
    private int mMonth;
    private int mDay;

    private Spinner spinnerCat;
    private Spinner spinnerSub;
    private Spinner spinnerTarjetas;
    private Spinner spinnerModoPago;

    private SQLiteDatabase db;
    final GestionBBDD gestion = new GestionBBDD();

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    // Productos que posee el usuario
    boolean isPremium = false;
    boolean isCategoriaPremium = false;
    boolean isSinPublicidad = false;
    boolean crearAnuncio = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_movimientos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);

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
                if (!txtCant.getText().toString().equals("0")) {
                    // Obtenemos la posicion del objeto seleccionado en los spinner
                    int posC = spinnerCat.getSelectedItemPosition();
                    int posS = spinnerSub.getSelectedItemPosition();
                    int modoPago = spinnerModoPago.getSelectedItemPosition();
                    int posTarjeta = spinnerTarjetas.getSelectedItemPosition();

                    int idCategoria = 0;
                    int idSubcategoria = 0;
                    int idTarjeta = 0;

                    int anio = 0;
                    int mes = 0;

                    boolean error = false;
                    Float cant = new Float(0);

                    // Obtenemos el objeto seleccionado en spinner partiendo de la
                    // pos

                    Categoria categoria = (Categoria) spinnerCat
                            .getItemAtPosition(posC);
                    // Obtenemos el id de los objetos seleccionados
                    idCategoria = Integer.parseInt(categoria.getId());


                    Categoria subcategoria = (Categoria) spinnerSub
                            .getItemAtPosition(posS);
                    // Obtenemos el id de los objetos seleccionados
                    idSubcategoria = Integer.parseInt(subcategoria.getId());


                    Tarjeta tarjetaAux = (Tarjeta) spinnerTarjetas
                            .getItemAtPosition(posTarjeta);
                    // Obtenemos el id de los objetos seleccionados
                    idTarjeta = Integer.parseInt(tarjetaAux.getId());

                    // Obtenemos el resto de datos
                    Date fecha = new Date(mYear - 1900, mMonth, mDay);

                    if (tipoRegistro == 0) {
                        cant = -1
                                * Float.parseFloat(txtCant.getText()
                                .toString());
                    } else {
                        cant = Float.parseFloat(txtCant.getText().toString());
                    }

                    // Si no hay error realizamos el insert
                    boolean tarjeta = false;

                    if (modoPago == 1) {
                        tarjeta = true;
                    }

                    // Introducimos el movimiento en BBDD
                    boolean ok = false;
                    int idCuenta = cuentaSeleccionada();

                    db = openOrCreateDatabase(BD_NOMBRE, 1, null);
                    if (db != null) {
                        ok = gestion
                                .insertarMovimiento(db, tipoRegistro, cant, descripcion.getText().toString().trim(),
                                        fecha, idCategoria, idSubcategoria,
                                        false, tarjeta, mes + 1, anio,
                                        idTarjeta, 99, idCuenta);
                    }
                    db.close();

                    if (ok) {
                        Context context = getApplicationContext();
                        CharSequence text = getResources()
                                .getString(R.string.guardarRegistroOK);
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        finish();
                    } else {
                        Context context = getApplicationContext();
                        CharSequence text = getResources()
                                .getString(R.string.guardarRegistroKO);
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }else{
                    Context context = getApplicationContext();
                    CharSequence text = "ES_Debe introducir una cantidad";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
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

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        txtCant = (TextView) findViewById(R.id.txtCant);
        btnGasto = (LinearLayout) findViewById(R.id.btnGasto);
        btnIngreso = (LinearLayout) findViewById(R.id.btnIngreso);
        txtGasto = (TextView) findViewById(R.id.txtGasto);
        txtIngreso = (TextView) findViewById(R.id.txtIngreso);
        descripcion = (EditText) findViewById(R.id.descripcion);

        layoutFecha = (LinearLayout) findViewById(R.id.layoutFecha);
        layoutCant = (LinearLayout) findViewById(R.id.layoutCant);
        txtFecha = (TextView) findViewById(R.id.txtFecha);

        spinnerCat = (Spinner) findViewById(
                R.id.spinnerCategoria);
        spinnerSub = (Spinner) findViewById(
                R.id.spinnerSubcategoria);
        spinnerTarjetas = (Spinner) findViewById(
                R.id.spinnerTarjetas);
        spinnerModoPago = (Spinner) findViewById(
                R.id.spinnerTipoPago);

        //Se carga la publicidad
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        updateDisplay();

        iniciarActivity();

        txtCant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(NuevoActivity.this, CantidadActivity.class);
                startActivityForResult(intent, IMPORTE);
            }
        });

        btnGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tipoRegistro = 0;
                btnGasto.setBackgroundResource(R.drawable.rounded_layout_azul);
                btnIngreso.setBackgroundResource(R.drawable.rounded_layout_gris);
                txtGasto.setTextColor(getResources().getColor(R.color.blanco));
                txtIngreso.setTextColor(getResources().getColor(R.color.txtGris));

            }
        });

        btnIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tipoRegistro = 2;
                btnIngreso.setBackgroundResource(R.drawable.rounded_layout_azul);
                btnGasto.setBackgroundResource(R.drawable.rounded_layout_gris);
                txtIngreso.setTextColor(getResources().getColor(R.color.blanco));
                txtGasto.setTextColor(getResources().getColor(R.color.txtGris));
            }
        });

        layoutFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onCreateDialog(DATE_DIALOG_ID);
            }
        });

        spinnerModoPago
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                               public void onItemSelected(AdapterView<?> parent,
                                                                          View view, int position, long id) {
                                                   // TODO Auto-generated method stub
                                                   if (position == 0) {
                                                       spinnerTarjetas.setVisibility(View.GONE);
                                                   } else {
                                                       spinnerTarjetas.setVisibility(View.VISIBLE);
                                                   }
                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> parent) {

                                               }
                                           }
                );


    }

    public void obtenerCategorias() {
        ArrayList<Categoria> listCategorias = new ArrayList<Categoria>();
        db = openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            // Recuperamos el listado del spinner Categorias
            listCategorias = (ArrayList<Categoria>) gestion.getCategoriasEditDelete(db,
                    "Categorias", "idCategoria");
        }
        db.close();

        // Creamos el adaptador
        ListAdapterSpinner spinner_adapterCat = new ListAdapterSpinner(
                this, R.layout.spinner_iconos,
                listCategorias);

        spinnerCat.setAdapter(spinner_adapterCat);
    }

    public void iniciarActivity() {
        obtenerCategorias();
        obtenerSubcategorias();
        obtenerTarjetas();
        obtenerModoPago();
    }

    private void updateDisplay() {
        txtFecha.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(mDay).append("-").append(mMonth + 1).append("-")
                .append(mYear).append(" "));
    }

    public void obtenerTarjetas() {
        ArrayList<Tarjeta> listTarjetas = new ArrayList<Tarjeta>();
        db = openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            // Recuperamos el listado del spinner Categorias
            listTarjetas = (ArrayList<Tarjeta>) gestion.getTarjetas(db);
        }
        db.close();
        // Creamos la lista
        LinkedList<Tarjeta> listaTarjetas = new LinkedList<Tarjeta>();
        // Rellenamos la lista
        for (int i = 0; i < listTarjetas.size(); i++) {
            Tarjeta tarjeta = new Tarjeta();
            tarjeta = listTarjetas.get(i);
            listaTarjetas.add(tarjeta);
        }
        // Creamos el adaptador
        ArrayAdapter<Tarjeta> spinner_adapterTar = new ArrayAdapter<Tarjeta>(
                this, android.R.layout.simple_spinner_item,
                listaTarjetas);
        // Aadimos el layout para el men y se lo damos al spinner
        spinner_adapterTar.setDropDownViewResource(R.layout.spinner);
        spinnerTarjetas.setAdapter(spinner_adapterTar);
    }

    public void obtenerSubcategorias() {
        ArrayList<Categoria> listSubcategorias = new ArrayList<Categoria>();
        db = openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            listSubcategorias = (ArrayList<Categoria>) gestion.getCategorias(
                    db, "Subcategorias", "idSubcategoria", this);
        }
        db.close();

        // Creamos el adaptador
        ListAdapterSpinner spinner_adapterSubcat = new ListAdapterSpinner(
                this, R.layout.spinner_iconos,
                listSubcategorias);
        spinnerSub.setAdapter(spinner_adapterSubcat);
    }

    // Rellena el spinner modo pago
    public void obtenerModoPago() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.modoPago,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner);
        spinnerModoPago.setAdapter(adapter);

        spinnerModoPago.setSelection(0);
    }


    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alert;
        switch (id) {
            case DATE_DIALOG_ID:
                DatePickerDialog datadialog = new DatePickerDialog(
                        this, mDateSetListener, mYear, mMonth, mDay);
                datadialog.show();
                break;
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMPORTE:
                    String importe = data.getExtras().getString("importe");
                    txtCant.setText(importe);
                    break;
            }
        }
    }

    public int cuentaSeleccionada() {
        prefs = getSharedPreferences("ficheroConf",
                Context.MODE_PRIVATE);

        int idCuenta = prefs.getInt("cuenta", 0);
        return idCuenta;
    }

}
