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

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterCatGrafico;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.graficos.PieChartView;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Movimiento;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public final class GraficoFragment extends Fragment {
    private static final String KEY_CONTENT = "GraficoFragment:Content";
    private SQLiteDatabase db;
    private final String BD_NOMBRE = "BDGestionGastos";
    private GestionBBDD gestion = new GestionBBDD();
    SharedPreferences prefs;

    // Productos que posee el usuario
    boolean isPremium = false;
    boolean isCategoriaPremium = false;
    boolean isSinPublicidad = false;

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

        Cursor movimientos = null;
        ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
        int idCuenta = cuentaSeleccionada();

        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);

        db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            listMov = gestion.getMovimientosExcelMes(db, mes, anio,
                    idCuenta);
        }
        db.close();

        listMov = filtrarMovimientosGastos(listMov);
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.grafico);
        ArrayList<Categoria> listCat = obtenerListaCategorias(listMov);
        layout.addView(new PieChartView(getActivity(), null, listCat));

        ListView listaCatGrafico = (ListView) getView().findViewById(R.id.listaCategoriasGrafico);
        listaCatGrafico.setAdapter(new ListAdapterCatGrafico(getActivity(), listCat));
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
}
