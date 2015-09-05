package com.agudoApp.salaryApp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.NuevoActivity;
import com.agudoApp.salaryApp.adapters.ListAdapter;
import com.agudoApp.salaryApp.adapters.ListAdapterSpinner;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Movimiento;
import com.agudoApp.salaryApp.model.Tarjeta;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class NuevoResumenFragment extends Fragment {
    private static final String KEY_CONTENT = "NuevoResumenFragment:Content";
    private String mContent = "???";

    private final String BD_NOMBRE = "BDGestionGastos";
    final GestionBBDD gestion = new GestionBBDD();
    private SQLiteDatabase db;

    private final int RESUMEN = 1;

    protected ListView listMovView;
    protected ExpandableListView listMovCatView;

    private DrawerLayout drawer;

    private ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();

    private Spinner spinnerFechas;
    private Spinner spinnerTipoFiltroResumen;
    private Spinner spinnerCategorias;
    private Spinner spinnerSubcategorias;
    private Spinner spinnerTipoModoPago;
    private Spinner spinnerTarjetas;

    private LinearLayout mesAnterior;
    private LinearLayout mesSiguiente;

    private LinearLayout filtros;

    private TextView txtMesActual;
    private TextView txtAnioActual;

    private TextView txtTotal;
    private float total;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private int mYear;
    private int mMonth;
    int idCuenta;

    // Productos que posee el usuario
    boolean isPremium = false;
    boolean isCategoriaPremium = false;
    boolean isSinPublicidad = false;

    ProgressDialog progDailog;

    public NuevoResumenFragment(boolean isUserPremium, boolean isUserSinpublicidad,
                                boolean isUserCategoriaPremium) {
        isPremium = isUserPremium;
        isCategoriaPremium = isUserCategoriaPremium;
        isSinPublicidad = isUserSinpublicidad;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if ((savedInstanceState != null)
                && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nuevo_resumen, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listMovView = (ListView) this.getView().findViewById(
                R.id.listaMovimientos);
        listMovCatView = (ExpandableListView) this.getView().findViewById(
                R.id.listaMovimientosCategoria);

        drawer = (DrawerLayout) getActivity().findViewById(
                R.id.drawer_layout);

        filtros = (LinearLayout) getActivity().findViewById(R.id.right_drawer);

        spinnerFechas = (Spinner) getActivity().findViewById(R.id.spinnerFecha);
        spinnerTipoFiltroResumen = (Spinner) getActivity().findViewById(R.id.spinnerTipoLista);
        spinnerCategorias = (Spinner) getActivity().findViewById(R.id.spinnerCategoriasFiltro);
        spinnerSubcategorias = (Spinner) getActivity().findViewById(R.id.spinnerSubcategoriasFiltro);
        spinnerTipoModoPago = (Spinner) getActivity().findViewById(R.id.spinnerModoPagoFiltro);
        spinnerTarjetas = (Spinner) getActivity().findViewById(R.id.spinnerTarjetasFiltro);
        txtMesActual = (TextView) getView().findViewById(R.id.mesActual);
        txtAnioActual = (TextView) getView().findViewById(R.id.anioActual);
        txtTotal = (TextView) getView().findViewById(R.id.txtTotal);

        mesAnterior = (LinearLayout) getView().findViewById(R.id.layoutLeft);
        mesSiguiente = (LinearLayout) getView().findViewById(R.id.layoutRight);

        //Se carga la publicidad
        AdView adView = (AdView) getActivity().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        obternerFechaActual();
        idCuenta = cuentaSeleccionada();
        txtAnioActual.setText(String.valueOf(mYear));
        rellenarSpinners();
        obtenerTextoMes();
        new CargarMovimientosTask().execute();

        mesAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMonth == 0) {
                    mMonth = 12;
                    mYear = mYear - 1;
                    txtAnioActual.setText(String.valueOf(mYear));
                } else {
                    mMonth = mMonth - 1;
                }
                obtenerTextoMes();
                new CargarMovimientosTask().execute();
            }
        });

        mesSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMonth == 11){
                    mMonth = 0;
                    mYear = mYear + 1;
                    txtAnioActual.setText(String.valueOf(mYear));
                }else{
                    mMonth = mMonth + 1;
                }
                obtenerTextoMes();
                new CargarMovimientosTask().execute();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_setting, menu);
    }

    // Aadiendo funcionalidad a las opciones de men
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(getActivity(), NuevoActivity.class);
                startActivityForResult(intent, RESUMEN);
                return true;
            case R.id.action_filter:
                drawer.openDrawer(filtros);
                return true;
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void rellenarLista() {
        db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            listMov = gestion.getMovimientosFiltros(db, mMonth, mYear, idCuenta);
        }
        db.close();
    }

    public int cuentaSeleccionada() {
        prefs = getActivity().getSharedPreferences("ficheroConf",
                Context.MODE_PRIVATE);

        int idCuenta = prefs.getInt("cuenta", 0);
        return idCuenta;
    }

    public void rellenarSpinners() {
        obtenerCategorias();
        obtenerSubcategorias();
        obtenerModoPago();
        obtenerTarjetas();
        obtenerTipoListas();
        obtenerSpinnerFechas();
    }

    public void obternerFechaActual() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
    }

    public void obtenerCategorias() {
        ArrayList<Categoria> listCategorias = new ArrayList<Categoria>();
        db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            // Recuperamos el listado del spinner Categorias
            listCategorias = (ArrayList<Categoria>) gestion.getCategoriasEditDelete(db,
                    "Categorias", "idCategoria");
        }
        db.close();

        // Creamos el adaptador
        ListAdapterSpinner spinner_adapterCat = new ListAdapterSpinner(
                getActivity(), R.layout.spinner_iconos,
                listCategorias);

        spinnerCategorias.setAdapter(spinner_adapterCat);
    }

    public void obtenerSubcategorias() {
        ArrayList<Categoria> listSubcategorias = new ArrayList<Categoria>();
        db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            listSubcategorias = (ArrayList<Categoria>) gestion.getCategoriasEditDelete(
                    db, "Subcategorias", "idSubcategoria");
        }
        db.close();

        // Creamos el adaptador
        ListAdapterSpinner spinner_adapterSubcat = new ListAdapterSpinner(
                getActivity(), R.layout.spinner_iconos,
                listSubcategorias);
        spinnerSubcategorias.setAdapter(spinner_adapterSubcat);
    }

    // Rellena el spinner modo pago
    public void obtenerModoPago() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.modoPago,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner);
        spinnerTipoModoPago.setAdapter(adapter);

        spinnerTipoModoPago.setSelection(0);
    }

    public void obtenerSpinnerFechas() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.fechas,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner);
        spinnerFechas.setAdapter(adapter);

        spinnerFechas.setSelection(0);
    }

    public void obtenerTipoListas() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.tipoFiltroResumen,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner);
        spinnerTipoFiltroResumen.setAdapter(adapter);

        spinnerTipoFiltroResumen.setSelection(0);
    }

    public void obtenerTarjetas() {
        ArrayList<Tarjeta> listTarjetas = new ArrayList<Tarjeta>();
        db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
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
                getActivity(), android.R.layout.simple_spinner_item,
                listaTarjetas);
        // Aadimos el layout para el men y se lo damos al spinner
        spinner_adapterTar.setDropDownViewResource(R.layout.spinner);
        spinnerTarjetas.setAdapter(spinner_adapterTar);
    }

    public void calcularTotal() {
        // Calculamos el total
        total = 0;
        total = obtenerTotal();
    }

    public float obtenerTotal() {
        float total = 0;
        for (int i = 0; i < listMov.size(); i++) {
            Movimiento mov = listMov.get(i);
            total = total + Float.parseFloat(mov.getCantidad());
        }
        return total;
    }

    public void obtenerTextoMes() {
        switch (mMonth + 1) {
            case 1:
                txtMesActual.setText(getResources().getString(R.string.enero));
                break;
            case 2:
                txtMesActual.setText(getResources().getString(R.string.febrero));
                break;
            case 3:
                txtMesActual.setText(getResources().getString(R.string.marzo));
                break;
            case 4:
                txtMesActual.setText(getResources().getString(R.string.abril));
                break;
            case 5:
                txtMesActual.setText(getResources().getString(R.string.mayo));
                break;
            case 6:
                txtMesActual.setText(getResources().getString(R.string.junio));
                break;
            case 7:
                txtMesActual.setText(getResources().getString(R.string.julio));
                break;
            case 8:
                txtMesActual.setText(getResources().getString(R.string.agosto));
                break;
            case 9:
                txtMesActual.setText(getResources().getString(R.string.septiembre));
                break;
            case 10:
                txtMesActual.setText(getResources().getString(R.string.octubre));
                break;
            case 11:
                txtMesActual.setText(getResources().getString(R.string.noviembre));
                break;
            case 12:
                txtMesActual.setText(getResources().getString(R.string.diciembre));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        new CargarMovimientosTask().execute();
    }

    public class CargarMovimientosTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(getActivity());
            progDailog.setIndeterminate(false);
            progDailog.setMessage(getResources().getString(R.string.cargando));
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
        }

        // Decode image in background.
        @Override
        protected Void doInBackground(Integer... params) {
            // Recuperamos las prendas
            rellenarLista();
            calcularTotal();
            return null;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Void result) {
            listMovView.setAdapter(new ListAdapter(getActivity(), listMov));

            // Rellenamos el dato con el total
            DecimalFormat df = new DecimalFormat("0.00");
            txtTotal.setText(df.format(total) + " €");
            if (total > 0) {
                txtTotal.setTextColor(getResources().getColor(R.color.txtAzul));
            } else {
                txtTotal.setTextColor(Color.RED);
            }
            progDailog.dismiss();
        }
    }

}
