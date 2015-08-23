package com.agudoApp.salaryApp.fragments;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.agudoApp.salaryApp.activities.AddCat;
import com.agudoApp.salaryApp.activities.AddSub;
import com.agudoApp.salaryApp.activities.DeleteCat;
import com.agudoApp.salaryApp.activities.DeleteSub;
import com.agudoApp.salaryApp.activities.EditCat;
import com.agudoApp.salaryApp.activities.EditSub;
import com.agudoApp.salaryApp.adapters.ListAdapterSpinner;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.general.ControlGastosActivity;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Movimiento;
import com.agudoApp.salaryApp.model.Tarjeta;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public final class MovimientosFragment extends Fragment {
	private static final String KEY_CONTENT = "MovimientosFragment:Content";
	private final String BD_NOMBRE = "BDGestionGastos";
	static final int DATE_DIALOG_ID = 0;
	static final int MENSAJE_OK = 1;
	static final int MENSAJE_KO = 2;
	static final int MENSAJE_ERROR = 3;
	static final int MENSAJE_ERROR_NOMINA = 4;
	static final int MENSAJE_ERROR_DELETE = 5;
	static final int MENSAJE_OK_DELETE = 6;
	static final int BOTON_BACK = 99;
	static final int PRO = 7;
	private int mYear;
	private int mMonth;
	private int mDay;
	private Button botonFecha;
	private Button botonCrear;
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
	private ProgressDialog progressDialog;
	// private final MyHandler handler = new MyHandler();
	LinearLayout layout;
	// private AdView adView;
	LinearLayout layoutMovimientos;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	int style;
	private final MyHandler handler = new MyHandler();
	// Productos que posee el usuario
	boolean isPremium = false;
	boolean isCategoriaPremium = false;
	boolean isSinPublicidad = false;
	boolean crearAnuncio = false;

	private InterstitialAd interstitial;

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			iniciarActivity();
			// Aqu quitamos el progress dialog
			progressDialog.dismiss();
		}
	}

	public MovimientosFragment(boolean isUserPremium,
			boolean isUserSinpublicidad, boolean isUserCategoriaPremium) {
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

		return inflater.inflate(R.layout.movimientos, container, false);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
	}

	public void obtenerCategorias() {
		ArrayList<Categoria> listCategorias = new ArrayList<Categoria>();
		db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			// Recuperamos el listado del spinner Categorias
			listCategorias = (ArrayList<Categoria>) gestion.getCategorias(db,
					"Categorias", "idCategoria");
		}
		db.close();

		// Creamos el adaptador
		ListAdapterSpinner spinner_adapterCat = new ListAdapterSpinner(
				getActivity(),android.R.layout.simple_spinner_item,
				listCategorias);

		spinnerCat.setAdapter(spinner_adapterCat);
	}

	public void obtenerSubcategorias() {
		ArrayList<Categoria> listSubcategorias = new ArrayList<Categoria>();
		db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			listSubcategorias = (ArrayList<Categoria>) gestion.getCategorias(
					db, "Subcategorias", "idSubcategoria");
		}
		db.close();

		// Creamos el adaptador
		ListAdapterSpinner spinner_adapterSubcat = new ListAdapterSpinner(
				getActivity(), android.R.layout.simple_spinner_item,
				listSubcategorias);
		spinnerSub.setAdapter(spinner_adapterSubcat);
	}

	// Rellena el spinner meses
	public void obtenerMeses() {
		final Calendar c = Calendar.getInstance();
		ArrayAdapter<CharSequence> adapterMes = ArrayAdapter
				.createFromResource(getActivity(), R.array.mesArray,
						android.R.layout.simple_spinner_item);
		adapterMes.setDropDownViewResource(R.layout.spinner);
		spinnerMeses.setAdapter(adapterMes);
		spinnerMeses.setSelection(c.get(Calendar.MONTH));
	}

	public void obtenerTarjetas() {
		ArrayList<Tarjeta> listTarjetas = new ArrayList<Tarjeta>();
		db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			// Recuperamos el listado del spinner Categorias
			listTarjetas = (ArrayList<Tarjeta>) gestion.getTarjetas(db);
		}
		db.close();
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
				getActivity(), android.R.layout.simple_spinner_item,
				listaTarjetas);
		// Aadimos el layout para el men y se lo damos al spinner
		spinner_adapterTar.setDropDownViewResource(R.layout.spinner);
		spinnerTarjetas.setAdapter(spinner_adapterTar);
	}

	// Rellena el spinner movimientos
	public void obtenerMovimientos() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.tipoEditMovimientosArray,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner);
		spinnerMovimiento.setAdapter(adapter);
	}

	private void updateDisplay() {
		botonFecha.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mDay).append("-").append(mMonth + 1).append("-")
				.append(mYear).append(" "));
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		if (!isPremium && !isSinPublicidad) {
			crearAnuncio = true;
		}

		// Asignamos el tipo de fuente
		Typeface miPropiaTypeFace = Typeface.createFromAsset(this.getActivity()
				.getAssets(), "fonts/Berlin.ttf");

		if (!isPremium && !isSinPublicidad) {
			// Look up the AdView as a resource and load a request.
			AdView adView = (AdView) this.getView().findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);
		}

		TextView txtTipo = (TextView) this.getView().findViewById(R.id.Tipo);
		txtTipo.setTypeface(miPropiaTypeFace);
		TextView txtCantidad = (TextView) this.getView().findViewById(
				R.id.cantidad);
		txtCantidad.setTypeface(miPropiaTypeFace);
		TextView txtDescripcion = (TextView) this.getView().findViewById(
				R.id.descripcion);
		txtDescripcion.setTypeface(miPropiaTypeFace);
		TextView txtFecha = (TextView) this.getView().findViewById(R.id.fecha);
		txtFecha.setTypeface(miPropiaTypeFace);
		TextView txtTextoCategoria = (TextView) this.getView().findViewById(
				R.id.textoCategoria);
		txtTextoCategoria.setTypeface(miPropiaTypeFace);
		TextView txtTextoSubCategoria = (TextView) this.getView().findViewById(
				R.id.textoSubCategoria);
		txtTextoSubCategoria.setTypeface(miPropiaTypeFace);
		TextView txtTextoPagoTarjeta = (TextView) this.getView().findViewById(
				R.id.textoPagoTarjeta);
		txtTextoPagoTarjeta.setTypeface(miPropiaTypeFace);

		spinnerCat = (Spinner) this.getView().findViewById(
				R.id.spinnerCategoria);
		spinnerSub = (Spinner) this.getView().findViewById(
				R.id.spinnerSubCategoria);
		spinnerMovimiento = (Spinner) this.getView().findViewById(R.id.spinner);
		spinnerMeses = (Spinner) this.getView().findViewById(R.id.spinnerMeses);
		// botonAddCat = (Button) this.getView().findViewById(R.id.botonAddCat);
		// botonAddSub = (Button) this.getView().findViewById(R.id.botonAddSub);
		botonFecha = (Button) this.getView().findViewById(R.id.botonFecha);
		spinnerTarjetas = (Spinner) this.getView().findViewById(
				R.id.spinnerTarjetas);
		cantidad = (EditText) this.getView().findViewById(R.id.cajaCantidad);
		descripcion = (EditText) this.getView().findViewById(
				R.id.cajaDescripcion);
		checkTarjeta = (CheckBox) this.getView()
				.findViewById(R.id.checkTarjeta);
		botonFecha = (Button) this.getView().findViewById(R.id.botonFecha);
		botonCrear = (Button) this.getView().findViewById(R.id.botonCrear);
		layoutMovimientos = (LinearLayout) this.getView().findViewById(
				R.id.layoutMovimientos);

		iniciarActivity();

		db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			// Comprobamos si existen las tablas
			tablasCreadas = gestion.comprobarTablas(db);
		}
		db.close();
		if (!tablasCreadas) {
			progressDialog = ProgressDialog.show(getActivity(), getResources()
					.getString(R.string.espere),
					getResources().getString(R.string.preparandoBD), true,
					false);

			// Se crean las tablas y registros si no existen
			new Thread() {
				public void run() {
					try {
						Thread.sleep(5000);
						db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1,
								null);
						if (db != null) {
							gestion.createTables(db);
						}
						db.close();
						handler.sendEmptyMessage(0);
					} catch (Exception e) {
					}
				}
			}.start();
		}

		// Funcionalidad boton fecha
		botonFecha.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onCreateDialog(DATE_DIALOG_ID);
			}
		});

		// Evento al seleccionar un tipo de movimiento
		spinnerMovimiento
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						ArrayList<Categoria> listCategorias = new ArrayList<Categoria>();
						if (position == 1) {
							int posi = 0;
							descripcion.setVisibility(8);
							spinnerMeses.setVisibility(0);

							db = getActivity().openOrCreateDatabase(BD_NOMBRE,
									1, null);
							if (db != null) {
								listCategorias = (ArrayList<Categoria>) gestion
										.getCategorias(db, "Categorias",
												"idCategoria");
							}
							db.close();

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
									.setBackgroundResource(R.drawable.spinner_gris);
						} else if (position == 3) {
							onCreateDialog(PRO);
							iniciarActivity();
							// Intent intent = new Intent(getActivity(),
							// Recibos.class);
							// startActivity(intent);
							// spinnerMovimiento.setSelection(0);
						} else {
							descripcion.setVisibility(0);
							spinnerMeses.setVisibility(8);
							spinnerCat.setSelection(0);
							spinnerSub.setSelection(0);
							if (position == 2) {
								checkTarjeta.setClickable(false);
								checkTarjeta.setChecked(false);
								spinnerTarjetas.setEnabled(false);
								spinnerTarjetas
										.setBackgroundResource(R.drawable.spinner_gris);
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
					spinnerTarjetas.setBackgroundResource(R.drawable.spinner_azul);
				} else {
					spinnerTarjetas.setEnabled(false);
					spinnerTarjetas
							.setBackgroundResource(R.drawable.spinner_gris);
				}
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
				Date fecha = new Date(mYear - 1900, mMonth, mDay);
				if ("".equals(cantidad.getText().toString())
						|| ".".equals(cantidad.getText().toString())
						|| Float.parseFloat(cantidad.getText().toString()) <= 0) {
					error = true;
				} else {
					if (tipo == 0) {
						cant = -1
								* Float.parseFloat(cantidad.getText()
										.toString());
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
					ArrayList<Movimiento> listNominas = new ArrayList<Movimiento>();
					int idCuenta = cuentaSeleccionada();
					db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
					if (db != null) {
						listNominas = gestion.getNominas(db, idCuenta);
					}
					db.close();
					for (int i = 0; i < listNominas.size(); i++) {
						Movimiento mov = listNominas.get(i);
						if (mov.getMes().equals(String.valueOf(mes + 1))
								&& (mov.getAnio().equals(String.valueOf(anio)))) {
							errorNomina = true;
							break;
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
					boolean ok = false;
					int idCuenta = cuentaSeleccionada();
					db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
					if (db != null) {
						ok = gestion
								.insertarMovimiento(db, tipo, cant, desc,
										fecha, idCategoria, idSubcategoria,
										recibo, tarjeta, mes + 1, anio,
										idTarjeta, 99, idCuenta);
					}
					db.close();
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

	public void aplicarEstilo() {
		switch (style) {
		case 1:
			layoutMovimientos.setBackgroundResource(R.drawable.layout_azul);
			break;
		case 2:
			layoutMovimientos.setBackgroundResource(R.drawable.layout_rojo);
			break;
		case 3:
			layoutMovimientos.setBackgroundResource(R.drawable.layout_naranja);
			break;
		case 4:
			layoutMovimientos.setBackgroundResource(R.drawable.layout_verde);
			break;
		case 5:
			layoutMovimientos.setBackgroundResource(R.drawable.layout_rosa);
			break;
		default:
			layoutMovimientos.setBackgroundResource(R.drawable.layout_azul);
			break;
		}
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

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

	public void iniciarActivity() {
		if (crearAnuncio && !isPremium && !isSinPublicidad) {
			crearAnuncio();
		}
		// aplicarEstilo();
		obtenerMovimientos();
		obtenerMeses();
		obtenerCategorias();
		obtenerSubcategorias();
		obtenerTarjetas();

		// Obtiene la fecha de hoy
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// Actualiza el texto del boton fecha
		updateDisplay();

		cantidad.setText("");
		descripcion.setText("");
		checkTarjeta.setChecked(false);
		spinnerTarjetas.setEnabled(false);
		spinnerTarjetas.setBackgroundResource(R.drawable.spinner_gris);

	}

	protected Dialog onCreateDialog(int id) {
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		AlertDialog alert;
		switch (id) {
		case DATE_DIALOG_ID:
			DatePickerDialog datadialog = new DatePickerDialog(
					this.getActivity(), mDateSetListener, mYear, mMonth, mDay);
			datadialog.show();
			break;
		case MENSAJE_OK:
			builder.setMessage(
					getResources().getString(R.string.guardarRegistroOK))
					.setTitle(getResources().getString(R.string.info))
					.setIcon(R.drawable.ic_info_azul)
					.setPositiveButton(
							getResources().getString(R.string.aceptar),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									displayInterstitial();
									iniciarActivity();
								}
							});
			alert = builder.create();
			alert.show();
			break;
		case MENSAJE_KO:
			builder.setMessage(
					getResources().getString(R.string.guardarRegistroKO))
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
		case PRO:
			LayoutInflater li = LayoutInflater.from(getActivity());
			View view = null;
			view = li.inflate(R.layout.pro, null);
			builder.setView(view);
			builder.setTitle(getResources().getString(R.string.versionPro));
			builder.setIcon(R.drawable.icon_app_pro);
			builder.setCancelable(false);
			builder.setPositiveButton(
					getResources().getString(R.string.botonDescargar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent intent1 = null;
							intent1 = new Intent(
									"android.intent.action.VIEW",
									Uri.parse("https://play.google.com/store/apps/details?id=com.agba.salaryControlPro"));
							startActivity(intent1);
							dialog.cancel();
						}
					}).setNegativeButton(
					getResources().getString(R.string.botonMasTarde),
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

	@Override
	public boolean onContextItemSelected(MenuItem aItem) {
		/* Switch on the ID of the item, to get what the user selected. */
		switch (aItem.getItemId()) {
		case R.id.opcion1:
			if (opcionesCategoria) {
				Intent inCat = new Intent(getActivity(), AddCat.class);
				this.startActivityForResult(inCat, 0);
			} else if (opcionesSubcategoria) {
				Intent inCat = new Intent(getActivity(), AddSub.class);
				this.startActivityForResult(inCat, 0);
			}
			opcionesCategoria = false;
			opcionesSubcategoria = false;
			return true;
		case R.id.opcion2:
			if (opcionesCategoria) {
				Intent inCat = new Intent(getActivity(), EditCat.class);
				this.startActivityForResult(inCat, 0);
			} else if (opcionesSubcategoria) {
				Intent inCat = new Intent(getActivity(), EditSub.class);
				this.startActivityForResult(inCat, 0);
			}
			opcionesCategoria = false;
			opcionesSubcategoria = false;
			return true;
		case R.id.opcion3:
			if (opcionesCategoria) {
				Intent inCat = new Intent(getActivity(), DeleteCat.class);
				this.startActivityForResult(inCat, 0);
			} else if (opcionesSubcategoria) {
				Intent inSub = new Intent(getActivity(), DeleteSub.class);
				this.startActivityForResult(inSub, 0);
			}
			opcionesCategoria = false;
			opcionesSubcategoria = false;
			return true;
		}
		return false;
	}

	// Inicializa los campos al crear un registro
	public void inicializar() {
		Intent intent = new Intent(getActivity(), ControlGastosActivity.class);
		getActivity().finish();
		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.menu_pulsacion_corta, menu);
		menu.setHeaderTitle(getResources().getString(R.string.elijaOpcion));

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			if (tablasCreadas) {
				// iniciarActivity();
			}
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		spinnerMovimiento.setSelection(0);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		// this.getActivity().finish();
		super.onDestroy();
	}

	public int cuentaSeleccionada() {
		prefs = getActivity().getSharedPreferences("ficheroConf",
				Context.MODE_PRIVATE);

		int idCuenta = prefs.getInt("cuenta", 0);
		return idCuenta;
	}

	public void crearAnuncio() {
		interstitial = new InterstitialAd(getActivity());
		interstitial.setAdUnitId("ca-app-pub-2303483383476811/6338442889");

		AdRequest adRequestCompleto = new AdRequest.Builder().build();
		interstitial.loadAd(adRequestCompleto);
		crearAnuncio = false;
	}

	// Comprobamos si debemos mostrar la publicidad o no
	public void displayInterstitial() {
		if (interstitial != null && interstitial.isLoaded() && !isPremium
				&& !isSinPublicidad && mostrarAnuncioCompleto()) {
			interstitial.show();
			crearAnuncio = true;
		}
	}

	// Comprobamos si debemos mostrar el anuncio completo o no
	public boolean mostrarAnuncioCompleto() {
		boolean mostrarAnuncio = false;

		int contadorPubli = 0;
		int ultimoNumPubli = 0;

		if (getActivity() != null) {
			prefs = getActivity().getSharedPreferences("ficheroConf",
					Context.MODE_PRIVATE);
			editor = prefs.edit();
		}

		contadorPubli = prefs.getInt("contadorPubli", 0);
		ultimoNumPubli = prefs.getInt("ultimoNumPubli", 0);
		contadorPubli++;

		if (contadorPubli == (ultimoNumPubli + 5)) {
			editor.putInt("ultimoNumPubli", ultimoNumPubli + 5);
			mostrarAnuncio = true;
		} else {
			mostrarAnuncio = false;
		}

		editor.putInt("contadorPubli", contadorPubli);
		editor.commit();

		return mostrarAnuncio;
	}

}
