/* ===========================================================
 * AFreeChart : a free chart library for Android(tm) platform.
 *              (based on JFreeChart and JCommon)
 * ===========================================================
 *
 * (C) Copyright 2010, by Icom Systech Co., Ltd.
 * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
 *
 * Project Info:
 *    AFreeChart: http://code.google.com/p/afreechart/
 *    JFreeChart: http://www.jfree.org/jfreechart/index.html
 *    JCommon   : http://www.jfree.org/jcommon/index.html
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * [Android is a trademark of Google Inc.]
 *
 * -----------------
 * BarChartDemo1Activity.java
 * -----------------
 * (C) Copyright 2010, by Icom Systech Co., Ltd.
 *
 * Original Author:  Niwano Masayoshi (for Icom Systech Co., Ltd);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 19-Nov-2010 : Version 0.0.1 (NM);
 */

package com.agudoApp.salaryApp.informes;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ParseException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.Utilidades.GraficoBarras;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Movimiento;

/**
 * PieChartDemo1Activity
 */
public class GraficoBarrasActivity extends Activity {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	static final int MENSAJE_FECHA = 0;
	SharedPreferences prefs;
	private boolean mostrarNominas = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean error = false;
		Bundle extras = getIntent().getExtras();
		String tipo = extras.getString("tipo");
		ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
		GraficoBarras mView = null;
		int idCuenta = cuentaSeleccionada();
		mostrarNominas = mostrarNominaActivado();

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
			mView = new GraficoBarras(this, listMov);
		} else if (tipo.equals("2")) {
			int mYearD = extras.getInt("mYearD");
			int mMonthD = extras.getInt("mMonthD");
			int mDayD = extras.getInt("mDayD");
			int mYearH = extras.getInt("mYearH");
			int mMonthH = extras.getInt("mMonthH");
			int mDayH = extras.getInt("mDayH");

			Cursor movimientos = null;
			Date fechaDes = new Date(mYearD - 1900, mMonthD, mDayD);
			Date fechaHas = new Date(mYearH - 1900, mMonthH, mDayH);
			String fechaDesString = mDayD + "/" + mMonthD + "/" + mYearD;
			String fechaHasString = mDayH + "/" + mMonthH + "/" + mYearH;

			int dias = 0;
			int meses = 0;
			int anios = 0;
			String tipoGrafico = "";
			try {
				dias = diferenciaFechas(fechaDesString, fechaHasString, 1);
				meses = diferenciaFechas(fechaDesString, fechaHasString, 2);
				anios = diferenciaFechas(fechaDesString, fechaHasString, 3);
				if (meses > 28) {
					tipoGrafico = "anio";
				} else if (dias > 62) {
					tipoGrafico = "mes";
				} else {
					tipoGrafico = "dia";
				}
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}

			db = openOrCreateDatabase(BD_NOMBRE, 1, null);
			if (db != null) {
				movimientos = gestion.consultarMovimientosExcel(db, fechaDes,
						fechaHas, idCuenta);
			}
			listMov = GestionBBDD.obtenerDatosMovimientosExcel(movimientos);

			mView = new GraficoBarras(this, listMov, tipoGrafico, db, idCuenta);
			db.close();
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(mView);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			openOptionsMenu();
		}
		return true;
	}

	public int diferenciaFechas(String fec1, String fec2, int valor)
			throws java.text.ParseException {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		int retorno = 0;
		java.util.Date date1 = null;
		java.util.Date date2 = null;
		try {
			Calendar cal1 = null;
			date1 = df.parse(fec1);
			cal1 = Calendar.getInstance();

			Calendar cal2 = null;
			date2 = df.parse(fec2);
			cal2 = Calendar.getInstance();

			// different date might have different offset
			cal1.setTime(date1);
			long ldate1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET)
					+ cal1.get(Calendar.DST_OFFSET);

			cal2.setTime(date2);
			long ldate2 = date2.getTime() + cal2.get(Calendar.ZONE_OFFSET)
					+ cal2.get(Calendar.DST_OFFSET);

			// Use integer calculation, truncate the decimals
			int hr1 = (int) (ldate1 / 3600000); // 60*60*1000
			int hr2 = (int) (ldate2 / 3600000);

			int days1 = (int) hr1 / 24;
			int days2 = (int) hr2 / 24;

			int dateDiff = days2 - days1;
			int yearDiff = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
			int monthDiff = yearDiff * 12 + cal2.get(Calendar.MONTH)
					- cal1.get(Calendar.MONTH);

			if (valor == 1) {
				if (dateDiff < 0)
					dateDiff = dateDiff * (-1);
				retorno = dateDiff;
			} else if (valor == 2) {
				if (monthDiff < 0)
					monthDiff = monthDiff * (-1);
				retorno = monthDiff;
			} else if (valor == 3) {
				if (yearDiff < 0)
					yearDiff = yearDiff * (-1);
				retorno = yearDiff;
			}
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return retorno;
	}

	// Aadiendo las opciones de men
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_setting_return, menu);
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

		case R.id.btInfoEdit:
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
		case R.id.btReturnEdit:
			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog alert;
		LayoutInflater li = LayoutInflater.from(this);
		View view = null;
		switch (id) {
		case MENSAJE_FECHA:
			view = li.inflate(R.layout.datos_informes_msn, null);
			builder.setView(view);
			builder.setTitle(getResources().getString(R.string.info));
			builder.setIcon(R.drawable.ic_alert);
			builder.setCancelable(false);
			builder.setPositiveButton(
					getResources().getString(R.string.aceptar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							finish();
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

	public int cuentaSeleccionada() {
		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);

		int idCuenta = prefs.getInt("cuenta", 0);
		return idCuenta;
	}

	public boolean mostrarNominaActivado() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		boolean mostrarNomina = sharedPrefs.getBoolean("mostrarNominas", false);
		return mostrarNomina;
	}
}
