package com.agudoApp.salaryApp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.util.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListAdapterCatGrafico extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Categoria> listaCat = new ArrayList<Categoria>();
    private Context _context;
    private float total = 0;
    SharedPreferences prefs;

    public ListAdapterCatGrafico(Context context, ArrayList<Categoria> lista) {
        listaCat = lista;
        mInflater = LayoutInflater.from(context);
        _context = context;
        prefs = _context.getSharedPreferences("ficheroConf",
                Context.MODE_PRIVATE);

        total = obtenerTotal();
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
        Categoria cat = listaCat.get(position);

        String headerTitle = cat.toString();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lista_categorias_grafico,
                    null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.textCategoriasGrafico);
        lblListHeader.setText(headerTitle);
        ImageView imagenCat = (ImageView) convertView
                .findViewById(R.id.imagenCat);
        imagenCat.setBackgroundDrawable(_context.getResources().getDrawable(
                Util.obtenerIconoCategoria(cat.getIdIcon())));

        TextView porcentaje = (TextView) convertView
                .findViewById(R.id.textPorcentaje);
        porcentaje.setText(obtenerPorcentaje(cat.getTotal()));

        return convertView;
    }

    public float obtenerTotal() {
        float tot = 0;

        for (int i = 0; i < listaCat.size(); i++) {
            tot = tot + listaCat.get(i).getTotal();
        }
        return tot;
    }

    public String obtenerPorcentaje(float cant) {
        float por = 0;
        String porcentaje;
        String cantidad;
        String texto;

        DecimalFormat df = new DecimalFormat("0.00");
        por = (cant * 100) / total;
        porcentaje = df.format(por);
        cantidad = Util.formatear(cant, prefs);

        texto = porcentaje + "% (" + cantidad + ")";
        return texto;
    }


}
