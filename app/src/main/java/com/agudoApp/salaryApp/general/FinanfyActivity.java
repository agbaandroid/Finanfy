package com.agudoApp.salaryApp.general;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.CuentasActivity;
import com.agudoApp.salaryApp.activities.PreferencesActivity;
import com.agudoApp.salaryApp.activities.SeguridadComprobar;
import com.agudoApp.salaryApp.adapters.ListAdapterNavigator;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.fragments.CategoriasFragment;
import com.agudoApp.salaryApp.fragments.DatabaseFragment;
import com.agudoApp.salaryApp.fragments.EstadisticasFragment;
import com.agudoApp.salaryApp.fragments.GraficoFragment;
import com.agudoApp.salaryApp.fragments.NuevoResumenFragment;
import com.agudoApp.salaryApp.fragments.NuevoTarjetasFragment;
import com.agudoApp.salaryApp.fragments.RegistrosFijosFragment;
import com.agudoApp.salaryApp.fragments.SeguridadFragment;
import com.agudoApp.salaryApp.fragments.TiendaFragment;
import com.agudoApp.salaryApp.model.Cuenta;
import com.agudoApp.salaryApp.util.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FinanfyActivity extends AppCompatActivity {
    private String[] titlesMenu;
    private String[] titlesMenuMayusculas;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ListView navList, left_cuentas;
    private LinearLayout layoutCuentas, layoutGestionCuentas, layoutHeader, layoutHeader2;
    private LinearLayout left_drawer_cuentas;
    private DrawerLayout navDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawer;
    private ListAdapterNavigator mAdapter;
    private ListAdapterCuentasNavigator mAdapterCuentas;
    private final String BD_NOMBRE = "BDGestionGastos";
    private SQLiteDatabase db;
    final GestionBBDD gestion = new GestionBBDD();

    private long mLastPress = 0; // Cundo se puls atrs por ltima vez
    private long mTimeLimit = 3000; // Lmite de tiempo entre pulsaciones, en ms

    static final String SKU_COMPRA_PREMIUM = "version_premium";
    static final String SKU_CATEGORIAS_PREMIUM = "categorias_premium";
    static final String SKU_SIN_PUBLICIDAD = "sin_publicidad";

    // Publicidad
    private int mYear;
    private int mMonth;
    private int mDay;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private InterstitialAd interstitial;

    // Productos que posee el usuario
    boolean isPremium = false;
    boolean isCategoriaPremium = false;
    boolean isSinPublicidad = false;

    // Seguridad
    boolean appIniciada;
    boolean seguridadActivada;
    boolean listaCuentas;

    ArrayList<Cuenta> listCuentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        /*
        interstitialIteracion = new InterstitialAd(this);
        interstitialIteracion.setAdUnitId("ca-app-pub-2303483383476811/8788233284");

        interstitialRegistro = new InterstitialAd(this);
        interstitialRegistro.setAdUnitId("ca-app-pub-2303483383476811/2598438883");*/

        // Anuncio Inicial
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-2303483383476811/4287719686");

        AdRequest adRequestCompleto = new AdRequest.Builder().build();
        interstitial.loadAd(adRequestCompleto);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Seguridad
        prefs = this.getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
        appIniciada = prefs.getBoolean("appIniciada", false);
        seguridadActivada = prefs.getBoolean("seguridadActivada", false);
        editor = prefs.edit();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isPremium = extras.getBoolean("isPremium", false);
            isSinPublicidad = extras.getBoolean("isSinPublicidad", false);
            isCategoriaPremium = extras.getBoolean("isCategoriaPremium", false);
        }

        if (!appIniciada && seguridadActivada) {
            Intent in = new Intent(this, SeguridadComprobar.class);
            in.putExtra("funcionalidad", "comprobar");
            in.putExtra("isPremium", isPremium);
            in.putExtra("isSinPublicidad", isSinPublicidad);
            startActivity(in);
        } else {
            editor.putBoolean("appIniciada", true);
            editor.commit();
        }

        // comprobarProductos();

        View header = getLayoutInflater().inflate(R.layout.header, null);
        View headerCuentas = getLayoutInflater().inflate(R.layout.header_cuentas, null);
        View footer = getLayoutInflater().inflate(R.layout.footer, null);


        navDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navList = (ListView) findViewById(R.id.left_drawer);
        left_drawer_cuentas = (LinearLayout) findViewById(R.id.left_drawer_cuentas);
        left_cuentas = (ListView) findViewById(R.id.left_cuentas);
        layoutCuentas = (LinearLayout) findViewById(R.id.layoutCuentas);
        layoutGestionCuentas = (LinearLayout) findViewById(R.id.layoutGestionCuentas);
        layoutHeader = (LinearLayout) findViewById(R.id.layoutHeader);
        layoutHeader2 = (LinearLayout) findViewById(R.id.layoutHeader2);

        navList.addHeaderView(header);
        navList.addFooterView(footer);

        left_cuentas.addHeaderView(headerCuentas);

        mTitle = mDrawerTitle = getTitle();

        layoutCuentas.setVisibility(View.GONE);

        actionBarDrawer = new ActionBarDrawerToggle(this, navDrawerLayout,
                R.string.aceptar, R.string.cancelar) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        navDrawerLayout.setDrawerListener(actionBarDrawer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().show();

        titlesMenu = getResources().getStringArray(R.array.titles);
        titlesMenuMayusculas = getResources().getStringArray(R.array.titlesMayusculas);

        // Set previous array as adapter of the list
        mAdapter = new ListAdapterNavigator(this, titlesMenuMayusculas);
        navList.setAdapter(mAdapter);
        navList.setOnItemClickListener(new DrawerItemClickListener());
        left_cuentas.setOnItemClickListener(new DrawerItemClickListener());

        listCuentas = new ArrayList<Cuenta>();
        listCuentas = obtenerCuentas();

        mAdapterCuentas = new ListAdapterCuentasNavigator(this, listCuentas);
        left_cuentas.setAdapter(mAdapterCuentas);

        ImageView closfyIcon = (ImageView) findViewById(R.id.closfyIcon);
        ImageView twitter = (ImageView) findViewById(R.id.twitterIcon);

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                displayInterstitial();
                super.onAdLoaded();
            }
        });


        if (savedInstanceState == null) {
            // on first time display view for first nav item
            selectItem(1);
        }

        closfyIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = null;
                intent1 = new Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("https://play.google.com/store/apps/details?id=com.agba.closfy"));
                startActivity(intent1);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("https://twitter.com/Finanfy"));
                startActivity(browserIntent);
            }
        });

        layoutGestionCuentas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FinanfyActivity.this, CuentasActivity.class);
                intent.putExtra("isPremium", isPremium);
                intent.putExtra("isSinPublicidad", isSinPublicidad);
                startActivityForResult(intent, 1);

                navList.setVisibility(View.VISIBLE);
                layoutCuentas.setVisibility(View.GONE);

                listaCuentas = false;

                navDrawerLayout.closeDrawer(left_drawer_cuentas);
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawer.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawer.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawer.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Opciones del menu de navegacin
    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position - 1) {
            case -1:
                //fragment = new CuentasFragment();
                if (listaCuentas) {
                    listaCuentas = false;
                    navList.setVisibility(View.VISIBLE);
                    layoutCuentas.setVisibility(View.GONE);
                } else {
                    listaCuentas = true;
                    navList.setVisibility(View.GONE);
                    layoutCuentas.setVisibility(View.VISIBLE);
                }
                break;
            case 0:
                fragment = new NuevoResumenFragment(isPremium, isSinPublicidad,
                        isCategoriaPremium);
                break;
            case 1:
                fragment = new CategoriasFragment(isPremium, isSinPublicidad,
                        isCategoriaPremium);
                break;
            case 2:
                fragment = new GraficoFragment(isPremium, isSinPublicidad,
                        isCategoriaPremium);
                break;
            case 3:
                fragment = new NuevoTarjetasFragment(isPremium, isSinPublicidad,
                        isCategoriaPremium);
                break;
            case 4:
                fragment = new RegistrosFijosFragment(isPremium, isSinPublicidad,
                        isCategoriaPremium);
                break;
            case 5:
                fragment = new SeguridadFragment(isPremium, isSinPublicidad,
                        isCategoriaPremium);
                break;
            case 6:
                fragment = new DatabaseFragment(isPremium, isSinPublicidad,
                        isCategoriaPremium);
                break;
            case 7:
                fragment = new EstadisticasFragment(isPremium, isSinPublicidad,
                        isCategoriaPremium);
                break;
            case 8:
                Intent intent1 = null;
                intent1 = new Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("https://play.google.com/store/apps/details?id=com.agudoApp.salaryApp"));
                startActivity(intent1);
                navDrawerLayout.closeDrawer(left_drawer_cuentas);
                break;
            case 9:
                fragment = new TiendaFragment(isPremium, isSinPublicidad,
                        isCategoriaPremium);
                break;
            case 10:
                LayoutInflater li = LayoutInflater.from(this);
		        View view = null;
		        AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        AlertDialog alert;
                view = li.inflate(R.layout.acerca, null);
                builder.setView(view);
                builder.setTitle(getResources().getString(R.string.app_name));
                builder.setIcon(R.drawable.icon_app);
                builder.setCancelable(false);
                builder.setPositiveButton(getResources()
                                .getString(R.string.aceptar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alert = builder.create();
                alert.show();
                navDrawerLayout.closeDrawer(left_drawer_cuentas);
                break;
            default:
                break;
            case 11:
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                navDrawerLayout.closeDrawer(left_drawer_cuentas);
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();

            navList.setItemChecked(position, true);
            TextView textoHeader = (TextView) findViewById(R.id.nombreCuenta);


            if (position != 0) {
                setTitle(titlesMenu[position - 1]);
                mAdapter.setSelectedItem(position - 1);
                //textoHeader.setTextColor(Color.GRAY);
            }

            navDrawerLayout.closeDrawer(left_drawer_cuentas);
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    // Estamos siendo destruidos. Es importante eliminar el helper aqu!
    @Override
    public void onDestroy() {
        super.onDestroy();

        prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.putBoolean("appIniciada", false);
        editor.commit();
    }

    @Override
    protected void onResume() {
        prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
        TextView textoHeader = (TextView) findViewById(R.id.nombreCuenta);
        ImageView idIcon = (ImageView) findViewById(R.id.iconCuenta);

        TextView textoHeader2 = (TextView) findViewById(R.id.nombreCuenta2);
        ImageView idIcon2 = (ImageView) findViewById(R.id.iconCuenta2);

        int cuen = cuentaSeleccionada();
        db = openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            Cuenta cuenta = gestion.getCuentaSeleccionada(db, cuen);
            textoHeader.setText(cuenta.getDescCuenta());
            idIcon.setBackgroundResource(Util.obtenerIconoUser(cuenta.getIdIcon()));
            textoHeader2.setText(cuenta.getDescCuenta());
            idIcon2.setBackgroundResource(Util.obtenerIconoUser(cuenta.getIdIcon()));
        }
        db.close();
        super.onResume();
    }

    void actualizaPremium() {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(getResources().getString(R.string.gracias));
        bld.setNeutralButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(FinanfyActivity.this,
                                FinanfyActivity.class);
                        startActivity(intent);
                    }
                });
        bld.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        prefs = this.getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
        boolean isCompra = prefs.getBoolean("isCompra", false);
        if (isCompra && resultCode == -1) {
            editor = prefs.edit();
            editor.putBoolean("isCompra", false);
            editor.commit();
            alert(getResources().getString(R.string.gracias));
        } else if (isCompra && resultCode == 0) {
            editor = prefs.edit();
            editor.putBoolean("isCompra", false);
            editor.commit();
        }

        listCuentas = obtenerCuentas();

        mAdapterCuentas = new ListAdapterCuentasNavigator(this, listCuentas);
        left_cuentas.setAdapter(mAdapterCuentas);

        prefs = this.getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton(getResources().getString(R.string.ok), null);
        bld.create().show();
    }

    public int cuentaSeleccionada() {
        prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);

        int idCuenta = prefs.getInt("cuenta", 0);
        return idCuenta;
    }

    public void comprobarProductos() {
        prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
        if (!isPremium) {
            isPremium = prefs.getBoolean("isPremium", false);
        }
        if (!isCategoriaPremium) {
            isCategoriaPremium = prefs.getBoolean("isCategoriaPremium", false);
        }
        if (!isSinPublicidad) {
            isSinPublicidad = prefs.getBoolean("isSinPublicidad", false);
        }
    }

    // Comprobamos si debemos mostrar la publicidad o no
    public void displayInterstitial() {
        if (interstitial != null && interstitial.isLoaded() && mostrarAnuncioCompleto()) {
            interstitial.show();
        }
    }

    @Override
    public void onBackPressed() {
        Toast onBackPressedToast = Toast.makeText(this, R.string.pulseDosVeces,
                Toast.LENGTH_SHORT);
        long currentTime = System.currentTimeMillis();

        if (currentTime - mLastPress > mTimeLimit) {
            onBackPressedToast.show();
            mLastPress = currentTime;
        } else {
            onBackPressedToast.cancel();
            super.onBackPressed();
        }
    }

    public ArrayList<Cuenta> obtenerCuentas() {
        ArrayList<Cuenta> listCuentas = new ArrayList<Cuenta>();
        db = openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            listCuentas = (ArrayList) gestion.getCuentas(db);
        }
        return listCuentas;
    }

    public class ListAdapterCuentasNavigator extends BaseAdapter {
        private LayoutInflater mInflater;
        private int mSelectedItem;
        private ArrayList<Cuenta> lCuentas;
        Locale locale = Locale.getDefault();

        public ListAdapterCuentasNavigator(Context context, ArrayList<Cuenta> lista) {
            lCuentas = lista;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return lCuentas.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return lCuentas.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView text;
            ImageView icon;
            LinearLayout layoutNavigator;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.lista_navigator_cuentas, null);
            }

            text = (TextView) convertView.findViewById(R.id.textNavigatorCuentas);
            icon = (ImageView) convertView.findViewById(R.id.iconNavigatorCuentas);
            layoutNavigator = (LinearLayout) convertView.findViewById(R.id.layoutNavigatorCuentas);
            text.setText(lCuentas.get(position).getDescCuenta());

            icon.setBackgroundResource(Util.obtenerIconoUser(lCuentas.get(position).getIdIcon()));

            layoutNavigator.setTag(position);

            layoutNavigator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posiSel = (int) v.getTag();
                    Cuenta cuenta = listCuentas.get(posiSel);
                    seleccionarCuenta(cuenta.getIdCuenta());

                    Fragment fragment = new NuevoResumenFragment(isPremium, isSinPublicidad,
                            isCategoriaPremium);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment).commit();

                    navList.setItemChecked(1, true);
                    setTitle(titlesMenu[0]);
                    mAdapter.setSelectedItem(0);

                    navList.setVisibility(View.VISIBLE);
                    layoutCuentas.setVisibility(View.GONE);

                    listaCuentas = false;

                    navDrawerLayout.closeDrawer(left_drawer_cuentas);
                }
            });

            return convertView;
        }

        public int getSelectedItem() {
            return mSelectedItem;
        }

        public void setSelectedItem(int selectedItem) {
            mSelectedItem = selectedItem;
        }

    }

    public void seleccionarCuenta(String idCuenta) {
        prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.putInt("cuenta", Integer.parseInt(idCuenta));
        editor.commit();

        TextView textoHeader = (TextView) findViewById(R.id.nombreCuenta);
        ImageView idIcon = (ImageView) findViewById(R.id.iconCuenta);

        TextView textoHeader2 = (TextView) findViewById(R.id.nombreCuenta2);
        ImageView idIcon2 = (ImageView) findViewById(R.id.iconCuenta2);

        int cuen = cuentaSeleccionada();
        db = openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            Cuenta cuenta = gestion.getCuentaSeleccionada(db, cuen);
            textoHeader.setText(cuenta.getDescCuenta());
            idIcon.setBackgroundResource(Util.obtenerIconoUser(cuenta.getIdIcon()));
            textoHeader2.setText(cuenta.getDescCuenta());
            idIcon2.setBackgroundResource(Util.obtenerIconoUser(cuenta.getIdIcon()));
        }
        db.close();
    }

    /*public void mostrarPublicidad(boolean isInteraccion, boolean isRegistro, boolean isMia) {
        boolean mostrarAnuncio = false;

        if (!isPremium && !isSinPublicidad) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
            String fechaAct = mDay + "/" + (mMonth + 1) + "/" + mYear;

            String fechaAnuncio = prefs.getString("fechaAnuncio", "");
            int contadorPubli = prefs.getInt("contadorPubli", 0);
            int nRegistros = prefs.getInt("nRegistros", 0);
            boolean isPubliMostrada = prefs.getBoolean("isPubliMostrada", false);

            if (fechaAnuncio.equals(fechaAct)) {
                if (isInteraccion) {
                    contadorPubli = contadorPubli + 1;
                    if (contadorPubli % 10 == 0 && !isPubliMostrada) {
                        mostrarAnuncio = true;
                        isPubliMostrada = true;
                    }
                }
                if (isRegistro) {
                    nRegistros = nRegistros + 1;
                    if (nRegistros == 2) {
                        mostrarAnuncio = true;
                    }
                }

                editor = prefs.edit();
                editor.putInt("contadorPubli", contadorPubli);
                editor.putInt("nRegistros", nRegistros);
                editor.putBoolean("isPubliMostrada", isPubliMostrada);
                editor.commit();
            } else {
                editor = prefs.edit();
                editor.putString("fechaAnuncio", fechaAct);
                editor.putInt("contadorPubli", 0);
                editor.putInt("nRegistros", 0);
                editor.putBoolean("isPubliMostrada", false);
                editor.commit();
                mostrarAnuncio = false;
            }

            if (mostrarAnuncio) {
                if(isInteraccion) {
                    // Anuncio Inicial
                    AdRequest adRequestCompleto = new AdRequest.Builder().build();
                    interstitialIteracion.loadAd(adRequestCompleto);
                }else if(isRegistro){
                    // Anuncio Inicial
                    AdRequest adRequestCompleto = new AdRequest.Builder().build();
                    interstitialRegistro.loadAd(adRequestCompleto);
                }
            }
        }
    }*/

    // Comprobamos si debemos mostrar el anuncio completo o no
    public boolean mostrarAnuncioCompleto() {
        boolean mostrarAnuncio = true;

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
        String fechaAct = mDay + "/" + (mMonth + 1) + "/" + mYear;
        String fechaAnuncio = prefs.getString("fechaAnuncio", "");

        if (fechaAnuncio.equals(fechaAct)) {
            mostrarAnuncio = false;
        } else {
            editor = prefs.edit();
            editor.putString("fechaAnuncio", fechaAct);
            editor.putInt("contadorPubli", 0);
            editor.putInt("ultimoNumPubli", 0);
            editor.commit();
        }

        return mostrarAnuncio;
    }
}