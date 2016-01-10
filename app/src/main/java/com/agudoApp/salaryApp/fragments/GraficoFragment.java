package com.agudoApp.salaryApp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterCatGrafico;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.graficos.PieChartView;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Movimiento;
import com.agudoApp.salaryApp.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public final class GraficoFragment extends Fragment {
    private static final String KEY_CONTENT = "GraficoFragment:Content";
    private SQLiteDatabase db;
    private final String BD_NOMBRE = "BDGestionGastos";
    private GestionBBDD gestion = new GestionBBDD();
    SharedPreferences prefs;
    SharedPreferences prefsFiltros;

    // Productos que posee el usuario
    boolean isPremium = false;
    boolean isCategoriaPremium = false;
    boolean isSinPublicidad = false;

    LinearLayout layoutSinRegistro;
    LinearLayout layoutGrafico;
    private RelativeLayout layoutPubli;
    ListAdapterCatGrafico adapterGrafico;

    int mDayDesde;
    int mMonthDesde;
    int mYearDesde;

    int mDayHasta;
    int mMonthHasta;
    int mYearHasta;

    boolean ingresoPulsado = false;
    boolean gastoPulsado = false;

    boolean fechaDesde = false;
    boolean fechaHasta = false;

    int posiFechas;
    int posiTipo;
    String idCat;
    String idSub;
    int posiTipoModoPago;
    String idTarjeta;

    private String mContent = "???";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if ((savedInstanceState != null)
                && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    public GraficoFragment(boolean isUserPremium, boolean isUserSinpublicidad,
                           boolean isUserCategoriaPremium) {
        isPremium = isUserPremium;
        isCategoriaPremium = isUserCategoriaPremium;
        isSinPublicidad = isUserSinpublicidad;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grafico_rosco, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        //((FinanfyActivity)getActivity()).mostrarPublicidad(true, false);

        Cursor movimientos = null;
        ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
        int idCuenta = cuentaSeleccionada();

        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);

        configurarFiltros();

        db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            listMov = gestion.getMovimientosFiltros(db, gastoPulsado, ingresoPulsado, posiFechas, posiTipo, null, anio,
                    idCat, idSub, posiTipoModoPago, idTarjeta, mes, anio, mDayDesde, mMonthDesde, mYearDesde,
                    mDayHasta, mMonthHasta, mYearHasta, idCuenta);
        }
        db.close();

        layoutPubli = (RelativeLayout) getView().findViewById(R.id.layoutPubli);

        //Se carga la publicidad
        AdView adView = (AdView) getView().findViewById(R.id.adView);
        if (isPremium || isSinPublicidad) {
            layoutPubli.setVisibility(View.GONE);
        } else {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        listMov = filtrarMovimientosGastos(listMov);
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.grafico);
        layoutSinRegistro = (LinearLayout) getView().findViewById(R.id.layoutSinRegistro);
        layoutGrafico = (LinearLayout) getView().findViewById(R.id.layoutGrafico);
        ArrayList<Categoria> listCat = obtenerListaCategorias(listMov);
        layout.addView(new PieChartView(getActivity(), null, listCat));

        if(listMov.size()>0){
            ListView listaCatGrafico = (ListView) getView().findViewById(R.id.listaCategoriasGrafico);
            adapterGrafico = new ListAdapterCatGrafico(getActivity(), listCat);
            listaCatGrafico.setAdapter(adapterGrafico);
            layoutSinRegistro.setVisibility(View.GONE);
            layoutGrafico.setVisibility(View.VISIBLE);
        }else{
            layoutSinRegistro.setVisibility(View.VISIBLE);
            layoutGrafico.setVisibility(View.GONE);
        }
    }

    public int cuentaSeleccionada() {
        prefs = getActivity().getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);

        int idCuenta = prefs.getInt("cuenta", 0);
        return idCuenta;
    }

    public ArrayList<Movimiento> filtrarMovimientosGastos(
            ArrayList<Movimiento> listMov) {
        ArrayList<Movimiento> listaFiltrada = new ArrayList<Movimiento>();

        for (int i = 0; i < listMov.size(); i++) {
            Movimiento mov = listMov.get(i);
            if (Float.parseFloat(mov.getCantidad()) < 0) {
                listaFiltrada.add(mov);
            }
        }

        return listaFiltrada;
    }

    public ArrayList<Categoria> obtenerListaCategorias(
            ArrayList<Movimiento> listMov) {
        ArrayList<Categoria> listaCategorias = new ArrayList<Categoria>();

        for (int i = 0; i < listMov.size(); i++) {
            Movimiento mov = listMov.get(i);
            if (!contieneCategoria(listaCategorias, mov)) {
                Float cant = new Float("0.0");
                if (Float.parseFloat(mov.getCantidad()) > 0) {
                    cant = cant + Float.parseFloat(mov.getCantidad());
                } else {
                    Float aux = Float.parseFloat(mov.getCantidad()) * -1;
                    cant = cant + aux;
                }
                for (int j = 0; j < listMov.size(); j++) {
                    Movimiento mov2 = listMov.get(j);
                    if (!mov.getId().equals(mov2.getId())
                            && mov.getIdCategoria().equals(
                            mov2.getIdCategoria())) {
                        if (Float.parseFloat(mov2.getCantidad()) > 0) {
                            cant = cant + Float.parseFloat(mov2.getCantidad());
                        } else {
                            Float cant2 = Float.parseFloat(mov2.getCantidad())
                                    * -1;
                            cant = cant + cant2;
                        }
                    }
                }

                Categoria cat = new Categoria();
                cat.setTotal(cant);
                cat.setId(mov.getIdCategoria());
                cat.setIdIcon(mov.getIdIconCat());

                if (!mov.getDescCategoria().equals("-")) {
                    cat.setDescripcion(mov.getDescCategoria());
                } else {
                    Locale locale = Locale.getDefault();
                    String languaje = locale.getLanguage();

                    if (languaje.equals("es") || languaje.equals("es-rUS")
                            || languaje.equals("ca")) {
                        cat.setDescripcion("Sin categoria");
                    } else if (languaje.equals("fr")) {
                        cat.setDescripcion("Sans catégorie");
                    } else if (languaje.equals("de")) {
                        cat.setDescripcion("Keine Kategorie");
                    } else if (languaje.equals("en")) {
                        cat.setDescripcion("No category");
                    } else if (languaje.equals("it")) {
                        cat.setDescripcion("Senza categoria");
                    } else if (languaje.equals("pt")) {
                        cat.setDescripcion("Sem categoria");
                    } else {
                        cat.setDescripcion("No category");
                    }
                }

                listaCategorias.add(cat);
            }
        }
        return listaCategorias;
    }

    public boolean contieneCategoria(ArrayList<Categoria> listCat,
                                     Movimiento mov) {
        boolean contiene = false;

        for (int i = 0; i < listCat.size(); i++) {
            Categoria cat = listCat.get(i);
            if (cat.getId().equals(mov.getIdCategoria())) {
                contiene = true;
            }
        }
        return contiene;
    }

    public void configurarFiltros() {
        prefsFiltros = getActivity().getSharedPreferences("ficheroConfFiltros", Context.MODE_PRIVATE);

        ingresoPulsado = prefsFiltros.getBoolean("ingresoPulsado", true);
        gastoPulsado = prefsFiltros.getBoolean("gastoPulsado", true);

        fechaDesde = prefsFiltros.getBoolean("fechaDesde", false);
        fechaHasta = prefsFiltros.getBoolean("fechaHasta", false);

        Calendar c = Calendar.getInstance();
        if (fechaDesde) {
            mDayDesde = prefsFiltros.getInt("diaDesde", 0);
            mMonthDesde = prefsFiltros.getInt("mesDesde", 0);
            mYearDesde = prefsFiltros.getInt("anioDesde", 0);
        } else {
            mDayDesde = 1;
            mMonthDesde = c.get(Calendar.MONTH);
            mYearDesde = c.get(Calendar.YEAR);
        }

        if (fechaHasta) {
            mDayHasta = prefsFiltros.getInt("diaHasta", 0);
            mMonthHasta = prefsFiltros.getInt("mesHasta", 0);
            mYearHasta = prefsFiltros.getInt("anioHasta", 0);
        } else {
            Date fechaFin = Util.getFinMes(mMonthDesde + 1, mYearDesde);
            String fechaF = fechaFin.toString();
            mYearHasta = Integer.parseInt(fechaF.substring(0, 4));
            mMonthHasta = Integer.parseInt(fechaF.substring(5, 7)) - 1;
            mDayHasta = Integer.parseInt(fechaF.substring(8, 10));
        }

        posiFechas = prefsFiltros.getInt("spinnerFechas", 0);
        posiTipo = prefsFiltros.getInt("tipoFiltro", 0);

        idCat = prefsFiltros.getString("idCategoria", "-1");
        idSub = prefsFiltros.getString("idSubcategoria", "-1");

        posiTipoModoPago = prefsFiltros.getInt("spinnerTipoModoPago", 0);

        idTarjeta = prefsFiltros.getString("idTarjeta", "-1");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapterGrafico != null) {
            adapterGrafico.notifyDataSetChanged();
        }
    }
}
