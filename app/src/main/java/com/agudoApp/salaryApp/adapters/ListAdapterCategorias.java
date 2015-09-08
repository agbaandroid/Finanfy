package com.agudoApp.salaryApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.util.Util;

import java.util.ArrayList;

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

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lista_pestana_categorias, null);
        }

        text = (TextView) convertView.findViewById(R.id.txtCategoria);
        text.setText(listaCat.get(position).toString());

        iconCategoria = (ImageView) convertView.findViewById(R.id.imagenCat);
        iconCategoria.setBackgroundDrawable(context.getResources().getDrawable(
                Util.obtenerIconoCategoria(listaCat.get(position).getIdIcon())));
        return convertView;
    }

}
