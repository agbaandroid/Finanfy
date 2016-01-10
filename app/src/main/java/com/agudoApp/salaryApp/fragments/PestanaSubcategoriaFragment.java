package com.agudoApp.salaryApp.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.NuevoEditCategoriaActivity;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.util.Util;

import java.util.ArrayList;

public class PestanaSubcategoriaFragment extends Fragment {
	private static final String KEY_CONTENT = "PestanaCategoriaFragment:Content";
	private final String BD_NOMBRE = "BDGestionGastos";
	final GestionBBDD gestion = new GestionBBDD();
	private SQLiteDatabase db;
	protected ListView listCatView;
	private boolean isPremium = false;
	private boolean isSinPublicidad = false;
	private boolean isCategoriaPremium = false;
	private final int CATEGORIA = 1;

	private String mContent = "???";

	public PestanaSubcategoriaFragment() {
	}
	
	public PestanaSubcategoriaFragment(boolean isUserPremium, boolean isCatPrem, boolean isSinPubli) {
		isPremium = isUserPremium;
		isCategoriaPremium = isCatPrem;
		isSinPublicidad = isSinPubli;
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

		listCatView = (ListView) this.getView().findViewById(
				R.id.listaPestanaCategoria);

		rellenarListaExpansibleCategorias();
	}

	public void rellenarListaExpansibleCategorias() {

		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		ArrayList<Object> opciones = new ArrayList<Object>();
		opciones.add("1");

		categorias = obtenerSubcategorias();
		listCatView.setClickable(true);

		boolean isCatPremium = false;
		if(isPremium || isCategoriaPremium){
			isCatPremium = true;
		}
		ListAdapterCategorias listAdapter = new ListAdapterCategorias(
				getActivity(), categorias);
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

	public class ListAdapterCategorias extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<Categoria> listaCat = new ArrayList<Categoria>();
		private Context context;

		public ListAdapterCategorias(Context context, ArrayList<Categoria> lista) {
			listaCat = lista;
			mInflater = LayoutInflater.from(context);
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listaCat.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listaCat.get(position);
		}

		public int getPositionById(String id) {
			int posi = 0;
			for (int i = 0; i < listaCat.size(); i++) {
				Categoria cat = listaCat.get(i);
				if (cat.getId().equals(id)) {
					posi = i;
					break;
				}
			}
			return posi;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView text;
			ImageView iconCategoria;
			LinearLayout layoutCategoria;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.lista_pestana_categorias, null);
			}

			text = (TextView) convertView.findViewById(R.id.txtCategoria);
			text.setText(listaCat.get(position).toString());

			iconCategoria = (ImageView) convertView.findViewById(R.id.imagenCat);
			iconCategoria.setBackgroundDrawable(context.getResources().getDrawable(
					Util.obtenerIconoCategoria(listaCat.get(position).getIdIcon())));

			layoutCategoria = (LinearLayout) convertView.findViewById(R.id.layoutCategoria);

			layoutCategoria.setTag(position);

			layoutCategoria.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					int posiSel = (int) v.getTag();
					Categoria cat = listaCat.get(posiSel);
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, NuevoEditCategoriaActivity.class);
					Bundle bundle = new Bundle();
					bundle.putBoolean("isCategoria", false);
					bundle.putString("idCategoria", cat.getId());
					intent.putExtra("isPremium", isPremium);
					intent.putExtra("isSinPublicidad", isSinPublicidad);
					intent.putExtra("isCategoriaPremium", isCategoriaPremium);
					intent.putExtras(bundle);
					startActivityForResult(intent, CATEGORIA);
				}
			});

			return convertView;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == getActivity().RESULT_OK) {
			switch (requestCode) {
				case CATEGORIA :
					//((FinanfyActivity)getActivity()).mostrarPublicidad(false, true);
					break;
			}
		}
	}
}
