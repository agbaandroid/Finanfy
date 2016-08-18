package com.agudoApp.salaryApp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.NuevoActivity;
import com.agudoApp.salaryApp.activities.NuevoEditMovimientosActivity;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.informes.Informes;
import com.agudoApp.salaryApp.model.Backup;
import com.agudoApp.salaryApp.model.Movimiento;
import com.agudoApp.salaryApp.model.Tarjeta;
import com.agudoApp.salaryApp.util.Util;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.session.AppKeyPair;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DropBoxFragment extends Fragment {
    private static final String KEY_CONTENT = "DropBoxFragment:Content";
    private String mContent = "???";

    private final String BD_NOMBRE = "BDGestionGastos";
    private SQLiteDatabase db;
    final GestionBBDD gestion = new GestionBBDD();

    final static private String APP_KEY = "yty5ua0nmg6nv9a";
    final static private String APP_SECRET = "sn345hty9ytw7v3";
    private DropboxAPI<AndroidAuthSession> mDBApi;

    final static int MENSAJE_CREAR_COPIA = 0;
    final static int MENSAJE_RESTAURAR_COPIA = 1;
    final static int MENSAJE_ACTUALIZAR_COPIA = 2;
    final static int MENSAJE_CERRAR_SESION = 3;
    final static int CREAR_COPIA = 4;
    final static int ERROR_COPIA_EXISTENTE = 5;
    final static int MENSAJE_ELIMINAR_COPIA = 6;

    AlertDialog dialogMenu;

    boolean showError = false;

    String txtName;

    SharedPreferences prefs;
    ProgressDialog progDailog;

    TextView cerrarSesion;

    LinearLayout layoutBackup;
    LinearLayout layoutNoBackup;

    protected ListView listViewBackups;

    ArrayList<Backup> listBackups;

    boolean hayBackup;
    boolean isSinPublicidad;
    boolean isPremium;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            isSinPublicidad = bundle.getBoolean("isSinPublicidad");
            isPremium = bundle.getBoolean("isPremium");
        }

        if ((savedInstanceState != null)
                && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }

        final String[] items = new String[]{
                getResources().getString(R.string.restaurar),
                getResources().getString(R.string.actualizar),
                getResources().getString(R.string.eliminar)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.selectOption));

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { // pick from
                // camera
                if (item == 0) {
                    onCreateDialog(MENSAJE_RESTAURAR_COPIA);
                } else if (item == 1) {
                    onCreateDialog(MENSAJE_ACTUALIZAR_COPIA);
                } else { // pick from file
                    onCreateDialog(MENSAJE_ELIMINAR_COPIA);
                }
            }
        });

        dialogMenu = builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dropbox, container, false);
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

        //cerrarSesion = (TextView) getView().findViewById(R.id.botonCerrarSesion);
        layoutBackup = (LinearLayout) getView().findViewById(R.id.layoutBackup);
        layoutNoBackup = (LinearLayout) getView().findViewById(R.id.layoutNoBackup);
        listViewBackups = (ListView) getView().findViewById(R.id.listBackups);


        RelativeLayout layoutPubli = (RelativeLayout) getView().findViewById(R.id.layoutPubli);
        //Se carga la publicidad
        AdView adView = (AdView) getActivity().findViewById(R.id.adView);
        if (isPremium || isSinPublicidad) {
            layoutPubli.setVisibility(View.GONE);
        } else {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);

        prefs = getActivity().getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
        String token = prefs.getString("dropboxToken", null);

        if (token != null) {
            mDBApi.getSession().setOAuth2AccessToken(token);

            if (mDBApi.getSession().isLinked()) {
                new CargarInfoTask().execute();
            }
        }

        /*cerrarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCreateDialog(MENSAJE_CERRAR_SESION);
            }
        });*/

    }

    public void cerrarSesion() {
        mDBApi.getSession().unlink();
    }

    public void crearDirectorios() {
        try {
            DropboxAPI.Entry response;

            //Creamos los directorios
            response = mDBApi.createFolder(txtName);
        } catch (Exception e) {
            showError = true;
            Log.i("Error", e.getMessage());
        }
    }

    public void subirBD() {
        try {
            DropboxAPI.Entry response;

            //Subimos la base de datos
            File dbFile = new File(Environment.getDataDirectory()
                    + "/data/com.agudoApp.salaryApp/databases/BDGestionGastos");

            FileInputStream inputStream = new FileInputStream(dbFile);
            response = mDBApi.putFileOverwrite("/" + txtName + "/" + dbFile.getName(), inputStream,
                    dbFile.length(), null);

        } catch (Exception e) {
            showError = true;
            Log.i("Error", e.getMessage());
        }
    }

    public void crearCopia() {
        try {
            crearDirectorios();
            subirBD();
        } catch (Exception e) {
            showError = true;
            Log.i("Error", e.getMessage());
        }
    }

    public void actualizarCopia() {
        try {
            subirBD();
        } catch (Exception e) {
            showError = true;
        }
    }

    public void restaurarCopia() {
        try {
            DropboxAPI.Entry response;

            //Se borran todas las imagenes del dispositivo
            File dirFinanfy = new File(Environment.getExternalStorageDirectory() + "/Finanfy/");

            if (!dirFinanfy.exists()) {
                dirFinanfy.mkdir();
            }


            DropboxAPI.Entry entries = mDBApi.metadata("/" + txtName + "/", 0, null, true, null);

            if (entries.contents.size() != 0) {
                File dbFile = new File(Environment.getExternalStorageDirectory()
                        + "/Finanfy/BDGestionGastosTmp");

                OutputStream out = new BufferedOutputStream(new FileOutputStream(dbFile));
                DropboxAPI.DropboxFileInfo info = mDBApi.getFile("/" + txtName + "/BDGestionGastos", null, out, null);

                File dataBaseDir = new File(Environment.getDataDirectory()
                        + "/data/com.agudoApp.salaryApp/databases/");

                File file = new File(dataBaseDir, "BDGestionGastos");

                file.createNewFile();
                copyFile(dbFile, file);

                dbFile.delete();
            }
        } catch (Exception e) {
            showError = true;
            Log.i("Error", e.getMessage());
        }
    }

    public void eliminarCopia() {
        try {
            DropboxAPI.Entry response;

            DropboxAPI.Entry entries = mDBApi.metadata("/" + txtName + "/", 0, null, true, null);

            if (entries.contents.size() != 0) {
                mDBApi.delete("/" + txtName + "/");
            }
        } catch (Exception e) {
            showError = true;
            Log.i("Error", e.getMessage());
        }
    }

    public void cargarInfo() {
        String path = "";
        listBackups = new ArrayList<>();

        try {
            DropboxAPI.Entry entries = mDBApi.metadata(path, 0, null, true, null);

            if (entries.contents.size() != 0) {
                hayBackup = true;

                for (DropboxAPI.Entry e : entries.contents) {
                    if (!e.isDeleted) {
                        Backup backup = new Backup();
                        backup.setName(e.fileName());

                        DropboxAPI.Entry entries2 = mDBApi.metadata("/" + e.fileName() +"/", 0, null, true, null);
                        for (DropboxAPI.Entry e2 : entries2.contents) {
                            if (!e.isDeleted) {
                                try {
                                    SimpleDateFormat dfDb = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                                    Date dateDb = dfDb.parse(e2.modified);

                                    SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
                                    backup.setFecha(fecha.format(dateDb));

                                } catch (Exception ex) {
                                    Log.d("Error", "Error al formatear fecha");
                                }
                            }
                        }

                        listBackups.add(backup);
                    }
                }
            }

        } catch (DropboxServerException dse) {
            if (dse.error != DropboxServerException._404_NOT_FOUND) {
                showError = true;
                Log.i("Error", dse.getMessage());
            }
        } catch (DropboxException e) {
            showError = true;
            Log.i("Error", e.getMessage());
        }
    }

    public class CargarInfoTask extends AsyncTask<Integer, Void, Void> {

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
            cargarInfo();
            return null;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Void result) {
            if (hayBackup) {
                ListAdapterBackups listAdapter = new ListAdapterBackups(getActivity(), listBackups);
                listViewBackups.setAdapter(listAdapter);

                layoutBackup.setVisibility(View.VISIBLE);
                layoutNoBackup.setVisibility(View.GONE);
            } else {
                layoutBackup.setVisibility(View.GONE);
                layoutNoBackup.setVisibility(View.VISIBLE);
            }

            progDailog.dismiss();
        }
    }

    public class CrearCopiaSeguridadTask extends AsyncTask<Integer, Void, Void> {

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
            crearCopia();
            return null;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Void result) {
            progDailog.dismiss();
            if(showError){
                showToast(getResources().getString(R.string.errorDropbox));
                showError = false;
            }else{
                showToast(getResources().getString(R.string.msgCrearCopiaOK));
                new CargarInfoTask().execute();
            }
        }
    }

    public class ActualizarCopiaSeguridadTask extends AsyncTask<Integer, Void, Void> {

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
            actualizarCopia();
            return null;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Void result) {
            progDailog.dismiss();
            if(showError){
                showToast(getResources().getString(R.string.errorDropbox));
                showError = false;
            }else{
                showToast(getResources().getString(R.string.msgActualizarCopiaOK));
                new CargarInfoTask().execute();
            }
        }
    }

    public class RestaurarCopiaSeguridadTask extends AsyncTask<Integer, Void, Void> {

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
            restaurarCopia();
            return null;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Void result) {
            progDailog.dismiss();
            if(showError){
                showToast(getResources().getString(R.string.errorDropbox));
                showError = false;
            }else{
                showToast(getResources().getString(R.string.msgRestaurarCopiaOK));
                new CargarInfoTask().execute();
            }
        }
    }

    public class EliminarCopiaSeguridadTask extends AsyncTask<Integer, Void, Void> {

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
            eliminarCopia();
            return null;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Void result) {
            progDailog.dismiss();
            if(showError){
                showToast(getResources().getString(R.string.errorDropbox));
                showError = false;
            }else{
                showToast(getResources().getString(R.string.msgEliminarCopiaOK));
                new CargarInfoTask().execute();
            }
        }
    }

    public class CerrarSesionTask extends AsyncTask<Integer, Void, Void> {

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
            cerrarSesion();
            return null;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Void result) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("dropboxToken", null);
            editor.commit();
            Bundle bundle = new Bundle();

            bundle.putBoolean("isSinPublicidad", isSinPublicidad);

            Fragment fragment = new DropBoxInicioFragment();
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();

            progDailog.dismiss();
        }
    }

    void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
        toast.show();
    }

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog.Builder builderCrear = new AlertDialog.Builder(getActivity());
        AlertDialog alert;
        switch (id) {
            case CREAR_COPIA:
                final LayoutInflater li = LayoutInflater.from(getActivity());
                View view = li.inflate(R.layout.dropbox_crear_backup, null);
                final EditText name = (EditText) view.findViewById(R.id.nombre);

                builderCrear.setView(view);
                builderCrear.setTitle(getResources().getString(R.string.creaCopiaSeguridad));
                builderCrear.setIcon(R.drawable.dropbox);
                builderCrear.setCancelable(false);
                builderCrear.setPositiveButton(
                        getResources().getString(R.string.aceptar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                txtName = name.getText().toString();
                                if (!txtName.equals("")) {
                                    boolean error = false;
                                    for (Backup backup: listBackups) {
                                        if (backup.getName().equalsIgnoreCase(txtName)){
                                            error = true;
                                            break;
                                        }
                                    }
                                    if(error){
                                        onCreateDialog(ERROR_COPIA_EXISTENTE);
                                    }else{
                                        onCreateDialog(MENSAJE_CREAR_COPIA);
                                    }
                                    dialog.cancel();
                                }
                            }
                        }).setNegativeButton(
                        getResources().getString(R.string.cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alert = builderCrear.create();
                alert.show();
                break;
            case MENSAJE_CREAR_COPIA:
                builder.setMessage(
                        getResources().getString(R.string.msgCrearCopia))
                        .setTitle(getResources().getString(R.string.informacion))
                        .setIcon(R.drawable.ic_info_azul)
                        .setNegativeButton(getResources().getString(R.string.cancelar),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton(getResources().getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        new CrearCopiaSeguridadTask().execute();
                                        dialog.cancel();
                                    }
                                });
                alert = builder.create();
                alert.show();
                break;
            case ERROR_COPIA_EXISTENTE:
                builder.setMessage("Ya exite una copia de seguridad con este nombre. Por favor elige otro nombre.")
                        .setTitle(getResources().getString(R.string.info))
                        .setIcon(R.drawable.ic_alert)
                        .setPositiveButton(getResources().getString(R.string.aceptar),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        onCreateDialog(CREAR_COPIA);
                                        dialog.cancel();
                                    }
                                });
                alert = builder.create();
                alert.show();
                break;
            case MENSAJE_RESTAURAR_COPIA:
                builder.setMessage(
                        getResources().getString(R.string.msgRestaurarCopia))
                        .setTitle(getResources().getString(R.string.informacion))
                        .setIcon(R.drawable.ic_info_azul)
                        .setNegativeButton(getResources().getString(R.string.cancelar),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton(getResources().getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        new RestaurarCopiaSeguridadTask().execute();
                                        dialog.cancel();
                                    }
                                });
                alert = builder.create();
                alert.show();
                break;
            case MENSAJE_ACTUALIZAR_COPIA:
                builder.setMessage(
                        getResources().getString(R.string.msgActualizarCopia))
                        .setTitle(getResources().getString(R.string.informacion))
                        .setIcon(R.drawable.ic_info_azul)
                        .setNegativeButton(getResources().getString(R.string.cancelar),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton(getResources().getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        new ActualizarCopiaSeguridadTask().execute();
                                        dialog.cancel();
                                    }
                                });
                alert = builder.create();
                alert.show();
                break;
            case MENSAJE_ELIMINAR_COPIA:
                builder.setMessage(
                        getResources().getString(R.string.msgEliminarCopia))
                        .setTitle(getResources().getString(R.string.informacion))
                        .setIcon(R.drawable.ic_info_azul)
                        .setNegativeButton(getResources().getString(R.string.cancelar),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton(getResources().getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        new EliminarCopiaSeguridadTask().execute();
                                        dialog.cancel();
                                    }
                                });
                alert = builder.create();
                alert.show();
                break;
            case MENSAJE_CERRAR_SESION:
                builder.setMessage(
                        getResources().getString(R.string.msgCerrarSesion))
                        .setTitle(getResources().getString(R.string.informacion))
                        .setIcon(R.drawable.ic_info_azul)
                        .setNegativeButton(getResources().getString(R.string.cancelar),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton(getResources().getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        new CerrarSesionTask().execute();
                                        dialog.cancel();
                                    }
                                });
                alert = builder.create();
                alert.show();
                break;
        }
        return null;
    }

    public class ListAdapterBackups extends BaseAdapter {
        private LayoutInflater mInflater;
        private ArrayList<Backup> listaCopias = new ArrayList<Backup>();
        Locale locale = Locale.getDefault();
        String languaje = locale.getLanguage();
        Context context;
        Backup backup;

        public ListAdapterBackups(Context context, ArrayList<Backup> lista) {
            listaCopias = lista;
            mInflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listaCopias.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return listaCopias.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView txtNombreCopia;
            TextView txtFecha;
            LinearLayout layoutCopias;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.lista_copias, null);
            }

            txtFecha = (TextView) convertView.findViewById(R.id.txtFecha);
            txtNombreCopia = (TextView) convertView.findViewById(R.id.txtNombreCopia);
            layoutCopias = (LinearLayout) convertView.findViewById(R.id.layoutCopias);

            txtNombreCopia.setText(listaCopias.get(position).getName());
            txtFecha.setText(listaCopias.get(position).getFecha());

            layoutCopias.setTag(position);

            layoutCopias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posiSel = (int) v.getTag();
                    backup = listaCopias.get(posiSel);

                    txtName = backup.getName();
                    dialogMenu.show();
                }
            });

            return convertView;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_setting_dropbox, menu);
    }

    // Aadiendo funcionalidad a las opciones de men
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                onCreateDialog(CREAR_COPIA);
                return true;
            case R.id.action_logout:
                onCreateDialog(MENSAJE_CERRAR_SESION);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
