package com.agudoApp.salaryApp.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.AddSub;
import com.agudoApp.salaryApp.adapters.ListaAdapterPestanaSubExpandibleAdapter;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Categoria;

public class PestanaSubcategoriaFragment extends Fragment {
	private static final String KEY_CONTENT = "PestanaCategoriaFragment:Content";
	private final String BD_NOMBRE = "BDGestionGastos";
	final GestionBBDD gestion = new GestionBBDD();
	private SQLiteDatabase db;
	protected ExpandableListView listCatView;
	private LinearLayout linearLayoutAdd;
	private Button botonAdd;
	private ImageView imagenAdd;
	private boolean isPremium = false;

	private String mContent = "???";
	
	public PestanaSubcategoriaFragment(boolean isUserPremium) {
		isPremium = isUserPremium;
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

		return inflater.inflate(R.layout.pestana_categoria, container, false);
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

		listCatView = (ExpandableListView) this.getView().findViewById(
				R.id.listaPestanaCategoria);
		linearLayoutAdd = (LinearLayout) this.getView().findViewById(
				R.id.layoutAddCategoria);
		botonAdd = (Button) this.getView().findViewById(R.id.botonAdd);
		imagenAdd = (ImageView) this.getView().findViewById(R.id.imagenAdd);

		botonAdd.setText(getActivity().getResources().getString(
				R.string.aniadirSubcategoria));

		// Bundle bundle = getArguments();
		// isPremium = bundle.getBoolean("isPremium", false);

		rellenarListaExpansibleCategorias();

		listCatView.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousGroup = -1;

			@Override
			public void onGroupExpand(int groupPosition) {
				if (groupPosition != previousGroup)
					listCatView.collapseGroup(previousGroup);
				previousGroup = groupPosition;
			}
		});

		linearLayoutAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent inCat = new Intent(getActivity(), AddSub.class);
				inCat.putExtra("isPremium", isPremium);
				getActivity().startActivityForResult(inCat, 0);
			}
		});

		botonAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent inCat = new Intent(getActivity(), AddSub.class);
				inCat.putExtra("isPremium", isPremium);
				getActivity().startActivityForResult(inCat, 0);
			}
		});

		imagenAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent inCat = new Intent(getActivity(), AddSub.class);
				inCat.putExtra("isPremium", isPremium);
				getActivity().startActivityForResult(inCat, 0);
			}
		});

	}

	public void rellenarListaExpansibleCategorias() {

		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		ArrayList<Object> opciones = new ArrayList<Object>();
		opciones.add("1");

		categorias = obtenerSubcategorias();

		listCatView.setDividerHeight(2);
		listCatView.setGroupIndicator(null);
		listCatView.setClickable(true);

		boolean isCatPremium = false;
		if(isPremium || isCatPremium){
			isCatPremium = true;
		}
		ListaAdapterPestanaSubExpandibleAdapter listAdapter = new ListaAdapterPestanaSubExpandibleAdapter(
				getActivity(), categorias, isCatPremium);
		listCatView.setAdapter(listAdapter);

	}

	public ArrayList<Categoria> obtenerSubcategorias() {
		ArrayList<Categoria> listSubcategorias = new ArrayList<Categoria>();
		db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			// Recuperamos el listado del spinner Categorias
			listSubcategorias = (ArrayList<Categoria>) gestion
					.getCategoriasEditDelete(db, "Subcategorias",
							"idSubcategoria");
		}
		return listSubcategorias;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		rellenarListaExpansibleCategorias();
	}
}
