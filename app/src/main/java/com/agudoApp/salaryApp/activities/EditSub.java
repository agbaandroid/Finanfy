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

public class EditSub extends Activity {

	private SQLiteDatabase db;
	private final String BD_NOMBRE = "BDGestionGastos";
	private GestionBBDD gestion = new GestionBBDD();
	private ListView listaSubcategoriasAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_delete);
		// Abrimos la base de datos si no esta abierta
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		gestion = new GestionBBDD();
		listaSubcategoriasAdapter = (ListView) findViewById(R.id.listaCategorias);
		obtenerSubcategorias();

		listaSubcategoriasAdapter
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Categoria cat = (Categoria) parent
								.getItemAtPosition(position);
						Intent inSubEdit = new Intent(EditSub.this,
								EditSubTexto.class);
						inSubEdit.putExtra("id", cat.getId());
						inSubEdit.putExtra("textEdit", cat.toString());
						EditSub.this.startActivityForResult(inSubEdit, 0);
						EditSub.this.setResult(1);
						finish();
					}
				});
	}

	public void obtenerSubcategorias() {
		ArrayList<Categoria> listCategorias = new ArrayList<Categoria>();
		// Recuperamos el listado del spinner Categorias
		listCategorias = (ArrayList<Categoria>) gestion
				.getCategoriasEditDelete(db, "Subcategorias", "idSubcategoria");
		listaSubcategoriasAdapter.setAdapter(new ListAdapterCategorias(this,
				listCategorias));
	}
}