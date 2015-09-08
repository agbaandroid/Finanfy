package com.agudoApp.salaryApp.adapters;

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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListaAdapterResumenExpandibleAdapter extends
		BaseExpandableListAdapter {

	private Context _context;
	private Activity activity;
	private ArrayList<Object> childtems;
	private LayoutInflater inflater;
	private ArrayList<Categoria> parentItems;
	private ArrayList<Movimiento> child;
	private Movimiento mov;

	public ListaAdapterResumenExpandibleAdapter(Context context,
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
		LinearLayout layoutTarjeta;

		child = (ArrayList<Movimiento>) childtems.get(groupPosition);
		mov = child.get(childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.lista, null);
		}

		text = (TextView) convertView.findViewById(R.id.txtDescripcion);
		text2 = (TextView) convertView.findViewById(R.id.txtCant);
		text3 = (TextView) convertView.findViewById(R.id.txtCategoria);
		text4 = (TextView) convertView.findViewById(R.id.txtFecha);
		layoutTarjeta = (LinearLayout) convertView.findViewById(R.id.layoutTarjeta);

		text.setText(mov.toString());
		if(mov.toString().equals("")){
			text.setVisibility(View.GONE);
		}

		text2.setText(mov.getCantidadAux());
		text4.setText(mov.getFecha().toString());

		if (mov.getCantidadAux().substring(0, 1).equals("-")) {
			text2.setTextColor(Color.RED);
		} else {
			text2.setTextColor(_context.getResources().getColor(R.color.txtAzul));
		}

		if (!mov.getDescSubcategoria().equals("-")) {
			text3.setText(mov.getDescSubcategoria());
		} else {
			text3.setText(_context.getResources().getString(R.string.otros));
		}

		if (mov.isTarjeta()) {
			layoutTarjeta.setVisibility(View.VISIBLE);
		}else{
			layoutTarjeta.setVisibility(View.GONE);
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
			if (mov.getIdCategoria().equals(cat.getId())) {
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
				.findViewById(R.id.txtCategoria);
		lblListHeader.setText(headerTitle);

		TextView lblListCant = (TextView) convertView
				.findViewById(R.id.txtCant);

		lblListCant.setText(String.valueOf(df.format(total)));

		ImageView categoriaIcon = (ImageView) convertView
				.findViewById(R.id.iconCategoria);

		categoriaIcon.setBackgroundDrawable(_context.getResources()
				.getDrawable(Util.obtenerIconoCategoria(cat.getIdIcon())));

		if (total < 0) {
			lblListCant.setTextColor(Color.RED);
		} else {
			lblListCant.setTextColor(_context.getResources().getColor(R.color.txtAzul));
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