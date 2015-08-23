package com.agudoApp.salaryApp.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.CantidadActivity;
import com.agudoApp.salaryApp.adapters.ListAdapterSpinner;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Tarjeta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public final class NuevoMovimientosFragment extends Fragment {

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

	private LinearLayout btnGasto;
	private LinearLayout btnIngreso;
	private TextView txtGasto;
	private TextView txtIngreso;
	int tipoRegistro = 0;


	private LinearLayout layoutFecha;
	private LinearLayout layoutCant;
	private TextView txtFecha;
	private TextView txtCant;
	private EditText descripcion;

	private int mYear;
	private int mMonth;
	private int mDay;

	private Button botonCrear;

	private Spinner spinnerCat;
	private Spinner spinnerSub;
	private Spinner spinnerTarjetas;

	private Spinner spinnerTipoPago;


	private SQLiteDatabase db;

	final GestionBBDD gestion = new GestionBBDD();

	boolean tablasCreadas = false;
	boolean opcionesCategoria = false;
	boolean opcionesSubcategoria = false;

	private ProgressDialog progressDialog;

	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	int style;
	// Productos que posee el usuario
	boolean isPremium = false;
	boolean isCategoriaPremium = false;
	boolean isSinPublicidad = false;
	boolean crearAnuncio = false;

	private static final String KEY_CONTENT = "MovimientosFragment:Content";

	public NuevoMovimientosFragment(boolean isUserPremium,
			boolean isUserSinpublicidad, boolean isUserCategoriaPremium) {

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

		return inflater.inflate(R.layout.nuevo_movimientos, container, false);
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

		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		txtCant = (TextView) this.getView().findViewById(R.id.txtCant);
		btnGasto = (LinearLayout) this.getView().findViewById(R.id.btnGasto);
		btnIngreso = (LinearLayout) this.getView().findViewById(R.id.btnIngreso);
		txtGasto = (TextView) this.getView().findViewById(R.id.txtGasto);
		txtIngreso = (TextView) this.getView().findViewById(R.id.txtIngreso);

		layoutFecha = (LinearLayout) this.getView().findViewById(R.id.layoutFecha);
		txtFecha = (TextView) this.getView().findViewById(R.id.txtFecha);

		spinnerCat = (Spinner) this.getView().findViewById(
				R.id.spinnerCategoria);
		spinnerSub = (Spinner) this.getView().findViewById(
				R.id.spinnerSubcategoria);
		spinnerTarjetas = (Spinner) this.getView().findViewById(
				R.id.spinnerTarjetas);

		updateDisplay();

		iniciarActivity();

		txtCant.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), CantidadActivity.class);
				startActivity(intent);
			}
		});

		btnGasto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tipoRegistro = 0;
				btnGasto.setBackgroundResource(R.drawable.rounded_layout_azul);
				btnIngreso.setBackgroundResource(R.drawable.rounded_layout_gris);
				txtGasto.setTextColor(getResources().getColor(R.color.blanco));
				txtIngreso.setTextColor(getResources().getColor(R.color.txtGris));

			}
		});

		btnIngreso.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tipoRegistro = 2;
				btnIngreso.setBackgroundResource(R.drawable.rounded_layout_azul);
				btnGasto.setBackgroundResource(R.drawable.rounded_layout_gris);
				txtIngreso.setTextColor(getResources().getColor(R.color.blanco));
				txtGasto.setTextColor(getResources().getColor(R.color.txtGris));
			}
		});

		layoutFecha.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onCreateDialog(DATE_DIALOG_ID);
			}
		});
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
				getActivity(), R.layout.spinner_iconos,
				listCategorias);

		spinnerCat.setAdapter(spinner_adapterCat);
	}

	public void iniciarActivity() {

		obtenerCategorias();
		obtenerSubcategorias();
		obtenerTarjetas();

	}

	private void updateDisplay() {
		txtFecha.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mDay).append("-").append(mMonth + 1).append("-")
				.append(mYear).append(" "));
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
				getActivity(), R.layout.spinner_iconos,
				listSubcategorias);
		spinnerSub.setAdapter(spinner_adapterSubcat);
	}

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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.menu_pulsacion_corta, menu);
		menu.setHeaderTitle(getResources().getString(R.string.elijaOpcion));

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		AlertDialog alert;
		switch (id) {
			case DATE_DIALOG_ID:
				DatePickerDialog datadialog = new DatePickerDialog(
						this.getActivity(), mDateSetListener, mYear, mMonth, mDay);
				datadialog.show();
				break;
		}
		return null;
	}
}
