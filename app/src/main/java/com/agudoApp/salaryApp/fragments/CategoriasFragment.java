package com.agudoApp.salaryApp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.NuevoAddCategoriaActivity;

public class CategoriasFragment extends Fragment {

	private static final String KEY_CONTENT = "InformesFragment:Content";

	// Productos que posee el usuario
	boolean isUserPremium = false;
	boolean isCategoriaPremium = false;
	boolean isSinPublicidad = false;
	boolean isPremium = false;
	LinearLayout layoutCat;
	LinearLayout layoutSubcat;
	TextView textCat;
	TextView textSubcat;
	boolean isCategoria = false;
	boolean isSubcategoria = false;

	private final int CATEGORIA = 1;

	private String mContent = "???";

	public CategoriasFragment(boolean isPrem, boolean isUserSinpublicidad,
			boolean isUserCategoriaPremium) {
		isUserPremium = isPrem;
		isCategoriaPremium = isUserCategoriaPremium;
		isSinPublicidad = isUserSinpublicidad;

		if (isUserPremium || isCategoriaPremium) {
			isPremium = true;
		}

		// args.putBoolean("isPremium", isPremium);
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

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(
				R.layout.tabs_categorias_personalizadas, container, false);

		layoutCat = (LinearLayout) rootView.findViewById(R.id.pestanaCat);
		layoutSubcat = (LinearLayout) rootView.findViewById(R.id.pestanaSubcat);
		
		textCat = (TextView) rootView.findViewById(R.id.cat);
		textSubcat = (TextView) rootView.findViewById(R.id.sub);

		Fragment fragment = new PestanaCategoriaFragment(isPremium);
		if (fragment != null) {
			FragmentManager fragmentManager = getActivity()
					.getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.realtabcontent, fragment).commit();

			isCategoria = true;

		}

		layoutCat.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!isCategoria) {
					Fragment fragment = new PestanaCategoriaFragment(isPremium);
					if (fragment != null) {
						FragmentManager fragmentManager = getActivity()
								.getSupportFragmentManager();
						fragmentManager.beginTransaction()
								.replace(R.id.realtabcontent, fragment)
								.commit();

						isCategoria = true;
						isSubcategoria = false;
						
						textCat.setTextColor(Color.BLACK);
						textSubcat.setTextColor(Color.rgb(195, 195, 195));
					}
				}
			}
		});

		layoutSubcat.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!isSubcategoria) {
					Fragment fragment = new PestanaSubcategoriaFragment(
							isPremium);
					if (fragment != null) {
						FragmentManager fragmentManager = getActivity()
								.getSupportFragmentManager();
						fragmentManager.beginTransaction()
								.replace(R.id.realtabcontent, fragment)
								.commit();

						isSubcategoria = true;
						isCategoria = false;

						textSubcat.setTextColor(Color.BLACK);
						textCat.setTextColor(Color.rgb(195, 195, 195));
					}
				}
			}
		});

		return rootView;
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
				Intent intent = new Intent(getActivity(), NuevoAddCategoriaActivity.class);
				intent.putExtra("isCategoria", isCategoria);
				startActivityForResult(intent, CATEGORIA);
				return true;
			case android.R.id.home:
				getActivity().finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
