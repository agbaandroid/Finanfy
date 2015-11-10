package com.agudoApp.salaryApp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class DatabaseFragment extends Fragment {
    private static final String KEY_CONTENT = "DatabaseFragment:Content";
    private final String BD_NOMBRE = "BDGestionGastos";
    private SQLiteDatabase db;
    final GestionBBDD gestion = new GestionBBDD();

    static final int DATABASE_EXPORT = 1;
    static final int DATABASE_IMPORT = 2;
    static final int DATABASE_RESET = 3;
    static final int DATABASE_IMPORT_PRO = 6;

    private LinearLayout layoutExportar;
    private LinearLayout layoutImportar;
    private LinearLayout layoutReset;
    private RelativeLayout layoutPubli;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    // Productos que posee el usuario
    boolean isPremium = false;
    boolean isCategoriaPremium = false;
    boolean isSinPublicidad = false;

    public DatabaseFragment(boolean isUserPremium, boolean isUserSinpublicidad,
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

        return inflater.inflate(R.layout.menu_database, container, false);
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

        // gestion.importarBBDDLite(db);

        layoutExportar = (LinearLayout) getView().findViewById(R.id.layoutExport);
        layoutImportar = (LinearLayout) getView().findViewById(R.id.layoutImport);
        layoutReset = (LinearLayout) getView().findViewById(R.id.layoutReset);

        layoutPubli = (RelativeLayout) getView().findViewById(R.id.layoutPubli);

        //Se carga la publicidad
        AdView adView = (AdView) getView().findViewById(R.id.adView);
        if (isPremium || isSinPublicidad) {
            layoutPubli.setVisibility(View.GONE);
        } else {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        layoutExportar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCreateDialog(DATABASE_EXPORT);
            }
        });

        layoutImportar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCreateDialog(DATABASE_IMPORT);
            }
        });

        layoutReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCreateDialog(DATABASE_RESET);
            }
        });

    }

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        AlertDialog alert;
        LayoutInflater li = LayoutInflater.from(getActivity());
        View view = null;
        switch (id) {
            case DATABASE_EXPORT:
                view = li.inflate(R.layout.database_export, null);
                builder.setView(view);
                builder.setTitle(getResources().getString(R.string.exportar));
                builder.setIcon(R.drawable.exportar);
                builder.setCancelable(false);
                builder.setPositiveButton(
                        getResources().getString(R.string.aceptar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean ok = false;
                                db = getActivity().openOrCreateDatabase(BD_NOMBRE,
                                        1, null);
                                if (db != null) {
                                    ok = gestion.exportarBBDD();
                                }
                                db.close();
                                if (ok) {
                                    Context context = getActivity()
                                            .getApplicationContext();
                                    CharSequence text = getResources().getString(
                                            R.string.exportOK);
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text,
                                            duration);
                                    toast.show();
                                } else {
                                    Context context = getActivity()
                                            .getApplicationContext();
                                    CharSequence text = getResources().getString(
                                            R.string.exportError);
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text,
                                            duration);
                                    toast.show();
                                }
                                dialog.cancel();
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
            case DATABASE_IMPORT_PRO:
                view = li.inflate(R.layout.database_import_pro, null);
                builder.setView(view);
                builder.setTitle(getResources().getString(R.string.botonImportPro));
                builder.setIcon(R.drawable.importar);
                builder.setCancelable(false);
                builder.setPositiveButton(
                        getResources().getString(R.string.aceptar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean ok = false;
                                db = getActivity().openOrCreateDatabase(BD_NOMBRE,
                                        1, null);
                                if (db != null) {
                                    ok = gestion.importarBBDDPro(db);
                                }
                                db.close();
                                if (ok) {
                                    cuentaDefecto();
                                    Context context = getActivity()
                                            .getApplicationContext();
                                    CharSequence text = getResources().getString(
                                            R.string.importarOK);
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text,
                                            duration);
                                    toast.show();
                                } else {
                                    Context context = getActivity()
                                            .getApplicationContext();
                                    CharSequence text = getResources().getString(
                                            R.string.importarError);
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text,
                                            duration);
                                    toast.show();
                                }
                                dialog.cancel();
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
            case DATABASE_IMPORT:
                view = li.inflate(R.layout.database_import, null);
                builder.setView(view);
                builder.setTitle(getResources().getString(R.string.importar));
                builder.setIcon(R.drawable.importar);
                builder.setCancelable(false);
                builder.setPositiveButton(
                        getResources().getString(R.string.aceptar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean ok = false;
                                db = getActivity().openOrCreateDatabase(BD_NOMBRE,
                                        1, null);
                                if (db != null) {
                                    ok = gestion.importarBBDD(db);
                                }
                                db.close();
                                if (ok) {
                                    cuentaDefecto();
                                    Context context = getActivity()
                                            .getApplicationContext();
                                    CharSequence text = getResources().getString(
                                            R.string.importarOK);
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text,
                                            duration);
                                    toast.show();
                                } else {
                                    Context context = getActivity()
                                            .getApplicationContext();
                                    CharSequence text = getResources().getString(
                                            R.string.importarError);
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text,
                                            duration);
                                    toast.show();
                                }
                                dialog.cancel();
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
            case DATABASE_RESET:
                view = li.inflate(R.layout.database, null);
                builder.setView(view);
                builder.setTitle(getResources().getString(R.string.reset));
                builder.setIcon(R.drawable.restaurar);
                builder.setCancelable(false);
                builder.setPositiveButton(
                        getResources().getString(R.string.aceptar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean ok = false;
                                db = getActivity().openOrCreateDatabase(BD_NOMBRE,
                                        1, null);
                                if (db != null) {
                                    ok = gestion.eliminarTablas(db);
                                }
                                db.close();
                                if (ok) {
                                    db = getActivity().openOrCreateDatabase(
                                            BD_NOMBRE, 1, null);
                                    if (db != null) {
                                        gestion.createTables(db);
                                    }
                                    db.close();
                                    cuentaDefecto();
                                    Context context = getActivity()
                                            .getApplicationContext();
                                    CharSequence text = getResources().getString(
                                            R.string.msnDBOk);
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text,
                                            duration);
                                    toast.show();
                                } else {
                                    Context context = getActivity()
                                            .getApplicationContext();
                                    CharSequence text = getResources().getString(
                                            R.string.msnDBError);
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text,
                                            duration);
                                    toast.show();
                                }
                                dialog.cancel();
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

    public void cuentaDefecto() {
        SharedPreferences prefs;
        SharedPreferences.Editor editor;

        prefs = getActivity().getSharedPreferences("ficheroConf",
                Context.MODE_PRIVATE);
        editor = prefs.edit();

        editor.putInt("cuenta", 0);
        editor.commit();
    }
}
