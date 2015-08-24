package com.agudoApp.salaryApp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.AddTarjeta;
import com.agudoApp.salaryApp.activities.DeleteTarjeta;
import com.agudoApp.salaryApp.activities.EditTarjeta;
import com.agudoApp.salaryApp.database.GestionBBDD;

public class TarjetasFragment extends Fragment {
	private static final String KEY_CONTENT = "CuentasFragment:Content";
	final GestionBBDD gestion = new GestionBBDD();
	static final int MENSAJE_OK_DELETE = 5;
	static final int MENSAJE_ERROR_DELETE = 4;
	static final int ADD_TARJETA = 1;
	static final int EDIT_TARJETA = 2;
	static final int DELETE_TARJETA = 3;

	private Button botonAddTarjeta;
	private Button botonEditTarjeta;
	private Button botonDeleteTarjeta;

	SharedPreferences prefs;
	SharedPreferences.Editor editor;

	// Productos que posee el usuario
	boolean isPremium = false;
	boolean isCategoriaPremium = false;
	boolean isSinPublicidad = false;

	public TarjetasFragment(boolean isUserPremium, boolean isUserSinpublicidad,
			boolean isUserCategoriaPremium) {
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

		return inflater.inflate(R.layout.menu_tarjetas, container, false);
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

		botonAddTarjeta = (Button) this.getView().findViewById(
				R.id.botonAddTarjeta);
		botonEditTarjeta = (Button) this.getView().findViewById(
				R.id.botonEditTarjeta);
		botonDeleteTarjeta = (Button) this.getView().findViewById(
				R.id.botonDeleteTarjeta);

		botonAddTarjeta.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent in = new Intent(getActivity(), AddTarjeta.class);
				startActivity(in);
			}
		});

		botonEditTarjeta.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent in = new Intent(getActivity(), EditTarjeta.class);
				startActivity(in);
			}
		});

		botonDeleteTarjeta.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent in = new Intent(getActivity(), DeleteTarjeta.class);
				startActivity(in);
			}
		});

	}

	// Aadiendo las opciones de men
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_setting, menu);
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
}
