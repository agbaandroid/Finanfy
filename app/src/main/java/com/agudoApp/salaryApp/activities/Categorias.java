package com.agudoApp.salaryApp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.fragments.IconosCategoriaLiteFragment;
import com.agudoApp.salaryApp.fragments.IconosCategoriaPremiumFragment;


public class Categorias extends FragmentActivity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    boolean isPremium = false;
    boolean isCategoriaPremium = false;

    LinearLayout layoutLite;
    LinearLayout layoutPremium;
    boolean isPestanaLite = false;
    boolean isPestanaPremium = false;

    TextView textLite;
    TextView textPremium;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tabs_categorias);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isPremium = extras.getBoolean("isPremium", false);
            isCategoriaPremium = extras.getBoolean("isCategoriaPremium", false);
        }

        Bundle args = new Bundle();
        args.putBoolean("isPremium", isPremium);

        layoutLite = (LinearLayout) findViewById(R.id.pestanaLite);
        layoutPremium = (LinearLayout) findViewById(R.id.pestanaPremium);

        textLite = (TextView) findViewById(R.id.textLite);
        textPremium = (TextView) findViewById(R.id.textPremium);

        Fragment fragment = new IconosCategoriaLiteFragment();
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.realtabcontent, fragment).commit();

            isPestanaLite = true;

        }

        layoutLite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isPestanaLite) {
                    Fragment fragment = new IconosCategoriaLiteFragment();
                    if (fragment != null) {
                        FragmentManager fragmentManager =
                                getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.realtabcontent, fragment)
                                .commit();

                        isPestanaLite = true;
                        isPestanaPremium = false;

                        textLite.setTextColor(Color.BLACK);
                        textPremium.setTextColor(Color.rgb(195, 195, 195));
                    }
                }
            }
        });

        layoutPremium.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isPestanaPremium) {
                    Fragment fragment = new IconosCategoriaPremiumFragment();
                    if (fragment != null) {
                        FragmentManager fragmentManager =
                                getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.realtabcontent, fragment)
                                .commit();

                        isPestanaPremium = true;
                        isPestanaLite = false;

                        textPremium.setTextColor(Color.BLACK);
                        textLite.setTextColor(Color.rgb(195, 195, 195));
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        prefs = this.getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
        boolean isCompra = prefs.getBoolean("isCompra", false);
        if (isCompra && resultCode == -1) {
            editor = prefs.edit();
            editor.putBoolean("isCompra", false);
            editor.commit();
            alert(getResources().getString(R.string.gracias));
        } else if (isCompra && resultCode == 0) {
            editor = prefs.edit();
            editor.putBoolean("isCompra", false);
            editor.commit();
        }
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton(getResources().getString(R.string.ok), null);
        bld.create().show();
    }

    /**
     * Verifica la "developer payload" de una compra
     */
    /*boolean verifyDeveloperPayload(Purchase p) {
        return true;
	}*/

    void error(String message) {
        alert(message);
    }
}
