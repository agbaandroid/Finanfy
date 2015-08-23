package com.agudoApp.salaryApp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.fragments.IconosCategoriaLiteFragment;
import com.agudoApp.salaryApp.fragments.IconosCategoriaPremiumFragment;


public class Categorias extends FragmentActivity {
	private FragmentTabHost mTabHost;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	boolean isPremium = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			isPremium = extras.getBoolean("isPremium", false);
		}

		Bundle args = new Bundle();
		args.putBoolean("isPremium", isPremium);

		setContentView(R.layout.tabs_categorias);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		mTabHost.addTab(
				mTabHost.newTabSpec(getResources().getString(R.string.iconLite))
						.setIndicator(
								getResources().getString(R.string.iconLite)),
				IconosCategoriaLiteFragment.class, args);
		mTabHost.addTab(
				mTabHost.newTabSpec(
						getResources().getString(R.string.iconPremium))
						.setIndicator(
								getResources().getString(R.string.iconPremium)),
				IconosCategoriaPremiumFragment.class, args);

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

	/** Verifica la "developer payload" de una compra */
	/*boolean verifyDeveloperPayload(Purchase p) {
		return true;
	}*/

	void error(String message) {
		alert(message);
	}
}
