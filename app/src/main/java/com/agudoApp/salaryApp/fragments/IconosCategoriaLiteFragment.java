package com.agudoApp.salaryApp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;

public class IconosCategoriaLiteFragment extends Fragment {
	private static final String KEY_CONTENT = "PestanaCategoriaFragment:Content";
	private SQLiteDatabase db;
	final GestionBBDD gestion = new GestionBBDD();
	static final int DATABASE = 1;
	private final String BD_NOMBRE = "BDGestionGastos";
	SharedPreferences prefs;
	SharedPreferences.Editor editor;

	private ImageView imagenCat1;
	private ImageView imagenCat2;
	private ImageView imagenCat3;
	private ImageView imagenCat4;
	private ImageView imagenCat5;
	private ImageView imagenCat6;
	private ImageView imagenCat7;
	private ImageView imagenCat8;
	private ImageView imagenCat9;
	private ImageView imagenCat10;
	private ImageView imagenCat11;
	private ImageView imagenCat12;
	private ImageView imagenCat13;
	private ImageView imagenCat14;
	private ImageView imagenCat15;
	private ImageView imagenCat16;
	private ImageView imagenCat17;
	private ImageView imagenCat18;
	private ImageView imagenCat19;
	private ImageView imagenCat20;
	private ImageView imagenCat21;
	private ImageView imagenCat22;
	private ImageView imagenCat23;
	private ImageView imagenCat24;
	private ImageView imagenCat25;
	private ImageView imagenCat26;
	private ImageView imagenCat27;
	private ImageView imagenCat28;
	private ImageView imagenCat29;
	private ImageView imagenCat30;
	private ImageView imagenCat31;
	private ImageView imagenCat32;
	private ImageView imagenCat33;
	private ImageView imagenCat34;
	private ImageView imagenCat35;
	private ImageView imagenCat36;
	private ImageView imagenCat37;
	private ImageView imagenCat38;
	private ImageView imagenCat39;
	private ImageView imagenCat40;
	private ImageView imagenCat41;
	private ImageView imagenCat42;
	private ImageView imagenCat43;
	private ImageView imagenCat44;
	private ImageView imagenCat45;
	private ImageView imagenCat46;
	private ImageView imagenCat47;

	String id;
	String textEdit;
	String tipo;
	String flujo;
	boolean isPremium = false;

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

		return inflater.inflate(R.layout.categorias_gratis, container, false);
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

		Bundle extras = getActivity().getIntent().getExtras();
		if (extras != null) {
			id = extras.getString("id");
			textEdit = extras.getString("textEdit");
			tipo = extras.getString("tipo");
			flujo = extras.getString("flujo");
			isPremium = extras.getBoolean("isPremium");
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

		imagenCat1 = (ImageView) getView().findViewById(R.id.imagenCat1);
		imagenCat2 = (ImageView) getView().findViewById(R.id.imagenCat2);
		imagenCat3 = (ImageView) getView().findViewById(R.id.imagenCat3);
		imagenCat4 = (ImageView) getView().findViewById(R.id.imagenCat4);
		imagenCat5 = (ImageView) getView().findViewById(R.id.imagenCat5);
		imagenCat6 = (ImageView) getView().findViewById(R.id.imagenCat6);
		imagenCat7 = (ImageView) getView().findViewById(R.id.imagenCat7);
		imagenCat8 = (ImageView) getView().findViewById(R.id.imagenCat8);
		imagenCat9 = (ImageView) getView().findViewById(R.id.imagenCat9);
		imagenCat10 = (ImageView) getView().findViewById(R.id.imagenCat10);
		imagenCat11 = (ImageView) getView().findViewById(R.id.imagenCat11);
		imagenCat12 = (ImageView) getView().findViewById(R.id.imagenCat12);
		imagenCat13 = (ImageView) getView().findViewById(R.id.imagenCat13);
		imagenCat14 = (ImageView) getView().findViewById(R.id.imagenCat14);
		imagenCat15 = (ImageView) getView().findViewById(R.id.imagenCat15);
		imagenCat16 = (ImageView) getView().findViewById(R.id.imagenCat16);
		imagenCat17 = (ImageView) getView().findViewById(R.id.imagenCat17);
		imagenCat18 = (ImageView) getView().findViewById(R.id.imagenCat18);
		imagenCat19 = (ImageView) getView().findViewById(R.id.imagenCat19);
		imagenCat20 = (ImageView) getView().findViewById(R.id.imagenCat20);
		imagenCat21 = (ImageView) getView().findViewById(R.id.imagenCat21);
		imagenCat22 = (ImageView) getView().findViewById(R.id.imagenCat22);
		imagenCat23 = (ImageView) getView().findViewById(R.id.imagenCat23);
		imagenCat24 = (ImageView) getView().findViewById(R.id.imagenCat24);
		imagenCat25 = (ImageView) getView().findViewById(R.id.imagenCat25);
		imagenCat26 = (ImageView) getView().findViewById(R.id.imagenCat26);
		imagenCat27 = (ImageView) getView().findViewById(R.id.imagenCat27);
		imagenCat28 = (ImageView) getView().findViewById(R.id.imagenCat28);
		imagenCat29 = (ImageView) getView().findViewById(R.id.imagenCat29);
		imagenCat30 = (ImageView) getView().findViewById(R.id.imagenCat30);
		imagenCat31 = (ImageView) getView().findViewById(R.id.imagenCat31);
		imagenCat32 = (ImageView) getView().findViewById(R.id.imagenCat32);
		imagenCat33 = (ImageView) getView().findViewById(R.id.imagenCat33);
		imagenCat34 = (ImageView) getView().findViewById(R.id.imagenCat34);
		imagenCat35 = (ImageView) getView().findViewById(R.id.imagenCat35);
		imagenCat36 = (ImageView) getView().findViewById(R.id.imagenCat36);
		imagenCat37 = (ImageView) getView().findViewById(R.id.imagenCat37);
		imagenCat38 = (ImageView) getView().findViewById(R.id.imagenCat38);
		imagenCat39 = (ImageView) getView().findViewById(R.id.imagenCat39);
		imagenCat40 = (ImageView) getView().findViewById(R.id.imagenCat40);
		imagenCat41 = (ImageView) getView().findViewById(R.id.imagenCat41);
		imagenCat42 = (ImageView) getView().findViewById(R.id.imagenCat42);
		imagenCat43 = (ImageView) getView().findViewById(R.id.imagenCat43);
		imagenCat44 = (ImageView) getView().findViewById(R.id.imagenCat44);
		imagenCat45 = (ImageView) getView().findViewById(R.id.imagenCat45);
		imagenCat46 = (ImageView) getView().findViewById(R.id.imagenCat46);
		imagenCat47 = (ImageView) getView().findViewById(R.id.imagenCat47);

		imagenCat1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(0);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 0);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 0);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(1);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 1);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 1);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(2);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 2);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 2);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(3);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 3);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 3);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(4);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 4);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 4);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(5);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 5);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 5);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat7.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(6);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 6);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 6);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat8.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(7);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 7);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 7);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat9.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(8);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 8);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 8);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat10.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(9);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 9);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 9);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat11.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(10);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 10);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 10);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat12.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(11);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 11);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 11);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat13.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(12);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 12);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 12);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat14.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(13);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 13);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 13);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat15.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(14);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 14);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 14);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat16.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(15);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 15);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 15);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat17.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(16);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 16);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 16);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat18.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(17);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 17);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 17);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat19.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(18);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 18);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 18);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat20.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(19);
				} else {

					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 19);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 19);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat21.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(20);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 20);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 20);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat22.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(21);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 21);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 21);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat23.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(22);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 22);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 22);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat24.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(23);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 23);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 23);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat25.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(24);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 24);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 24);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat26.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(25);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 25);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 25);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat27.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(26);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 26);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 26);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat28.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(27);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 27);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 27);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat29.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(28);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 28);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 28);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat30.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(29);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 29);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 29);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat31.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(30);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 30);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 30);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat32.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(31);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 31);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 31);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat33.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(32);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 32);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 32);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat34.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(33);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 33);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 33);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat35.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(34);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 34);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 34);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat36.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(35);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 35);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 35);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat37.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(36);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 36);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 36);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat38.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(37);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 37);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 37);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat39.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(38);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 38);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 38);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat40.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(39);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 39);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 39);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat41.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(40);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 40);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 40);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat42.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(41);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 41);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 41);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat43.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(42);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 42);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 42);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat44.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(43);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 43);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 43);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat45.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(44);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 44);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 44);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat46.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(45);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 45);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 45);
					}
				}
				getActivity().finish();
			}
		});

		imagenCat47.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (flujo.equals("add")) {
					guardarIdIcon(46);
				} else {
					if (tipo.equals("categoria")) {
						gestion.editCategoria(db, textEdit, id, 46);
					} else {
						gestion.editSubcategoria(db, textEdit, id, 46);
					}
				}
				getActivity().finish();
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
}
