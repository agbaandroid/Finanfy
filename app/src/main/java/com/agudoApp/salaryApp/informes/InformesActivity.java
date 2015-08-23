package com.agudoApp.salaryApp.informes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;

public class InformesActivity extends Activity {

	private SQLiteDatabase db;
	final GestionBBDD gestion = new GestionBBDD();
	RelativeLayout layoutInformes;
	RelativeLayout layoutInformes2;
	SharedPreferences prefs;
	static final int BOTON_BACK = 99;
	SharedPreferences.Editor editor;	
	ImageView excel;
	ImageView grafico;
	TextView textExcel;
	private boolean isPremium = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.informes);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			isPremium = extras.getBoolean("isPremium", false);						
		}

		layoutInformes = (RelativeLayout) findViewById(R.id.layoutInformes1);
		layoutInformes2 = (RelativeLayout) findViewById(R.id.layoutInformes2);

		excel = (ImageView) findViewById(R.id.excelIcon);
		textExcel = (TextView) findViewById(R.id.generarExcel);
		grafico = (ImageView) findViewById(R.id.graficoIcon);

		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
		editor = prefs.edit();		

		// Informes.CrearExcel();

		excel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(InformesActivity.this,
						DatosInforme.class);
				
				intent.putExtra("isPremium", isPremium);				
				startActivity(intent);
				onPause();
			}
		});

		textExcel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(InformesActivity.this,
						DatosInforme.class);
				startActivity(intent);
				onPause();
			}
		});

		grafico.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(InformesActivity.this,
						GraficoBarrasActivity.class);

				startActivity(intent);
				onPause();
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
									if (db != null) {
										db.close();
									}
									editor.putBoolean("appIniciada", false);
									editor.commit();
									finish();
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
			openOptionsMenu();
		}
		return true;
	}

	// Aadiendo las opciones de men
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_setting, menu);
		return true;
	}

	// Aadiendo funcionalidad a las opciones de men
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		LayoutInflater li = LayoutInflater.from(this);
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
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
