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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;

public class DatabaseFragment extends Fragment {
	private static final String KEY_CONTENT = "DatabaseFragment:Content";
	private final String BD_NOMBRE = "BDGestionGastos";
	private SQLiteDatabase db;
	final GestionBBDD gestion = new GestionBBDD();
	static final int MENSAJE_OK_DELETE = 5;
	static final int MENSAJE_ERROR_DELETE = 4;
	static final int DATABASE_EXPORT = 1;
	static final int DATABASE_IMPORT = 2;
	static final int DATABASE_RESET = 3;
	static final int DATABASE_IMPORT_PRO = 6;	

	private Button botonExportarBD;
	private Button botonImportarBD;
	private Button botonResetearBD;
	private Button botonImportProBD;

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

		boolean existeBD = gestion.existeBDPro();

		// gestion.importarBBDDLite(db);

		botonExportarBD = (Button) getView().findViewById(R.id.botonExportarBD);
		botonImportarBD = (Button) getView().findViewById(R.id.botonImportarBD);
		botonResetearBD = (Button) getView().findViewById(R.id.botonResetearBD);
		botonImportProBD = (Button) getView().findViewById(
				R.id.botonImportProBD);

		if (existeBD) {
			botonImportProBD.setVisibility(0);
		}

		botonExportarBD.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onCreateDialog(DATABASE_EXPORT);
			}
		});

		botonImportarBD.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onCreateDialog(DATABASE_IMPORT);
			}
		});

		botonResetearBD.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onCreateDialog(DATABASE_RESET);
			}
		});

		botonImportProBD.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onCreateDialog(DATABASE_IMPORT_PRO);
			}
		});

	}

	// Aadiendo las opciones de men
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_setting, menu);
	}

	// Aadiendo funcionalidad a las opciones de men
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*LayoutInflater li = LayoutInflater.from(this);
		View view = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog alert;
		switch (item.getItemId()) {
		case R.id.btInfo:
			view = li.inflate(R.layout.info, null);
			builder.setView(view);
			builder.setTitle(getResources().getString(R.string.informacion));
			builder.setIcon(R.drawable.ic_info_azul);
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
			return true;
		case R.id.btAcerca:
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
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}*/
		return true;
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
			builder.setIcon(R.drawable.ic_database);
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
			builder.setIcon(R.drawable.ic_database);
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
			builder.setIcon(R.drawable.ic_database);
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
			builder.setIcon(R.drawable.ic_database);
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
