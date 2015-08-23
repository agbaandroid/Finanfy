package com.agudoApp.salaryApp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.informes.DatosGrafico;
import com.agudoApp.salaryApp.informes.DatosInforme;
import com.agudoApp.salaryApp.informes.GraficoRoscoActivity;

public final class InformesFragment extends Fragment {
	private static final String KEY_CONTENT = "InformesFragment:Content";
	private final String BD_NOMBRE = "BDGestionGastos";
	private SQLiteDatabase db;
	final GestionBBDD gestion = new GestionBBDD();
	LinearLayout layoutInformes;
	LinearLayout layoutInformes2;
	SharedPreferences prefs;
	static final int BOTON_BACK = 99;
	static final int PRO = 1;
	SharedPreferences.Editor editor;
	ImageView excel;
	ImageView grafico;
	TextView textExcel;
	TextView textGrafico;

	// Productos que posee el usuario
	boolean isPremium = false;
	boolean isCategoriaPremium = false;
	boolean isSinPublicidad = false;

	public InformesFragment(boolean isUserPremium, boolean isUserSinpublicidad,
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
		return inflater.inflate(R.layout.informes, container, false);
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

		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Se crea o abre la BD
		db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);

		layoutInformes = (LinearLayout) this.getView().findViewById(
				R.id.layoutInformes1);
		layoutInformes2 = (LinearLayout) this.getView().findViewById(
				R.id.layoutInformes2);

		excel = (ImageView) this.getView().findViewById(R.id.excelIcon);
		textExcel = (TextView) this.getView().findViewById(R.id.generarExcel);
		grafico = (ImageView) this.getView().findViewById(R.id.graficoIcon);
		textGrafico = (TextView) this.getView().findViewById(
				R.id.generarGrafico);

		// Definimos el tipo de fuente
//		Typeface miPropiaTypeFace = Typeface.createFromAsset(this.getActivity()
//				.getAssets(), "fonts/Berlin.ttf");
//		textExcel.setTypeface(miPropiaTypeFace);
//		textGrafico.setTypeface(miPropiaTypeFace);

		prefs = getActivity().getSharedPreferences("ficheroConf",
				Context.MODE_PRIVATE);
		editor = prefs.edit();

		excel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), DatosInforme.class);
				
				boolean isSinPubli = false;
				if (isPremium || isSinPublicidad) {
					isSinPubli = true;
				}
				
				intent.putExtra("isPremium", isSinPubli);
				startActivity(intent);
				onPause();
			}
		});

		textExcel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), DatosInforme.class);
				startActivity(intent);
				onPause();
			}
		});

		grafico.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), DatosGrafico.class);
				startActivity(intent);
				onPause();
			}
		});

		textGrafico.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						GraficoRoscoActivity.class);
				startActivity(intent);
				onPause();
			}
		});
	}

	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		AlertDialog alert;
		switch (id) {
		case BOTON_BACK:
			builder.setMessage(getResources().getString(R.string.msnSalirApp))
					.setTitle(getResources().getString(R.string.atencion))
					.setIcon(R.drawable.ic_alert)
					.setPositiveButton(
							getResources().getString(R.string.aceptar),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									gestion.closeDB(db);
									editor.putBoolean("appIniciada", false);
									editor.commit();
									getActivity().finish();
								}
							})
					.setNegativeButton(
							getResources().getString(R.string.cancelar),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			alert = builder.create();
			alert.show();
			break;
		}
		return null;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onCreateDialog(BOTON_BACK);
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			getActivity().openOptionsMenu();
		}
		return true;
	}

	// Aadiendo las opciones de men
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_setting, menu);
	}

	// Aadiendo funcionalidad a las opciones de men
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		LayoutInflater li = LayoutInflater.from(getActivity());
		View view = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		editor.putBoolean("appIniciada", false);
		editor.commit();
		// this.getActivity().finish();
		super.onDestroy();
	}
}
