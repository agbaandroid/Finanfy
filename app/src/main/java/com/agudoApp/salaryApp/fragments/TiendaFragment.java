package com.agudoApp.salaryApp.fragments;

import android.support.v4.app.Fragment;

public class TiendaFragment  {
/*	private static final String KEY_CONTENT = "TiendaFragment:Content";
	private LinearLayout layoutNoPubli;
	private LinearLayout layoutMoreCat;
	private LinearLayout layoutPremium;

	// arbitrario) cdigo de solicitud para el flujo de compra
	static final int RC_REQUEST = 10001;

	// The helper object
	IabHelper mHelper;

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

		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtE1VzKOP/eS4Q8sV9F/"
				+ "2iCONOmNXvdrbjJmFe3cpevp9Bfm4TK7mm4ZNzG8UBAIntUlFkLFm1T5medFnohtklilMPM5D3NkuAQVd9uRsRZ"
				+ "wB6nakkE7Kz0ayv+h81SyiL2fzWiZSMT355tdCmFM730hWqSuK5syd60wOPYv8FUzqf1cjXQ8kZdIHTiAnJqox3k"
				+ "IswcLz2WnaQSvKiSDIUmNGqL6BxLKx0wF+h181nb6AcZ6FlhphfmATAI5DJUxTXAs7m0D0bAE6xnh66uXGhWKDxJ"
				+ "AXkXZ0n2lsUi+ZEofK6Jw4NnsIRi3BDy5G16ILD4ELLZWj4zSXBhCXFVC9TQIDAQAB";

		// Cree el helper, pas�ndole nuestro contexto y la clave p�blica para
		// verificar la firma
		mHelper = new IabHelper(getActivity(), base64EncodedPublicKey);

		// Iniciar configuraci�n. Esta es as�ncrona y el listener
		// especificado ser� llamado una vez completada la configuraci�n.
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {

				if (!result.isSuccess()) {
					// �Oh, no, hay un problema.
					return;
				}

				// Entretanto �Hemos sido eliminados? Si es as�, deje de
				// hacerlo.
				if (mHelper == null)
					return;

				// IAB est� totalmente configurado. Ahora, vamos a obtener un
				// inventario de las cosas que poseemos.
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});

		layoutNoPubli = (LinearLayout) getView().findViewById(
				R.id.layoutNoPubli);
		layoutMoreCat = (LinearLayout) getView().findViewById(
				R.id.layoutMoreCat);
		layoutPremium = (LinearLayout) getView().findViewById(
				R.id.layoutPremium);

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

	// A�adiendo las opciones de men�
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_setting, menu);
	}

	// A�adiendo funcionalidad a las opciones de men�
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		LayoutInflater li = LayoutInflater.from(getActivity());
		View view = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		AlertDialog alert;
		switch (item.getItemId()) {
		case R.id.btInfo:
			view = li.inflate(R.layout.info, null);
			builder.setView(view);
			builder.setTitle(getResources().getString(R.string.informacion));
			builder.setIcon(R.drawable.ic_info_azul);
			builder.setCancelable(false);
			builder.setPositiveButton(getResources()
					.getString(R.string.aceptar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			alert = builder.create();
			alert.show();
			return true;
		case R.id.btAcerca:
			view = li.inflate(R.layout.acerca, null);
			builder.setView(view);
			builder.setTitle(getResources().getString(R.string.app_name));
			builder.setIcon(R.drawable.icon_app);
			builder.setCancelable(false);
			builder.setPositiveButton(getResources()
					.getString(R.string.aceptar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			alert = builder.create();
			alert.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
							onUpgradeAppButtonClicked(SKU_SIN_PUBLICIDAD);
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
							onUpgradeAppButtonClicked(SKU_CATEGORIAS_PREMIUM);
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
							onUpgradeAppButtonClicked(SKU_COMPRA_PREMIUM);
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

	// --------------------- art�culo NO consumible -------------------------
	public void onUpgradeAppButtonClicked(String producto) {
		Log.d("info", "Comprar art�culo NO consumible.");

		mHelper.launchPurchaseFlow(getActivity(), producto, RC_REQUEST,
				mPurchaseFinishedListener, null);

		// mHelper.launchPurchaseFlow(getActivity(), "android.test.purchased",
		// RC_REQUEST, mPurchaseFinishedListener, null);

		SharedPreferences prefs = getActivity().getSharedPreferences(
				"ficheroConf", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean("isCompra", true);
		editor.commit();

	}

	// Callback para cuando se termina una compra
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {

			if (mHelper == null)
				return;

			if (result.isFailure()) {
				SharedPreferences prefs = getActivity().getSharedPreferences(
						"ficheroConf", Context.MODE_PRIVATE);
				Editor editor = prefs.edit();
				editor.putBoolean("isCompra", false);
				editor.commit();
				switch (result.getResponse()) {
				case IabHelper.IABHELPER_USER_CANCELLED:
					break;
				case IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED:
					error(getActivity().getResources().getString(
							R.string.productoComprado));
					break;
				default:
					error(getResources().getString(R.string.errorCompra));
					break;
				}
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				error(getResources().getString(R.string.errorVeri));
				return;
			}

			// TODO: Si se compra un art�culo consumible
			// cambiar
			if (purchase.getSku().equals(SKU_COMPRA_PREMIUM)) {
				isPremium = true;
			}
			if (purchase.getSku().equals(SKU_SIN_PUBLICIDAD)) {
				isSinPublicidad = true;
			}
			if (purchase.getSku().equals(SKU_CATEGORIAS_PREMIUM)) {
				isCategoriaPremium = true;
			}
		}
	};

* Verifica la "developer payload" de una compra

	boolean verifyDeveloperPayload(Purchase p) {
		return true;
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

	// Listener, se llama cuando terminamos de consultar los art�culos y las
	// suscripciones que poseemos.
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			// Entretanto �Hemos sido eliminados? Si es as�, deje de hacerlo.
			if (mHelper == null)
				return;

			// �Es un error?
			if (result.isFailure()) {
				return;
			}

			// eliminar
			// if (inventory.hasPurchase("android.test.purchased")) {
			// mHelper.consumeAsync(
			// inventory.getPurchase("android.test.purchased"), null);
			// }

			 * Compruebe que art�culos poseemos. N�tese que por cada compra,
			 * comprobamos el "developer payload" para ver si es correcta! Ver
			 * verifyDeveloperPayload ().


			if (inventory.hasPurchase(SKU_COMPRA_PREMIUM)) {
				isPremium = true;
			}

			if (inventory.hasPurchase(SKU_CATEGORIAS_PREMIUM)) {
				isCategoriaPremium = true;
			}

			if (inventory.hasPurchase(SKU_SIN_PUBLICIDAD)) {
				isSinPublicidad = true;
			}
		}
	};*/
}
