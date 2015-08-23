package com.agudoApp.salaryApp.adapters;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Movimiento;
import com.agudoApp.salaryApp.util.Util;

public class ListaAdapterResumenExpandibleSubAdapter extends
		BaseExpandableListAdapter {

	private Context _context;
	private Activity activity;
	private ArrayList<Object> childtems;
	private LayoutInflater inflater;
	private ArrayList<Categoria> parentItems;
	private ArrayList<Movimiento> child;
	private Movimiento mov;

	public ListaAdapterResumenExpandibleSubAdapter(Context context,
			ArrayList<Categoria> parents, ArrayList<Object> childern) {
		_context = context;
		this.parentItems = parents;
		this.childtems = childern;
	}

	public void setInflater(LayoutInflater inflater, Activity activity) {
		this.inflater = inflater;
		this.activity = activity;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		TextView text;
		TextView text2;
		TextView text3;
		TextView text4;
		LinearLayout linearLayoutLista;

		child = (ArrayList<Movimiento>) childtems.get(groupPosition);
		mov = child.get(childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.lista, null);
		}

		linearLayoutLista = (LinearLayout) convertView
				.findViewById(R.id.linearLayoutLista);

		text = (TextView) convertView.findViewById(R.id.textConcep);
		text2 = (TextView) convertView.findViewById(R.id.textCant);
		text3 = (TextView) convertView.findViewById(R.id.textCat);
		text4 = (TextView) convertView.findViewById(R.id.textFech);

		text.setText(mov.toString());
		text2.setText(mov.getCantidadAux());
		text4.setText(mov.getFecha().toString());

		if (mov.getCantidadAux().substring(0, 1).equals("-")) {
			text2.setTextColor(Color.RED);
		} else {
			text2.setTextColor(Color.argb(255, 98, 186, 14));
			// text2.setTextColor(Color.argb(255, 31, 107, 38));
		}

		if (!mov.getDescSubcategoria().equals("-")) {
			text3.setText(mov.getDescSubcategoria());
		} else {
			Locale locale = Locale.getDefault();
			String languaje = locale.getLanguage();

			if (languaje.equals("es") || languaje.equals("es-rUS")
					|| languaje.equals("ca")) {
				text3.setText("Sin categoria");
			} else if (languaje.equals("fr")) {
				text3.setText("Sans catégorie");
			} else if (languaje.equals("de")) {
				text3.setText("Keine Kategorie");
			} else if (languaje.equals("en")) {
				text3.setText("No category");
			} else if (languaje.equals("it")) {
				text3.setText("Senza categoria");
			} else if (languaje.equals("pt")) {
				text3.setText("Sem categoria");
			} else {
				text3.setText("No category");
			}
		}

		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		float total = 0;

		Categoria cat = parentItems.get(groupPosition);
		ArrayList<Movimiento> listaMov = cat.getListaMovimientos();

		for (int i = 0; i < listaMov.size(); i++) {
			Movimiento mov = listaMov.get(i);
			if (mov.getIdSubcategoria().equals(cat.getId())) {
				total = total + Float.parseFloat(mov.getCantidad());
			}
		}

		DecimalFormat df = new DecimalFormat("0.00");

		String headerTitle = cat.toString();
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater
					.inflate(R.layout.lista_categorias, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.textCategorias);
		lblListHeader.setText(headerTitle);

		TextView lblListCant = (TextView) convertView
				.findViewById(R.id.textCantCategoria);

		lblListCant.setText(String.valueOf(df.format(total)));

		ImageView categoriaIcon = (ImageView) convertView
				.findViewById(R.id.categoriaIcon);

		categoriaIcon.setBackgroundDrawable(_context.getResources()
				.getDrawable(Util.obtenerIconoCategoria(cat.getIdIcon())));

		if (total < 0) {
			lblListCant.setTextColor(Color.RED);
		} else {
			lblListCant.setTextColor(Color.argb(255, 98, 186, 14));
			// lblListCant.setTextColor(Color.argb(255, 31, 107, 38));
		}

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
		return ((ArrayList<Movimiento>) childtems.get(groupPosition)).size();
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
		return true;
	}
}