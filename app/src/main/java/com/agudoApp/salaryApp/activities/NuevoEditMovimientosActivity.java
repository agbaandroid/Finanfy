package com.agudoApp.salaryApp.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterSpinner;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Movimiento;
import com.agudoApp.salaryApp.model.Tarjeta;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;

public final class NuevoEditMovimientosActivity extends AppCompatActivity {

    private final String BD_NOMBRE = "BDGestionGastos";
    static final int DATE_DIALOG_ID = 0;
    static final int MENSAJE_CONFIRMAR_ELIMINAR = 1;

    static final int IMPORTE = 1;

    String idMov, tipoMovimientoSel, cantidadSel, notasSel, fechaSel, idCatSel, idSubcatSel, idTarjetaSel;
    boolean isTarjetaSel = false;

    int posCat, posSub, posTar;

    private LinearLayout btnGasto;
    private LinearLayout btnIngreso;
    private TextView txtGasto;
    private TextView txtIngreso;
    private TextView nVeces;
    int tipoRegistro = 0;


    private LinearLayout layoutFecha;
    private TextView txtFecha;
    private TextView txtCant;
    private EditText descripcion;

    private int mYear;
    private int mMonth;
    private int mDay;

    private Button botonCrear;

    private Spinner spinnerCat;
    private Spinner spinnerSub;
    private Spinner spinnerTarjetas;
    private Spinner spinnerTipoPago;
    private RelativeLayout layoutPubli;

    private SQLiteDatabase db;

    final GestionBBDD gestion = new GestionBBDD();

    boolean tablasCreadas = false;
    boolean opcionesCategoria = false;
    boolean opcionesSubcategoria = false;

    private ProgressDialog progressDialog;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    int style;
    // Productos que posee el usuario
    boolean isPremium = false;
    boolean isCategoriaPremium = false;
    boolean isSinPublicidad = false;
    boolean crearAnuncio = false;

    private static final String KEY_CONTENT = "MovimientosFragment:Content";
    private String mContent = "???";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_movimientos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isPremium = extras.getBoolean("isPremium", false);
            isSinPublicidad = extras.getBoolean("isSinPublicidad", false);
        }

        txtCant = (TextView) findViewById(R.id.txtCant);
        btnGasto = (LinearLayout) findViewById(R.id.btnGasto);
        btnIngreso = (LinearLayout) findViewById(R.id.btnIngreso);
        txtGasto = (TextView) findViewById(R.id.txtGasto);
        txtIngreso = (TextView) findViewById(R.id.txtIngreso);
        nVeces = (TextView) findViewById(R.id.nVeces);
        descripcion = (EditText) findViewById(R.id.descripcion);

        layoutFecha = (LinearLayout) findViewById(R.id.layoutFecha);
        txtFecha = (TextView) findViewById(R.id.txtFecha);

        spinnerCat = (Spinner) findViewById(
                R.id.spinnerCategoria);
        spinnerSub = (Spinner) findViewById(
                R.id.spinnerSubcategoria);
        spinnerTarjetas = (Spinner) findViewById(
                R.id.spinnerTarjetas);
        spinnerTipoPago = (Spinner) findViewById(
                R.id.spinnerTipoPago);

        layoutPubli = (RelativeLayout) findViewById(R.id.layoutPubli);

        //Se carga la publicidad
        AdView adView = (AdView) findViewById(R.id.adView);
        if (isPremium || isSinPublicidad) {
            layoutPubli.setVisibility(View.GONE);
        } else {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        updateDisplay();

        iniciarActivity();

        // Inflate the custom view and add click handlers for the buttons
        View actionBarButtons = getLayoutInflater().inflate(R.layout.edit_delete_actionbar,
                new LinearLayout(this), false);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        View deleteActionView = actionBarButtons.findViewById(R.id.action_cancel);
        deleteActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(MENSAJE_CONFIRMAR_ELIMINAR);
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
                    int modoPago = spinnerTipoPago.getSelectedItemPosition();
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
                    Date fecha = new Date(mYear - 1900, mMonth - 1, mDay);

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
                                .editarMovimiento(db, idMov, tipoRegistro, cant, descripcion.getText().toString().trim(),
                                        fecha, idCategoria, idSubcategoria,
                                        false, tarjeta, mes + 1, anio,
                                        idTarjeta);
                    }
                    db.close();

                    if (ok) {
                        Context context = getApplicationContext();
                        CharSequence text = getResources()
                                .getString(R.string.guardarRegistroOK);
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        setResult(RESULT_OK, getIntent());
                        finish();
                    } else {
                        Context context = getApplicationContext();
                        CharSequence text = getResources()
                                .getString(R.string.guardarRegistroKO);
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = getResources().getString(R.string.introducirCant);
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
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

        txtCant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(NuevoEditMovimientosActivity.this, CantidadActivity.class);
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

        spinnerTipoPago
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    public void obtenerCategorias() {
        ArrayList<Categoria> listCategorias = new ArrayList<Categoria>();
        db = openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            // Recuperamos el listado del spinner Categorias
            listCategorias = (ArrayList<Categoria>) gestion.getCategorias(db,
                    "Categorias", "idCategoria", this);
        }
        db.close();

        // Creamos el adaptador
        ListAdapterSpinner spinner_adapterCat = new ListAdapterSpinner(
                this, R.layout.spinner_iconos,
                listCategorias);

        spinnerCat.setAdapter(spinner_adapterCat);
    }

    // Rellena el spinner modo pago
    public void obtenerModoPago() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.modoPago,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner);
        spinnerTipoPago.setAdapter(adapter);

        if (isTarjetaSel) {
            spinnerTipoPago.setSelection(1);
            spinnerTarjetas.setVisibility(View.VISIBLE);
        } else {
            spinnerTipoPago.setSelection(0);
            spinnerTarjetas.setVisibility(View.GONE);
        }
    }

    public void iniciarActivity() {

        obtenerCategorias();
        obtenerSubcategorias();
        obtenerModoPago();
        obtenerTarjetas();
        obtenerDatos();

    }

    public void obtenerDatos() {
        Bundle extras = getIntent().getExtras();
        idMov = extras.getString("idMovimiento");
        tipoMovimientoSel = extras.getString("tipoMovimiento");
        cantidadSel = extras.getString("cantidad");
        notasSel = extras.getString("notas");
        fechaSel = extras.getString("fecha");
        idCatSel = extras.getString("idCategoria");
        idSubcatSel = extras.getString("idSubcategoria");
        isTarjetaSel = extras.getBoolean("tipoPago");
        idTarjetaSel = extras.getString("idTarjeta");
        rellenarDatos();
    }

    public void rellenarDatos() {
        posCat = 0;
        posSub = 0;
        posTar = 0;
        ArrayList<Categoria> listCategorias = new ArrayList<Categoria>();
        ArrayList<Categoria> listSubcategorias = new ArrayList<Categoria>();
        ArrayList<Tarjeta> listTarjetas = new ArrayList<Tarjeta>();

        String cant = "";
        db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db.isOpen()) {
            // Recuperamos el listado del spinner Categorias
            listCategorias = (ArrayList<Categoria>) gestion
                    .getCategorias(db, "Categorias", "idCategoria", this);
            // Recuperamos el listado del spinner Subategorias
            listSubcategorias = (ArrayList<Categoria>) gestion
                    .getCategorias(db, "Subcategorias", "idSubcategoria", this);
            // Recuperamos el listado del spinner Tarjetas
            listTarjetas = (ArrayList<Tarjeta>) gestion.getTarjetas(db);

            for (int i = 0; i < listCategorias.size(); i++) {
                Categoria cat = listCategorias.get(i);
                if (cat.getId().equals(idCatSel)) {
                    posCat = i;
                }
            }
            for (int i = 0; i < listSubcategorias.size(); i++) {
                Categoria subcat = listSubcategorias.get(i);
                if (subcat.getId().equals(idSubcatSel)) {
                    posSub = i;
                }
            }
            for (int i = 0; i < listTarjetas.size(); i++) {
                Tarjeta tarjeta = listTarjetas.get(i);
                if (tarjeta.getId().equals(idTarjetaSel)) {
                    posTar = i;
                }
            }
        }
        db.close();

        spinnerCat.setSelection(posCat);
        spinnerSub.setSelection(posSub);
        spinnerTarjetas.setSelection(posTar);

        if (tipoMovimientoSel.equals("1")) {
            tipoRegistro = 0;
            btnGasto.setBackgroundResource(R.drawable.rounded_layout_azul);
            btnIngreso.setBackgroundResource(R.drawable.rounded_layout_gris);
            txtGasto.setTextColor(getResources().getColor(R.color.blanco));
            txtIngreso.setTextColor(getResources().getColor(R.color.txtGris));
        } else if (tipoMovimientoSel.equals("2")) {
            tipoRegistro = 2;
            btnIngreso.setBackgroundResource(R.drawable.rounded_layout_azul);
            btnGasto.setBackgroundResource(R.drawable.rounded_layout_gris);
            txtIngreso.setTextColor(getResources().getColor(R.color.blanco));
            txtGasto.setTextColor(getResources().getColor(R.color.txtGris));
        }

        DecimalFormat df = new DecimalFormat("0.00");
        if (Float.parseFloat(cantidadSel) < 0) {
            cant = df.format(Float.parseFloat(cantidadSel) * -1);
        } else {
            cant = df.format(Float.parseFloat(cantidadSel));
        }
        txtCant.setText(cant.replace(",", "."));
        mDay = Integer.parseInt(fechaSel.substring(8, 10));
        mMonth = Integer.parseInt(fechaSel.substring(5, 7));
        mYear = Integer.parseInt(fechaSel.substring(0, 4));

        updateDisplay();

        if (isTarjetaSel) {
            spinnerTipoPago.setSelection(1);
            for(int i =0; i< listTarjetas.size(); i++){
                if(listTarjetas.get(i).getId().equals(idTarjetaSel)){
                    spinnerTarjetas.setSelection(i);
                    break;
                }
            }
            spinnerTarjetas.setVisibility(View.VISIBLE);
        }else{
            spinnerTarjetas.setVisibility(View.GONE);
        }

        descripcion.setText(notasSel);
    }

    private void updateDisplay() {
        String mes;
        String dia;

        if((mMonth) < 10){
            mes = "0" + String.valueOf(mMonth);
        }else{
            mes = String.valueOf(mMonth);
        }

        if(mDay < 10){
            dia = "0" + String.valueOf(mDay);
        }else{
            dia = String.valueOf(mDay);
        }

        txtFecha.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(dia).append("-").append(mes).append("-")
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pulsacion_corta, menu);
        menu.setHeaderTitle(getResources().getString(R.string.elijaOpcion));

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear + 1;
            ;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NuevoEditMovimientosActivity.this);
        AlertDialog alert;
        switch (id) {
            case DATE_DIALOG_ID:
                DatePickerDialog datadialog = new DatePickerDialog(
                        this, mDateSetListener, mYear, mMonth - 1, mDay);
                datadialog.show();
                break;
            case MENSAJE_CONFIRMAR_ELIMINAR:
                builder.setTitle(getResources().getString(R.string.atencion));
                builder.setMessage(getResources().getString(
                        R.string.msnEliminarRegistro));
                builder.setIcon(R.drawable.ic_delete);
                builder.setCancelable(false);
                builder.setPositiveButton(
                        getResources().getString(R.string.aceptar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean ok = false;
                                db = openOrCreateDatabase(BD_NOMBRE, 1, null);
                                if (db != null) {
                                    ok = gestion.eliminarRegistro(db, idMov);
                                }
                                db.close();
                                if (ok) {
                                    ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
                                    int idCuenta = cuentaSeleccionada();
                                    db = openOrCreateDatabase(BD_NOMBRE, 1, null);

                                    Context context = getApplicationContext();
                                    CharSequence text = getResources().getString(
                                            R.string.deleteRegistroOk);
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                    setResult(RESULT_OK, getIntent());
                                } else {
                                    Context context = getApplicationContext();
                                    CharSequence text = getResources().getString(
                                            R.string.deleteRegistroError);
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }
                                dialog.cancel();
                                finish();
                            }
                        }).setNegativeButton(
                        getResources().getString(R.string.cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alert = builder.create();
                alert.show();
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
}
