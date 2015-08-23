package com.agudoApp.salaryApp.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.DeleteCat;
import com.agudoApp.salaryApp.activities.EditCatTexto;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.util.Util;

public class ListaAdapterPestanaCatExpandibleAdapter extends
		BaseExpandableListAdapter {

	private Context _context;
	private Activity activity;
	private ArrayList<String> childtems;
	private LayoutInflater inflater;
	private ArrayList<Categoria> parentItems;
	private Categoria categoria = new Categoria();
	private boolean isPremium = false;

	public ListaAdapterPestanaCatExpandibleAdapter(Context context,
			ArrayList<Categoria> parents, boolean isUserPremium) {
		_context = context;
		this.parentItems = parents;
		isPremium = isUserPremium;

	}

	public void setInflater(LayoutInflater inflater, Activity activity) {
		this.inflater = inflater;
		this.activity = activity;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ImageView editIcon;
		ImageView deleteIcon;
		TextView editText;
		TextView deleteText;
		LinearLayout layoutEdit;
		LinearLayout layoutDelete;

		categoria = (Categoria) this.parentItems.get(groupPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.child_categoria, null);
		}

		layoutEdit = (LinearLayout) convertView.findViewById(R.id.layoutEdit);
		layoutDelete = (LinearLayout) convertView
				.findViewById(R.id.layoutDelete);
		editIcon = (ImageView) convertView.findViewById(R.id.editIcon);
		deleteIcon = (ImageView) convertView.findViewById(R.id.deleteIcon);
		editText = (TextView) convertView.findViewById(R.id.editText);
		deleteText = (TextView) convertView.findViewById(R.id.deleteText);

		layoutEdit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(_context, EditCatTexto.class);
				intent.putExtra("id", categoria.getId());
				intent.putExtra("textEdit", categoria.toString());
				intent.putExtra("idIcon", categoria.getIdIcon());
				intent.putExtra("isPremium", isPremium);
				_context.startActivity(intent);
			}
		});

		editIcon.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(_context, EditCatTexto.class);
				intent.putExtra("id", categoria.getId());
				intent.putExtra("textEdit", categoria.toString());
				intent.putExtra("idIcon", categoria.getIdIcon());
				intent.putExtra("isPremium", isPremium);
				_context.startActivity(intent);
			}

		});

		editText.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(_context, EditCatTexto.class);
				intent.putExtra("id", categoria.getId());
				intent.putExtra("textEdit", categoria.toString());
				intent.putExtra("idIcon", categoria.getIdIcon());
				intent.putExtra("isPremium", isPremium);
				_context.startActivity(intent);
			}

		});

		layoutDelete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(_context, DeleteCat.class);
				intent.putExtra("id", categoria.getId());
				intent.putExtra("textEdit", categoria.toString());
				intent.putExtra("idIcon", categoria.getIdIcon());
				_context.startActivity(intent);
			}
		});

		deleteIcon.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(_context, DeleteCat.class);
				intent.putExtra("id", categoria.getId());
				intent.putExtra("textEdit", categoria.toString());
				intent.putExtra("idIcon", categoria.getIdIcon());
				_context.startActivity(intent);
			}

		});

		deleteText.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(_context, DeleteCat.class);
				intent.putExtra("id", categoria.getId());
				_context.startActivity(intent);
			}

		});

		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		float total = 0;

		Categoria cat = parentItems.get(groupPosition);

		String headerTitle = cat.toString();
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(
					R.layout.lista_pestana_categorias, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.textCategoriasEditDelete);
		lblListHeader.setText(headerTitle);
		ImageView imagenCat = (ImageView) convertView
				.findViewById(R.id.imagenCat);
		imagenCat.setBackgroundDrawable(_context.getResources().getDrawable(
				Util.obtenerIconoCategoria(cat.getIdIcon())));

		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return parentItems.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}