package com.agudoApp.salaryApp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class EstadisticasFragment extends Fragment {
    private static final String KEY_CONTENT = "EstadisticasFragment:Content";
    private final String BD_NOMBRE = "BDGestionGastos";
    final GestionBBDD gestion = new GestionBBDD();
    private SQLiteDatabase db;
    private TextView totalIngresosView;
    private TextView totalGatosView;
    private TextView totalBalanceView;
    private TextView numRegistrosView;
    private TextView numIngresosView;
    private TextView numGastosView;
    private TextView numTotalCategoriasView;
    private TextView numCategoriasView;
    private TextView numSubcategoriasView;
    RelativeLayout layoutEstadisticas;
    RelativeLayout layoutEstadisticas2;
    RelativeLayout layoutEstadisticas3;
    private RelativeLayout layoutPubli;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    // Productos que posee el usuario
    boolean isPremium = false;
    boolean isCategoriaPremium = false;
    boolean isSinPublicidad = false;

    public EstadisticasFragment(boolean isUserPremium,
                                boolean isUserSinpublicidad, boolean isUserCategoriaPremium) {
        isPremium = isUserPremium;
        isCategoriaPremium = isUserCategoriaPremium;
        isSinPublicidad = isUserSinpublicidad;
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.estadisticas, container, false);
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

        prefs = getActivity().getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
        db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);

        totalIngresosView = (TextView) this.getView().findViewById(
                R.id.valorIngresosTotales);
        totalGatosView = (TextView) this.getView().findViewById(
                R.id.valorGastosTotales);
        totalBalanceView = (TextView) this.getView().findViewById(
                R.id.valorBalanceTotal);
        numRegistrosView = (TextView) this.getView().findViewById(
                R.id.valorNumeroRegistros);
        numIngresosView = (TextView) this.getView().findViewById(
                R.id.valorNumeroIngresos);
        numGastosView = (TextView) this.getView().findViewById(
                R.id.valorNumeroGastos);
        numTotalCategoriasView = (TextView) this.getView().findViewById(
                R.id.valorNumeroTotalCategorias);
        numCategoriasView = (TextView) this.getView().findViewById(
                R.id.valorNumeroCategorias);
        numSubcategoriasView = (TextView) this.getView().findViewById(
                R.id.valorNumeroSubcategorias);
        layoutEstadisticas = (RelativeLayout) this.getView().findViewById(
                R.id.layoutEstadisticas1);
        layoutEstadisticas2 = (RelativeLayout) this.getView().findViewById(
                R.id.layoutEstadisticas2);
        layoutEstadisticas3 = (RelativeLayout) this.getView().findViewById(
                R.id.layoutEstadisticas3);

        layoutPubli = (RelativeLayout) getView().findViewById(R.id.layoutPubli);

        //Se carga la publicidad
        AdView adView = (AdView) getView().findViewById(R.id.adView);
        if (isPremium || isSinPublicidad) {
            layoutPubli.setVisibility(View.GONE);
        } else {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        // Asignamos el tipo de fuente
        Typeface miPropiaTypeFace = Typeface.createFromAsset(getActivity()
                .getAssets(), "fonts/Berlin.ttf");

        TextView informacionGlobal = (TextView) this.getView().findViewById(
                R.id.informacionGlobal);
        TextView informacionRegistros = (TextView) this.getView().findViewById(
                R.id.informacionRegistros);
        TextView informacionCategorias = (TextView) this.getView()
                .findViewById(R.id.informacionCategorias);

        cargarDatos();

    }

    public void cargarDatos() {
        int idCuenta = cuentaSeleccionada();
        String totalIngresos = gestion.getTotalIngresos(db, idCuenta);
        String totalGastos = gestion.getTotalGastos(db, idCuenta);
        float totalBalance = Float.parseFloat(totalIngresos)
                + Float.parseFloat(totalGastos);
        String numRegistros = gestion.getNumRegistros(db, idCuenta);
        String numIngresos = gestion.getNumIngresos(db, idCuenta);
        String numGastos = gestion.getNumGastos(db, idCuenta);
        String numCategorias = gestion.getNumCategorias(db);
        String numSubcategorias = gestion.getNumSubcategorias(db);
        int numTotalCategorias = Integer.parseInt(numCategorias)
                + Integer.parseInt(numSubcategorias);

        totalIngresosView.setText(Util.formatear(Float.parseFloat(totalIngresos), prefs));
        totalGatosView.setText(Util.formatear(Float.parseFloat(totalGastos), prefs));
        totalBalanceView.setText(Util.formatear(totalBalance, prefs));
        numRegistrosView.setText(numRegistros);
        numIngresosView.setText(numIngresos);
        numGastosView.setText(numGastos);
        numTotalCategoriasView.setText(String.valueOf(numTotalCategorias));
        numCategoriasView.setText(numCategorias);
        numSubcategoriasView.setText(numSubcategorias);
    }

    public int cuentaSeleccionada() {
        prefs = getActivity().getSharedPreferences("ficheroConf",
                Context.MODE_PRIVATE);

        int idCuenta = prefs.getInt("cuenta", 0);
        return idCuenta;
    }
}
