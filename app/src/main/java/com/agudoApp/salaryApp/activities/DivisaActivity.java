package com.agudoApp.salaryApp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;

public class DivisaActivity extends AppCompatActivity {

    private LinearLayout btnDefecto;
    private LinearLayout btnPers;
    private TextView txtDefecto;
    private TextView txtPers;

    private LinearLayout btnIzq;
    private LinearLayout btnDer;
    private TextView txtIzq;
    private TextView txtDer;

    private LinearLayout layoutPers;
    private TextView txtCant;

    private EditText simbolo;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.divisa);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setTitle(getResources().getString(R.string.divisa));
        setSupportActionBar(toolbar);

        // Hide the icon, title and home/up button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        btnDefecto = (LinearLayout) findViewById(R.id.btnDefecto);
        btnPers = (LinearLayout) findViewById(R.id.btnPers);
        txtDefecto = (TextView) findViewById(R.id.txtDefecto);
        txtPers = (TextView) findViewById(R.id.txtPers);

        btnIzq = (LinearLayout) findViewById(R.id.btnIzq);
        btnDer = (LinearLayout) findViewById(R.id.btnDer);
        txtIzq = (TextView) findViewById(R.id.txtIzq);
        txtDer = (TextView) findViewById(R.id.txtDer);

        layoutPers = (LinearLayout) findViewById(R.id.layoutPers);
        txtCant = (TextView) findViewById(R.id.txtCant);

        simbolo = (EditText) findViewById(R.id.simbolo);

        prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
        editor = prefs.edit();

        configurarDivisa();

        btnDefecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
                editor = prefs.edit();

                editor.putBoolean("defecto", true);
                editor.commit();
                configurarDivisa();
            }
        });

        btnPers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
                editor = prefs.edit();

                editor.putBoolean("defecto", false);
                editor.commit();
                configurarDivisa();
            }
        });

        btnIzq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
                editor = prefs.edit();

                editor.putBoolean("derecha", false);
                editor.commit();
                configurarDivisa();
            }
        });

        btnDer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
                editor = prefs.edit();

                editor.putBoolean("derecha", true);
                editor.commit();
                configurarDivisa();
            }
        });

        simbolo.addTextChangedListener(mTextEditorWatcher);

    }

    private final TextWatcher  mTextEditorWatcher = new TextWatcher() {
        private String lastValue = "";

        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
        }

        public void afterTextChanged(Editable s)
        {

            String newValue = simbolo.getText().toString();
            if (!newValue.equals(lastValue)) {
                lastValue = newValue;

                cambiarText();
                simbolo.setText(newValue);
                simbolo.setSelection(simbolo.getText().toString().length());
            }
        }
    };


    public void cambiarText(){
        prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
        editor = prefs.edit();

        editor.putString("simboloPer", simbolo.getText().toString());
        editor.commit();

        configurarDivisa();
    }

    public void configurarDivisa(){
        prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);

        boolean defecto = prefs.getBoolean("defecto", true);
        boolean der = prefs.getBoolean("derecha", true);

        String simboloConf;

        if(defecto){
            btnDefecto.setBackgroundResource(R.drawable.rounded_layout_azul);
            btnPers.setBackgroundResource(R.drawable.rounded_layout_gris);
            txtDefecto.setTextColor(getResources().getColor(R.color.blanco));
            txtPers.setTextColor(getResources().getColor(R.color.txtGris));

            layoutPers.setVisibility(View.GONE);
            simboloConf = "€";

            txtCant.setText("12345,67 " + simboloConf);
        }else{
            btnPers.setBackgroundResource(R.drawable.rounded_layout_azul);
            btnDefecto.setBackgroundResource(R.drawable.rounded_layout_gris);
            txtPers.setTextColor(getResources().getColor(R.color.blanco));
            txtDefecto.setTextColor(getResources().getColor(R.color.txtGris));

            layoutPers.setVisibility(View.VISIBLE);
            simboloConf = prefs.getString("simboloPer", "");

            simbolo.setText(simboloConf);

            if(der){
                btnDer.setBackgroundResource(R.drawable.rounded_layout_azul);
                btnIzq.setBackgroundResource(R.drawable.rounded_layout_gris);
                txtDer.setTextColor(getResources().getColor(R.color.blanco));
                txtIzq.setTextColor(getResources().getColor(R.color.txtGris));
            }else{
                btnIzq.setBackgroundResource(R.drawable.rounded_layout_azul);
                btnDer.setBackgroundResource(R.drawable.rounded_layout_gris);
                txtIzq.setTextColor(getResources().getColor(R.color.blanco));
                txtDer.setTextColor(getResources().getColor(R.color.txtGris));
            }

            if (der) {
                txtCant.setText("12345,67 " + simboloConf);
            } else {
                txtCant.setText(simboloConf + " 12345,67");
            }
        }
    }

    // Anadiendo funcionalidad a las opciones de menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
