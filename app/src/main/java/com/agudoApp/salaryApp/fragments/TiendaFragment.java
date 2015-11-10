package com.agudoApp.salaryApp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.agudoApp.salaryApp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class TiendaFragment extends Fragment {
	private static final String KEY_CONTENT = "TiendaFragment:Content";
	private LinearLayout layoutNoPubli;
	private LinearLayout layoutMoreCat;
	private LinearLayout layoutPremium;
	private RelativeLayout layoutPubli;

	// arbitrario) cdigo de solicitud para el flujo de compra
	static final int RC_REQUEST = 10001;

	// Ventana comprar
	static final int MSG_NO_PUBLI = 1;
	static final int MSG_MORE_CAT = 2;
	static final int MSG_PREMIUM = 3;

	// Productos que posee el usuario
	boolean isPremium = false;
	boolean isCategoriaPremium = false;
	boolean isSinPublicidad = false;

	// Podructos integrados
	// id de los productos
	static final String SKU_COMPRA_PREMIUM = "version_premium";
	static final String SKU_CATEGORIAS_PREMIUM = "categorias_premium";
	static final String SKU_SIN_PUBLICIDAD = "sin_publicidad";

	private String mContent = "???";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getString(KEY_CONTENT);
		}
	}

	public TiendaFragment(boolean isUserPremium, boolean isUserSinpublicidad,
			boolean isUserCategoriaPremium) {
		isPremium = isUserPremium;
		isCategoriaPremium = isUserCategoriaPremium;
		isSinPublicidad = isUserSinpublicidad;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.tienda, container, false);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		layoutNoPubli = (LinearLayout) getView().findViewById(
				R.id.layoutNoPubli);
		layoutMoreCat = (LinearLayout) getView().findViewById(
				R.id.layoutMoreCat);
		layoutPremium = (LinearLayout) getView().findViewById(
				R.id.layoutPremium);

		layoutPubli = (RelativeLayout) getView().findViewById(R.id.layoutPubli);

		//Se carga la publicidad
		AdView adView = (AdView) getView().findViewById(R.id.adView);
		if (isPremium || isSinPublicidad) {
			layoutPubli.setVisibility(View.GONE);
		} else {
			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);
		}

		layoutNoPubli.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium || isSinPublicidad) {
					alert(getActivity().getResources().getString(
							R.string.productoComprado));
				} else {
					onCreateDialog(MSG_NO_PUBLI);
				}
			}
		});

		layoutMoreCat.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium || isCategoriaPremium) {
					alert(getActivity().getResources().getString(
							R.string.productoComprado));
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		layoutPremium.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					alert(getActivity().getResources().getString(
							R.string.productoComprado));
				} else {
					onCreateDialog(MSG_PREMIUM);
				}
			}
		});
	}

	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		AlertDialog alert;
		LayoutInflater li = LayoutInflater.from(getActivity());
		View view = null;
		switch (id) {
		case MSG_NO_PUBLI:
			view = li.inflate(R.layout.comprar_nopubli, null);
			builder.setView(view);
			builder.setCancelable(true);
			builder.setPositiveButton(
					getResources().getString(R.string.comprar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//onUpgradeAppButtonClicked(SKU_SIN_PUBLICIDAD);
						}
					}).setNegativeButton(
					getResources().getString(R.string.masTarde),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			alert = builder.create();
			alert.show();
			break;
		case MSG_MORE_CAT:
			view = li.inflate(R.layout.comprar_more_cat, null);
			builder.setView(view);
			builder.setCancelable(true);
			builder.setPositiveButton(
					getResources().getString(R.string.comprar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//onUpgradeAppButtonClicked(SKU_CATEGORIAS_PREMIUM);
						}
					}).setNegativeButton(
					getResources().getString(R.string.masTarde),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			alert = builder.create();
			alert.show();
			break;
		case MSG_PREMIUM:
			view = li.inflate(R.layout.comprar_premium, null);
			builder.setView(view);
			builder.setCancelable(true);
			builder.setPositiveButton(
					getResources().getString(R.string.comprar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//onUpgradeAppButtonClicked(SKU_COMPRA_PREMIUM);
						}
					}).setNegativeButton(
					getResources().getString(R.string.masTarde),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			alert = builder.create();
			alert.show();
			break;
		}
		return null;
	}

	void error(String message) {
		alert(message);
	}

	void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
		bld.setMessage(message);
		bld.setNeutralButton(getResources().getString(R.string.ok), null);
		bld.create().show();
	}

}
