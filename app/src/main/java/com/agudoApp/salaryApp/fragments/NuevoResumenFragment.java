package com.agudoApp.salaryApp.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.NuevoActivity;
import com.agudoApp.salaryApp.activities.NuevoEditMovimientosActivity;
import com.agudoApp.salaryApp.adapters.ListAdapterSpinner;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.informes.Informes;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Movimiento;
import com.agudoApp.salaryApp.model.Recibo;
import com.agudoApp.salaryApp.model.Tarjeta;
import com.agudoApp.salaryApp.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;

public class NuevoResumenFragment extends Fragment {
    private static final String KEY_CONTENT = "NuevoResumenFragment:Content";
    private String mContent = "???";

    static final int DATE_DIALOG_DESDE_ID = 0;
    static final int DATE_DIALOG_HASTA_ID = 1;

    private final String BD_NOMBRE = "BDGestionGastos";
    final GestionBBDD gestion = new GestionBBDD();
    private SQLiteDatabase db;

    private final int RESUMEN = 1;
    private final int EDIT_DELETE = 2;

    protected ListView listMovView;
    protected ExpandableListView listMovCatView;
    ListAdapter listAdapter;
    ListaAdapterResumenExpandibleAdapter listAdapterCategorias;
    ListaAdapterResumenExpandibleSubAdapter listAdapterSubcategorias;
    LinearLayout layoutSinRegistro;

    private DrawerLayout drawer;

    private ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
    private ArrayList<Categoria> listGroupChild = new ArrayList<Categoria>();
    ArrayList<Categoria> categorias = new ArrayList<Categoria>();
    ArrayList<Categoria> subcategorias = new ArrayList<Categoria>();
    ArrayList<Object> listMovAux = new ArrayList<Object>();

    private Spinner spinnerFechas;
    private Spinner spinnerTipoFiltroResumen;
    private Spinner spinnerCategorias;
    private Spinner spinnerSubcategorias;
    private Spinner spinnerTipoModoPago;
    private Spinner spinnerTarjetas;

    private LinearLayout mes;
    private LinearLayout anio;
    private TextView anioSelec;
    private LinearLayout diario;
    private TextView diaSelec;
    private LinearLayout mesAnterior;
    private LinearLayout mesSiguiente;
    private LinearLayout btnAceptarFiltros;
    private LinearLayout btnCancelarFiltros;
    private LinearLayout layoutFechas;
    private LinearLayout btnGasto;
    private LinearLayout btnIngreso;
    private LinearLayout btnFechaDesde;
    private LinearLayout btnFechaHasta;
    private LinearLayout layoutTarjetasFiltro;
    private RelativeLayout layoutPubli;
    private LinearLayout layoutIntervaloFecha;
    private LinearLayout layoutMeses;

    private LinearLayout filtros;

    private TextView txtMesActual;
    private TextView txtAnioActual;
    private TextView txtGasto;
    private TextView txtIngreso;
    private TextView txtFechaDesde;
    private TextView txtFechaHasta;
    private TextView txtIntevaloFechas;

    Date daySelect;

    private TextView txtTotal;
    private float total;

    SharedPreferences prefs;
    SharedPreferences prefsFiltros;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editorFiltros;

    private int mYear;
    private int mYearFiltro;
    private int mMonth;

    private int mDaySelectFiltro;
    private int mMonthSelectFiltro;
    private int mYearSelectFiltro;

    private int mYearDesde;
    private int mMonthDesde;
    private int mDayDesde;
    private int mYearHasta;
    private int mMonthHasta;
    private int mDayHasta;

    ArrayList<Categoria> listCategoriasFiltros;
    ArrayList<Categoria> listSubcategoriasFiltros;
    ArrayList<Tarjeta> listTarjetas;

    private int idCuenta;
    private boolean gastoPulsado = true;
    private boolean ingresoPulsado = true;
    private int tipoFiltro;
    private int tipoFecha;

    // Productos que posee el usuario
    boolean isPremium = false;
    boolean isCategoriaPremium = false;
    boolean isSinPublicidad = false;

    ProgressDialog progDailog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        isPremium = bundle.getBoolean("isPremium");
        isCategoriaPremium = bundle.getBoolean("isCategoriaPremium");
        isSinPublicidad = bundle.getBoolean("isSinPublicidad");

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

        //((FinanfyActivity) getActivity()).mostrarPublicidad(true, false);

        listMovView = (ListView) this.getView().findViewById(
                R.id.listaMovimientos);
        listMovCatView = (ExpandableListView) this.getView().findViewById(
                R.id.listaMovimientosCategoria);

        drawer = (DrawerLayout) getActivity().findViewById(
                R.id.drawer_layout);
        filtros = (LinearLayout) getActivity().findViewById(R.id.right_drawer);

        txtMesActual = (TextView) getView().findViewById(R.id.mesActual);
        txtAnioActual = (TextView) getView().findViewById(R.id.anioActual);
        txtTotal = (TextView) getView().findViewById(R.id.txtTotal);

        btnAceptarFiltros = (LinearLayout) getActivity().findViewById(R.id.btnAceptarFiltros);
        btnCancelarFiltros = (LinearLayout) getActivity().findViewById(R.id.btnCancelarFiltros);
        btnGasto = (LinearLayout) getActivity().findViewById(R.id.btnGasto);
        btnIngreso = (LinearLayout) getActivity().findViewById(R.id.btnIngreso);
        btnFechaDesde = (LinearLayout) getActivity().findViewById(R.id.btnFechaDesde);
        btnFechaHasta = (LinearLayout) getActivity().findViewById(R.id.btnFechaHasta);
        txtFechaDesde = (TextView) getActivity().findViewById(R.id.txtFechaDesde);
        txtFechaHasta = (TextView) getActivity().findViewById(R.id.txtFechaHasta);
        layoutFechas = (LinearLayout) getActivity().findViewById(R.id.layoutFechas);
        layoutTarjetasFiltro = (LinearLayout) getActivity().findViewById(R.id.layoutTarjetasFiltro);
        layoutIntervaloFecha = (LinearLayout) getActivity().findViewById(R.id.layoutIntervaloFecha);
        layoutMeses = (LinearLayout) getActivity().findViewById(R.id.layoutMeses);
        spinnerFechas = (Spinner) getActivity().findViewById(R.id.spinnerFecha);
        spinnerTipoFiltroResumen = (Spinner) getActivity().findViewById(R.id.spinnerTipoLista);
        spinnerCategorias = (Spinner) getActivity().findViewById(R.id.spinnerCategoriasFiltro);
        spinnerSubcategorias = (Spinner) getActivity().findViewById(R.id.spinnerSubcategoriasFiltro);
        spinnerTipoModoPago = (Spinner) getActivity().findViewById(R.id.spinnerModoPagoFiltro);
        spinnerTarjetas = (Spinner) getActivity().findViewById(R.id.spinnerTarjetasFiltro);
        mesAnterior = (LinearLayout) getView().findViewById(R.id.layoutLeft);
        mesSiguiente = (LinearLayout) getView().findViewById(R.id.layoutRight);
        mes = (LinearLayout) getView().findViewById(R.id.mes);
        anio = (LinearLayout) getView().findViewById(R.id.anio);
        anioSelec = (TextView) getView().findViewById(R.id.anioSelec);
        diario = (LinearLayout) getView().findViewById(R.id.diario);
        diaSelec = (TextView) getView().findViewById(R.id.diaSelec);
        txtGasto = (TextView) getActivity().findViewById(R.id.txtGasto);
        txtIngreso = (TextView) getActivity().findViewById(R.id.txtIngreso);
        txtIntevaloFechas = (TextView) getActivity().findViewById(R.id.intervaloFecha);
        layoutSinRegistro = (LinearLayout) getView().findViewById(R.id.layoutSinRegistro);
        layoutPubli = (RelativeLayout) getView().findViewById(R.id.layoutPubli);

        //Se carga la publicidad
        AdView adView = (AdView) getActivity().findViewById(R.id.adView);
        if (isPremium || isSinPublicidad) {
            layoutPubli.setVisibility(View.GONE);
        } else {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        obternerFechaActual();
        idCuenta = cuentaSeleccionada();
        txtAnioActual.setText(String.valueOf(mYear));
        rellenarSpinners();
        configurarFiltros();
        obtenerTexto();

        try {
            insertarRecibos();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new CargarMovimientosTask().execute();

        mesAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipoFecha == 0) {
                    if (mMonth == 0) {
                        mMonth = 11;
                        mYear = mYear - 1;
                        txtAnioActual.setText(String.valueOf(mYear));
                    } else {
                        mMonth = mMonth - 1;
                    }
                } else if (tipoFecha == 2) {
                    mYearFiltro = mYearFiltro - 1;
                }else if(tipoFecha == 3){
                    daySelect = Util.obtenerDiaSelect(daySelect, false);
                }
                obtenerTexto();
                new CargarMovimientosTask().execute();
            }
        });

        mesSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tipoFecha == 0){
                    if (mMonth == 11) {
                        mMonth = 0;
                        mYear = mYear + 1;
                        txtAnioActual.setText(String.valueOf(mYear));
                    } else {
                        mMonth = mMonth + 1;
                    }
                }else if (tipoFecha == 2) {
                    mYearFiltro = mYearFiltro + 1;
                }else if(tipoFecha == 3){
                    daySelect = Util.obtenerDiaSelect(daySelect, true);
                }
                obtenerTexto();
                new CargarMovimientosTask().execute();
            }
        });

        spinnerFechas
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                               public void onItemSelected(AdapterView<?> parent,
                                                                          View view, int position, long id) {
                                                   // TODO Auto-generated method stub
                                                   if (position == 0) {
                                                       layoutFechas.setVisibility(View.GONE);
                                                       tipoFecha = 0;
                                                   } else if (position == 1) {
                                                       layoutFechas.setVisibility(View.VISIBLE);
                                                       tipoFecha = 1;
                                                   } else if (position == 2) {
                                                       layoutFechas.setVisibility(View.GONE);
                                                       tipoFecha = 2;
                                                   } else if (position == 3) {
                                                       layoutFechas.setVisibility(View.GONE);
                                                       tipoFecha = 3;
                                                   }
                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> parent) {

                                               }
                                           }
                );

        spinnerTipoModoPago
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                               public void onItemSelected(AdapterView<?> parent,
                                                                          View view, int position, long id) {
                                                   // TODO Auto-generated method stub
                                                   if (position == 2) {
                                                       layoutTarjetasFiltro.setVisibility(View.VISIBLE);
                                                   } else {
                                                       layoutTarjetasFiltro.setVisibility(View.GONE);
                                                   }
                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> parent) {

                                               }
                                           }
                );

        spinnerTipoFiltroResumen
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                               public void onItemSelected(AdapterView<?> parent,
                                                                          View view, int position, long id) {
                                                   // TODO Auto-generated method stub
                                                   tipoFiltro = position;
                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> parent) {

                                               }
                                           }
                );

        spinnerCategorias
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                               public void onItemSelected(AdapterView<?> parent,
                                                                          View view, int position, long id) {
                                                   // TODO Auto-generated method stub
                                                   if (listCategoriasFiltros.get(position).getIdIcon() == -1) {

                                                   } else {

                                                   }
                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> parent) {

                                               }
                                           }
                );

        listMovCatView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Categoria cat = listGroupChild.get(groupPosition);
                Movimiento mov = cat.getListaMovimientos().get(childPosition);
                //guardarMovimientoSeleccionado(mov.getId());
                getActivity().openContextMenu(v);
                return true;
            }
        });

        btnGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gastoPulsado) {
                    if (!ingresoPulsado) {
                        ingresoPulsado = true;
                    }
                    gastoPulsado = false;
                } else {
                    gastoPulsado = true;
                }
                pintarBotones();
            }
        });

        btnIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingresoPulsado) {
                    if (!gastoPulsado) {
                        gastoPulsado = true;
                    }
                    ingresoPulsado = false;
                } else {
                    ingresoPulsado = true;
                }
                pintarBotones();
            }
        });

        btnFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(DATE_DIALOG_DESDE_ID);
            }
        });

        btnFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(DATE_DIALOG_HASTA_ID);
            }
        });

        btnAceptarFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefsFiltros = getActivity().getSharedPreferences("ficheroConfFiltros", Context.MODE_PRIVATE);
                editorFiltros = prefsFiltros.edit();

                editorFiltros.putBoolean("ingresoPulsado", ingresoPulsado);
                editorFiltros.putBoolean("gastoPulsado", gastoPulsado);

                editorFiltros.putInt("spinnerFechas", spinnerFechas.getSelectedItemPosition());

                if (spinnerFechas.getSelectedItemPosition() == 0) {
                    editorFiltros.putBoolean("fechaDesde", false);
                    editorFiltros.putBoolean("fechaHasta", false);

                    Calendar c = Calendar.getInstance();
                    mDayDesde = 1;
                    mMonthDesde = c.get(Calendar.MONTH);
                    mYearDesde = c.get(Calendar.YEAR);

                    Date fechaFin = Util.getFinMes(mMonthDesde + 1, mYearDesde);
                    String fechaF = fechaFin.toString();
                    mYearHasta = Integer.parseInt(fechaF.substring(0, 4));
                    mMonthHasta = Integer.parseInt(fechaF.substring(5, 7)) - 1;
                    mDayHasta = Integer.parseInt(fechaF.substring(8, 10));
                } else {
                    editorFiltros.putBoolean("fechaDesde", true);
                    editorFiltros.putBoolean("fechaHasta", true);
                }

                String mesDesde;
                String diaDesde;
                String mesHasta;
                String diaHasta;

                if ((mMonthDesde) < 10) {
                    mesDesde = "0" + String.valueOf(mMonthDesde);
                } else {
                    mesDesde = String.valueOf(mMonthDesde);
                }

                if (mDayDesde < 10) {
                    diaDesde = "0" + String.valueOf(mDayDesde);
                } else {
                    diaDesde = String.valueOf(mDayDesde);
                }

                if ((mMonthHasta) < 10) {
                    mesHasta = "0" + String.valueOf(mMonthHasta);
                } else {
                    mesHasta = String.valueOf(mMonthHasta);
                }

                if (mDayHasta < 10) {
                    diaHasta = "0" + String.valueOf(mDayHasta);
                } else {
                    diaHasta = String.valueOf(mDayHasta);
                }

                editorFiltros.putInt("diaDesde", Integer.parseInt(diaDesde));
                editorFiltros.putInt("diaHasta", Integer.parseInt(diaHasta));
                editorFiltros.putInt("mesDesde", Integer.parseInt(mesDesde));
                editorFiltros.putInt("mesHasta", Integer.parseInt(mesHasta));
                editorFiltros.putInt("anioDesde", mYearDesde);
                editorFiltros.putInt("anioHasta", mYearHasta);

                editorFiltros.putInt("tipoFiltro", tipoFiltro);

                String idCat = listCategoriasFiltros.get(spinnerCategorias.getSelectedItemPosition()).getId();
                String idSub = listCategoriasFiltros.get(spinnerSubcategorias.getSelectedItemPosition()).getId();

                editorFiltros.putString("idCategoria", idCat);
                editorFiltros.putString("idSubcategoria", idSub);

                editorFiltros.putInt("spinnerTipoModoPago", spinnerTipoModoPago.getSelectedItemPosition());
                editorFiltros.putString("idTarjeta", listTarjetas.get(spinnerTarjetas.getSelectedItemPosition()).getId());

                editorFiltros.commit();
                drawer.closeDrawer(filtros);
                new CargarMovimientosTask().execute();
            }
        });

        btnCancelarFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configurarFiltros();
                drawer.closeDrawer(filtros);
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
                intent.putExtra("isPremium", isPremium);
                intent.putExtra("isSinPublicidad", isSinPublicidad);
                startActivityForResult(intent, RESUMEN);
                return true;
            case R.id.action_filter:
                drawer.openDrawer(filtros);
                return true;
            case R.id.action_excel:
                File file = Informes.CrearExcel("", listMov);
                if (file != null) {
                    Uri uri = Uri.fromFile(file);
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Finanfy - XLS File");
                    emailIntent.setType("message/rfc822");
                    startActivity(Intent.createChooser(emailIntent, "Email "));
                }
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
            //listMov = gestion.getMovimientosFiltros(db, mMonth, mYear, idCuenta);
            int tipoFecha = spinnerFechas.getSelectedItemPosition();
            String idCategoria = listCategoriasFiltros.get(spinnerCategorias.getSelectedItemPosition()).getId();
            String idSubcategoria = listSubcategoriasFiltros.get(spinnerSubcategorias.getSelectedItemPosition()).getId();
            int tipoPago = spinnerTipoModoPago.getSelectedItemPosition();
            String idTarjeta = listTarjetas.get(spinnerTarjetas.getSelectedItemPosition()).getId();

            listMov = gestion.getMovimientosFiltros(db, gastoPulsado, ingresoPulsado, tipoFecha, mYearFiltro, daySelect, tipoFiltro,
                    idCategoria, idSubcategoria, tipoPago, idTarjeta, mMonth, mYear, mDayDesde, mMonthDesde, mYearDesde,
                    mDayHasta, mMonthHasta, mYearHasta, idCuenta);
        }
        db.close();
    }

    public void rellenarListaExpansibleCategorias() {

        ArrayList<Categoria> categoriasAux = new ArrayList<Categoria>();
        ArrayList<Object> movimientos = new ArrayList<Object>();

        categoriasAux = rellenarListaCategorias();
        categorias = categoriasAux;

        for (int i = 0; i < categorias.size(); i++) {
            ArrayList<Movimiento> movimientosAux = new ArrayList<Movimiento>();
            Categoria cat = categorias.get(i);
            movimientosAux = getMovimientosCategoria(cat.getId(), listMov);
            cat.setListaMovimientos(movimientosAux);
            movimientos.add(movimientosAux);
        }

        listMovAux = movimientos;
        listGroupChild = categorias;
    }

    public void rellenarListaExpansibleSubcategorias() {

        ArrayList<Categoria> categoriasAux = new ArrayList<Categoria>();
        ArrayList<Object> movimientos = new ArrayList<Object>();

        categoriasAux = rellenarListaSubcategorias();
        categorias = categoriasAux;

        for (int i = 0; i < categorias.size(); i++) {
            ArrayList<Movimiento> movimientosAux = new ArrayList<Movimiento>();
            Categoria cat = categorias.get(i);
            movimientosAux = getMovimientosSubcategoria(cat.getId(), listMov);
            cat.setListaMovimientos(movimientosAux);
            movimientos.add(movimientosAux);
        }

        listMovAux = movimientos;
        listGroupChild = categorias;
    }

    public ArrayList<Movimiento> getMovimientosCategoria(String idCat,
                                                         ArrayList<Movimiento> listMovimientos) {
        ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();

        for (int i = 0; i < listMovimientos.size(); i++) {
            Movimiento mov = listMovimientos.get(i);
            if (idCat.equals(mov.getIdCategoria())) {
                listMov.add(mov);
            }
        }
        return listMov;
    }

    public ArrayList<Movimiento> getMovimientosSubcategoria(String idCat,
                                                            ArrayList<Movimiento> listMovimientos) {
        ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();

        for (int i = 0; i < listMovimientos.size(); i++) {
            Movimiento mov = listMovimientos.get(i);
            if (idCat.equals(mov.getIdSubcategoria())) {
                listMov.add(mov);
            }
        }
        return listMov;
    }

    public ArrayList<Categoria> rellenarListaCategorias() {
        ArrayList<Categoria> listaCat = new ArrayList<Categoria>();
        boolean aniadir = true;

        for (int i = 0; i < listMov.size(); i++) {
            aniadir = true;
            Movimiento mov = listMov.get(i);
            Categoria cat = new Categoria();
            cat.setId(mov.getIdCategoria());
            cat.setDescripcion(mov.getDescCategoria());
            cat.setIdIcon(mov.getIdIconCat());
            if (listaCat.size() == 0) {
                if (cat.getId().equals("0")) {
                    cat.setDescripcion(getResources().getString(R.string.otros));
                }
                listaCat.add(cat);
            } else {
                for (int j = 0; j < listaCat.size(); j++) {
                    Categoria cat2 = listaCat.get(j);
                    if (cat.getId().equals(cat2.getId())) {
                        aniadir = false;
                        break;
                    } else {
                        if (cat.getId().equals("0")) {
                            cat.setDescripcion(getResources().getString(R.string.otros));
                        }
                    }
                }
                if (aniadir) {
                    listaCat.add(cat);
                }
            }
        }
        Collections.sort(listaCat);
        return listaCat;
    }

    public ArrayList<Categoria> rellenarListaSubcategorias() {
        ArrayList<Categoria> listaCat = new ArrayList<Categoria>();
        boolean aniadir = true;

        for (int i = 0; i < listMov.size(); i++) {
            aniadir = true;
            Movimiento mov = listMov.get(i);
            Categoria cat = new Categoria();
            cat.setId(mov.getIdSubcategoria());
            cat.setDescripcion(mov.getDescSubcategoria());
            cat.setIdIcon(mov.getIdIconSub());
            if (listaCat.size() == 0) {
                if (cat.getId().equals("0")) {
                    cat.setDescripcion(getResources().getString(R.string.otros));
                }
                listaCat.add(cat);
            } else {
                for (int j = 0; j < listaCat.size(); j++) {
                    Categoria cat2 = listaCat.get(j);
                    if (cat.getId().equals(cat2.getId())) {
                        aniadir = false;
                        break;
                    } else {
                        if (cat.getId().equals("0")) {
                            cat.setDescripcion(getResources().getString(R.string.otros));
                        }
                    }
                }
                if (aniadir) {
                    listaCat.add(cat);
                }
            }
        }
        Collections.sort(listaCat);
        return listaCat;
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

    public void pintarBotones() {
        if (gastoPulsado) {
            btnGasto.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.rounded_layout_azul));
            txtGasto.setTextColor(getActivity().getResources().getColor(R.color.blanco));
        } else {
            btnGasto.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.rounded_layout_gris));
            txtGasto.setTextColor(getActivity().getResources().getColor(R.color.txtGris));
        }

        if (ingresoPulsado) {
            btnIngreso.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.rounded_layout_azul));
            txtIngreso.setTextColor(getActivity().getResources().getColor(R.color.blanco));
        } else {
            btnIngreso.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.rounded_layout_gris));
            txtIngreso.setTextColor(getActivity().getResources().getColor(R.color.txtGris));
        }
    }

    public void insertarRecibos() {
        Calendar c = Calendar.getInstance();
        int mYearAc = c.get(Calendar.YEAR);
        int mMonthAc = c.get(Calendar.MONTH) + 1;
        int mDayAc = c.get(Calendar.DAY_OF_MONTH);
        Date fechaAc = new Date(mYearAc - 1900, mMonthAc - 1, mDayAc);
        int idCuenta = cuentaSeleccionada();

        ArrayList<Recibo> recibos = null;
        db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            recibos = (ArrayList<Recibo>) gestion.getRecibos(db, idCuenta);
        }
        db.close();
        for (int i = 0; i < recibos.size(); i++) {
            Recibo recibo = recibos.get(i);
            int anioDesdeRecibo = Integer.parseInt(recibo.getFechaIni()
                    .substring(0, 4));
            int diaDesdeRecibo = Integer.parseInt(recibo.getFechaIni()
                    .substring(8, 10));
            int mesDesdeRecibo = Integer.parseInt(recibo.getFechaIni()
                    .substring(5, 7));
            Date fechaDesdeRecibo = new Date(anioDesdeRecibo - 1900,
                    mesDesdeRecibo - 1, diaDesdeRecibo);

            int anioHastaRecibo = Integer.parseInt(recibo.getFechaFin()
                    .substring(0, 4));
            int diaHastaRecibo = Integer.parseInt(recibo.getFechaFin()
                    .substring(8, 10));
            int mesHastaRecibo = Integer.parseInt(recibo.getFechaFin()
                    .substring(5, 7));
            Date fechaHastaRecibo = new Date(anioHastaRecibo - 1900,
                    mesHastaRecibo - 1, diaHastaRecibo);

            int idRecibo = Integer.parseInt(recibo.getId());

            for (int j = anioDesdeRecibo; j <= mYearAc; j++) {
                if (j < mYearAc) {
                    int desdeMes = 0;
                    if (j == anioDesdeRecibo) {
                        desdeMes = mesDesdeRecibo;
                    } else {
                        desdeMes = 1;
                    }
                    for (int k = desdeMes; k <= 12; k++) {

                        Date fechaIniMovimiento = new Date(j - 1900, k - 1, 1);
                        Date fechaFinMovimiento = null;
                        db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1,
                                null);
                        if (db != null) {
                            fechaFinMovimiento = gestion.getFinMes(k, j);
                        }
                        db.close();

                        boolean isRegistrado = false;
                        db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1,
                                null);
                        if (db != null) {
                            isRegistrado = gestion.getReciboId(db, idRecibo,
                                    fechaIniMovimiento, fechaFinMovimiento);
                        }
                        if (!isRegistrado) {
                            Date fechaRegistro = new Date(j - 1900, k - 1,
                                    diaDesdeRecibo);
                            if (fechaRegistro.before(fechaAc)
                                    || fechaRegistro.equals(fechaAc)) {
                                gestion.insertarMovimiento(
                                        db,
                                        3,
                                        Float.parseFloat(recibo.getCantidad()),
                                        recibo.getDescripcion(),
                                        fechaRegistro,
                                        Integer.parseInt(recibo
                                                .getIdCategoria()),
                                        Integer.parseInt(recibo
                                                .getIdSubcategoria()),
                                        true,
                                        recibo.isTarjeta(),
                                        k,
                                        j,
                                        Integer.parseInt(recibo.getIdTarjeta()),
                                        Integer.parseInt(recibo.getId()),
                                        idCuenta);
                            }
                        }
                        db.close();
                    }
                } else {
                    for (int k = 1; k <= mMonthAc; k++) {
                        Date fechaIniMovimiento = new Date(mYearAc - 1900,
                                k - 1, 1);
                        Date fechaFinMovimiento = null;
                        db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1,
                                null);
                        if (db != null) {
                            fechaFinMovimiento = gestion.getFinMes(k, mYearAc);
                        }
                        db.close();

                        if (fechaDesdeRecibo.before(fechaIniMovimiento)
                                && fechaHastaRecibo.after(fechaFinMovimiento)) {
                            boolean isRegistrado = false;
                            db = getActivity().openOrCreateDatabase(BD_NOMBRE,
                                    1, null);
                            if (db != null) {
                                isRegistrado = gestion.getReciboId(db,
                                        idRecibo, fechaIniMovimiento,
                                        fechaFinMovimiento);
                            }
                            if (!isRegistrado) {
                                Date fechaRegistro = new Date(j - 1900, k - 1,
                                        diaDesdeRecibo);
                                if (fechaRegistro.before(fechaAc)
                                        || fechaRegistro.equals(fechaAc)) {
                                    gestion.insertarMovimiento(db, 3, Float
                                                    .parseFloat(recibo.getCantidad()),
                                            recibo.getDescripcion(),
                                            fechaRegistro, Integer
                                                    .parseInt(recibo
                                                            .getIdCategoria()),
                                            Integer.parseInt(recibo
                                                    .getIdSubcategoria()),
                                            true, recibo.isTarjeta(), k, j,
                                            Integer.parseInt(recibo
                                                    .getIdTarjeta()), Integer
                                                    .parseInt(recibo.getId()),
                                            idCuenta);
                                }
                            }
                            db.close();
                        } else {
                            if (mesDesdeRecibo == k) {
                                if (fechaDesdeRecibo.before(fechaAc) || fechaDesdeRecibo.equals(fechaAc)) {
                                    db = getActivity().openOrCreateDatabase(
                                            BD_NOMBRE, 1, null);
                                    if (db != null) {
                                        Date fechaI = new Date(j - 1900,
                                                mesDesdeRecibo - 1, 1);
                                        Date fechaF = gestion.getFinMes(
                                                mesDesdeRecibo, j);
                                        boolean isRegistrado = gestion
                                                .getReciboId(db, idRecibo,
                                                        fechaI, fechaF);
                                        if (!isRegistrado) {
                                            Date fechaRegistro = new Date(j - 1900, k - 1,
                                                    diaDesdeRecibo);
                                            gestion.insertarMovimiento(db, 3,
                                                    Float.parseFloat(recibo.getCantidad()), recibo.getDescripcion(), fechaRegistro,
                                                    Integer.parseInt(recibo.getIdCategoria()),
                                                    Integer.parseInt(recibo.getIdSubcategoria()), true, recibo.isTarjeta(),
                                                    k, j,
                                                    Integer.parseInt(recibo.getIdTarjeta()), Integer.parseInt(recibo.getId()), idCuenta);
                                        }
                                    }
                                    db.close();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void obternerFechaActual() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mYearHasta = c.get(Calendar.YEAR);
        mMonthHasta = c.get(Calendar.MONTH);
        mDayHasta = c.get(Calendar.DAY_OF_MONTH);
        mYearDesde = c.get(Calendar.YEAR);
        mMonthDesde = c.get(Calendar.MONTH);
        mDayDesde = c.get(Calendar.DAY_OF_MONTH);

        mYearFiltro = c.get(Calendar.YEAR);

        mDaySelectFiltro = c.get(Calendar.DAY_OF_MONTH);
        mMonthSelectFiltro = c.get(Calendar.MONTH);
        mYearSelectFiltro = c.get(Calendar.YEAR);

        daySelect = new Date(mYearSelectFiltro - 1900, mMonthSelectFiltro, mDaySelectFiltro);
    }

    public void obtenerCategorias() {
        //listCategorias = new ArrayList<Categoria>();
        listCategoriasFiltros = new ArrayList<Categoria>();
        db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            // Recuperamos el listado del spinner Categorias
            //listCategorias = (ArrayList<Categoria>) gestion.getCategorias(db,
            //        "Categorias", "idCategoria", getActivity());
            listCategoriasFiltros = (ArrayList<Categoria>) gestion.getCategoriasFiltros(db,
                    "Categorias", "idCategoria", getActivity());
        }
        db.close();

        // Creamos el adaptador
        ListAdapterSpinner spinner_adapterCat = new ListAdapterSpinner(
                getActivity(), R.layout.spinner_iconos,
                listCategoriasFiltros);

        spinnerCategorias.setAdapter(spinner_adapterCat);
    }

    public void obtenerSubcategorias() {
        listSubcategoriasFiltros = new ArrayList<Categoria>();
        db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            listSubcategoriasFiltros = (ArrayList<Categoria>) gestion.getCategoriasFiltros(
                    db, "Subcategorias", "idSubcategoria", getActivity());
        }
        db.close();

        // Creamos el adaptador
        ListAdapterSpinner spinner_adapterSubcat = new ListAdapterSpinner(
                getActivity(), R.layout.spinner_iconos,
                listSubcategoriasFiltros);
        spinnerSubcategorias.setAdapter(spinner_adapterSubcat);
    }

    // Rellena el spinner modo pago
    public void obtenerModoPago() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.modoPagoFiltro,
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
        listTarjetas = new ArrayList<Tarjeta>();
        db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            // Recuperamos el listado del spinner Categorias
            listTarjetas = (ArrayList<Tarjeta>) gestion.getTarjetasFiltro(db, getActivity());
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

    public void configurarFiltros() {
        prefsFiltros = getActivity().getSharedPreferences("ficheroConfFiltros", Context.MODE_PRIVATE);

        ingresoPulsado = prefsFiltros.getBoolean("ingresoPulsado", true);
        if (ingresoPulsado) {
            btnIngreso.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.rounded_layout_azul));
            txtIngreso.setTextColor(getActivity().getResources().getColor(R.color.blanco));
        } else {
            btnIngreso.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.rounded_layout_gris));
            txtIngreso.setTextColor(getActivity().getResources().getColor(R.color.txtGris));
        }

        gastoPulsado = prefsFiltros.getBoolean("gastoPulsado", true);
        if (gastoPulsado) {
            btnGasto.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.rounded_layout_azul));
            txtGasto.setTextColor(getActivity().getResources().getColor(R.color.blanco));
        } else {
            btnGasto.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.rounded_layout_gris));
            txtGasto.setTextColor(getActivity().getResources().getColor(R.color.txtGris));
        }

        boolean fechaDesde = prefsFiltros.getBoolean("fechaDesde", false);
        boolean fechaHasta = prefsFiltros.getBoolean("fechaHasta", false);

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

        updateDisplayDesde();
        updateDisplayHasta();

        int posiFechas = prefsFiltros.getInt("spinnerFechas", 0);
        spinnerFechas.setSelection(posiFechas);

        if (posiFechas == 0) {
            layoutFechas.setVisibility(View.GONE);
            tipoFecha = 0;
        } else if (posiFechas == 1) {
            layoutFechas.setVisibility(View.VISIBLE);
            tipoFecha = 1;
        } else if (posiFechas == 2) {
            layoutFechas.setVisibility(View.GONE);
            tipoFecha = 2;
        } else if (posiFechas == 3) {
            layoutFechas.setVisibility(View.GONE);
            tipoFecha = 3;
        }

        int posiTipo = prefsFiltros.getInt("tipoFiltro", 0);
        spinnerTipoFiltroResumen.setSelection(posiTipo);
        tipoFiltro = posiTipo;

        String idCat = prefsFiltros.getString("idCategoria", "-1");
        int posiCat = 0;
        for (int i = 0; i < listCategoriasFiltros.size(); i++) {
            if (listCategoriasFiltros.get(i).getId().equals(idCat)) {
                posiCat = i;
                break;
            }
        }
        spinnerCategorias.setSelection(posiCat);

        String idSub = prefsFiltros.getString("idSubcategoria", "-1");
        int posiSub = 0;
        for (int i = 0; i < listSubcategoriasFiltros.size(); i++) {
            if (listSubcategoriasFiltros.get(i).getId().equals(idSub)) {
                posiSub = i;
                break;
            }
        }
        spinnerSubcategorias.setSelection(posiSub);

        int posiTipoModoPago = prefsFiltros.getInt("spinnerTipoModoPago", 0);
        spinnerTipoModoPago.setSelection(posiTipoModoPago);
        if (posiTipoModoPago == 2) {
            layoutTarjetasFiltro.setVisibility(View.VISIBLE);
        } else {
            layoutTarjetasFiltro.setVisibility(View.GONE);
        }

        String idTarjeta = prefsFiltros.getString("idTarjeta", "-1");
        int posiTar = 0;
        for (int i = 0; i < listTarjetas.size(); i++) {
            if (listTarjetas.get(i).getId().equals(idTarjeta)) {
                posiTar = i;
                break;
            }
        }
        spinnerTarjetas.setSelection(posiTar);
    }

    private void updateDisplayDesde() {
        String mes;
        String dia;
        int formato = prefs.getInt("formatoFecha", 1);

        if ((mMonthDesde + 1) < 10) {
            mes = "0" + String.valueOf(mMonthDesde + 1);
        } else {
            mes = String.valueOf(mMonthDesde + 1);
        }

        if (mDayDesde < 10) {
            dia = "0" + String.valueOf(mDayDesde);
        } else {
            dia = String.valueOf(mDayDesde);
        }

        if(formato == 1){
            txtFechaDesde.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(dia).append("/").append(mes).append("/")
                    .append(mYearDesde));
        }else if (formato == 2){
            txtFechaDesde.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mYearDesde).append("/").append(mes).append("/")
                    .append(dia));
        }

    }

    private void updateDisplayHasta() {
        String mes;
        String dia;
        int formato = prefs.getInt("formatoFecha", 1);

        if ((mMonthHasta + 1) < 10) {
            mes = "0" + String.valueOf(mMonthHasta + 1);
        } else {
            mes = String.valueOf(mMonthHasta + 1);
        }

        if (mDayHasta < 10) {
            dia = "0" + String.valueOf(mDayHasta);
        } else {
            dia = String.valueOf(mDayHasta);
        }

        if(formato == 1){
            txtFechaHasta.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(dia).append("/").append(mes).append("/")
                    .append(mYearHasta));
        }else if (formato == 2){
            txtFechaHasta.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mYearHasta).append("/").append(mes).append("/")
                    .append(dia));
        }
    }

    private DatePickerDialog.OnDateSetListener mDateSetListenerDesde = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYearDesde = year;
            mMonthDesde = monthOfYear;
            mDayDesde = dayOfMonth;
            updateDisplayDesde();
        }
    };

    private DatePickerDialog.OnDateSetListener mDateSetListenerHasta = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYearHasta = year;
            mMonthHasta = monthOfYear;
            mDayHasta = dayOfMonth;
            updateDisplayHasta();
        }
    };

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_DESDE_ID:
                DatePickerDialog dialogDesde = new DatePickerDialog(
                        getActivity(), mDateSetListenerDesde, mYearDesde, mMonthDesde, mDayDesde);
                dialogDesde.show();
                break;
            case DATE_DIALOG_HASTA_ID:
                DatePickerDialog dialogHasta = new DatePickerDialog(
                        getActivity(), mDateSetListenerHasta, mYearHasta, mMonthHasta, mDayHasta);
                dialogHasta.show();
                break;
        }
        return null;
    }

    public void obtenerTexto() {
        if (tipoFecha == 0) {
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
        } else if (tipoFecha == 2) {
            anioSelec.setText(String.valueOf(mYearFiltro));
        } else if (tipoFecha == 3){
            diaSelec.setText(Util.formatearFecha(daySelect.toString(), prefs));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case RESUMEN:
                   // ((FinanfyActivity) getActivity()).mostrarPublicidad(false, true);
                    break;
                case EDIT_DELETE:
                    //((FinanfyActivity) getActivity()).mostrarPublicidad(true, false);
                    break;
            }
        }
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
            if (tipoFiltro == 1) {
                rellenarListaExpansibleCategorias();
            } else if (tipoFiltro == 2) {
                rellenarListaExpansibleSubcategorias();
            }

            calcularTotal();
            return null;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Void result) {
            if (tipoFiltro == 0) {
                listAdapter = new ListAdapter(getActivity(), listMov);
                listMovView.setAdapter(listAdapter);
                if (listMov.size() == 0) {
                    layoutSinRegistro.setVisibility(View.VISIBLE);
                    listMovView.setVisibility(View.GONE);
                    listMovCatView.setVisibility(View.GONE);
                } else {
                    listMovView.setVisibility(View.VISIBLE);
                    listMovCatView.setVisibility(View.GONE);
                    layoutSinRegistro.setVisibility(View.GONE);
                }
            } else if (tipoFiltro == 1) {
                listMovCatView.setGroupIndicator(null);
                listMovCatView.setClickable(true);
                listMovCatView.setDividerHeight(2);

                listAdapterCategorias = new ListaAdapterResumenExpandibleAdapter(
                        getActivity(), categorias, listMovAux);
                listMovCatView.setAdapter(listAdapterCategorias);

                if (listMov.size() == 0) {
                    layoutSinRegistro.setVisibility(View.VISIBLE);
                    listMovView.setVisibility(View.GONE);
                    listMovCatView.setVisibility(View.GONE);
                } else {
                    layoutSinRegistro.setVisibility(View.GONE);
                    listMovView.setVisibility(View.GONE);
                    listMovCatView.setVisibility(View.VISIBLE);
                }
            } else {
                listMovCatView.setGroupIndicator(null);
                listMovCatView.setClickable(true);
                listMovCatView.setDividerHeight(2);

                listAdapterSubcategorias = new ListaAdapterResumenExpandibleSubAdapter(
                        getActivity(), categorias, listMovAux);
                listMovCatView.setAdapter(listAdapterSubcategorias);

                if (listMov.size() == 0) {
                    layoutSinRegistro.setVisibility(View.VISIBLE);
                    listMovView.setVisibility(View.GONE);
                    listMovCatView.setVisibility(View.GONE);
                } else {
                    layoutSinRegistro.setVisibility(View.GONE);
                    listMovView.setVisibility(View.GONE);
                    listMovCatView.setVisibility(View.VISIBLE);
                }
            }

            // Rellenamos el dato con el total
            txtTotal.setText(Util.formatear(total, prefs));
            if (total > 0) {
                txtTotal.setTextColor(getResources().getColor(R.color.txtAzul));
            } else {
                txtTotal.setTextColor(Color.RED);
            }

            if (tipoFecha == 0) {
                layoutMeses.setVisibility(View.VISIBLE);
                layoutIntervaloFecha.setVisibility(View.GONE);
                mes.setVisibility(View.VISIBLE);
                anio.setVisibility(View.GONE);
                diario.setVisibility(View.GONE);
            } else if (tipoFecha == 1) {
                txtIntevaloFechas.setText(txtFechaDesde.getText().toString().trim() + " - " + txtFechaHasta.getText().toString().trim());
                layoutIntervaloFecha.setVisibility(View.VISIBLE);
                layoutMeses.setVisibility(View.GONE);
                mes.setVisibility(View.GONE);
                anio.setVisibility(View.GONE);
                diario.setVisibility(View.GONE);
            } else if (tipoFecha == 2) {
                layoutMeses.setVisibility(View.VISIBLE);
                layoutIntervaloFecha.setVisibility(View.GONE);
                mes.setVisibility(View.GONE);
                anio.setVisibility(View.VISIBLE);
                diario.setVisibility(View.GONE);
            }else if(tipoFecha == 3){
                layoutMeses.setVisibility(View.VISIBLE);
                layoutIntervaloFecha.setVisibility(View.GONE);
                mes.setVisibility(View.GONE);
                anio.setVisibility(View.GONE);
                diario.setVisibility(View.VISIBLE);
            }

            obtenerTexto();

            progDailog.dismiss();
        }
    }

    public class ListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private ArrayList<Movimiento> listaMov = new ArrayList<Movimiento>();
        Locale locale = Locale.getDefault();
        String languaje = locale.getLanguage();
        Context context;
        Movimiento mov;

        public ListAdapter(Context context, ArrayList<Movimiento> lista) {
            listaMov = lista;
            mInflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listaMov.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return listaMov.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView txtFecha;
            TextView txtCategoria;
            TextView txtDescripcion;
            TextView txtCant;
            ImageView imgView;
            ImageView imgTarjeta;
            LinearLayout layoutTarjeta;
            LinearLayout layoutMovimiento;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.lista_movimientos, null);
            }

            txtFecha = (TextView) convertView.findViewById(R.id.txtFecha);
            txtCategoria = (TextView) convertView.findViewById(R.id.txtCategoria);
            txtDescripcion = (TextView) convertView.findViewById(R.id.txtDescripcion);
            txtCant = (TextView) convertView.findViewById(R.id.txtCant);
            imgView = (ImageView) convertView.findViewById(R.id.iconCategoria);
            layoutTarjeta = (LinearLayout) convertView.findViewById(R.id.layoutTarjeta);
            imgTarjeta = (ImageView) convertView.findViewById(R.id.imgTarjeta);
            layoutMovimiento = (LinearLayout) convertView.findViewById(R.id.layoutMovimiento);

            if (!listaMov.get(position).toString().equals("")) {
                txtDescripcion.setText(listaMov.get(position).toString());
            } else {
                txtDescripcion.setVisibility(View.GONE);
            }
            txtCant.setText(listaMov.get(position).getCantidadAux());
            String fecha = Util.formatearFecha(listaMov.get(position).getFecha().toString(), prefs);
            txtFecha.setText(fecha);
            imgView.setBackgroundResource(Util.obtenerIconoCategoria(listaMov.get(position).getIdIconCat()));

            txtCant.setText(Util.formatear(Float.parseFloat(listaMov.get(position).getCantidad()), prefs));
            if (listaMov.get(position).getCantidadAux().substring(0, 1).equals("-")) {
                txtCant.setTextColor(Color.RED);
            } else {
                txtCant.setTextColor(context.getResources().getColor(R.color.txtAzul));
            }

            // rellenamos el campo de las categorias
            if (!listaMov.get(position).getDescCategoria().equals("-")) {
                txtCategoria.setText(listaMov.get(position).getDescCategoria());
            } else {
                txtCategoria.setText(context.getResources().getString(R.string.otros));
            }

            if (listaMov.get(position).isTarjeta()) {
                layoutTarjeta.setVisibility(View.VISIBLE);

                Tarjeta tar = new Tarjeta();
                db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
                if (db != null) {
                    tar = gestion.getTarjetaId(db, Integer.parseInt(listaMov.get(position).getIdTarjeta()));
                }
                db.close();

                imgTarjeta.setBackgroundResource(Util.obtenerIconoTarjeta(tar.getIdIcon()));
            } else {
                layoutTarjeta.setVisibility(View.GONE);
            }

            layoutMovimiento.setTag(position);

            layoutMovimiento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int posiSel = (int) v.getTag();
                    mov = listaMov.get(posiSel);
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(context, NuevoEditMovimientosActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("idMovimiento", mov.getId());
                    bundle.putString("cantidad", mov.getCantidad());
                    bundle.putString("tipoMovimiento", mov.getTipo());
                    bundle.putString("idCategoria", mov.getIdCategoria());
                    bundle.putString("idSubcategoria", mov.getIdSubcategoria());
                    bundle.putBoolean("tipoPago", mov.isTarjeta());
                    bundle.putString("idTarjeta", mov.getIdTarjeta());
                    bundle.putString("fecha", mov.getFecha().toString());
                    bundle.putString("notas", mov.toString());
                    intent.putExtra("isPremium", isPremium);
                    intent.putExtra("isSinPublicidad", isSinPublicidad);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, EDIT_DELETE);
                }
            });


            return convertView;
        }

    }

    public class ListaAdapterResumenExpandibleAdapter extends
            BaseExpandableListAdapter {

        private Context _context;
        private Activity activity;
        private ArrayList<Object> childtems;
        private LayoutInflater inflater;
        private ArrayList<Categoria> parentItems;
        private ArrayList<Movimiento> child;
        private Movimiento mov;

        public ListaAdapterResumenExpandibleAdapter(Context context,
                                                    ArrayList<Categoria> parents, ArrayList<Object> childern) {
            _context = context;
            this.parentItems = parents;
            this.childtems = childern;
        }

        public void setInflater(LayoutInflater inflater, Activity activity) {
            this.inflater = inflater;
            this.activity = activity;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            TextView text;
            TextView text2;
            TextView text3;
            TextView text4;
            LinearLayout layoutTarjeta;
            LinearLayout layoutMovimiento;
            ImageView imgTarjeta;

            child = (ArrayList<Movimiento>) childtems.get(groupPosition);
            mov = child.get(childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.lista, null);
            }

            text = (TextView) convertView.findViewById(R.id.txtDescripcion);
            text2 = (TextView) convertView.findViewById(R.id.txtCant);
            text3 = (TextView) convertView.findViewById(R.id.txtCategoria);
            text4 = (TextView) convertView.findViewById(R.id.txtFecha);
            layoutTarjeta = (LinearLayout) convertView.findViewById(R.id.layoutTarjeta);
            imgTarjeta = (ImageView) convertView.findViewById(R.id.imgTarjeta);
            layoutMovimiento = (LinearLayout) convertView.findViewById(R.id.layoutMovimiento);

            text.setText(mov.toString());
            if (mov.toString().equals("")) {
                text.setVisibility(View.GONE);
            }

            String cant = mov.getCantidadAux().replace(",", ".");
            text2.setText(Util.formatear(Float.parseFloat(cant), prefs));

            String fecha = Util.formatearFecha(mov.getFecha().toString(), prefs);
            text4.setText(fecha);

            if (mov.getCantidadAux().substring(0, 1).equals("-")) {
                text2.setTextColor(Color.RED);
            } else {
                text2.setTextColor(_context.getResources().getColor(R.color.txtAzul));
            }

            if (!mov.getDescSubcategoria().equals("-")) {
                text3.setText(mov.getDescSubcategoria());
            } else {
                text3.setText(_context.getResources().getString(R.string.otros));
            }

            if (mov.isTarjeta()) {
                layoutTarjeta.setVisibility(View.VISIBLE);

                Tarjeta tar = new Tarjeta();
                db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
                if (db != null) {
                    tar = gestion.getTarjetaId(db, Integer.parseInt(mov.getIdTarjeta()));
                }
                db.close();

                imgTarjeta.setBackgroundResource(Util.obtenerIconoTarjeta(tar.getIdIcon()));
            } else {
                layoutTarjeta.setVisibility(View.GONE);
            }

            layoutMovimiento.setTag(childPosition);

            layoutMovimiento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int posiSel = (int) v.getTag();
                    mov = child.get(posiSel);
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(_context, NuevoEditMovimientosActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("idMovimiento", mov.getId());
                    bundle.putString("cantidad", mov.getCantidad());
                    bundle.putString("tipoMovimiento", mov.getTipo());
                    bundle.putString("idCategoria", mov.getIdCategoria());
                    bundle.putString("idSubcategoria", mov.getIdSubcategoria());
                    bundle.putBoolean("tipoPago", mov.isTarjeta());
                    bundle.putString("idTarjeta", mov.getIdTarjeta());
                    bundle.putString("fecha", mov.getFecha().toString());
                    bundle.putString("notas", mov.toString());
                    intent.putExtra("isPremium", isPremium);
                    intent.putExtra("isSinPublicidad", isSinPublicidad);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, EDIT_DELETE);
                }
            });

            return convertView;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            float total = 0;

            Categoria cat = parentItems.get(groupPosition);
            ArrayList<Movimiento> listaMov = cat.getListaMovimientos();

            for (int i = 0; i < listaMov.size(); i++) {
                Movimiento mov = listaMov.get(i);
                if (mov.getIdCategoria().equals(cat.getId())) {
                    total = total + Float.parseFloat(mov.getCantidad());
                }
            }

            DecimalFormat df = new DecimalFormat("0.00");

            String headerTitle = cat.toString();
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater
                        .inflate(R.layout.lista_categorias, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.txtCategoria);
            lblListHeader.setText(headerTitle);

            TextView lblListCant = (TextView) convertView
                    .findViewById(R.id.txtCant);

            String cant = df.format(total);
            cant = cant.replace(",", ".");

            lblListCant.setText(Util.formatear(Float.parseFloat(cant), prefs));

            ImageView categoriaIcon = (ImageView) convertView
                    .findViewById(R.id.iconCategoria);

            categoriaIcon.setBackgroundDrawable(_context.getResources()
                    .getDrawable(Util.obtenerIconoCategoria(cat.getIdIcon())));

            if (total < 0) {
                lblListCant.setTextColor(Color.RED);
            } else {
                lblListCant.setTextColor(_context.getResources().getColor(R.color.txtAzul));
            }

            return convertView;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return ((ArrayList<Movimiento>) childtems.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public int getGroupCount() {
            return parentItems.size();
        }

        @Override
        public void onGroupCollapsed(int groupPosition) {
            super.onGroupCollapsed(groupPosition);
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            super.onGroupExpanded(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public class ListaAdapterResumenExpandibleSubAdapter extends
            BaseExpandableListAdapter {

        private Context _context;
        private Activity activity;
        private ArrayList<Object> childtems;
        private LayoutInflater inflater;
        private ArrayList<Categoria> parentItems;
        private ArrayList<Movimiento> child;
        private Movimiento mov;

        public ListaAdapterResumenExpandibleSubAdapter(Context context,
                                                       ArrayList<Categoria> parents, ArrayList<Object> childern) {
            _context = context;
            this.parentItems = parents;
            this.childtems = childern;
        }

        public void setInflater(LayoutInflater inflater, Activity activity) {
            this.inflater = inflater;
            this.activity = activity;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            TextView text;
            TextView text2;
            TextView text3;
            TextView text4;
            LinearLayout layoutTarjeta;
            LinearLayout layoutMovimiento;
            ImageView imgTarjeta;

            child = (ArrayList<Movimiento>) childtems.get(groupPosition);
            mov = child.get(childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.lista, null);
            }

            layoutTarjeta = (LinearLayout) convertView.findViewById(R.id.layoutTarjeta);
            layoutMovimiento = (LinearLayout) convertView.findViewById(R.id.layoutMovimiento);
            text = (TextView) convertView.findViewById(R.id.txtDescripcion);
            text2 = (TextView) convertView.findViewById(R.id.txtCant);
            text3 = (TextView) convertView.findViewById(R.id.txtCategoria);
            text4 = (TextView) convertView.findViewById(R.id.txtFecha);
            imgTarjeta = (ImageView) convertView.findViewById(R.id.imgTarjeta);

            text.setText(mov.toString());
            if (mov.toString().equals("")) {
                text.setVisibility(View.GONE);
            }

            String cant = mov.getCantidadAux().replace(",", ".");
            text2.setText(Util.formatear(Float.parseFloat(cant), prefs));

            String fecha = Util.formatearFecha(mov.getFecha().toString(), prefs);
            text4.setText(fecha);

            if (mov.getCantidadAux().substring(0, 1).equals("-")) {
                text2.setTextColor(Color.RED);
            } else {
                text2.setTextColor(_context.getResources().getColor(R.color.txtAzul));
            }

            if (!mov.getDescCategoria().equals("-")) {
                text3.setText(mov.getDescCategoria());
            } else {
                text3.setText(_context.getResources().getString(R.string.otros));
            }

            if (mov.isTarjeta()) {
                layoutTarjeta.setVisibility(View.VISIBLE);

                Tarjeta tar = new Tarjeta();
                db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
                if (db != null) {
                    tar = gestion.getTarjetaId(db, Integer.parseInt(mov.getIdTarjeta()));
                }
                db.close();

                imgTarjeta.setBackgroundResource(Util.obtenerIconoTarjeta(tar.getIdIcon()));
            } else {
                layoutTarjeta.setVisibility(View.GONE);
            }

            layoutMovimiento.setTag(childPosition);

            layoutMovimiento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int posiSel = (int) v.getTag();
                    mov = child.get(posiSel);
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(_context, NuevoEditMovimientosActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("idMovimiento", mov.getId());
                    bundle.putString("cantidad", mov.getCantidad());
                    bundle.putString("tipoMovimiento", mov.getTipo());
                    bundle.putString("idCategoria", mov.getIdCategoria());
                    bundle.putString("idSubcategoria", mov.getIdSubcategoria());
                    bundle.putBoolean("tipoPago", mov.isTarjeta());
                    bundle.putString("idTarjeta", mov.getIdTarjeta());
                    bundle.putString("fecha", mov.getFecha().toString());
                    bundle.putString("notas", mov.toString());
                    intent.putExtra("isPremium", isPremium);
                    intent.putExtra("isSinPublicidad", isSinPublicidad);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, EDIT_DELETE);
                }
            });

            return convertView;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            float total = 0;

            Categoria cat = parentItems.get(groupPosition);
            ArrayList<Movimiento> listaMov = cat.getListaMovimientos();

            for (int i = 0; i < listaMov.size(); i++) {
                Movimiento mov = listaMov.get(i);
                if (mov.getIdSubcategoria().equals(cat.getId())) {
                    total = total + Float.parseFloat(mov.getCantidad());
                }
            }

            DecimalFormat df = new DecimalFormat("0.00");

            String headerTitle = cat.toString();
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater
                        .inflate(R.layout.lista_categorias, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.txtCategoria);
            lblListHeader.setText(headerTitle);

            TextView lblListCant = (TextView) convertView
                    .findViewById(R.id.txtCant);

            String cant = df.format(total);
            cant = cant.replace(",", ".");

            lblListCant.setText(Util.formatear(Float.parseFloat(cant), prefs));

            ImageView categoriaIcon = (ImageView) convertView
                    .findViewById(R.id.iconCategoria);

            categoriaIcon.setBackgroundDrawable(_context.getResources()
                    .getDrawable(Util.obtenerIconoCategoria(cat.getIdIcon())));

            if (total < 0) {
                lblListCant.setTextColor(Color.RED);
            } else {
                lblListCant.setTextColor(_context.getResources().getColor(R.color.txtAzul));
            }

            return convertView;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return ((ArrayList<Movimiento>) childtems.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public int getGroupCount() {
            return parentItems.size();
        }

        @Override
        public void onGroupCollapsed(int groupPosition) {
            super.onGroupCollapsed(groupPosition);
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            super.onGroupExpanded(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
            txtTotal.setText(Util.formatear(total, prefs));
        }
        if (listAdapterCategorias != null) {
            listAdapterCategorias.notifyDataSetChanged();
            txtTotal.setText(Util.formatear(total, prefs));
        }
        if (listAdapterSubcategorias != null) {
            listAdapterSubcategorias.notifyDataSetChanged();
            txtTotal.setText(Util.formatear(total, prefs));
        }
    }
}
