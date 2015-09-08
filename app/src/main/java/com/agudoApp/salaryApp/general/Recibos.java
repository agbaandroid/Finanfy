package com.agudoApp.salaryApp.general;

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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.AddCat;
import com.agudoApp.salaryApp.activities.AddSub;
import com.agudoApp.salaryApp.activities.DeleteCat;
import com.agudoApp.salaryApp.activities.DeleteSub;
import com.agudoApp.salaryApp.activities.EditCat;
import com.agudoApp.salaryApp.activities.EditSub;
import com.agudoApp.salaryApp.adapters.ListAdapterSpinner;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Tarjeta;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class Recibos extends Activity {

	private final String BD_NOMBRE = "BDGestionGastos";
	static final int DATE_DIALOG_D_ID = 10;
	static final int DATE_DIALOG_H_ID = 11;
	static final int MENSAJE_ERROR = 3;
	static final int MENSAJE_ERROR_NOMINA = 4;
	static final int MENSAJE_ERROR_DELETE = 5;
	static final int MENSAJE_OK_DELETE = 6;
	static final int BOTON_BACK = 99;
	private int mYearD;
	private int mMonthD;
	private int mDayD;
	private int mYearH;
	private int mMonthH;
	private int mDayH;
	private Button botonFechaDesde;
	private Button botonFechaHasta;
	private Button botonCrear;
	private EditText cantidad;
	private Spinner spinnerCat;
	private Spinner spinnerSub;
	private Spinner spinnerTarjetas;
	private EditText descripcion;
	private CheckBox checkTarjeta;
	private SQLiteDatabase db;
	final GestionBBDD gestion = new GestionBBDD();
	boolean tablasCreadas = false;
	boolean opcionesCategoria = false;
	boolean opcionesSubcategoria = false;
	LinearLayout layout;
	// private AdView adView;
	RelativeLayout layoutMovimientos;
	LinearLayout layoutSupMovimientos;
	SharedPreferences prefs;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recibos);

		// Elementos del activity
		spinnerCat = (Spinner) findViewById(R.id.spinnerCategoriaRecibo);
		spinnerSub = (Spinner) findViewById(R.id.spinnerSubCategoriaRecibo);
		spinnerTarjetas = (Spinner) findViewById(R.id.spinnerTarjetasRecibo);
		cantidad = (EditText) findViewById(R.id.cajaCantidadRecibo);
		descripcion = (EditText) findViewById(R.id.cajaDescripcionRecibo);
		checkTarjeta = (CheckBox) findViewById(R.id.checkTarjetaRecibo);
		botonCrear = (Button) findViewById(R.id.botonCrearRecibo);
		botonFechaDesde = (Button) findViewById(R.id.botonFechaReciboDesde);
		botonFechaHasta = (Button) findViewById(R.id.botonFechaReciboHasta);

		// Asignamos el tipo de fuente
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/Berlin.ttf");

		TextView txtDescripcion = (TextView) findViewById(R.id.descripcionRecibo);
		txtDescripcion.setTypeface(miPropiaTypeFace);
		TextView txtCantidad = (TextView) findViewById(R.id.cantidadRecibo);
		txtCantidad.setTypeface(miPropiaTypeFace);
		TextView txtDesde = (TextView) findViewById(R.id.textoFechaDesdeRecibo);
		txtDesde.setTypeface(miPropiaTypeFace);
		TextView txtHasta = (TextView) findViewById(R.id.textoFechaHastaRecibo);
		txtHasta.setTypeface(miPropiaTypeFace);
		TextView txtTextoCategoria = (TextView) findViewById(R.id.textoCategoriaRecibo);
		txtTextoCategoria.setTypeface(miPropiaTypeFace);
		TextView txtTextoSubCategoria = (TextView) findViewById(R.id.textoSubCategoriaRecibo);
		txtTextoSubCategoria.setTypeface(miPropiaTypeFace);
		TextView txtTextoPagoTarjeta = (TextView) findViewById(R.id.textoPagoTarjetaRecibo);
		txtTextoPagoTarjeta.setTypeface(miPropiaTypeFace);

		// Se crea o abre la BD
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);

		iniciarActivity();

		checkTarjeta.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (checkTarjeta.isChecked()) {
					spinnerTarjetas.setEnabled(true);
					spinnerTarjetas.setBackgroundResource(R.drawable.spinner);
				} else {
					spinnerTarjetas.setEnabled(false);
					spinnerTarjetas
							.setBackgroundResource(R.drawable.spinner_deshabilitado);
				}
			}
		});

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

		// Funcionalidad boton guardar
		botonCrear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Obtenemos la posicion del objeto seleccionado en los spinner
				int posC = spinnerCat.getSelectedItemPosition();
				int posS = spinnerSub.getSelectedItemPosition();
				int posTarjeta = spinnerTarjetas.getSelectedItemPosition();
				int idCategoria = 0;
				int idSubcategoria = 0;
				int idTarjeta = 0;
				boolean error = false;
				String desc = "";
				Float cant = new Float(0);
				int idCuenta = cuentaSeleccionada();

				// Obtenemos el objeto seleccionado en spinner partiendo de la
				// pos
				if (posC != 0) {
					Categoria categoria = (Categoria) spinnerCat
							.getItemAtPosition(posC);
					// Obtenemos el id de los objetos seleccionados
					idCategoria = Integer.parseInt(categoria.getId());
				}
				if (posS != 0) {
					Categoria subcategoria = (Categoria) spinnerSub
							.getItemAtPosition(posS);
					// Obtenemos el id de los objetos seleccionados
					idSubcategoria = Integer.parseInt(subcategoria.getId());
				}

				Tarjeta tarjetaAux = (Tarjeta) spinnerTarjetas
						.getItemAtPosition(posTarjeta);
				// Obtenemos el id de los objetos seleccionados
				idTarjeta = Integer.parseInt(tarjetaAux.getId());

				// Obtenemos el resto de datos
				Date fechaD = new Date(mYearD - 1900, mMonthD, mDayD);
				Date fechaH = new Date(mYearH - 1900, mMonthH, mDayH);
				if ("".equals(cantidad.getText().toString())
						|| ".".equals(cantidad.getText().toString())
						|| Float.parseFloat(cantidad.getText().toString()) <= 0) {
					error = true;
				} else {
					cant = -1 * Float.parseFloat(cantidad.getText().toString());
					error = false;
				}

				// Si el tipo es Nomina, obtenemos el valor del combo meses
				// sino obtenemos el valor de concepto

				if ("".equals(descripcion.getText().toString()) || error) {
					error = true;
				} else {
					desc = descripcion.getText().toString().trim();
					error = false;
				}

				// Si no hay error realizamos el insert
				if (!error) {
					boolean tarjeta = checkTarjeta.isChecked();

					// Introducimos el movimiento en BBDD
					boolean ok = gestion.insertarRecibo(db, cant, desc, fechaD,
							fechaH, idCategoria, idSubcategoria, tarjeta,
							idTarjeta, idCuenta);
					if (ok) {
						Context context = getApplicationContext();
						CharSequence textMsg = getResources().getString(
								R.string.addReciboOK);
						int duration = Toast.LENGTH_SHORT;
						Toast toast = Toast
								.makeText(context, textMsg, duration);
						toast.show();

						finish();
					} else {
						Context context = getApplicationContext();
						CharSequence textMsg = getResources().getString(
								R.string.addReciboKO);
						int duration = Toast.LENGTH_SHORT;
						Toast toast = Toast
								.makeText(context, textMsg, duration);
						toast.show();

						finish();
					}
				} else {
					onCreateDialog(MENSAJE_ERROR);
				}
			}
		});
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

	private DatePickerDialog.OnDateSetListener mDateSetListenerH = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYearH = year;
			mMonthH = monthOfYear;
			mDayH = dayOfMonth;
			updateDisplayHasta(mDayH, mMonthH, mYearH);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog alert;
		switch (id) {
		case DATE_DIALOG_D_ID:
			return new DatePickerDialog(this, mDateSetListenerD, mYearD,
					mMonthD, mDayD);
		case DATE_DIALOG_H_ID:
			return new DatePickerDialog(this, mDateSetListenerH, mYearH,
					mMonthH, mDayH);
		case MENSAJE_ERROR:
			builder.setMessage(
					getResources().getString(R.string.camposObligatorios))
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

	public void obtenerCategorias() {
		ArrayList<Categoria> listCategorias = new ArrayList<Categoria>();
		// Recuperamos el listado del spinner Categorias
		listCategorias = (ArrayList<Categoria>) gestion.getCategorias(db,
				"Categorias", "idCategoria", this);

		// Creamos el adaptador
		ListAdapterSpinner spinner_adapterCat = new ListAdapterSpinner(this,
				android.R.layout.simple_spinner_item, listCategorias);

		spinnerCat.setAdapter(spinner_adapterCat);
	}

	public void obtenerSubcategorias() {
		ArrayList<Categoria> listSubcategorias = new ArrayList<Categoria>();
		listSubcategorias = (ArrayList<Categoria>) gestion.getCategorias(db,
				"Subcategorias", "idSubcategoria", this);

		// Creamos el adaptador
		ListAdapterSpinner spinner_adapterSubcat = new ListAdapterSpinner(this,
				android.R.layout.simple_spinner_item, listSubcategorias);

		spinnerSub.setAdapter(spinner_adapterSubcat);
	}

	public void obtenerTarjetas() {
		ArrayList<Tarjeta> listTarjetas = new ArrayList<Tarjeta>();
		// Recuperamos el listado del spinner Categorias
		listTarjetas = (ArrayList<Tarjeta>) gestion.getTarjetas(db);
		// Creamos la lista
		LinkedList<Tarjeta> listaTarjetas = new LinkedList<Tarjeta>();
		// Rellenamos la lista
		for (int i = 0; i < listTarjetas.size(); i++) {
			Tarjeta tarjeta = new Tarjeta();
			tarjeta = listTarjetas.get(i);
			listaTarjetas.add(tarjeta);
		}
		// Creamos el adaptador
		ArrayAdapter<Tarjeta> spinner_adapterTar = new ArrayAdapter<Tarjeta>(
				this, android.R.layout.simple_spinner_item, listaTarjetas);
		// Aadimos el layout para el men y se lo damos al spinner
		spinner_adapterTar.setDropDownViewResource(R.layout.spinner);
		spinnerTarjetas.setAdapter(spinner_adapterTar);
	}

	// Evento que recoge el resultado de las subActivities llamadas
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) {
			obtenerCategorias();
		} else {
			obtenerSubcategorias();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_pulsacion_corta, menu);
		menu.setHeaderTitle(getResources().getString(R.string.elijaOpcion));

	}

	@Override
	public boolean onContextItemSelected(MenuItem aItem) {
		/* Switch on the ID of the item, to get what the user selected. */
		switch (aItem.getItemId()) {
		case R.id.opcion1:
			if (opcionesCategoria) {
				Intent inCat = new Intent(this, AddCat.class);
				this.startActivityForResult(inCat, 0);
			} else if (opcionesSubcategoria) {
				Intent inCat = new Intent(this, AddSub.class);
				this.startActivityForResult(inCat, 0);
			}
			opcionesCategoria = false;
			opcionesSubcategoria = false;
			break;
		case R.id.opcion2:
			if (opcionesCategoria) {
				Intent inCat = new Intent(this, EditCat.class);
				this.startActivityForResult(inCat, 0);
			} else if (opcionesSubcategoria) {
				Intent inCat = new Intent(this, EditSub.class);
				this.startActivityForResult(inCat, 0);
			}
			opcionesCategoria = false;
			opcionesSubcategoria = false;
			break;
		case R.id.opcion3:
			if (opcionesCategoria) {
				Intent inCat = new Intent(this, DeleteCat.class);
				this.startActivityForResult(inCat, 0);
			} else if (opcionesSubcategoria) {
				Intent inSub = new Intent(this, DeleteSub.class);
				this.startActivityForResult(inSub, 0);
			}
			opcionesCategoria = false;
			opcionesSubcategoria = false;
			break;
		}
		return true;
	}

	public void iniciarActivity() {
		// Recuperamos el listado del spinner categorias
		obtenerCategorias();
		// Recuperamos el listado del spinner subcategorias
		obtenerSubcategorias();
		// Recuperamos el listado del spinner tarjetas
		obtenerTarjetas();

		// Obtiene la fecha de hoy
		final Calendar c = Calendar.getInstance();
		mYearD = c.get(Calendar.YEAR);
		mMonthD = c.get(Calendar.MONTH);
		mDayD = c.get(Calendar.DAY_OF_MONTH);
		mYearH = c.get(Calendar.YEAR);
		mMonthH = c.get(Calendar.MONTH);
		mDayH = c.get(Calendar.DAY_OF_MONTH);

		// Actualiza el texto del boton fecha
		updateDisplayDesde(mDayD, mMonthD, mYearD);
		updateDisplayHasta(mDayH, mMonthH, mYearH);

		// Seleccionamos el mes del combo con el mes actual
		spinnerTarjetas.setEnabled(false);
		spinnerTarjetas.setBackgroundResource(R.drawable.spinner_deshabilitado);
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

	public int cuentaSeleccionada() {
		SharedPreferences prefs = getSharedPreferences("ficheroConf",
				Context.MODE_PRIVATE);

		int idCuenta = prefs.getInt("cuenta", 0);
		return idCuenta;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		return true;
	}
}