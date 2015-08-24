package com.agudoApp.salaryApp.general;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.activities.Preferences;
import com.agudoApp.salaryApp.activities.SeguridadComprobar;
import com.agudoApp.salaryApp.adapters.ListAdapterNavigator;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.fragments.CategoriasFragment;
import com.agudoApp.salaryApp.fragments.CuentasFragment;
import com.agudoApp.salaryApp.fragments.DatabaseFragment;
import com.agudoApp.salaryApp.fragments.EstadisticasFragment;
import com.agudoApp.salaryApp.fragments.InformesFragment;
import com.agudoApp.salaryApp.fragments.RecibosFragment;
import com.agudoApp.salaryApp.fragments.ResumenFragment;
import com.agudoApp.salaryApp.fragments.SeguridadFragment;
import com.agudoApp.salaryApp.fragments.TarjetasFragment;
import com.agudoApp.salaryApp.model.Cuenta;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Calendar;

public class ControlGastosActivity extends ActionBarActivity {
	// Menu navegacin
	private String[] titlesMenu;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private ListView navList;
	private DrawerLayout navDrawerLayout;
	private ActionBarDrawerToggle actionBarDrawer;
	private ListAdapterNavigator mAdapter;
	private final String BD_NOMBRE = "BDGestionGastos";
	private SQLiteDatabase db;
	final GestionBBDD gestion = new GestionBBDD();

	private long mLastPress = 0; // Cundo se puls atrs por ltima vez
	private long mTimeLimit = 3000; // Lmite de tiempo entre pulsaciones, en ms

	// Publicidad
	private int mYear;
	private int mMonth;
	private int mDay;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	private InterstitialAd interstitial;

	// Productos que posee el usuario
	boolean isPremium = false;
	boolean isCategoriaPremium = false;
	boolean isSinPublicidad = false;

	// Seguridad
	boolean appIniciada;
	boolean seguridadActivada;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_layout);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// Anuncio Inicial
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId("ca-app-pub-2303483383476811/4287719686");

		AdRequest adRequestCompleto = new AdRequest.Builder().build();
		interstitial.loadAd(adRequestCompleto);

		// Seguridad
		prefs = this.getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
		appIniciada = prefs.getBoolean("appIniciada", false);
		seguridadActivada = prefs.getBoolean("seguridadActivada", false);
		editor = prefs.edit();

		if (!appIniciada && seguridadActivada) {
			Intent in = new Intent(this, SeguridadComprobar.class);
			in.putExtra("funcionalidad", "comprobar");
			startActivity(in);
		} else {
			editor.putBoolean("appIniciada", true);
			editor.commit();
		}

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			isPremium = extras.getBoolean("isPremium", false);
			isSinPublicidad = extras.getBoolean("isSinPublicidad", false);
			isCategoriaPremium = extras.getBoolean("isCategoriaPremium", false);
		}

		// comprobarProductos();

		View header = getLayoutInflater().inflate(R.layout.header, null);
		View footer = getLayoutInflater().inflate(R.layout.footer, null);


		navDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		navList = (ListView) findViewById(R.id.left_drawer);

		navList.addHeaderView(header);
		navList.addFooterView(footer);

		mTitle = mDrawerTitle = getTitle();



		actionBarDrawer = new ActionBarDrawerToggle(this, navDrawerLayout,
				R.string.aceptar, R.string.cancelar) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getSupportActionBar().setTitle(mTitle);
			}

			public void onDrawerOpened(View view) {
				super.onDrawerOpened(view);
				getSupportActionBar().setTitle(mDrawerTitle);
			}
		};

		navDrawerLayout.setDrawerListener(actionBarDrawer);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().show();

		titlesMenu = getResources().getStringArray(R.array.titles);

		// Set previous array as adapter of the list
		mAdapter = new ListAdapterNavigator(this, titlesMenu);
		navList.setAdapter(mAdapter);
		navList.setOnItemClickListener(new DrawerItemClickListener());

		ImageView closfyIcon = (ImageView) findViewById(R.id.closfyIcon);
		ImageView daysCounter = (ImageView) findViewById(R.id.daysCounterIcon);
		ImageView twitter = (ImageView) findViewById(R.id.twitterIcon);

		interstitial.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				displayInterstitial();
				super.onAdLoaded();
			}
		});

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			selectItem(1);
		}

		closfyIcon.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent1 = null;
				intent1 = new Intent(
						"android.intent.action.VIEW",
						Uri.parse("https://play.google.com/store/apps/details?id=com.agba.closfy"));
				startActivity(intent1);
			}
		});
		
		daysCounter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent1 = null;
				intent1 = new Intent(
						"android.intent.action.VIEW",
						Uri.parse("https://play.google.com/store/apps/details?id=com.agba.dayscounter"));
				startActivity(intent1);
			}
		});

		twitter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://twitter.com/#!/AGBAAndroidApps"));
				startActivity(browserIntent);
			}
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		actionBarDrawer.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		actionBarDrawer.onConfigurationChanged(newConfig);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (actionBarDrawer.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	// Opciones del menu de navegacin
	private void selectItem(int position) {

		Fragment fragment = null;

		switch (position - 1) {
		case -1:
			fragment = new CuentasFragment();
			break;
		case 0:
			fragment = new ResumenFragment(isPremium, isSinPublicidad,
					isCategoriaPremium);
			break;
		case 1:
			fragment = new CategoriasFragment(isPremium, isSinPublicidad,
					isCategoriaPremium);
			break;
		case 2:
			fragment = new InformesFragment(isPremium, isSinPublicidad,
					isCategoriaPremium);
			break;
		case 3:
			fragment = new TarjetasFragment(isPremium, isSinPublicidad,
					isCategoriaPremium);
			break;
		case 4:
			fragment = new RecibosFragment(isPremium, isSinPublicidad,
					isCategoriaPremium);
			break;
		case 5:
			fragment = new SeguridadFragment(isPremium, isSinPublicidad,
					isCategoriaPremium);
			break;
		case 6:
			fragment = new DatabaseFragment(isPremium, isSinPublicidad,
					isCategoriaPremium);
			break;
		case 7:
			fragment = new EstadisticasFragment(isPremium, isSinPublicidad,
					isCategoriaPremium);
			break;
		case 8:
			Intent intent = new Intent(this, Preferences.class);
			startActivity(intent);
			break;
		case 9:
			Intent intent1 = null;
			intent1 = new Intent(
					"android.intent.action.VIEW",
					Uri.parse("https://play.google.com/store/apps/details?id=com.agudoApp.salaryApp"));
			startActivity(intent1);
			break;
		case 10:
			/*fragment = new TiendaFragment(isPremium, isSinPublicidad,
					isCategoriaPremium);
			break;*/
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			navList.setItemChecked(position, true);
			TextView textoHeader = (TextView) findViewById(R.id.txtHeader);

			if (position != 0) {
				setTitle(titlesMenu[position - 1]);
				mAdapter.setSelectedItem(position - 1);
				//textoHeader.setTextColor(Color.GRAY);
			} else {
				setTitle("Cuentas");
				mAdapter.setSelectedItem(99);
				textoHeader.setTextColor(Color.argb(255, 0, 163, 232));
			}
			navDrawerLayout.closeDrawer(navList);
		} else {
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	// Comprobamos si debemos mostrar el anuncio completo o no
	public boolean mostrarAnuncioCompleto() {
		boolean mostrarAnuncio = true;

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
		String fechaAct = mDay + "/" + (mMonth + 1) + "/" + mYear;
		String fechaAnuncio = prefs.getString("fechaAnuncio", "");

		if (fechaAnuncio.equals(fechaAct)) {
			mostrarAnuncio = false;
		} else {
			editor = prefs.edit();
			editor.putString("fechaAnuncio", fechaAct);
			editor.putInt("contadorPubli", 0);
			editor.putInt("ultimoNumPubli", 0);
			editor.commit();
		}

		return mostrarAnuncio;
	}

	// Estamos siendo destruidos. Es importante eliminar el helper aqu!
	@Override
	public void onDestroy() {
		super.onDestroy();

		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
		editor = prefs.edit();
		editor.putBoolean("appIniciada", false);
		editor.commit();
	}

	@Override
	protected void onResume() {
		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
		TextView textoHeader = (TextView) findViewById(R.id.txtHeader);
		int cuen = cuentaSeleccionada();
		db = openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			Cuenta cuenta = gestion.getCuentaSeleccionada(db, cuen);
			textoHeader.setText(cuenta.getDescCuenta());
		}
		db.close();
		super.onResume();
	}

	void actualizaPremium() {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(getResources().getString(R.string.gracias));
		bld.setNeutralButton(getResources().getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(ControlGastosActivity.this,
								ControlGastosActivity.class);
						startActivity(intent);
					}
				});
		bld.create().show();
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
	}

	void error(String message) {
		alert(message);
	}*/

	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// finish();
	// } else if (keyCode == KeyEvent.KEYCODE_HOME) {
	// finish();
	// }
	// return true;
	// }

	public int cuentaSeleccionada() {
		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);

		int idCuenta = prefs.getInt("cuenta", 0);
		return idCuenta;
	}

	public void comprobarProductos() {
		prefs = getSharedPreferences("ficheroConf", Context.MODE_PRIVATE);
		if (!isPremium) {
			isPremium = prefs.getBoolean("isPremium", false);
		}
		if (!isCategoriaPremium) {
			isCategoriaPremium = prefs.getBoolean("isCategoriaPremium", false);
		}
		if (!isSinPublicidad) {
			isSinPublicidad = prefs.getBoolean("isSinPublicidad", false);
		}
	}

	// Comprobamos si debemos mostrar la publicidad o no
	public void displayInterstitial() {
		if (interstitial != null && interstitial.isLoaded() && !isPremium
				&& !isSinPublicidad && mostrarAnuncioCompleto()) {
			interstitial.show();
		}
	}

	@Override
	public void onBackPressed() {
		Toast onBackPressedToast = Toast.makeText(this, R.string.pulseDosVeces,
				Toast.LENGTH_SHORT);
		long currentTime = System.currentTimeMillis();

		if (currentTime - mLastPress > mTimeLimit) {
			onBackPressedToast.show();
			mLastPress = currentTime;
		} else {
			onBackPressedToast.cancel();
			super.onBackPressed();
		}
	}

}