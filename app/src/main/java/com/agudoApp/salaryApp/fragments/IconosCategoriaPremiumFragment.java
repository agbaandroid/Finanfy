package com.agudoApp.salaryApp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.general.ControlGastosActivity;

public class IconosCategoriaPremiumFragment extends Fragment {/*
	private static final String KEY_CONTENT = "PestanaCategoriaFragment:Content";
	private SQLiteDatabase db;
	final GestionBBDD gestion = new GestionBBDD();
	static final int DATABASE = 1;
	private final String BD_NOMBRE = "BDGestionGastos";
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	private boolean isPremium = false;

	// arbitrario) cdigo de solicitud para el flujo de compra
	static final int RC_REQUEST = 10001;

	// The helper object
	IabHelper mHelper;

	static final int MSG_MORE_CAT = 1;
	static final String SKU_CATEGORIAS_PREMIUM = "categorias_premium";

	private ImageView imagenCat48;
	private ImageView imagenCat49;
	private ImageView imagenCat50;
	private ImageView imagenCat51;
	private ImageView imagenCat52;
	private ImageView imagenCat53;
	private ImageView imagenCat54;
	private ImageView imagenCat55;
	private ImageView imagenCat56;
	private ImageView imagenCat57;
	private ImageView imagenCat58;
	private ImageView imagenCat59;
	private ImageView imagenCat60;
	private ImageView imagenCat61;
	private ImageView imagenCat62;
	private ImageView imagenCat63;
	private ImageView imagenCat64;
	private ImageView imagenCat65;
	private ImageView imagenCat66;
	private ImageView imagenCat67;

	String id;
	String textEdit;
	String tipo;
	String flujo;

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.categorias_premium, container, false);
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

		Bundle bundle = getArguments();
		isPremium = bundle.getBoolean("isPremium", false);

		Bundle extras = getActivity().getIntent().getExtras();
		if (extras != null) {
			id = extras.getString("id");
			textEdit = extras.getString("textEdit");
			tipo = extras.getString("tipo");
			flujo = extras.getString("flujo");
			isPremium = extras.getBoolean("isPremium", false);
		}

		if (id == null) {
			id = "";
		}
		if (textEdit == null) {
			textEdit = "";
		}
		if (tipo == null) {
			tipo = "";
		}
		if (flujo == null) {
			flujo = "";
		}

		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtE1VzKOP/eS4Q8sV9F/"
				+ "2iCONOmNXvdrbjJmFe3cpevp9Bfm4TK7mm4ZNzG8UBAIntUlFkLFm1T5medFnohtklilMPM5D3NkuAQVd9uRsRZ"
				+ "wB6nakkE7Kz0ayv+h81SyiL2fzWiZSMT355tdCmFM730hWqSuK5syd60wOPYv8FUzqf1cjXQ8kZdIHTiAnJqox3k"
				+ "IswcLz2WnaQSvKiSDIUmNGqL6BxLKx0wF+h181nb6AcZ6FlhphfmATAI5DJUxTXAs7m0D0bAE6xnh66uXGhWKDxJ"
				+ "AXkXZ0n2lsUi+ZEofK6Jw4NnsIRi3BDy5G16ILD4ELLZWj4zSXBhCXFVC9TQIDAQAB";

		// Cree el helper, pasndole nuestro contexto y la clave pblica para
		// verificar la firma
		mHelper = new IabHelper(getActivity(), base64EncodedPublicKey);

		// Iniciar configuracin. Esta es asncrona y el listener
		// especificado ser llamado una vez completada la configuracin.
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {

				if (!result.isSuccess()) {
					// Oh, no, hay un problema.
					return;
				}

				// Entretanto Hemos sido eliminados? Si es as, deje de
				// hacerlo.
				if (mHelper == null)
					return;

				// IAB est totalmente configurado. Ahora, vamos a obtener un
				// inventario de las cosas que poseemos.
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});

		db = getActivity().openOrCreateDatabase(BD_NOMBRE, 1, null);

		imagenCat48 = (ImageView) getView().findViewById(R.id.imagenCat48);
		imagenCat49 = (ImageView) getView().findViewById(R.id.imagenCat49);
		imagenCat50 = (ImageView) getView().findViewById(R.id.imagenCat50);
		imagenCat51 = (ImageView) getView().findViewById(R.id.imagenCat51);
		imagenCat52 = (ImageView) getView().findViewById(R.id.imagenCat52);
		imagenCat53 = (ImageView) getView().findViewById(R.id.imagenCat53);
		imagenCat54 = (ImageView) getView().findViewById(R.id.imagenCat54);
		imagenCat55 = (ImageView) getView().findViewById(R.id.imagenCat55);
		imagenCat56 = (ImageView) getView().findViewById(R.id.imagenCat56);
		imagenCat57 = (ImageView) getView().findViewById(R.id.imagenCat57);
		imagenCat58 = (ImageView) getView().findViewById(R.id.imagenCat58);
		imagenCat59 = (ImageView) getView().findViewById(R.id.imagenCat59);
		imagenCat60 = (ImageView) getView().findViewById(R.id.imagenCat60);
		imagenCat61 = (ImageView) getView().findViewById(R.id.imagenCat61);
		imagenCat62 = (ImageView) getView().findViewById(R.id.imagenCat62);
		imagenCat63 = (ImageView) getView().findViewById(R.id.imagenCat63);
		imagenCat64 = (ImageView) getView().findViewById(R.id.imagenCat64);
		imagenCat65 = (ImageView) getView().findViewById(R.id.imagenCat65);
		imagenCat66 = (ImageView) getView().findViewById(R.id.imagenCat66);
		imagenCat67 = (ImageView) getView().findViewById(R.id.imagenCat67);

		imagenCat48.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(47);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 47);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 47);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat49.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(48);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 48);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 48);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat50.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(49);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 49);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 49);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat51.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(50);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 50);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 50);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat52.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(51);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 51);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 51);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat53.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(52);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 52);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 52);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat54.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(53);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 53);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 53);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat55.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(54);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 54);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 54);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat56.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(55);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 55);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 55);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat57.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(56);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 56);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 56);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat58.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(57);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 57);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 57);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat59.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(58);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 58);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 58);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat60.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(59);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 59);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 59);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat61.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(60);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 60);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 60);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat62.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(61);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 61);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 61);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat63.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(62);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 62);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 62);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat64.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(63);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 63);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 63);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat65.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(64);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 64);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 64);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat66.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(65);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 65);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 65);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat67.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(66);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 66);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 66);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

	}

	public void guardarIdIcon(int idIcon) {
		prefs = getActivity().getSharedPreferences("ficheroConf",
				Context.MODE_PRIVATE);
		editor = prefs.edit();
		editor.putInt("idIcon", idIcon);
		editor.commit();
	}

	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		AlertDialog alert;
		LayoutInflater li = LayoutInflater.from(getActivity());
		View view = null;
		switch (id) {
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
		}
		return null;
	}

	// --------------------- artculo NO consumible -------------------------
	public void onUpgradeAppButtonClicked(String producto) {
		Log.d("info", "Comprar artculo NO consumible.");

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

			// TODO: Si se compra un artculo consumible
			// cambiar
			if (purchase.getSku().equals(SKU_CATEGORIAS_PREMIUM)) {
				isPremium = true;
				actualizaPremium(getResources().getString(R.string.gracias));
			}
		}
	};

	*//** Verifica la "developer payload" de una compra *//*
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

	void actualizaPremium(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
		bld.setMessage(message);
		bld.setNeutralButton(getResources().getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(getActivity(),
								ControlGastosActivity.class);
						startActivity(intent);
					}
				});
		bld.create().show();
	}

	// Listener, se llama cuando terminamos de consultar los artculos y las
	// suscripciones que poseemos.
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			// Entretanto Hemos sido eliminados? Si es as, deje de hacerlo.
			if (mHelper == null)
				return;

			// Es un error?
			if (result.isFailure()) {
				return;
			}

			// eliminar
			// if (inventory.hasPurchase("android.test.purchased")) {
			// mHelper.consumeAsync(
			// inventory.getPurchase("android.test.purchased"), null);
			// }

			*//*
			 * Compruebe que artculos poseemos. Ntese que por cada compra,
			 * comprobamos el "developer payload" para ver si es correcta! Ver
			 * verifyDeveloperPayload ().
			 *//*

			if (inventory.hasPurchase(SKU_CATEGORIAS_PREMIUM)) {
				isPremium = true;
			}
		}
	};*/
}
