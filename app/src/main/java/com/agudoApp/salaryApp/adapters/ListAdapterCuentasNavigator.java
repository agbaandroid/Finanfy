package com.agudoApp.salaryApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Cuenta;
import com.agudoApp.salaryApp.util.Util;

import java.util.ArrayList;
import java.util.Locale;

public class ListAdapterCuentasNavigator extends BaseAdapter {
	private LayoutInflater mInflater;
	private int mSelectedItem;
	private ArrayList<Cuenta> listaCuentas;
	Locale locale = Locale.getDefault();
	String languaje = locale.getLanguage();

	public ListAdapterCuentasNavigator(Context context, ArrayList<Cuenta> lista) {
		listaCuentas = lista;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listaCuentas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listaCuentas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView text;
		ImageView icon;
		LinearLayout layoutNavigator;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.lista_navigator_cuentas, null);
		}

		text = (TextView) convertView.findViewById(R.id.textNavigatorCuentas);
		icon = (ImageView) convertView.findViewById(R.id.iconNavigatorCuentas);
		layoutNavigator = (LinearLayout) convertView.findViewById(R.id.layoutNavigatorCuentas);
		text.setText(listaCuentas.get(position).getDescCuenta());
		
		layoutNavigator.setBackgroundResource(R.color.blanco);

		icon.setBackgroundResource(Util.obtenerIconoUser(listaCuentas.get(position).getIdIcon()));
		return convertView;
	}

	public int getSelectedItem() {
		return mSelectedItem;
	}

	public void setSelectedItem(int selectedItem) {
		mSelectedItem = selectedItem;
	}

}
