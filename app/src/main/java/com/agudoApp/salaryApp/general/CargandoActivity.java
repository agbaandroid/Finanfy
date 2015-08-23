package com.agudoApp.salaryApp.general;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.database.GestionBBDD;
import com.android.vending.billing.IInAppBillingService;

public class CargandoActivity extends Activity {
	private final String BD_NOMBRE = "BDGestionGastos";
	final GestionBBDD gestion = new GestionBBDD();
	boolean tablasCreadas = false;
	boolean tablasTarjetaCreadas = false;
	boolean tablasCuentasCreadas = false;
	boolean comprobarVersion30 = false;
	private SQLiteDatabase db;

	// Podructos integrados
	// id de los productos
	static final String SKU_COMPRA_PREMIUM = "version_premium";
	static final String SKU_CATEGORIAS_PREMIUM = "categorias_premium";
	static final String SKU_SIN_PUBLICIDAD = "sin_publicidad";

	// Productos que posee el usuario
	boolean isPremium = false;
	boolean isCategoriaPremium = false;
	boolean isSinPublicidad = false;

	IInAppBillingService mService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cargando);

		Intent serviceIntent = new Intent(
				"com.android.vending.billing.InAppBillingService.BIND");
		serviceIntent.setPackage("com.android.vending");
		bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

		// Asignamos el tipo de fuente
		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/Berlin.ttf");

//		TextView txtCargando = (TextView) findViewById(R.id.textCargando);
//		txtCargando.setTypeface(miPropiaTypeFace);

		if (!isPremium && gestion.existeBDPro()) {
			isPremium = true;
		}
	}

	public void iniciarApp() {

		// Se crea o abre la BD
		db = this.openOrCreateDatabase(BD_NOMBRE, 1, null);
		if (db != null) {
			// informar que se va a hacer

			// Comprobamos si existen las tablas
			tablasCreadas = gestion.comprobarTablas(db);
			tablasTarjetaCreadas = gestion.comprobarTablasTarjeta(db);

			// Comprobamos si existen las tablas cuentas
			tablasCuentasCreadas = gestion.comprobarTablasCuentas(db);

			// Comprobamos si tiene los cambios de la nueva version de la BD
			comprobarVersion30 = gestion.comprobarVersion30(db);

			if (!tablasCreadas) {
				gestion.createTables(db);
			} else if (tablasCreadas && !tablasCuentasCreadas) {
				gestion.actualizarBDCuentas(db);
			}
			if (tablasCreadas && !comprobarVersion30) {
				gestion.actualizarVersion30(db);
			}
		}
		db.close();

		ArrayList<String> skuList = new ArrayList<String>();
		skuList.add(SKU_COMPRA_PREMIUM);
		skuList.add(SKU_CATEGORIAS_PREMIUM);
		skuList.add(SKU_SIN_PUBLICIDAD);
		Bundle querySkus = new Bundle();
		querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

		//Consulta de detalles del producto integrado
//		try {
//			Bundle skuDetails = mService.getSkuDetails(3,
//					   getPackageName(), "inapp", querySkus);
//			
//			int response = skuDetails.getInt("RESPONSE_CODE");
//			if (response == 0) {
//			   ArrayList<String> responseList
//			      = skuDetails.getStringArrayList("DETAILS_LIST");
//
//			   for (String thisResponse : responseList) {
//			      JSONObject object = new JSONObject(thisResponse);
//			      String sku = object.getString("productId");
//			      String price = object.getString("price");
////			      if (sku.equals("premiumUpgrade")) mPremiumUpgradePrice = price;
////			      else if (sku.equals("gas")) mGasPrice = price;
//			   }
//			}
//		} catch (Exception e2) {
//			// TODO Auto-generated catch block
//			return;
//		}
		
		Bundle ownedItems;
		try {
			ownedItems = mService.getPurchases(3, getPackageName(), "inapp",
					null);
			int response = ownedItems.getInt("RESPONSE_CODE");
			if (response == 0) {		
				ArrayList<String> ownedSkus = ownedItems
						.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
				ArrayList<String> purchaseDataList = ownedItems
						.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
				// ArrayList<String> signatureList = ownedItems
				// .getStringArrayList("INAPP_DATA_SIGNATURE");
				// String continuationToken = ownedItems
				// .getString("INAPP_CONTINUATION_TOKEN");

				for (int i = 0; i < purchaseDataList.size(); ++i) {
					// String purchaseData = (String) purchaseDataList.get(i);
					// String signature = (String) signatureList.get(i);
					String sku = (String) ownedSkus.get(i);

					// do something with this purchase information
					// e.g. display the updated list of products owned by user
					if (sku.equals(SKU_COMPRA_PREMIUM)) {
						isPremium = true;
					}

					if (sku.equals(SKU_CATEGORIAS_PREMIUM)) {
						isCategoriaPremium = true;
					}

					if (sku.equals(SKU_SIN_PUBLICIDAD)) {
						isSinPublicidad = true;
					}
				}

			}
		} catch (RemoteException e1) {
			return;
		}
		// } catch (Exception e){
		// return;
		// }
		// if continuationToken != null, call getPurchases again
		// and pass in the token to retrieve more items

		if (!isPremium && gestion.existeBDPro()) {
			isPremium = true;
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public class MyLoadingAsyncTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			iniciarApp();
			return null;
		}

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent(CargandoActivity.this,
					ControlGastosActivity.class);
			intent.putExtra("isPremium", isPremium);
			intent.putExtra("isSinPublicidad", isSinPublicidad);
			intent.putExtra("isCategoriaPremium", isCategoriaPremium);
			startActivity(intent);
			finish();
		}

	}

	ServiceConnection mServiceConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IInAppBillingService.Stub.asInterface(service);
			new MyLoadingAsyncTask().execute();
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mServiceConn != null) {
			unbindService(mServiceConn);
		}
	}
}
