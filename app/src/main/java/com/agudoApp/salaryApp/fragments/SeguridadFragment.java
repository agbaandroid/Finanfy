package com.agudoApp.salaryApp.fragments;

import android.app.AlertDialog;
import android.content.Context;
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
import com.agudoApp.salaryApp.activities.SeguridadAntigua;
import com.agudoApp.salaryApp.activities.SeguridadComprobar;
import com.agudoApp.salaryApp.activities.SeguridadIntroducir;
import com.agudoApp.salaryApp.database.GestionBBDD;

public class SeguridadFragment extends Fragment {
	private static final String KEY_CONTENT = "SeguridadFragment:Content";
	final GestionBBDD gestion = new GestionBBDD();
	private Button botonActivarPass;
	private Button botonDesactivarPass;
	private Button botonModificarPass;
	boolean seguridadAct;

	SharedPreferences prefs;
	SharedPreferences.Editor editor;

	// Productos que posee el usuario
	boolean isPremium = false;
	boolean isCategoriaPremium = false;
	boolean isSinPublicidad = false;

	public SeguridadFragment(boolean isUserPremium,
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

		return inflater.inflate(R.layout.menu_seguridad, container, false);
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

		prefs = getActivity().getSharedPreferences("ficheroConf",
				Context.MODE_PRIVATE);
		editor = prefs.edit();

		seguridadAct = prefs.getBoolean("seguridadActivada", false);

		botonActivarPass = (Button) getView().findViewById(
				R.id.botonActivarPass);
		botonDesactivarPass = (Button) getView().findViewById(
				R.id.botonDesactivarPass);
		botonModificarPass = (Button) getView().findViewById(
				R.id.botonModificarPass);

		if (seguridadAct) {
			botonActivarPass.setVisibility(8);
			botonDesactivarPass.setVisibility(0);
			botonModificarPass.setVisibility(0);
		} else {
			botonActivarPass.setVisibility(0);
			botonDesactivarPass.setVisibility(8);
			botonModificarPass.setVisibility(8);
		}

		botonActivarPass.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						SeguridadIntroducir.class);
				startActivity(intent);
			}
		});

		botonDesactivarPass.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						SeguridadComprobar.class);
				intent.putExtra("funcionalidad", "desactivar");
				startActivity(intent);
			}
		});

		botonModificarPass.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						SeguridadAntigua.class);
				startActivity(intent);
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

	@Override
	public void onResume() {
		comprobarMenu();
		super.onResume();
	}

	public void comprobarMenu() {
		prefs = getActivity().getSharedPreferences("ficheroConf",
				Context.MODE_PRIVATE);
		seguridadAct = prefs.getBoolean("seguridadActivada", false);

		if (seguridadAct) {
			botonActivarPass.setVisibility(8);
			botonDesactivarPass.setVisibility(0);
			botonModificarPass.setVisibility(0);
		} else {
			botonActivarPass.setVisibility(0);
			botonDesactivarPass.setVisibility(8);
			botonModificarPass.setVisibility(8);
		}
	}
}
