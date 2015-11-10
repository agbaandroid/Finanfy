package com.agudoApp.salaryApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;

public class CantidadSinDecimalActivity extends Activity {

    private TextView txtCantidad;
    private TextView txtAceptar;

    private LinearLayout num1;
    private LinearLayout num2;
    private LinearLayout num3;
    private LinearLayout num4;
    private LinearLayout num5;
    private LinearLayout num6;
    private LinearLayout num7;
    private LinearLayout num8;
    private LinearLayout num9;
    private LinearLayout num0;
    private LinearLayout numPunto;

    private LinearLayout btnBorrar;
    private LinearLayout btnAceptar;
    private LinearLayout btnCancelar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cantidad_sin_decimales);

        txtCantidad = (TextView) findViewById(R.id.txtCant);
        txtAceptar = (TextView) findViewById(R.id.txtAceptar);

        num0 = (LinearLayout) findViewById(R.id.num0);
        num1 = (LinearLayout) findViewById(R.id.num1);
        num2 = (LinearLayout) findViewById(R.id.num2);
        num3 = (LinearLayout) findViewById(R.id.num3);
        num4 = (LinearLayout) findViewById(R.id.num4);
        num5 = (LinearLayout) findViewById(R.id.num5);
        num6 = (LinearLayout) findViewById(R.id.num6);
        num7 = (LinearLayout) findViewById(R.id.num7);
        num8 = (LinearLayout) findViewById(R.id.num8);
        num9 = (LinearLayout) findViewById(R.id.num9);
        numPunto = (LinearLayout) findViewById(R.id.numPunto);

        btnBorrar = (LinearLayout) findViewById(R.id.btnBorrar);
        btnAceptar = (LinearLayout) findViewById(R.id.btnAceptar);
        btnCancelar = (LinearLayout) findViewById(R.id.btnCancelar);

        num0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!txtCantidad.getText().equals("-") && !txtCantidad.getText().equals("0")) {
                txtCantidad.setText(txtCantidad.getText() + "0");
                btnAceptar.setEnabled(true);
                txtAceptar.setTextColor(getResources().getColor(R.color.txtNegro));
            }
            btnBorrar.setVisibility(View.VISIBLE);

            }
        });

        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCantidad.getText().equals("-")) {
                    txtCantidad.setText("1");
                } else if (txtCantidad.getText().equals("0")) {
                    txtCantidad.setText("0.1");
                } else {
                    txtCantidad.setText(txtCantidad.getText() + "1");
                }
                btnBorrar.setVisibility(View.VISIBLE);
                btnAceptar.setEnabled(true);
                txtAceptar.setTextColor(getResources().getColor(R.color.txtNegro));
            }
        });

        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCantidad.getText().equals("-")) {
                    txtCantidad.setText("2");
                } else if (txtCantidad.getText().equals("0")) {
                    txtCantidad.setText("0.2");
                } else {
                    txtCantidad.setText(txtCantidad.getText() + "2");
                }
                btnBorrar.setVisibility(View.VISIBLE);
                btnAceptar.setEnabled(true);
                txtAceptar.setTextColor(getResources().getColor(R.color.txtNegro));
            }
        });

        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCantidad.getText().equals("-")) {
                    txtCantidad.setText("3");
                } else if (txtCantidad.getText().equals("0")) {
                    txtCantidad.setText("0.3");
                } else {
                    txtCantidad.setText(txtCantidad.getText() + "3");
                }
                btnBorrar.setVisibility(View.VISIBLE);
                btnAceptar.setEnabled(true);
                txtAceptar.setTextColor(getResources().getColor(R.color.txtNegro));
            }
        });

        num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCantidad.getText().equals("-")) {
                    txtCantidad.setText("4");
                } else if (txtCantidad.getText().equals("0")) {
                    txtCantidad.setText("0.4");
                } else {
                    txtCantidad.setText(txtCantidad.getText() + "4");
                }
                btnBorrar.setVisibility(View.VISIBLE);
                btnAceptar.setEnabled(true);
                txtAceptar.setTextColor(getResources().getColor(R.color.txtNegro));
            }
        });

        num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCantidad.getText().equals("-")) {
                    txtCantidad.setText("5");
                } else if (txtCantidad.getText().equals("0")) {
                    txtCantidad.setText("0.5");
                } else {
                    txtCantidad.setText(txtCantidad.getText() + "5");
                }
                btnBorrar.setVisibility(View.VISIBLE);
                btnAceptar.setEnabled(true);
                txtAceptar.setTextColor(getResources().getColor(R.color.txtNegro));
            }
        });

        num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCantidad.getText().equals("-")) {
                    txtCantidad.setText("6");
                } else if (txtCantidad.getText().equals("0")) {
                    txtCantidad.setText("0.6");
                } else {
                    txtCantidad.setText(txtCantidad.getText() + "6");
                }
                btnBorrar.setVisibility(View.VISIBLE);
                btnAceptar.setEnabled(true);
                txtAceptar.setTextColor(getResources().getColor(R.color.txtNegro));
            }
        });

        num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCantidad.getText().equals("-")) {
                    txtCantidad.setText("7");
                } else if (txtCantidad.getText().equals("0")) {
                    txtCantidad.setText("0.7");
                } else {
                    txtCantidad.setText(txtCantidad.getText() + "7");
                }
                btnBorrar.setVisibility(View.VISIBLE);
                btnAceptar.setEnabled(true);
                txtAceptar.setTextColor(getResources().getColor(R.color.txtNegro));
            }
        });

        num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCantidad.getText().equals("-")) {
                    txtCantidad.setText("8");
                } else if (txtCantidad.getText().equals("0")) {
                    txtCantidad.setText("0.8");
                } else {
                    txtCantidad.setText(txtCantidad.getText() + "8");
                }
                btnBorrar.setVisibility(View.VISIBLE);
                btnAceptar.setEnabled(true);
                txtAceptar.setTextColor(getResources().getColor(R.color.txtNegro));
            }
        });

        num9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCantidad.getText().equals("-")) {
                    txtCantidad.setText("9");
                } else if (txtCantidad.getText().equals("0")) {
                    txtCantidad.setText("0.9");
                } else {
                    txtCantidad.setText(txtCantidad.getText() + "9");
                }
                btnBorrar.setVisibility(View.VISIBLE);
                btnAceptar.setEnabled(true);
                txtAceptar.setTextColor(getResources().getColor(R.color.txtNegro));
            }
        });

        numPunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtCantidad.getText().equals("-")) {
                    if (txtCantidad.getText().toString().indexOf(".") == -1) {
                        txtCantidad.setText(txtCantidad.getText() + ".");
                    }
                }
                btnBorrar.setVisibility(View.VISIBLE);
                btnAceptar.setEnabled(false);
                txtAceptar.setTextColor(getResources().getColor(R.color.txtGris));
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtCantidad.getText().equals("-")) {
                    txtCantidad.setText(txtCantidad.getText().toString().substring(0, txtCantidad.getText().toString().length() - 1));
                }

                if (txtCantidad.getText().equals("")) {
                    txtCantidad.setText("-");
                    btnBorrar.setVisibility(View.INVISIBLE);
                } else {
                    btnBorrar.setVisibility(View.VISIBLE);
                }

                if ((txtCantidad.getText().toString().substring(txtCantidad.getText().toString().length() - 1,
                        txtCantidad.getText().toString().length()).equals("."))) {
                    btnAceptar.setEnabled(false);
                    txtAceptar.setTextColor(getResources().getColor(R.color.txtGris));
                } else {
                    btnAceptar.setEnabled(true);
                    txtAceptar.setTextColor(getResources().getColor(R.color.txtNegro));
                }
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String importe;
                if (txtCantidad.getText().toString().equals("-")) {
                    importe = "0";
                } else {
                    importe = txtCantidad.getText().toString();
                }

                getIntent().putExtra("importe", importe);
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
