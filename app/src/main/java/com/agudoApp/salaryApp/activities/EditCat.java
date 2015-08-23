package com.agudoApp.salaryApp.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.adapters.ListAdapterCategorias;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Categoria;

public class EditCat extends Activity {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	private ListView listaCategoriasAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_delete);
		// Abrimos la base de datos si no esta abierta
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		gestion = new GestionBBDD();
		listaCategoriasAdapter = (ListView) findViewById(R.id.listaCategorias);
		obtenerCategorias();

		listaCategoriasAdapter
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Categoria cat = (Categoria) parent
								.getItemAtPosition(position);
						Intent inCatEdit = new Intent(EditCat.this,
								EditCatTexto.class);
						inCatEdit.putExtra("id", cat.getId());
						inCatEdit.putExtra("textEdit", cat.toString());
						EditCat.this.startActivityForResult(inCatEdit, 0);
						EditCat.this.setResult(0);
						finish();
					}
				});
	}

	public void obtenerCategorias() {
		ArrayList<Categoria> listCategorias = new ArrayList<Categoria>();
		// Recuperamos el listado del spinner Categorias
		listCategorias = (ArrayList<Categoria>) gestion
				.getCategoriasEditDelete(db, "Categorias", "idCategoria");
		listaCategoriasAdapter.setAdapter(new ListAdapterCategorias(this,
				listCategorias));
	}
}