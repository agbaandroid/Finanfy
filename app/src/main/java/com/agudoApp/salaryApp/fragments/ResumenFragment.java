package com.agudoApp.salaryApp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.EditMovimiento;
import com.agudoApp.salaryApp.activities.NuevoActivity;
import com.agudoApp.salaryApp.adapters.ListAdapter;
import com.agudoApp.salaryApp.adapters.ListaAdapterResumenExpandibleAdapter;
import com.agudoApp.salaryApp.adapters.ListaAdapterResumenExpandibleSubAdapter;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Movimiento;
import com.agudoApp.salaryApp.model.Recibo;
import com.agudoApp.salaryApp.model.Tarjeta;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Locale;

public final class ResumenFragment extends Fragment {
	private static final String KEY_CONTENT = "ResumenFragment:Content";
	private final String BD_NOMBRE = "BDGestionGastos";
	static final int MENSAJE_OK = 1;
	static final int MENSAJE_ERROR = 2;
	static final int MENSAJE_ERROR_DELETE = 5;
	static final int MENSAJE_OK_DELETE = 6;
	static final int MENSAJE_CONFIRMAR_ELIMINAR = 7;
	static final int NUEVO_REGISTRO = 1;
	static final int BOTON_BACK = 99;
	private Spinner spinnerNomina;
	private Spinner spinnerAnios;
	private Spinner spinnerTarjetas;
	private Spinner spinnerFiltro;
	private TextView textTotal;
	protected ListView listMovView;
	protected ExpandableListView listMovCatView;
	private int mYear;
	private int mMonth;
	private int mes;
	private int anio;
	private float total;
	private boolean onlyCards = false;
	boolean allYears = false;
	private int idTarjeta = 99;
	final GestionBBDD gestion = new GestionBBDD();
	private SQLiteDatabase db;
	boolean inicializarNomina = true;
	boolean inicializarAnios = true;
	boolean mostrarNominas = false;
	boolean mostrarPorCategorias = false;
	int tipoFiltro = 0;
	private CheckBox checkTarjeta;
	Movimiento movSeleccionado = new Movimiento();
	private ArrayList<Categoria> listGroupChild = new ArrayList<Categoria>();
	private ArrayList<Movimiento> listClasica = new ArrayList<Movimiento>();
	LinearLayout layoutSinRegistro;

	int style;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;

	// Productos que posee el usuario
	boolean isPremium = false;
	boolean isCategoriaPremium = false;
	boolean isSinPublicidad = false;

	private String mContent = "???";

	public ResumenFragment(boolean isUserPremium, boolean isUserSinpublicidad,
			boolean isUserCategoriaPremium) {
		isPremium = isUserPremium;
		isCategoriaPremium = isUserCategoriaPremium;
		isSinPublicidad = isUserSinpublicidad;
	}

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
		return inflater.inflate(R.layout.resumen, container, false);
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

		// Asignamos el tipo de fuente
		Typeface miPropiaTypeFace = Typeface.createFromAsset(this.getActivity()
				.getAssets(), "fonts/Berlin.ttf");

		TextView textFiltroTarjeta = (TextView) this.getView().findViewById(
				R.id.textFiltroTarjeta);
		textFiltroTarjeta.setTypeface(miPropiaTypeFace);

		spinnerNomina = (Spinner) this.getView().findViewById(
				R.id.spinnerResumenNomina);
		spinnerAnios = (Spinner) this.getView().findViewById(
				R.id.spinnerResumenAnio);
		spinnerTarjetas = (Spinner) this.getView().findViewById(
				R.id.spinnerResumenFiltroTarjeta);
		spinnerFiltro = (Spinner) this.getView().findViewById(
				R.id.spinnerFiltro);
		listMovView = (ListView) this.getView().findViewById(
				R.id.listaMovimientos);
		listMovCatView = (ExpandableListView) this.getView().findViewById(
				R.id.listaMovimientosCategoria);
		textTotal = (TextView) this.getView().findViewById(R.id.textTotal);

		layoutSinRegistro = (LinearLayout) this.getView().findViewById(
				R.id.layoutSinRegistro);

		checkTarjeta = (CheckBox) this.getView()
				.findViewById(R.id.checkTarjeta);
		checkTarjeta.setChecked(false);
		spinnerTarjetas.setEnabled(false);
		spinnerTarjetas.setBackgroundResource(R.drawable.spinner_gris);

		registerForContextMenu(listMovCatView);
		registerForContextMenu(listMovView);

		cargarPreferencias();

		ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
		listMov = inicializarResumen();
		rellenarListaExpansibleCategorias(listMov);
		calcularTotal(listMov);

		spinnerNomina
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						mes = position;
						if (!inicializarNomina) {
							ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
							int idCuenta = cuentaSeleccionada();
							db = getActivity().openOrCreateDatabase(BD_NOMBRE,
									1, null);
							if (db != null) {
								if (onlyCards) {
									if (!allYears) {
										if (mostrarNominas) {
											listMov = gestion
													.getMovimientosTarjeta(db,
															mes, anio,
															idTarjeta, idCuenta);
										} else {
											listMov = gestion
													.getMovimientosTarjetaInicio(
															db, idTarjeta, mes,
															anio, idCuenta);
										}
									} else {
										listMov = gestion
												.getTodosMovimientosTarjeta(db,
														idTarjeta, idCuenta);
									}
								} else {
									if (!allYears) {
										if (mostrarNominas) {
											listMov = gestion.getMovimientos(
													db, mes, anio, idCuenta);
										} else {
											listMov = gestion
													.getMovimientosInicioMes(
															db, mes, anio,
															idCuenta);
										}
									} else {
										listMov = gestion.getTodosMovimientos(
												db, idCuenta);
									}
								}
							}
							db.close();
							if (listMov.isEmpty()) {
								layoutSinRegistro.setVisibility(View.VISIBLE);
							} else {
								layoutSinRegistro.setVisibility(View.GONE);
								if (tipoFiltro == 0) {
									rellenarLista(listMov);
									listMovView.setVisibility(0);
									listMovCatView.setVisibility(8);
								} else if (tipoFiltro == 1) {
									rellenarListaExpansibleCategorias(listMov);
									listMovView.setVisibility(8);
									listMovCatView.setVisibility(0);
								} else if (tipoFiltro == 2) {
									rellenarListaExpansibleSubcategorias(listMov);
									listMovView.setVisibility(8);
									listMovCatView.setVisibility(0);
								}
							}
							// rellenarListaExpansibleCategorias(listMov);
							calcularTotal(listMov);
						}
						inicializarNomina = false;
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

						if (mov.toString().equals("-")) {
							anio = 0;
							allYears = true;
						} else {
							anio = Integer.parseInt(mov.toString());
							allYears = false;
						}

						if (!inicializarNomina) {
							ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
							int idCuenta = cuentaSeleccionada();
							db = getActivity().openOrCreateDatabase(BD_NOMBRE,
									1, null);
							if (db != null) {
								if (!allYears) {
									spinnerNomina.setEnabled(true);
									spinnerNomina
											.setBackgroundResource(R.drawable.spinner);
									if (onlyCards) {
										if (mostrarNominas) {
											listMov = gestion
													.getMovimientosTarjeta(db,
															mes, anio,
															idTarjeta, idCuenta);
										} else {
											listMov = gestion
													.getMovimientosTarjetaInicio(
															db, idTarjeta, mes,
															anio, idCuenta);
										}
									} else {
										if (mostrarNominas) {
											listMov = gestion.getMovimientos(
													db, mes, anio, idCuenta);
										} else {
											listMov = gestion
													.getMovimientosInicioMes(
															db, mes, anio,
															idCuenta);
										}
									}
								} else {
									spinnerNomina.setEnabled(false);
									spinnerNomina
											.setBackgroundResource(R.drawable.spinner_gris);
									spinnerNomina.setSelection(0);
									if (onlyCards) {
										listMov = gestion
												.getTodosMovimientosTarjeta(db,
														idTarjeta, idCuenta);
									} else {
										listMov = gestion.getTodosMovimientos(
												db, idCuenta);
									}
								}
							}
							db.close();
							if (listMov.isEmpty()) {
								layoutSinRegistro.setVisibility(View.VISIBLE);
							} else {
								layoutSinRegistro.setVisibility(View.GONE);
								if (tipoFiltro == 0) {
									rellenarLista(listMov);
									listMovView.setVisibility(0);
									listMovCatView.setVisibility(8);
								} else if (tipoFiltro == 1) {
									rellenarListaExpansibleCategorias(listMov);
									listMovView.setVisibility(8);
									listMovCatView.setVisibility(0);
								} else if (tipoFiltro == 2) {
									rellenarListaExpansibleSubcategorias(listMov);
									listMovView.setVisibility(8);
									listMovCatView.setVisibility(0);
								}
							}
							// rellenarListaExpansibleCategorias(listMov);
							calcularTotal(listMov);
						}
						inicializarNomina = false;
					}

					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		spinnerTarjetas
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						int posTarjeta = spinnerTarjetas
								.getSelectedItemPosition();
						Tarjeta tarjetaAux = (Tarjeta) spinnerTarjetas
								.getItemAtPosition(posTarjeta);
						// Obtenemos el id de los objetos seleccionados
						idTarjeta = Integer.parseInt(tarjetaAux.getId());

						if (!inicializarNomina) {
							ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
							int idCuenta = cuentaSeleccionada();
							db = getActivity().openOrCreateDatabase(BD_NOMBRE,
									1, null);
							if (db != null) {
								if (!allYears) {
									spinnerNomina.setEnabled(true);
									spinnerNomina
											.setBackgroundResource(R.drawable.spinner);
									if (onlyCards) {
										if (mostrarNominas) {
											listMov = gestion
													.getMovimientosTarjeta(db,
															mes, anio,
															idTarjeta, idCuenta);
										} else {
											listMov = gestion
													.getMovimientosTarjetaInicio(
															db, idTarjeta, mes,
															anio, idCuenta);
										}
									} else {
										if (mostrarNominas) {
											listMov = gestion.getMovimientos(
													db, mes, anio, idCuenta);
										} else {
											listMov = gestion
													.getMovimientosInicioMes(
															db, mes, anio,
															idCuenta);
										}
									}
								} else {
									spinnerNomina.setEnabled(false);
									spinnerNomina
											.setBackgroundResource(R.drawable.spinner_gris);
									spinnerNomina.setSelection(0);
									if (onlyCards) {
										listMov = gestion
												.getTodosMovimientosTarjeta(db,
														idTarjeta, idCuenta);
									} else {
										listMov = gestion.getTodosMovimientos(
												db, idCuenta);
									}
								}
							}
							db.close();
							if (listMov.isEmpty()) {
								layoutSinRegistro.setVisibility(View.VISIBLE);
							} else {
								layoutSinRegistro.setVisibility(View.GONE);
								if (tipoFiltro == 0) {
									rellenarLista(listMov);
									listMovView.setVisibility(0);
									listMovCatView.setVisibility(8);
								} else if (tipoFiltro == 1) {
									rellenarListaExpansibleCategorias(listMov);
									listMovView.setVisibility(8);
									listMovCatView.setVisibility(0);
								} else if (tipoFiltro == 2) {
									rellenarListaExpansibleSubcategorias(listMov);
									listMovView.setVisibility(8);
									listMovCatView.setVisibility(0);
								}
							}
							// rellenarListaExpansibleCategorias(listMov);
							calcularTotal(listMov);
						}
						inicializarNomina = false;
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
					onlyCards = true;
				} else {
					spinnerTarjetas.setEnabled(false);
					spinnerTarjetas
							.setBackgroundResource(R.drawable.spinner_gris);
					onlyCards = false;
				}

				if (!inicializarNomina) {
					ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
					int idCuenta = cuentaSeleccionada();
					db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
					if (db != null) {
						if (onlyCards) {
							if (!allYears) {
								if (mostrarNominas) {
									listMov = gestion.getMovimientosTarjeta(db,
											mes, anio, idTarjeta, idCuenta);
								} else {
									listMov = gestion
											.getMovimientosTarjetaInicio(db,
													idTarjeta, mes, anio,
													idCuenta);
								}
							} else {
								listMov = gestion.getTodosMovimientosTarjeta(
										db, idTarjeta, idCuenta);
							}
						} else {
							if (!allYears) {
								if (mostrarNominas) {
									listMov = gestion.getMovimientos(db, mes,
											anio, idCuenta);
								} else {
									listMov = gestion.getMovimientosInicioMes(
											db, mes, anio, idCuenta);
								}
							} else {
								listMov = gestion.getTodosMovimientos(db,
										idCuenta);
							}
						}
					}
					db.close();

					if (listMov.isEmpty()) {
						layoutSinRegistro.setVisibility(View.VISIBLE);
					} else {
						layoutSinRegistro.setVisibility(View.GONE);
						if (tipoFiltro == 0) {
							rellenarLista(listMov);
							listMovView.setVisibility(0);
							listMovCatView.setVisibility(8);
						} else if (tipoFiltro == 1) {
							rellenarListaExpansibleCategorias(listMov);
							listMovView.setVisibility(8);
							listMovCatView.setVisibility(0);
						} else if (tipoFiltro == 2) {
							rellenarListaExpansibleSubcategorias(listMov);
							listMovView.setVisibility(8);
							listMovCatView.setVisibility(0);
						}
					}
					// rellenarListaExpansibleCategorias(listMov);
					calcularTotal(listMov);
				}
				inicializarNomina = false;
			}
		});

		spinnerFiltro
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						tipoFiltro = spinnerFiltro.getSelectedItemPosition();

						if (!inicializarNomina) {
							ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
							int idCuenta = cuentaSeleccionada();
							db = getActivity().openOrCreateDatabase(BD_NOMBRE,
									1, null);
							if (db != null) {
								if (!allYears) {
									spinnerNomina.setEnabled(true);
									spinnerNomina
											.setBackgroundResource(R.drawable.spinner);
									if (onlyCards) {
										if (mostrarNominas) {
											listMov = gestion
													.getMovimientosTarjeta(db,
															mes, anio,
															idTarjeta, idCuenta);
										} else {
											listMov = gestion
													.getMovimientosTarjetaInicio(
															db, idTarjeta, mes,
															anio, idCuenta);
										}
									} else {
										if (mostrarNominas) {
											listMov = gestion.getMovimientos(
													db, mes, anio, idCuenta);
										} else {
											listMov = gestion
													.getMovimientosInicioMes(
															db, mes, anio,
															idCuenta);
										}
									}
								} else {
									spinnerNomina.setEnabled(false);
									spinnerNomina
											.setBackgroundResource(R.drawable.spinner_gris);
									spinnerNomina.setSelection(0);
									if (onlyCards) {
										listMov = gestion
												.getTodosMovimientosTarjeta(db,
														idTarjeta, idCuenta);
									} else {
										listMov = gestion.getTodosMovimientos(
												db, idCuenta);
									}
								}
							}
							db.close();

							if (listMov.isEmpty()) {
								layoutSinRegistro.setVisibility(View.VISIBLE);
							} else {
								layoutSinRegistro.setVisibility(View.GONE);
								if (tipoFiltro == 0) {
									rellenarLista(listMov);
									listMovView.setVisibility(0);
									listMovCatView.setVisibility(8);
								} else if (tipoFiltro == 1) {
									rellenarListaExpansibleCategorias(listMov);
									listMovView.setVisibility(8);
									listMovCatView.setVisibility(0);
								} else if (tipoFiltro == 2) {
									rellenarListaExpansibleSubcategorias(listMov);
									listMovView.setVisibility(8);
									listMovCatView.setVisibility(0);
								}
							}
							// rellenarListaExpansibleCategorias(listMov);
							calcularTotal(listMov);
						}
						inicializarNomina = false;
					}

					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		listMovCatView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				Categoria cat = listGroupChild.get(groupPosition);
				Movimiento mov = cat.getListaMovimientos().get(childPosition);
				guardarMovimientoSeleccionado(mov.getId());
				getActivity().openContextMenu(v);
				return true;
			}
		});

		listMovView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Movimiento mov = listClasica.get(position);
				guardarMovimientoSeleccionado(mov.getId());
				getActivity().openContextMenu(view);
			}
		});
	}

	// Rellena el spinner meses
	public ArrayList<Movimiento> inicializarResumen() {
		obtenerTarjetas();
		insertarRecibos();
		ArrayList<Movimiento> listaAnios = new ArrayList<Movimiento>();
		ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
		LinkedList<Movimiento> listAnios = new LinkedList<Movimiento>();
		ArrayList<Movimiento> listaAniosBBDD = new ArrayList<Movimiento>();
		int idCuenta = cuentaSeleccionada();

		db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			listaAniosBBDD = gestion.getAnios(db, idCuenta);
		}
		db.close();
		obternerAnio();

		// Rellenamos el spinner de nominas
		ArrayAdapter<CharSequence> adapterMes = ArrayAdapter
				.createFromResource(getActivity(), R.array.nominasArray,
						android.R.layout.simple_spinner_item);
		adapterMes.setDropDownViewResource(R.layout.spinner);
		spinnerNomina.setAdapter(adapterMes);

		ArrayAdapter<CharSequence> adapterFiltro = ArrayAdapter
				.createFromResource(getActivity(), R.array.tipoFiltroResumen,
						android.R.layout.simple_spinner_item);
		adapterFiltro.setDropDownViewResource(R.layout.spinner);
		spinnerFiltro.setAdapter(adapterFiltro);

		if (listaAniosBBDD.size() > 0) {
			// Rellenamos la lista con los aos a mostrar

			listaAnios = listaAniosBBDD;
			// Ordenamos la lista de aos
			Collections.sort(listaAnios, new YearsComparator());

			// Todos los anios
			Movimiento movAll = new Movimiento();
			movAll.setDescripcion("-");
			listAnios.add(movAll);

			// rellenamos el listado del combo de aos
			for (int i = 0; i < listaAnios.size(); i++) {
				Movimiento mov = new Movimiento();
				mov.setDescripcion(listaAnios.get(i).getAnio());
				listAnios.add(mov);
			}

			// Rellenamos el spinner de aos
			ArrayAdapter<Movimiento> spinner_adapterAnios = new ArrayAdapter<Movimiento>(
					getActivity(), android.R.layout.simple_spinner_item,
					listAnios);
			// Aadimos el layout para el men y se lo damos al spinner
			spinner_adapterAnios.setDropDownViewResource(R.layout.spinner);
			spinnerAnios.setAdapter(spinner_adapterAnios);
		} else {
			// Todos los anios
			Movimiento movAll = new Movimiento();
			movAll.setDescripcion("-");
			listAnios.add(movAll);

			Movimiento anio = new Movimiento();
			anio.setId(String.valueOf(mYear));
			anio.setDescripcion(String.valueOf(mYear));
			listAnios.add(anio);
			// Rellenamos el spinner de aos
			ArrayAdapter<Movimiento> spinner_adapterAnios = new ArrayAdapter<Movimiento>(
					getActivity(), android.R.layout.simple_spinner_item,
					listAnios);
			// Aadimos el layout para el men y se lo damos al spinner
			spinner_adapterAnios.setDropDownViewResource(R.layout.spinner);
			spinnerAnios.setAdapter(spinner_adapterAnios);
			spinnerAnios.setSelection(0);
		}

		if (listaAnios.size() > 0) {
			// Seleccionamos el ao correcto
			for (int i = 0; i < listaAnios.size(); i++) {
				Movimiento mov = listaAnios.get(i);
				if (String.valueOf(mYear).equals(mov.getAnio())) {
					spinnerAnios.setSelection(i + 1);
				}
			}
		} else {
			spinnerAnios.setSelection(1);
		}

		spinnerNomina.setSelection(mMonth + 1);

		db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			if (mostrarNominas) {
				listMov = gestion.getMovimientosInicioNominas(db, idCuenta);
			} else {
				listMov = gestion.getMovimientosInicioMes(db, mes, anio,
						idCuenta);
			}
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

		if (mostrarPorCategorias) {
			tipoFiltro = 1;
			listMovView.setVisibility(8);
			listMovCatView.setVisibility(0);
		} else {
			tipoFiltro = 0;
			listMovView.setVisibility(0);
			listMovCatView.setVisibility(8);
		}
		spinnerFiltro.setSelection(tipoFiltro);
		return listMov;
	}

	public void obternerAnio() {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
	}

	public float obtenerTotal(ArrayList<Movimiento> list) {
		float total = 0;
		for (int i = 0; i < list.size(); i++) {
			Movimiento mov = new Movimiento();
			mov = list.get(i);
			total = total + Float.parseFloat(mov.getCantidad());
		}
		return total;
	}

	public void calcularTotal(ArrayList<Movimiento> lista) {
		// Calculamos el total
		total = 0;
		total = obtenerTotal(lista);
		// Rellenamos el dato con el total
		DecimalFormat df = new DecimalFormat("0.00");
		textTotal.setText("Total: " + df.format(total));
		if (total > 0) {
			textTotal.setTextColor(Color.argb(255, 98, 186, 14));
			// textTotal.setTextColor(Color.GREEN);
		} else {
			textTotal.setTextColor(Color.RED);
		}
	}

	public void insertarRecibos() {
		Calendar c = Calendar.getInstance();
		int mYearAc = c.get(Calendar.YEAR);
		int mMonthAc = c.get(Calendar.MONTH) + 1;
		int mDayAc = c.get(Calendar.DAY_OF_MONTH);
		Date fechaAc = new Date(mYearAc - 1900, mMonthAc - 1, mDayAc);
		int idCuenta = cuentaSeleccionada();

		ArrayList<Recibo> recibos = null;
		db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			recibos = (ArrayList<Recibo>) gestion.getRecibos(db, idCuenta);
		}
		db.close();
		for (int i = 0; i < recibos.size(); i++) {
			Recibo recibo = recibos.get(i);
			int anioDesdeRecibo = Integer.parseInt(recibo.getFechaIni()
					.substring(0, 4));
			int diaDesdeRecibo = Integer.parseInt(recibo.getFechaIni()
					.substring(8, 10));
			int mesDesdeRecibo = Integer.parseInt(recibo.getFechaIni()
					.substring(5, 7));
			Date fechaDesdeRecibo = new Date(anioDesdeRecibo - 1900,
					mesDesdeRecibo - 1, diaDesdeRecibo);

			int anioHastaRecibo = Integer.parseInt(recibo.getFechaFin()
					.substring(0, 4));
			int diaHastaRecibo = Integer.parseInt(recibo.getFechaFin()
					.substring(8, 10));
			int mesHastaRecibo = Integer.parseInt(recibo.getFechaFin()
					.substring(5, 7));
			Date fechaHastaRecibo = new Date(anioHastaRecibo - 1900,
					mesHastaRecibo - 1, diaHastaRecibo);

			int idRecibo = Integer.parseInt(recibo.getId());

			for (int j = anioDesdeRecibo; j <= mYearAc; j++) {
				if (j < mYearAc) {
					int desdeMes = 0;
					if (j == anioDesdeRecibo) {
						desdeMes = mesDesdeRecibo;
					} else {
						desdeMes = 1;
					}
					for (int k = desdeMes; k <= 12; k++) {

						Date fechaIniMovimiento = new Date(j - 1900, k - 1, 1);
						Date fechaFinMovimiento = null;
						db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1,
								null);
						if (db != null) {
							fechaFinMovimiento = gestion.getFinMes(k, j);
						}
						db.close();

						boolean isRegistrado = false;
						db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1,
								null);
						if (db != null) {
							isRegistrado = gestion.getReciboId(db, idRecibo,
									fechaIniMovimiento, fechaFinMovimiento);
						}
						if (!isRegistrado) {
							Date fechaRegistro = new Date(j - 1900, k - 1,
									diaDesdeRecibo);
							if (fechaRegistro.before(fechaAc)
									|| fechaRegistro.equals(fechaAc)) {
								gestion.insertarMovimiento(
										db,
										3,
										Float.parseFloat(recibo.getCantidad()),
										recibo.getDescripcion(),
										fechaRegistro,
										Integer.parseInt(recibo
												.getIdCategoria()),
										Integer.parseInt(recibo
												.getIdSubcategoria()),
										true,
										recibo.isTarjeta(),
										k,
										j,
										Integer.parseInt(recibo.getIdTarjeta()),
										Integer.parseInt(recibo.getId()),
										idCuenta);
							}
						}
						db.close();
					}
				} else {
					for (int k = 1; k <= mMonthAc; k++) {
						Date fechaIniMovimiento = new Date(mYearAc - 1900,
								k - 1, 1);
						Date fechaFinMovimiento = null;
						db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1,
								null);
						if (db != null) {
							fechaFinMovimiento = gestion.getFinMes(k, mYearAc);
						}
						db.close();

						if (fechaDesdeRecibo.before(fechaIniMovimiento)
								&& fechaHastaRecibo.after(fechaFinMovimiento)) {
							boolean isRegistrado = false;
							db = getActivity().openOrCreateDatabase(BD_NOMBRE,
									1, null);
							if (db != null) {
								isRegistrado = gestion.getReciboId(db,
										idRecibo, fechaIniMovimiento,
										fechaFinMovimiento);
							}
							if (!isRegistrado) {
								Date fechaRegistro = new Date(j - 1900, k - 1,
										diaDesdeRecibo);
								if (fechaRegistro.before(fechaAc)
										|| fechaRegistro.equals(fechaAc)) {
									gestion.insertarMovimiento(db, 3, Float
											.parseFloat(recibo.getCantidad()),
											recibo.getDescripcion(),
											fechaRegistro, Integer
													.parseInt(recibo
															.getIdCategoria()),
											Integer.parseInt(recibo
													.getIdSubcategoria()),
											true, recibo.isTarjeta(), k, j,
											Integer.parseInt(recibo
													.getIdTarjeta()), Integer
													.parseInt(recibo.getId()),
											idCuenta);
								}
							}
							db.close();
						} else {
							if (mesDesdeRecibo == k) {
								if (fechaDesdeRecibo.before(fechaAc)) {
									db = getActivity().openOrCreateDatabase(
											BD_NOMBRE, 1, null);
									if (db != null) {
										Date fechaI = new Date(j - 1900,
												mesDesdeRecibo - 1, 1);
										Date fechaF = gestion.getFinMes(
												mesDesdeRecibo, j);
										boolean isRegistrado = gestion
												.getReciboId(db, idRecibo,
														fechaI, fechaF);
										if (!isRegistrado) {
											Date fechaRegistro = new Date(
													j - 1900, k - 1,
													diaDesdeRecibo);
											gestion.insertarMovimiento(
													db,
													3,
													Float.parseFloat(recibo
															.getCantidad()),
													recibo.getDescripcion(),
													fechaRegistro,
													Integer.parseInt(recibo
															.getIdCategoria()),
													Integer.parseInt(recibo
															.getIdSubcategoria()),
													true, recibo.isTarjeta(),
													k, j,
													Integer.parseInt(recibo
															.getIdTarjeta()),
													Integer.parseInt(recibo
															.getId()), idCuenta);
										}
									}
									db.close();
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_setting, menu);
	}

	// Aadiendo funcionalidad a las opciones de men
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			Intent intent = new Intent(getActivity(),  NuevoActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_filter:
			return true;
		case android.R.id.home:
			getActivity().finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem aItem) {
		/* Switch on the ID of the item, to get what the user selected. */
		switch (aItem.getItemId()) {
		case R.id.opc1:
			movSeleccionado = obtenerMovimientoSeleccionado();
			onCreateDialog(MENSAJE_CONFIRMAR_ELIMINAR);
			return true;
		case R.id.opc2:
			// llamar activity EditMovimiento
			movSeleccionado = obtenerMovimientoSeleccionado();
			Intent inEditMov = new Intent(getActivity(), EditMovimiento.class);
			inEditMov.putExtra("idMovimiento", movSeleccionado.getId());
			inEditMov.putExtra("tipo", movSeleccionado.getTipo());
			inEditMov.putExtra("cantidad", movSeleccionado.getCantidad());
			inEditMov.putExtra("concepto", movSeleccionado.toString());
			inEditMov.putExtra("fecha",
					String.valueOf(movSeleccionado.getFecha()));
			inEditMov.putExtra("categoria", movSeleccionado.getIdCategoria());
			inEditMov.putExtra("subcategoria",
					movSeleccionado.getIdSubcategoria());
			inEditMov.putExtra("tarjeta",
					String.valueOf(movSeleccionado.isTarjeta()));
			inEditMov.putExtra("mes", String.valueOf(movSeleccionado.getMes()));
			inEditMov.putExtra("idTarjeta", movSeleccionado.getIdTarjeta());

			boolean isSinPubli = false;
			if (isPremium || isSinPublicidad) {
				isSinPubli = true;
			}
			inEditMov.putExtra("isPremium", isSinPubli);

			this.startActivity(inEditMov);
			return true;
		}
		return false;
	}

	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		AlertDialog alert;
		switch (id) {
		case MENSAJE_OK:
			builder.setMessage(
					getResources().getString(R.string.deleteRegistroOk))
					.setTitle(getResources().getString(R.string.info))
					.setIcon(R.drawable.ic_alert)
					.setPositiveButton(
							getResources().getString(R.string.aceptar),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {//
									dialog.cancel();
								}
							});
			alert = builder.create();
			alert.show();
			break;
		case MENSAJE_ERROR:
			builder.setMessage(
					getResources().getString(R.string.deleteRegistroError))
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
		case MENSAJE_CONFIRMAR_ELIMINAR:
			builder.setTitle(getResources().getString(R.string.atencion));
			builder.setMessage(getResources().getString(
					R.string.msnEliminarRegistro));
			builder.setIcon(R.drawable.ic_delete);
			builder.setCancelable(false);
			builder.setPositiveButton(
					getResources().getString(R.string.aceptar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							boolean ok = false;
							db = getActivity().openOrCreateDatabase(BD_NOMBRE,
									1, null);
							if (db != null) {
								ok = gestion.eliminarRegistro(db,
										movSeleccionado.getId());
							}
							db.close();
							if (ok) {
								ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
								int idCuenta = cuentaSeleccionada();
								db = getActivity().openOrCreateDatabase(
										BD_NOMBRE, 1, null);
								if (db != null) {
									if (mostrarNominas) {
										listMov = gestion.getMovimientos(db,
												mes, anio, idCuenta);
									} else {
										listMov = gestion
												.getMovimientosInicioMes(db,
														mes, anio, idCuenta);
									}
								}
								db.close();
								rellenarListaExpansibleCategorias(listMov);
								calcularTotal(listMov);

								Context context = getActivity()
										.getApplicationContext();
								CharSequence text = getResources().getString(
										R.string.deleteRegistroOk);
								int duration = Toast.LENGTH_SHORT;
								Toast toast = Toast.makeText(context, text,
										duration);
								toast.show();
							} else {
								Context context = getActivity()
										.getApplicationContext();
								CharSequence text = getResources().getString(
										R.string.deleteRegistroError);
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
		}
		return null;
	}

	@Override
	public void onResume() {
		// cargarPreferencias();
		// ArrayList<Movimiento> listMov = inicializarResumen();
		// rellenarListaExpansibleCategorias(listMov);
		// calcularTotal(listMov);
		super.onResume();
	}

	@Override
	public void onDestroy() {
		prefs = getActivity().getSharedPreferences("ficheroConf",
				Context.MODE_PRIVATE);
		editor = prefs.edit();
		editor.putBoolean("appIniciada", false);
		editor.commit();
		super.onDestroy();
	}

	public class YearsComparator implements Comparator<Movimiento> {
		@Override
		public int compare(Movimiento m1, Movimiento m2) {
			return m1.getAnio().compareTo(m2.getAnio());
		}
	}

	public int cuentaSeleccionada() {
		prefs = getActivity().getSharedPreferences("ficheroConf",
				Context.MODE_PRIVATE);

		int idCuenta = prefs.getInt("cuenta", 0);
		return idCuenta;
	}

	public void rellenarLista(ArrayList<Movimiento> lista) {
		listClasica = lista;
		listMovView.setAdapter(new ListAdapter(getActivity(), lista));
	}

	public void rellenarListaExpansibleCategorias(ArrayList<Movimiento> lista) {

		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		ArrayList<Object> movimientos = new ArrayList<Object>();

		categorias = rellenarListaCategorias(lista);

		for (int i = 0; i < categorias.size(); i++) {
			ArrayList<Movimiento> movimientosAux = new ArrayList<Movimiento>();
			Categoria cat = categorias.get(i);
			movimientosAux = getMovimientosCategoria(cat.getId(), lista);
			cat.setListaMovimientos(movimientosAux);
			movimientos.add(movimientosAux);
		}

		listGroupChild = categorias;

		listMovCatView.setDividerHeight(2);
		listMovCatView.setGroupIndicator(null);
		listMovCatView.setClickable(true);

		ListaAdapterResumenExpandibleAdapter listAdapter = new ListaAdapterResumenExpandibleAdapter(
				getActivity(), categorias, movimientos);
		listMovCatView.setAdapter(listAdapter);

		for (int i = 0; i < listAdapter.getGroupCount(); i++) {
			listMovCatView.expandGroup(i);
		}

	}

	public void rellenarListaExpansibleSubcategorias(ArrayList<Movimiento> lista) {

		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		ArrayList<Object> movimientos = new ArrayList<Object>();

		categorias = rellenarListaSubcategorias(lista);

		for (int i = 0; i < categorias.size(); i++) {
			ArrayList<Movimiento> movimientosAux = new ArrayList<Movimiento>();
			Categoria cat = categorias.get(i);
			movimientosAux = getMovimientosSubcategoria(cat.getId(), lista);
			cat.setListaMovimientos(movimientosAux);
			movimientos.add(movimientosAux);
		}

		listGroupChild = categorias;

		listMovCatView.setDividerHeight(2);
		listMovCatView.setGroupIndicator(null);
		listMovCatView.setClickable(true);

		ListaAdapterResumenExpandibleSubAdapter listAdapter = new ListaAdapterResumenExpandibleSubAdapter(
				getActivity(), categorias, movimientos);
		listMovCatView.setAdapter(listAdapter);

		for (int i = 0; i < listAdapter.getGroupCount(); i++) {
			listMovCatView.expandGroup(i);
		}

	}

	public ArrayList<Categoria> rellenarListaCategorias(
			ArrayList<Movimiento> lista) {
		ArrayList<Categoria> listaCat = new ArrayList<Categoria>();
		boolean aniadir = true;

		for (int i = 0; i < lista.size(); i++) {
			aniadir = true;
			Movimiento mov = lista.get(i);
			Categoria cat = new Categoria();
			cat.setId(mov.getIdCategoria());
			cat.setDescripcion(mov.getDescCategoria());
			cat.setIdIcon(mov.getIdIconCat());
			if (listaCat.size() == 0) {
				if (cat.getId().equals("0")) {
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
				listaCat.add(cat);
			} else {
				for (int j = 0; j < listaCat.size(); j++) {
					Categoria cat2 = listaCat.get(j);
					if (cat.getId().equals(cat2.getId())) {
						aniadir = false;
						break;
					} else {
						if (cat.getId().equals("0")) {
							Locale locale = Locale.getDefault();
							String languaje = locale.getLanguage();
							if (languaje.equals("es")
									|| languaje.equals("es-rUS")
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
					}
				}
				if (aniadir) {
					listaCat.add(cat);
				}
			}
		}
		Collections.sort(listaCat);
		return listaCat;
	}

	public ArrayList<Categoria> rellenarListaSubcategorias(
			ArrayList<Movimiento> lista) {
		ArrayList<Categoria> listaCat = new ArrayList<Categoria>();
		boolean aniadir = true;

		for (int i = 0; i < lista.size(); i++) {
			aniadir = true;
			Movimiento mov = lista.get(i);
			Categoria cat = new Categoria();
			cat.setId(mov.getIdSubcategoria());
			cat.setDescripcion(mov.getDescSubcategoria());
			cat.setIdIcon(mov.getIdIconSub());
			if (listaCat.size() == 0) {
				if (cat.getId().equals("0")) {
					Locale locale = Locale.getDefault();
					String languaje = locale.getLanguage();
					if (languaje.equals("es") || languaje.equals("es-rUS")
							|| languaje.equals("ca")) {
						cat.setDescripcion("Sin subcategoria");
					} else if (languaje.equals("fr")) {
						cat.setDescripcion("Sans sous-catégorie");
					} else if (languaje.equals("de")) {
						cat.setDescripcion("Keine Unterkategorie");
					} else if (languaje.equals("en")) {
						cat.setDescripcion("No subcategory");
					} else if (languaje.equals("it")) {
						cat.setDescripcion("Senza sottocategoria");
					} else if (languaje.equals("pt")) {
						cat.setDescripcion("Sem subcategoria");
					} else {
						cat.setDescripcion("No subcategory");
					}
				}
				listaCat.add(cat);
			} else {
				for (int j = 0; j < listaCat.size(); j++) {
					Categoria cat2 = listaCat.get(j);
					if (cat.getId().equals(cat2.getId())) {
						aniadir = false;
						break;
					} else {
						if (cat.getId().equals("0")) {
							Locale locale = Locale.getDefault();
							String languaje = locale.getLanguage();
							if (languaje.equals("es")
									|| languaje.equals("es-rUS")
									|| languaje.equals("ca")) {
								cat.setDescripcion("Sin subcategoria");
							} else if (languaje.equals("fr")) {
								cat.setDescripcion("Sans sous-catégorie");
							} else if (languaje.equals("de")) {
								cat.setDescripcion("Keine Unterkategorie");
							} else if (languaje.equals("en")) {
								cat.setDescripcion("No subcategory");
							} else if (languaje.equals("it")) {
								cat.setDescripcion("Senza sottocategoria");
							} else if (languaje.equals("pt")) {
								cat.setDescripcion("Sem subcategoria");
							} else {
								cat.setDescripcion("No subcategory");
							}
						}
					}
				}
				if (aniadir) {
					listaCat.add(cat);
				}
			}
		}
		Collections.sort(listaCat);
		return listaCat;
	}

	public ArrayList<Movimiento> getMovimientosCategoria(String idCat,
			ArrayList<Movimiento> listMovimientos) {
		ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();

		for (int i = 0; i < listMovimientos.size(); i++) {
			Movimiento mov = listMovimientos.get(i);
			if (idCat.equals(mov.getIdCategoria())) {
				listMov.add(mov);
			}
		}
		return listMov;
	}

	public ArrayList<Movimiento> getMovimientosSubcategoria(String idCat,
			ArrayList<Movimiento> listMovimientos) {
		ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();

		for (int i = 0; i < listMovimientos.size(); i++) {
			Movimiento mov = listMovimientos.get(i);
			if (idCat.equals(mov.getIdSubcategoria())) {
				listMov.add(mov);
			}
		}
		return listMov;
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
		Tarjeta tarjetaAux = new Tarjeta();
		tarjetaAux.setId("99");
		String todas = getResources().getString(R.string.todas);
		tarjetaAux.setNombre(todas);
		listaTarjetas.add(tarjetaAux);
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

	public Movimiento obtenerMovimientoSeleccionado() {
		Movimiento mov = new Movimiento();
		SharedPreferences prefs;
		prefs = getActivity().getSharedPreferences("ficheroConf",
				Context.MODE_PRIVATE);

		String idMov = prefs.getString("idMov", "0");
		db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			mov = gestion.getMovimientoId(db, idMov);
		}
		db.close();
		return mov;
	}

	public void cargarPreferencias() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());

		mostrarNominas = sharedPrefs.getBoolean("mostrarNominas", false);
		mostrarPorCategorias = sharedPrefs.getBoolean("mostrarPorCategorias",
				false);

	}

	public void guardarMovimientoSeleccionado(String idMov) {
		SharedPreferences prefs;
		SharedPreferences.Editor editor;

		prefs = getActivity().getSharedPreferences("ficheroConf",
				Context.MODE_PRIVATE);
		editor = prefs.edit();
		editor.putString("idMov", idMov);
		editor.putBoolean("mostrarMenu", true);
		editor.commit();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		prefs = getActivity().getSharedPreferences("ficheroConf",
				Context.MODE_PRIVATE);
		boolean mostrar = prefs.getBoolean("mostrarMenu", false);

		if (mostrar) {
			prefs = getActivity().getSharedPreferences("ficheroConf",
					Context.MODE_PRIVATE);
			editor = prefs.edit();
			editor.putBoolean("mostrarMenu", false);
			editor.commit();

			// TODO Auto-generated method stub
			MenuInflater inflater = (getActivity()).getMenuInflater();
			inflater.inflate(R.menu.menu_pulsacion_larga, menu);
			menu.setHeaderTitle(getActivity().getResources().getString(
					R.string.elijaOpcion));
		}
	}


}
