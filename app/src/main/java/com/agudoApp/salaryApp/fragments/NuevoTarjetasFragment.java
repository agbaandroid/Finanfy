package com.agudoApp.salaryApp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.NuevoAddTarjetaActivity;
import com.agudoApp.salaryApp.activities.NuevoEditTarjetaActivity;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Tarjeta;
import com.agudoApp.salaryApp.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Locale;

public class NuevoTarjetasFragment extends Fragment {

    private static final String KEY_CONTENT = "TarjetasFragment:Content";
    private final String BD_NOMBRE = "BDGestionGastos";
    final GestionBBDD gestion = new GestionBBDD();
    private SQLiteDatabase db;

    private final int TARJETAS = 1;

    ListView listaTarjetas;
    private RelativeLayout layoutPubli;
    NuevoListAdapterTarjetas adapterTarjetas;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    // Productos que posee el usuario
    boolean isPremium = false;
    boolean isCategoriaPremium = false;
    boolean isSinPublicidad = false;

    public NuevoTarjetasFragment() {
    }

    public NuevoTarjetasFragment(boolean isUserPremium, boolean isUserSinpublicidad,
                                 boolean isUserCategoriaPremium) {
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

        return inflater.inflate(R.layout.nuevo_tarjetas, container, false);
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

        prefs = getActivity().getSharedPreferences("ficheroConf",
                Context.MODE_PRIVATE);

        listaTarjetas = (ListView) getView().findViewById(R.id.listaTarjetas);

        layoutPubli = (RelativeLayout) getView().findViewById(R.id.layoutPubli);

        //Se carga la publicidad
        AdView adView = (AdView) getView().findViewById(R.id.adView);
        if (isPremium || isSinPublicidad) {
            layoutPubli.setVisibility(View.GONE);
        } else {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        obtenerTarjetas();
    }

    public void obtenerTarjetas() {
        ArrayList<Tarjeta> listTarjetas = new ArrayList<Tarjeta>();
        db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
        if (db != null) {
            // Recuperamos el listado del spinner Categorias
            listTarjetas = (ArrayList<Tarjeta>) gestion.getTarjetas(db);
        }
        db.close();

        adapterTarjetas = new NuevoListAdapterTarjetas(getActivity(), listTarjetas);
        listaTarjetas.setAdapter(adapterTarjetas);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_setting_mas, menu);
    }

    // Aadiendo funcionalidad a las opciones de men
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(getActivity(), NuevoAddTarjetaActivity.class);
                intent.putExtra("isPremium", isPremium);
                intent.putExtra("isSinPublicidad", isSinPublicidad);
                startActivityForResult(intent, TARJETAS);
                return true;
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class NuevoListAdapterTarjetas extends BaseAdapter {
        private LayoutInflater mInflater;
        private ArrayList<Tarjeta> listaTar = new ArrayList<Tarjeta>();
        Locale locale = Locale.getDefault();
        String languaje = locale.getLanguage();
        Context context;

        public NuevoListAdapterTarjetas(Context context, ArrayList<Tarjeta> lista) {
            listaTar = lista;
            mInflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listaTar.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return listaTar.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView txtNombre;
            TextView txtTipo;
            TextView txtMaximo;
            ImageView iconTarjeta;
            LinearLayout layoutTarjetas;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.lista_tarjetas, null);
            }

            txtNombre = (TextView) convertView.findViewById(R.id.txtNombre);
            txtTipo = (TextView) convertView.findViewById(R.id.txtTipo);
            txtMaximo = (TextView) convertView.findViewById(R.id.txtMaximo);
            iconTarjeta = (ImageView) convertView.findViewById(R.id.iconTarjeta);
            layoutTarjetas = (LinearLayout) convertView.findViewById(R.id.layoutTarjetas);

            txtNombre.setText(listaTar.get(position).toString());

            Tarjeta tar = listaTar.get(position);

            if (tar.getTipo() == 0) {
                txtTipo.setText(getResources().getString(R.string.credito));
            } else {
                txtTipo.setText(getResources().getString(R.string.debito));
            }

            txtMaximo.setText(Util.formatear(tar.getCantMax(), prefs));
            iconTarjeta.setBackgroundResource(Util.obtenerIconoTarjeta(tar.getIdIcon()));

            layoutTarjetas.setTag(position);

            layoutTarjetas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int posiSel = (int) v.getTag();
                    Tarjeta tar = listaTar.get(posiSel);
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(context, NuevoEditTarjetaActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("idTarjeta", tar.getId());
                    intent.putExtras(bundle);
                    intent.putExtra("isPremium", isPremium);
                    intent.putExtra("isSinPublicidad", isSinPublicidad);
                    startActivityForResult(intent, TARJETAS);
                }
            });

            return convertView;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapterTarjetas != null) {
            adapterTarjetas.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        obtenerTarjetas();
    }
}
