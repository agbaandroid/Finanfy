package com.agudoApp.salaryApp.fragments;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.general.FinanfyActivity;
import com.android.vending.billing.IInAppBillingService;

import org.json.JSONObject;

public class IconosCategoriaPremiumFragment extends Fragment {
	private static final String KEY_CONTENT = "PestanaCategoriaFragment:Content";
	private SQLiteDatabase db;
	final GestionBBDD gestion = new GestionBBDD();
	static final int DATABASE = 1;
	private final String BD_NOMBRE = "BDGestionGastos";
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	private boolean isPremium = false;
	private boolean isCategoriaPremium = false;

	// arbitrario) cdigo de solicitud para el flujo de compra
	static final int RC_REQUEST = 10001;

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
	private ImageView imagenCat68;
	private ImageView imagenCat69;
	private ImageView imagenCat70;
	private ImageView imagenCat71;
	private ImageView imagenCat72;
	private ImageView imagenCat73;
	private ImageView imagenCat74;
	private ImageView imagenCat75;


	String id;
	String textEdit;
	String tipo;
	String flujo;

	IInAppBillingService mService;

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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1001) {
			int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
			String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");

			if (resultCode == 0) {//RESULT_OK
				try {
					JSONObject o = new JSONObject(purchaseData);
					isPremium = true;

					alert(getResources().getString(R.string.gracias));
				} catch (Exception e) {
					alert(getResources().getString(R.string.errorCompra));
				}
			}
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		Intent serviceIntent = new Intent(
				"com.android.vending.billing.InAppBillingService.BIND");
		serviceIntent.setPackage("com.android.vending");
		getActivity().bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

		Bundle extras = getActivity().getIntent().getExtras();
		if (extras != null) {
			id = extras.getString("id");
			textEdit = extras.getString("textEdit");
			tipo = extras.getString("tipo");
			flujo = extras.getString("flujo");
			isPremium = extras.getBoolean("isPremium", false);
			isCategoriaPremium = extras.getBoolean("isCategoriaPremium", false);
		}

		if (isPremium || isCategoriaPremium) {
			isPremium = true;
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
		imagenCat68 = (ImageView) getView().findViewById(R.id.imagenCat68);
		imagenCat69 = (ImageView) getView().findViewById(R.id.imagenCat69);
		imagenCat70 = (ImageView) getView().findViewById(R.id.imagenCat70);
		imagenCat71 = (ImageView) getView().findViewById(R.id.imagenCat71);
		imagenCat72 = (ImageView) getView().findViewById(R.id.imagenCat72);
		imagenCat73 = (ImageView) getView().findViewById(R.id.imagenCat73);
		imagenCat74 = (ImageView) getView().findViewById(R.id.imagenCat74);
		imagenCat75 = (ImageView) getView().findViewById(R.id.imagenCat75);


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

		imagenCat68.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(67);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 67);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 67);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat69.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(68);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 68);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 68);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat70.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(69);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 69);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 69);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat71.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(70);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 70);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 70);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat72.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(71);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 71);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 71);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat73.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(72);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 72);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 72);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat74.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(73);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 73);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 73);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

		imagenCat75.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isPremium) {
					if (flujo.equals("add")) {
						guardarIdIcon(74);
					} else {
						if (tipo.equals("categoria")) {
							gestion.editCategoria(db, textEdit, id, 74);
						} else {
							gestion.editSubcategoria(db, textEdit, id, 74);
						}
					}
					getActivity().finish();
				} else {
					onCreateDialog(MSG_MORE_CAT);
				}
			}
		});

	}

	ServiceConnection mServiceConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IInAppBillingService.Stub.asInterface(service);
		}
	};

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
								comprarProducto(SKU_CATEGORIAS_PREMIUM);
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

	public void comprarProducto(String sku) {
		try {
			Bundle buyIntentBundle = mService.getBuyIntent(3, getActivity().getPackageName(),
					sku, "inapp", "");

			int response = buyIntentBundle.getInt("RESPONSE_CODE", 0);
			if (response == 0) {
				PendingIntent pendingIntent = buyIntentBundle
						.getParcelable("BUY_INTENT");

				getActivity().startIntentSenderForResult(pendingIntent.getIntentSender(),
						1001, new Intent(), Integer.valueOf(0),
						Integer.valueOf(0), Integer.valueOf(0));
			} else if (response == 7) {
				alert(getResources().getString(R.string.productoComprado));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
								FinanfyActivity.class);
						startActivity(intent);
					}
				});
		bld.create().show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mServiceConn != null) {
			getActivity().unbindService(mServiceConn);

		}
	}
}