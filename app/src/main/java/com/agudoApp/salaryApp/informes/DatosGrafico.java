package com.agudoApp.salaryApp.informes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Movimiento;

public class DatosGrafico extends Activity {

	static final int DATE_DIALOG_D_ID = 0;
	static final int DATE_DIALOG_H_ID = 1;
	static final int MENSAJE_ALERTA_1 = 2;
	static final int MENSAJE_ALERTA_2 = 3;
	static final int MENSAJE_OK = 4;
	static final int MENSAJE_KO = 5;
	static final int MENSAJE_FECHAS = 6;
	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	Spinner spinnerGraficos;
	Spinner spinnerNomina;
	Spinner spinnerAnios;
	private int mYearActual;
	private int mMonthActual;
	private int mYearD;
	private int mMonthD;
	private int mDayD;
	private int mYearH;
	private int mMonthH;
	private int mDayH;
	private Button botonFechaDesde;
	private Button botonFechaHasta;
	private TextView textoDesde;
	private TextView textoHasta;
	private TextView textoMeses;
	private CheckBox checkCategorias;
	private TextView textoCategorias;
	private CheckBox checkCategorias2;
	private TextView textoCategorias2;
	private Button botonGenerar;
	private Button botonCancelar;
	private Button botonGenerar2;
	private Button botonCancelar2;
	private int mes;
	private int anio;
	SharedPreferences prefs;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datos_graficos);
		gestion = new GestionBBDD();

		spinnerGraficos = (Spinner) findViewById(R.id.spinnerGraficos);
		spinnerNomina = (Spinner) findViewById(R.id.spinnerNominaGraficos);
		spinnerAnios = (Spinner) findViewById(R.id.spinnerAniosGraficos);
		botonFechaDesde = (Button) findViewById(R.id.botonFechaGraficosDesde);
		botonFechaHasta = (Button) findViewById(R.id.botonFechaGraficosHasta);
		textoDesde = (TextView) findViewById(R.id.textoFechaDesdeGraficos);
		textoHasta = (TextView) findViewById(R.id.textoFechaHastaGraficos);
		textoMeses = (TextView) findViewById(R.id.textoNominasGraficos);
		checkCategorias = (CheckBox) findViewById(R.id.checkGraficosCategorias);
		textoCategorias = (TextView) findViewById(R.id.textoGraficosCategorias);
		checkCategorias2 = (CheckBox) findViewById(R.id.checkGraficosCategorias2);
		textoCategorias2 = (TextView) findViewById(R.id.textoGraficosCategorias2);
		botonGenerar = (Button) findViewById(R.id.botonGenerar);
		botonCancelar = (Button) findViewById(R.id.botonCancelarGraficos);
		botonGenerar2 = (Button) findViewById(R.id.botonGenerar2);
		botonCancelar2 = (Button) findViewById(R.id.botonCancelarGraficos2);
		TextView generarPor = (TextView) findViewById(R.id.GenerarPor);
		TextView textoNominasGrafico = (TextView) findViewById(R.id.textoNominasGraficos);

		// Definimos el tipo de fuente
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/Berlin.ttf");

		generarPor.setTypeface(miPropiaTypeFace);
		textoNominasGrafico.setTypeface(miPropiaTypeFace);
		textoDesde.setTypeface(miPropiaTypeFace);
		textoHasta.setTypeface(miPropiaTypeFace);
		textoMeses.setTypeface(miPropiaTypeFace);
		textoCategorias.setTypeface(miPropiaTypeFace);
		textoCategorias2.setTypeface(miPropiaTypeFace);

		obtenerOpcionesInfome();
		obtenerMeses();
		rellenarAnios();

		// Obtiene la fecha de hoy
		final Calendar c = Calendar.getInstance();
		mYearD = c.get(Calendar.YEAR);
		mMonthD = c.get(Calendar.MONTH);
		mDayD = c.get(Calendar.DAY_OF_MONTH);
		mYearH = c.get(Calendar.YEAR);
		mMonthH = c.get(Calendar.MONTH);
		mDayH = c.get(Calendar.DAY_OF_MONTH);

		// Obtiene la fecha de hoy
		final Calendar cAc = Calendar.getInstance();
		mYearActual = cAc.get(Calendar.YEAR);
		mMonthActual = cAc.get(Calendar.MONTH);

		// Actualiza el texto del boton fecha
		updateDisplayDesde(mDayD, mMonthD, mYearD);
		updateDisplayHasta(mDayH, mMonthH, mYearH);

		// Funcionalidad boton fecha
		botonFechaDesde.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_D_ID);
			}
		});

		// Funcionalidad boton fecha
		botonFechaHasta.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_H_ID);
			}
		});

		// Evento al seleccionar un tipo
		spinnerGraficos
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (position == 1) {
							botonFechaDesde.setVisibility(0);
							botonFechaHasta.setVisibility(0);
							textoDesde.setVisibility(0);
							textoHasta.setVisibility(0);
							textoCategorias2.setVisibility(0);
							checkCategorias2.setVisibility(0);
							botonGenerar2.setVisibility(0);
							botonCancelar2.setVisibility(0);
							spinnerNomina.setVisibility(8);
							spinnerAnios.setVisibility(8);
							textoMeses.setVisibility(8);
							textoCategorias.setVisibility(8);
							checkCategorias.setVisibility(8);
							botonGenerar.setVisibility(8);
							botonCancelar.setVisibility(8);
						} else {
							spinnerNomina.setVisibility(0);
							spinnerAnios.setVisibility(0);
							textoMeses.setVisibility(0);
							textoCategorias.setVisibility(0);
							checkCategorias.setVisibility(0);
							botonGenerar.setVisibility(0);
							botonCancelar.setVisibility(0);
							botonFechaDesde.setVisibility(8);
							botonFechaHasta.setVisibility(8);
							textoDesde.setVisibility(8);
							textoHasta.setVisibility(8);
							textoCategorias2.setVisibility(8);
							checkCategorias2.setVisibility(8);
							botonGenerar2.setVisibility(8);
							botonCancelar2.setVisibility(8);
						}
					}

					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		spinnerNomina
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						mes = position;
					}

					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		spinnerAnios
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						Movimiento mov = (Movimiento) parent
								.getItemAtPosition(position);

						anio = Integer.parseInt(mov.toString());
					}

					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		botonGenerar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (checkCategorias.isChecked()) {
					Intent graf = new Intent(DatosGrafico.this,
							GraficoRoscoActivity.class);
					graf.putExtra("mes", mes);
					graf.putExtra("anio", anio);
					graf.putExtra("tipo", "1");
					startActivity(graf);
					guardarPrefs();
					finish();
				} else {
					Intent graf = new Intent(DatosGrafico.this,
							GraficoBarrasActivity.class);
					graf.putExtra("mes", mes);
					graf.putExtra("anio", anio);
					graf.putExtra("tipo", "1");
					startActivity(graf);
					guardarPrefs();
					finish();
				}
			}
		});

		botonGenerar2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Date fechaDes = new Date(mYearD - 1900, mMonthD, mDayD);
				Date fechaHas = new Date(mYearH - 1900, mMonthH, mDayH);
				if (fechaDes.before(fechaHas)) {
					if (checkCategorias2.isChecked()) {
						Intent graf = new Intent(DatosGrafico.this,
								GraficoRoscoActivity.class);
						graf.putExtra("mYearD", mYearD);
						graf.putExtra("mMonthD", mMonthD);
						graf.putExtra("mDayD", mDayD);
						graf.putExtra("mYearH", mYearH);
						graf.putExtra("mMonthH", mMonthH);
						graf.putExtra("mDayH", mDayH);
						graf.putExtra("tipo", "2");
						startActivity(graf);
						guardarPrefs();
						finish();
					} else {
						Intent graf = new Intent(DatosGrafico.this,
								GraficoBarrasActivity.class);
						graf.putExtra("mYearD", mYearD);
						graf.putExtra("mMonthD", mMonthD);
						graf.putExtra("mDayD", mDayD);
						graf.putExtra("mYearH", mYearH);
						graf.putExtra("mMonthH", mMonthH);
						graf.putExtra("mDayH", mDayH);
						graf.putExtra("tipo", "2");
						startActivity(graf);
						guardarPrefs();
						finish();
					}
				} else {
					onCreateDialog(MENSAJE_FECHAS);
				}
			}
		});

		botonCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		botonCancelar2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog alert;
		LayoutInflater li = LayoutInflater.from(this);
		View view = null;
		switch (id) {
		case DATE_DIALOG_D_ID:
			return new DatePickerDialog(this, mDateSetListenerD, mYearD,
					mMonthD, mDayD);
		case DATE_DIALOG_H_ID:
			return new DatePickerDialog(this, mDateSetListenerH, mYearH,
					mMonthH, mDayH);
		case MENSAJE_ALERTA_1:
			view = li.inflate(R.layout.datos_informes_msn, null);
			builder.setView(view);
			builder.setTitle(getResources().getString(R.string.info));
			builder.setIcon(R.drawable.ic_alert);
			builder.setCancelable(false);
			builder.setPositiveButton(
					getResources().getString(R.string.aceptar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							try {
								// ArrayList<Movimiento> listMov = new
								// ArrayList<Movimiento>();

							} catch (Exception e) {
								onCreateDialog(MENSAJE_KO);
							}
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
		case MENSAJE_ALERTA_2:
			view = li.inflate(R.layout.datos_informes_msn, null);
			builder.setView(view);
			builder.setTitle(getResources().getString(R.string.info));
			builder.setIcon(R.drawable.ic_alert);
			builder.setCancelable(false);
			builder.setPositiveButton(
					getResources().getString(R.string.aceptar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							try {

							} catch (Exception e) {
								onCreateDialog(MENSAJE_KO);
							}
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
		case MENSAJE_OK:
			builder.setMessage("La hoja excel se ha generado correctamente.")
					.setTitle(getResources().getString(R.string.info))
					.setIcon(R.drawable.ic_alert)
					.setPositiveButton(
							getResources().getString(R.string.aceptar),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									finish();
								}
							});
			alert = builder.create();
			alert.show();
			break;
		case MENSAJE_KO:
			builder.setMessage("Se ha producido un error al generar el excel.")
					.setTitle(getResources().getString(R.string.info))
					.setIcon(R.drawable.ic_alert)
					.setPositiveButton(
							getResources().getString(R.string.aceptar),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									finish();
								}
							});
			alert = builder.create();
			alert.show();
			break;
		case MENSAJE_FECHAS:
			builder.setMessage(getResources().getString(R.string.errorExcel))
					.setTitle(getResources().getString(R.string.atencion))
					.setIcon(R.drawable.ic_alert)
					.setPositiveButton(
							getResources().getString(R.string.aceptar),
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

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListenerD = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYearD = year;
			mMonthD = monthOfYear;
			mDayD = dayOfMonth;
			updateDisplayDesde(mDayD, mMonthD, mYearD);
		}
	};

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListenerH = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYearH = year;
			mMonthH = monthOfYear;
			mDayH = dayOfMonth;
			updateDisplayHasta(mDayH, mMonthH, mYearH);
		}
	};

	// Rellena el spinner movimientos
	public void obtenerOpcionesInfome() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.opcionesInformesArray,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner);
		spinnerGraficos.setAdapter(adapter);

	}

	// Rellena el spinner meses
	public void obtenerMeses() {
		ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
		final Calendar c = Calendar.getInstance();
		ArrayAdapter<CharSequence> adapterMes = ArrayAdapter
				.createFromResource(this, R.array.nominasArray,
						android.R.layout.simple_spinner_item);
		adapterMes
				.setDropDownViewResource(R.layout.spinner);
		spinnerNomina.setAdapter(adapterMes);
		spinnerNomina.setSelection(c.get(Calendar.MONTH) + 1);
		
		int idCuenta = cuentaSeleccionada();

		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			listMov = gestion.getMovimientosInicioNominas(db, idCuenta);
		}
		db.close();
		// Seleccionamos el mes correcto
		for (int i = 0; i < listMov.size(); i++) {
			Movimiento mov = listMov.get(i);
			if (mov.getTipo().equals("1")) {
				spinnerNomina.setSelection(Integer.parseInt(mov.getMes()));
				break;
			}
		}
	}

	private void updateDisplayDesde(int dia, int mes, int anio) {
		botonFechaDesde.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(dia).append("-").append(mes + 1).append("-")
				.append(anio).append(" "));
	}

	private void updateDisplayHasta(int dia, int mes, int anio) {
		botonFechaHasta.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(dia).append("-").append(mes + 1).append("-")
				.append(anio).append(" "));
	}

	private void rellenarAnios() {
		ArrayList<Movimiento> listaAniosBBDD = new ArrayList<Movimiento>();
		int idCuenta = cuentaSeleccionada();
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			listaAniosBBDD = gestion.getAnios(db, idCuenta);
		}
		db.close();
		LinkedList<Movimiento> listAnios = new LinkedList<Movimiento>();
		ArrayList<Movimiento> listaAnios = new ArrayList<Movimiento>();
		final Calendar c = Calendar.getInstance();
		if (listaAniosBBDD.size() > 0) {
			// Rellenamos la lista con los aos a mostrar
			listaAnios = listaAniosBBDD;
			// rellenamos el listado del combo de aos
			for (int i = 0; i < listaAnios.size(); i++) {
				Movimiento mov = new Movimiento();
				mov.setDescripcion(listaAnios.get(i).getAnio());
				listAnios.add(mov);
			}

			// Rellenamos el spinner de aos
			ArrayAdapter<Movimiento> spinner_adapterAnios = new ArrayAdapter<Movimiento>(
					this, android.R.layout.simple_spinner_item, listAnios);
			// Aadimos el layout para el men y se lo damos al spinner
			spinner_adapterAnios
					.setDropDownViewResource(R.layout.spinner);
			spinnerAnios.setAdapter(spinner_adapterAnios);
		} else {
			Movimiento anio = new Movimiento();

			anio.setId(String.valueOf(c.get(Calendar.YEAR)));
			anio.setDescripcion(String.valueOf(c.get(Calendar.YEAR)));
			listAnios.add(anio);
			// Rellenamos el spinner de aos
			ArrayAdapter<Movimiento> spinner_adapterAnios = new ArrayAdapter<Movimiento>(
					this, android.R.layout.simple_spinner_item, listAnios);
			// Aadimos el layout para el men y se lo damos al spinner
			spinner_adapterAnios
					.setDropDownViewResource(R.layout.spinner);
			spinnerAnios.setAdapter(spinner_adapterAnios);
			spinnerAnios.setSelection(0);
		}
		// Seleccionamos el ao correcto
		for (int i = 0; i < listaAnios.size(); i++) {
			Movimiento mov = listaAnios.get(i);
			if (String.valueOf(c.get(Calendar.YEAR)).equals(mov.getAnio())) {
				spinnerAnios.setSelection(i);
			}
		}
	}

	public void guardarPrefs() {
		SharedPreferences prefs;
		SharedPreferences.Editor editor;
		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);

		String limite = "grafico" + mMonthActual + "," + mYearActual;
		int limiteInt = prefs.getInt(limite, 0);
		editor = prefs.edit();
		editor.putInt(limite, limiteInt + 1);
		editor.commit();
	}
	
	public int cuentaSeleccionada() {
		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);

		int idCuenta = prefs.getInt("cuenta", 0);
		return idCuenta;
	}
}
