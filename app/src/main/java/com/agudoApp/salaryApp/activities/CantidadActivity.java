package com.agudoApp.salaryApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;

public class CantidadActivity extends Activity {

    private TextView txtCantidad;

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
        setContentView(R.layout.cantidad);

        txtCantidad = (TextView) findViewById(R.id.txtCant);

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
                if (txtCantidad.getText().equals("-")) {
                    txtCantidad.setText("0.");
                } else if(txtCantidad.getText().equals("0")){
                    txtCantidad.setText("0.0");
                }else {
                    txtCantidad.setText(txtCantidad.getText() + "0");
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
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCantidad.setText(txtCantidad.getText() + "0");
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
