package com.agudoApp.salaryApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Icon;
import com.agudoApp.salaryApp.util.Util;

import java.util.ArrayList;

public class ListAdapterIconTarjeta extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Icon> listaIcon = new ArrayList<Icon>();
    private Context context;

    public ListAdapterIconTarjeta(Context context, ArrayList<Icon> lista) {
        listaIcon = lista;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listaIcon.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listaIcon.get(position);
    }

    public int getPositionById(String id) {
        int posi = 0;
        for (int i = 0; i < listaIcon.size(); i++) {
            Icon icon = listaIcon.get(i);
            if (icon.getId() == Integer.parseInt(id)) {
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
        ImageView iconTarjeta;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lista_icon_tarjeta, null);
        }

        iconTarjeta = (ImageView) convertView.findViewById(R.id.imagenTarjeta);
        iconTarjeta.setBackgroundDrawable(context.getResources().getDrawable(
                Util.obtenerIconoTarjeta(listaIcon.get(position).getId())));
        return convertView;
    }

}
