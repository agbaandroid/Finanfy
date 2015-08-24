package com.agudoApp.salaryApp.activities;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;

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
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterSpinner;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Movimiento;
import com.agudoApp.salaryApp.model.Tarjeta;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class EditMovimiento extends ActionBarActivity {

	private final String BD_NOMBRE = "BDGestionGastos";
	static final int DATE_DIALOG_ID = 0;
	static final int MENSAJE_OK = 1;
	static final int MENSAJE_KO = 2;
	static final int MENSAJE_ERROR = 3;
	static final int MENSAJE_ERROR_NOMINA = 4;
	static final int MENSAJE_ERROR_DELETE = 5;
	static final int MENSAJE_OK_DELETE = 6;
	static final int BOTON_BACK = 99;
	private int mYear;
	private int mMonth;
	private int mDay;
	private Button botonFecha;
	private Button botonEdit;
	private Spinner spinnerMovimiento;
	private EditText cantidad;
	private Spinner spinnerCat;
	private Spinner spinnerSub;
	private Spinner spinnerTarjetas;
	private EditText descripcion;
	private CheckBox checkTarjeta;
	private SQLiteDatabase db;
	private Spinner spinnerMeses;
	final GestionBBDD gestion = new GestionBBDD();
	boolean tablasCreadas = false;
	boolean opcionesCategoria = false;
	boolean opcionesSubcategoria = false;
	LinearLayout layout;
	// private AdView adView;
	String idMov;
	String tipoSel;
	String cantidadSel;
	String conceptoSel;
	String fechaSel;
	String idCat;
	String idSubcat;
	String tarjetaSel;
	String mesSel;
	String idTarjeta;
	int mesAnterior;
	int anioAnterior;
	int posCat;
	int posSub;
	int posTar;
	Date fecha;
	SharedPreferences prefs;
	int style;
	LinearLayout layoutEditMov;
	LinearLayout layoutSupEditMov;
	private boolean isPremium = false;	
	private InterstitialAd interstitial;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_movimiento);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		// Se crea o abre la BD
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);

		// Cargar publicidad
		// adView = new AdView(this, AdSize.BANNER, "a14e5bb020df61b");
		// LinearLayout layout = (LinearLayout) findViewById(R.id.adViewLayout);
		// layout.addView(adView);
		// AdRequest request = new AdRequest();
		// request.setTesting(true);
		// adView.loadAd(request);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			isPremium = extras.getBoolean("isPremium", false);						
		}

		if (!isPremium) {
			// Look up the AdView as a resource and load a request.
			AdView adView = (AdView) findViewById(R.id.adViewEdit);
			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);
			
			crearAnuncio();
		}

		// Elementos del activity
		spinnerMovimiento = (Spinner) findViewById(R.id.spinnerEdit);
		spinnerMeses = (Spinner) findViewById(R.id.spinnerMesesEdit);
		spinnerCat = (Spinner) findViewById(R.id.spinnerCategoriaEdit);
		spinnerSub = (Spinner) findViewById(R.id.spinnerSubCategoriaEdit);
		spinnerTarjetas = (Spinner) findViewById(R.id.spinnerTarjetaEdit);
		cantidad = (EditText) findViewById(R.id.cajaCantidadEdit);
		descripcion = (EditText) findViewById(R.id.cajaDescripcionEdit);
		checkTarjeta = (CheckBox) findViewById(R.id.checkTarjetaEdit);
		botonFecha = (Button) findViewById(R.id.botonFechaEdit);
		botonEdit = (Button) findViewById(R.id.botonEdit);
		layoutEditMov = (LinearLayout) findViewById(R.id.layoutEditMov);
		// layoutSupEditMov = (LinearLayout)
		// findViewById(R.id.layoutSupEditMov);

		// Asignamos el tipo de fuente
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/Berlin.ttf");

		TextView txtTipo = (TextView) findViewById(R.id.TipoEdit);
		txtTipo.setTypeface(miPropiaTypeFace);
		// TextView txtTipo2 = (TextView) findViewById(R.id.TipoEdit2);
		// txtTipo2.setTypeface(miPropiaTypeFace);
		TextView txtCantidad = (TextView) findViewById(R.id.cantidadEdit);
		txtCantidad.setTypeface(miPropiaTypeFace);
		TextView txtDescripcion = (TextView) findViewById(R.id.descripcionEdit);
		txtDescripcion.setTypeface(miPropiaTypeFace);
		TextView txtFecha = (TextView) findViewById(R.id.fechaEdit);
		txtFecha.setTypeface(miPropiaTypeFace);
		TextView txtTextoCategoria = (TextView) findViewById(R.id.textoCategoriaEdit);
		txtTextoCategoria.setTypeface(miPropiaTypeFace);
		TextView txtTextoSubCategoria = (TextView) findViewById(R.id.textoSubCategoriaEdit);
		txtTextoSubCategoria.setTypeface(miPropiaTypeFace);
		TextView txtTextoPagoTarjeta = (TextView) findViewById(R.id.textoPagoTarjetaEdit);
		txtTextoPagoTarjeta.setTypeface(miPropiaTypeFace);

		getSupportActionBar().setTitle(
				getResources().getString(R.string.editarRegistro));

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		iniciarActivity();

		// Funcionalidad boton fecha
		botonFecha.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		// Evento al seleccionar un tipo de movimiento
		spinnerMovimiento
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (position == 1) {
							int posi = 0;
							descripcion.setVisibility(8);
							spinnerMeses.setVisibility(0);
							ArrayList<Categoria> listCategorias = (ArrayList<Categoria>) gestion
									.getCategorias(db, "Categorias",
											"idCategoria");

							for (int i = 0; i < listCategorias.size(); i++) {
								Categoria cat = listCategorias.get(i);
								if (cat.getId().equals("1")) {
									posi = i;
									break;
								}
							}

							spinnerCat.setSelection(posi);
							spinnerSub.setSelection(0);
							checkTarjeta.setClickable(false);
							checkTarjeta.setChecked(false);
							spinnerTarjetas.setEnabled(false);
							spinnerTarjetas
									.setBackgroundResource(R.drawable.spinner_deshabilitado);
						} else {
							descripcion.setVisibility(0);
							spinnerMeses.setVisibility(8);
							spinnerCat.setSelection(posCat);
							spinnerSub.setSelection(posSub);
							if (position == 2) {
								checkTarjeta.setClickable(false);
								checkTarjeta.setChecked(false);
								spinnerTarjetas.setEnabled(false);
								spinnerTarjetas
										.setBackgroundResource(R.drawable.spinner_deshabilitado);
							} else {
								checkTarjeta.setClickable(true);
							}
						}
					}

					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

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

		// Funcionalidad boton editar
		botonEdit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// Obtenemos la posicion del objeto seleccionado en los spinner
				int posC = spinnerCat.getSelectedItemPosition();
				int posS = spinnerSub.getSelectedItemPosition();
				int posTarjeta = spinnerTarjetas.getSelectedItemPosition();
				int idCategoria = 0;
				int idSubcategoria = 0;
				int idTarjeta = 0;
				int anio = 0;
				int mes = 0;
				boolean error = false;
				boolean errorNomina = false;
				String desc = "";
				Float cant = new Float(0);

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

				int tipo = spinnerMovimiento.getSelectedItemPosition();

				// Obtenemos el resto de datos
				Date fecha = new Date(mYear - 1900, mMonth - 1, mDay);
				if ("".equals(cantidad.getText().toString())
						|| ".".equals(cantidad.getText().toString())
						|| Float.parseFloat(cantidad.getText().toString()) <= 0) {
					error = true;
				} else {
					if (tipo == 0) {
						cant = -1
								* Float.parseFloat(cantidad.getText()
										.toString().replace(",", "."));
					} else {
						cant = Float.parseFloat(cantidad.getText().toString());
					}
					error = false;
				}

				// Si el tipo es Nomina, obtenemos el valor del combo meses
				// sino obtenemos el valor de concepto
				if (tipo == 1) {
					mes = spinnerMeses.getSelectedItemPosition();
					desc = (String) spinnerMeses.getItemAtPosition(mes)
							.toString();
					anio = mYear;
					int idCuenta = cuentaSeleccionada();
					ArrayList<Movimiento> listNominas = gestion.getNominas(db,
							idCuenta);
					for (int i = 0; i < listNominas.size(); i++) {
						Movimiento mov = listNominas.get(i);
						if (mov.getMes().equals(String.valueOf(mes + 1))
								&& mov.getAnio().equals(String.valueOf(anio))) {
							if (mov.getMes()
									.equals(String.valueOf(mesAnterior))) {
								if (!mov.getAnio().equals(
										String.valueOf(anioAnterior))) {
									errorNomina = true;
									break;
								}
							} else {
								errorNomina = true;
								break;
							}
						}
					}
				} else {
					if ("".equals(descripcion.getText().toString()) || error) {
						error = true;
					} else {
						desc = descripcion.getText().toString().trim();
						error = false;
					}
				}

				// Si no hay error realizamos el insert
				if (!error && !errorNomina) {
					boolean tarjeta = checkTarjeta.isChecked();
					boolean recibo = false;
					if (tipo == 3) {
						recibo = true;
					}
					// Introducimos el movimiento en BBDD
					boolean ok = gestion.editarMovimiento(db, idMov, tipo,
							cant, desc, fecha, idCategoria, idSubcategoria,
							recibo, tarjeta, mes + 1, anio, idTarjeta);
					if (ok) {
						onCreateDialog(MENSAJE_OK);
					} else {
						onCreateDialog(MENSAJE_KO);
					}
				} else {
					if (errorNomina) {
						onCreateDialog(MENSAJE_ERROR_NOMINA);
					} else {
						onCreateDialog(MENSAJE_ERROR);
					}
				}
			}
		});
	}

	private void updateDisplay() {
		botonFecha.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mDay).append("-").append(mMonth).append("-")
				.append(mYear).append(" "));
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mDay = dayOfMonth;
			mMonth = monthOfYear + 1;
			mYear = year;
			updateDisplay();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog alert;
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear,
					mMonth - 1, mDay);
		case MENSAJE_OK:
			builder.setMessage(
					getResources().getString(R.string.editarRegistroOK))
					.setTitle(getResources().getString(R.string.info))
					.setIcon(R.drawable.ic_info_azul)
					.setPositiveButton(
							getResources().getString(R.string.aceptar),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									displayInterstitial();
									finish();
								}
							});
			alert = builder.create();
			alert.show();			
			break;
		case MENSAJE_KO:
			builder.setMessage(
					getResources().getString(R.string.editarRegistroKO))
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
		case MENSAJE_ERROR_NOMINA:
			builder.setMessage(getResources().getString(R.string.errorNomina))
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
		case MENSAJE_ERROR_DELETE:
			builder.setMessage(getResources().getString(R.string.msnDBError))
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
		case MENSAJE_OK_DELETE:
			builder.setMessage(getResources().getString(R.string.msnDBOk))
					.setTitle(getResources().getString(R.string.info))
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

	// Inicializa los compos al crear un registro
	public void inicializar() {
		finish();
	}

	public void obtenerCategorias() {
		ArrayList<Categoria> listCategorias = new ArrayList<Categoria>();
		// Recuperamos el listado del spinner Categorias
		listCategorias = (ArrayList<Categoria>) gestion.getCategorias(db,
				"Categorias", "idCategoria");

		// Creamos el adaptador
		ListAdapterSpinner spinner_adapterCat = new ListAdapterSpinner(this,
				android.R.layout.simple_spinner_item, listCategorias);

		spinnerCat.setAdapter(spinner_adapterCat);
	}

	public void obtenerSubcategorias() {
		ArrayList<Categoria> listSubcategorias = new ArrayList<Categoria>();
		listSubcategorias = (ArrayList<Categoria>) gestion.getCategorias(db,
				"Subcategorias", "idSubcategoria");

		// Creamos el adaptador
		ListAdapterSpinner spinner_adapterSubcat = new ListAdapterSpinner(this,
				android.R.layout.simple_spinner_item, listSubcategorias);
		spinnerSub.setAdapter(spinner_adapterSubcat);
	}

	// Rellena el spinner movimientos
	public void obtenerMovimientos() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.tipoEditMovimientosArray,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner);
		spinnerMovimiento.setAdapter(adapter);

	}

	// Rellena el spinner meses
	public void obtenerMeses() {
		ArrayAdapter<CharSequence> adapterMes = ArrayAdapter
				.createFromResource(this, R.array.mesArray,
						android.R.layout.simple_spinner_item);
		adapterMes.setDropDownViewResource(R.layout.spinner);
		spinnerMeses.setAdapter(adapterMes);
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

	public void iniciarActivity() {
		// Rellenamos el spinner de movimientos
		obtenerMovimientos();
		// Rellenamos el spinner de meses
		obtenerMeses();
		// Recuperamos el listado del spinner categorias
		obtenerCategorias();
		// Recuperamos el listado del spinner subcategorias
		obtenerSubcategorias();
		// Recuperamos el listado del spinner tarjetas
		obtenerTarjetas();

		// Seleccionamos el mes del combo con el mes actual
		spinnerMeses.setSelection(mMonth);

		obtenerDatos();

		if (checkTarjeta.isChecked()) {
			spinnerTarjetas.setEnabled(true);
			spinnerTarjetas.setBackgroundResource(R.drawable.spinner);
		} else {
			spinnerTarjetas.setEnabled(false);
			spinnerTarjetas
					.setBackgroundResource(R.drawable.spinner_deshabilitado);
		}

		// Actualiza el texto del boton fecha
		updateDisplay();
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

	public void obtenerDatos() {
		Bundle extras = getIntent().getExtras();
		idMov = extras.getString("idMovimiento");
		tipoSel = extras.getString("tipo");
		cantidadSel = extras.getString("cantidad");
		conceptoSel = extras.getString("concepto");
		fechaSel = extras.getString("fecha");
		idCat = extras.getString("categoria");
		idSubcat = extras.getString("subcategoria");
		tarjetaSel = extras.getString("tarjeta");
		mesSel = extras.getString("mes");
		idTarjeta = extras.getString("idTarjeta");
		rellenarDatos();
	}

	public void rellenarDatos() {
		posCat = 0;
		posSub = 0;
		posTar = 0;
		String cant = "";
		// Recuperamos el listado del spinner Categorias
		ArrayList<Categoria> listCategorias = (ArrayList<Categoria>) gestion
				.getCategorias(db, "Categorias", "idCategoria");
		// Recuperamos el listado del spinner Subategorias
		ArrayList<Categoria> listSubcategorias = (ArrayList<Categoria>) gestion
				.getCategorias(db, "Subcategorias", "idSubcategoria");
		// Recuperamos el listado del spinner Tarjetas
		ArrayList<Tarjeta> listTarjetas = (ArrayList<Tarjeta>) gestion
				.getTarjetas(db);

		for (int i = 0; i < listCategorias.size(); i++) {
			Categoria cat = listCategorias.get(i);
			if (cat.getId().equals(idCat)) {
				posCat = i;
			}
		}
		for (int i = 0; i < listSubcategorias.size(); i++) {
			Categoria subcat = listSubcategorias.get(i);
			if (subcat.getId().equals(idSubcat)) {
				posSub = i;
			}
		}
		for (int i = 0; i < listTarjetas.size(); i++) {
			Tarjeta tarjeta = listTarjetas.get(i);
			if (tarjeta.getId().equals(idTarjeta)) {
				posTar = i;
			}
		}

		spinnerCat.setSelection(posCat);
		spinnerSub.setSelection(posSub);
		spinnerTarjetas.setSelection(posTar);

		if (tipoSel.equals("3")) {
			spinnerMovimiento.setSelection(0);
		} else {
			spinnerMovimiento.setSelection(Integer.parseInt(tipoSel));
		}

		if (tipoSel.equals("1")) {
			spinnerMeses.setSelection(Integer.parseInt(mesSel) - 1);
		} else {
			descripcion.setText(conceptoSel);
		}
		DecimalFormat df = new DecimalFormat("0.00");
		if (Float.parseFloat(cantidadSel) < 0) {
			cant = df.format(Float.parseFloat(cantidadSel) * -1);
		} else {
			cant = df.format(Float.parseFloat(cantidadSel));
		}
		cantidad.setText(cant.replace(",", "."));
		mDay = Integer.parseInt(fechaSel.substring(8, 10));
		mMonth = Integer.parseInt(fechaSel.substring(5, 7));
		mYear = Integer.parseInt(fechaSel.substring(0, 4));

		mesAnterior = Integer.parseInt(mesSel);
		anioAnterior = mYear;

		if (Boolean.parseBoolean(tarjetaSel)) {
			checkTarjeta.setChecked(true);
		}
	}

	public int cuentaSeleccionada() {
		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);

		int idCuenta = prefs.getInt("cuenta", 0);
		return idCuenta;
	}
	
	// Comprobamos si debemos mostrar la publicidad o no
		public void displayInterstitial() {
			if (interstitial != null && interstitial.isLoaded()) {
				interstitial.show();
			}
		}

		public void crearAnuncio() {
			interstitial = new InterstitialAd(this);
			interstitial.setAdUnitId("ca-app-pub-2303483383476811/2195948084");

			AdRequest adRequestCompleto = new AdRequest.Builder().build();
			interstitial.loadAd(adRequestCompleto);
		}
}