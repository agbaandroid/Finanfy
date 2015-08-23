package com.agudoApp.salaryApp.informes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterCatGrafico;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.graficos.PieChartView;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Movimiento;

public class GraficoRoscoActivity extends ActionBarActivity {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	SharedPreferences prefs;
	private boolean mostrarNominas = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Cursor movimientos = null;
		ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
		Bundle extras = getIntent().getExtras();
		String tipo = extras.getString("tipo");
		int idCuenta = cuentaSeleccionada();

		mostrarNominas = mostrarNominaActivado();

		setContentView(R.layout.grafico_rosco);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setTitle(
				getResources().getString(R.string.tabInformes));

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (tipo.equals("1")) {
			int mes = extras.getInt("mes");
			int anio = extras.getInt("anio");

			db = openOrCreateDatabase(BD_NOMBRE, 1, null);
			if (db != null) {
				if (mostrarNominas) {
					listMov = gestion.getMovimientosExcel(db, mes, anio,
							idCuenta);
				} else {
					listMov = gestion.getMovimientosExcelMes(db, mes, anio,
							idCuenta);
				}
			}
			db.close();
		} else {
			int mYearD = extras.getInt("mYearD");
			int mMonthD = extras.getInt("mMonthD");
			int mDayD = extras.getInt("mDayD");
			int mYearH = extras.getInt("mYearH");
			int mMonthH = extras.getInt("mMonthH");
			int mDayH = extras.getInt("mDayH");

			Date fechaDes = new Date(mYearD - 1900, mMonthD, mDayD);
			Date fechaHas = new Date(mYearH - 1900, mMonthH, mDayH);

			db = openOrCreateDatabase(BD_NOMBRE, 1, null);
			if (db != null) {
				movimientos = gestion.consultarMovimientosExcel(db, fechaDes,
						fechaHas, idCuenta);
			}
			listMov = GestionBBDD.obtenerDatosMovimientosExcel(movimientos);
			db.close();			
		}

		// GraficoRosco mView = new GraficoRosco(this, listMov);
		listMov = filtrarMovimientosGastos(listMov);
		LinearLayout layot = (LinearLayout) findViewById(R.id.grafico);
		ArrayList<Categoria> listCat = obtenerListaCategorias(listMov);
		layot.addView(new PieChartView(this, null, listCat));

		ListView listaCatGrafico = (ListView) findViewById(R.id.listaCategoriasGrafico);
		listaCatGrafico.setAdapter(new ListAdapterCatGrafico(this, listCat));
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
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public int cuentaSeleccionada() {
		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);

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

	public boolean mostrarNominaActivado() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		boolean mostrarNomina = sharedPrefs.getBoolean("mostrarNominas", false);
		return mostrarNomina;
	}
}