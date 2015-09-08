package com.agudoApp.salaryApp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterSpinner;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Recibo;
import com.agudoApp.salaryApp.model.Tarjeta;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;

public class EditRecTexto extends Activity {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	static final int MENSAJE_OK = 1;
	static final int MENSAJE_ERROR = 2;
	static final int MENSAJE_ERROR_CAMPOS = 5;
	static final int DATE_DIALOG_INI = 3;
	static final int DATE_DIALOG_FIN = 4;
	private GestionBBDD gestion = new GestionBBDD();
	// private EditText editTar;
	private String id;
	private Button botonFechaDesde;
	private Button botonFechaHasta;
	private Button botonGuardar;
	private EditText cantidad;
	private Spinner spinnerCat;
	private Spinner spinnerSub;
	private Spinner spinnerTarjetas;
	private EditText descripcion;
	private CheckBox checkTarjeta;
	int posCat;
	int posSub;
	int posTar;
	private int mYearIni;
	private int mMonthIni;
	private int mDayIni;
	private int mYearFin;
	private int mMonthFin;
	private int mDayFin;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_recibo);
		gestion = new GestionBBDD();

		spinnerCat = (Spinner) findViewById(R.id.spinnerCategoriaReciboEdit);
		spinnerSub = (Spinner) findViewById(R.id.spinnerSubCategoriaReciboEdit);
		spinnerTarjetas = (Spinner) findViewById(R.id.spinnerTarjetasReciboEdit);
		cantidad = (EditText) findViewById(R.id.cajaCantidadReciboEdit);
		descripcion = (EditText) findViewById(R.id.cajaDescripcionReciboEdit);
		checkTarjeta = (CheckBox) findViewById(R.id.checkTarjetaReciboEdit);
		botonFechaDesde = (Button) findViewById(R.id.botonFechaReciboDesdeEdit);
		botonFechaHasta = (Button) findViewById(R.id.botonFechaReciboHastaEdit);
		botonGuardar = (Button) findViewById(R.id.botonEditarReciboEdit);

		// Asignamos el tipo de fuente
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/Berlin.ttf");

		TextView txtDescripcion = (TextView) findViewById(R.id.descripcionReciboEdit);
		txtDescripcion.setTypeface(miPropiaTypeFace);
		TextView txtCantidad = (TextView) findViewById(R.id.cantidadReciboEdit);
		txtCantidad.setTypeface(miPropiaTypeFace);
		TextView txtDesde = (TextView) findViewById(R.id.textoFechaDesdeReciboEdit);
		txtDesde.setTypeface(miPropiaTypeFace);
		TextView txtHasta = (TextView) findViewById(R.id.textoFechaHastaReciboEdit);
		txtHasta.setTypeface(miPropiaTypeFace);
		TextView txtTextoCategoria = (TextView) findViewById(R.id.textoCategoriaReciboEdit);
		txtTextoCategoria.setTypeface(miPropiaTypeFace);
		TextView txtTextoSubCategoria = (TextView) findViewById(R.id.textoSubCategoriaReciboEdit);
		txtTextoSubCategoria.setTypeface(miPropiaTypeFace);
		TextView txtTextoPagoTarjeta = (TextView) findViewById(R.id.textoPagoTarjetaRecibo);
		txtTextoPagoTarjeta.setTypeface(miPropiaTypeFace);

		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			// Recuperamos el listado del spinner categorias
			obtenerCategorias();
			// Recuperamos el listado del spinner subcategorias
			obtenerSubcategorias();
			// Recuperamos el listado del spinner tarjetas
			obtenerTarjetas();
		}

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id = extras.getString("id");
		}
		Recibo recibo = gestion.getReciboId(db, Integer.parseInt(id));
		rellenarDatos(recibo);
		db.close();

		// Funcionalidad boton fecha ini
		botonFechaDesde.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_INI);
			}
		});

		// Funcionalidad boton fecha ini
		botonFechaHasta.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_FIN);
			}
		});

		// Funcionalidad boton editar
		botonGuardar.setOnClickListener(new View.OnClickListener() {
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
				Date fechaIni = new Date(mYearIni - 1900, mMonthIni - 1,
						mDayIni);
				Date fechaFin = new Date(mYearFin - 1900, mMonthFin - 1,
						mDayFin);
				if ("".equals(cantidad.getText().toString())
						|| ".".equals(cantidad.getText().toString())
						|| Float.parseFloat(cantidad.getText().toString()) <= 0) {
					error = true;
				} else {
					cant = -1
							* Float.parseFloat(cantidad.getText().toString()
									.replace(",", "."));

					error = false;
				}

				if ("".equals(descripcion.getText().toString()) || error) {
					error = true;
				} else {
					desc = descripcion.getText().toString().trim();
					error = false;
				}

				// Si no hay error realizamos el insert
				if (!error) {
					boolean ok = false;
					boolean tarjeta = checkTarjeta.isChecked();
					db = openOrCreateDatabase(BD_NOMBRE, 1, null);
					if (db != null) {
						// Introducimos el movimiento en BBDD
						ok = gestion.editarRecibo(db, id, cant, desc,
								idCategoria, idSubcategoria, fechaIni,
								fechaFin, tarjeta, idTarjeta);
					}
					db.close();
					if (ok) {
						Context context = getApplicationContext();
						CharSequence textMsg = getResources().getString(
								R.string.reciboOK);
						int duration = Toast.LENGTH_SHORT;
						Toast toast = Toast
								.makeText(context, textMsg, duration);
						toast.show();

						finish();
					} else {
						Context context = getApplicationContext();
						CharSequence textMsg = getResources().getString(
								R.string.reciboKO);
						int duration = Toast.LENGTH_SHORT;
						Toast toast = Toast
								.makeText(context, textMsg, duration);
						toast.show();

						finish();
					}
				} else {
					onCreateDialog(MENSAJE_ERROR_CAMPOS);
				}
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

	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListenerIni = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mDayIni = dayOfMonth;
			mMonthIni = monthOfYear + 1;
			mYearIni = year;
			updateDisplayIni();
		}
	};

	private DatePickerDialog.OnDateSetListener mDateSetListenerFin = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mDayFin = dayOfMonth;
			mMonthFin = monthOfYear + 1;
			mYearFin = year;
			updateDisplayFin();
		}
	};

	private void updateDisplayIni() {
		botonFechaDesde.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mDayIni).append("-").append(mMonthIni).append("-")
				.append(mYearIni).append(" "));
	}

	private void updateDisplayFin() {
		botonFechaHasta.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mDayFin).append("-").append(mMonthFin).append("-")
				.append(mYearFin).append(" "));
	}

	public void rellenarDatos(Recibo recibo) {
		posCat = 0;
		posSub = 0;
		posTar = 0;

		// Recuperamos el listado del spinner Categorias
		ArrayList<Categoria> listCategorias = (ArrayList<Categoria>) gestion
				.getCategorias(db, "Categorias", "idCategoria", this);
		// Recuperamos el listado del spinner Subategorias
		ArrayList<Categoria> listSubcategorias = (ArrayList<Categoria>) gestion
				.getCategorias(db, "Subcategorias", "idSubcategoria", this);
		// Recuperamos el listado del spinner Tarjetas
		ArrayList<Tarjeta> listTarjetas = (ArrayList<Tarjeta>) gestion
				.getTarjetas(db);

		for (int i = 0; i < listCategorias.size(); i++) {
			Categoria cat = listCategorias.get(i);
			if (cat.getId().equals(recibo.getIdCategoria())) {
				posCat = i;
			}
		}
		for (int i = 0; i < listSubcategorias.size(); i++) {
			Categoria subcat = listSubcategorias.get(i);
			if (subcat.getId().equals(recibo.getIdSubcategoria())) {
				posSub = i;
			}
		}
		for (int i = 0; i < listTarjetas.size(); i++) {
			Tarjeta tarjeta = listTarjetas.get(i);
			if (tarjeta.getId().equals(recibo.getIdTarjeta())) {
				posTar = i;
			}
		}

		spinnerCat.setSelection(posCat);
		spinnerSub.setSelection(posSub);
		spinnerTarjetas.setSelection(posTar);

		DecimalFormat df = new DecimalFormat("0.00");
		String cant = "";
		if (Float.parseFloat(recibo.getCantidad()) < 0) {
			cant = df.format(Float.parseFloat(recibo.getCantidad()) * -1);
		} else {
			cant = df.format(Float.parseFloat(recibo.getCantidad()));
		}
		cantidad.setText(cant.replace(",", "."));
		descripcion.setText(recibo.getDescripcion());

		mDayIni = Integer.parseInt(recibo.getFechaIni().substring(8, 10));
		mMonthIni = Integer.parseInt(recibo.getFechaIni().substring(5, 7));
		mYearIni = Integer.parseInt(recibo.getFechaIni().substring(0, 4));

		mDayFin = Integer.parseInt(recibo.getFechaFin().substring(8, 10));
		mMonthFin = Integer.parseInt(recibo.getFechaFin().substring(5, 7));
		mYearFin = Integer.parseInt(recibo.getFechaFin().substring(0, 4));

		checkTarjeta.setChecked(recibo.isTarjeta());

		if (checkTarjeta.isChecked()) {
			spinnerTarjetas.setEnabled(true);
			spinnerTarjetas.setBackgroundResource(R.drawable.spinner);
		} else {
			spinnerTarjetas.setEnabled(false);
			spinnerTarjetas
					.setBackgroundResource(R.drawable.spinner_deshabilitado);
		}

		updateDisplayIni();
		updateDisplayFin();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog alert;
		switch (id) {
		case DATE_DIALOG_INI:
			return new DatePickerDialog(this, mDateSetListenerIni, mYearIni,
					mMonthIni - 1, mDayIni);
		case DATE_DIALOG_FIN:
			return new DatePickerDialog(this, mDateSetListenerFin, mYearFin,
					mMonthFin - 1, mDayFin);
		case MENSAJE_ERROR_CAMPOS:
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
}
