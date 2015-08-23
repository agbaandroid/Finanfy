package com.agudoApp.salaryApp.graficos;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Movimiento;
import com.agudoApp.salaryApp.util.Util;

public class PieChartView extends View {

	private Paint p;
	private int startX;
	private int startY;
	private int startXWhite;
	private int startYWhite;
	private int radius;
	private int radiusWhite;
	private int radiusBlack;
	private float densidad = 2;
	private boolean divide15 = false;
	private boolean divide1 = false;
	private ArrayList<Integer> colors;
	private ArrayList<Categoria> listCategorias;

	public PieChartView(Context context, AttributeSet attrs,
			ArrayList<Categoria> listCat) {
		super(context, attrs);

		Resources r = getResources();
		densidad = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
				r.getDisplayMetrics());
		if (densidad == 1) {
			divide1 = true;
		} else if (densidad == 1.5) {
			divide15 = true;
		}

		obtenerAnchoAlto();

		p = new Paint();
		p.setColor(Color.BLUE);
		p.setAntiAlias(true);

		colors = new ArrayList<Integer>();
		listCategorias = listCat;

		if (divide1) {
			radius = (300 / 8);
			radiusBlack = 180 / 8;
			radiusWhite = 130 / 8;
		} else if (divide15) {
			radius = (300 / 4);
			radiusBlack = 180 / 4;
			radiusWhite = 130 / 4;
		} else {
			radius = (300 / 2);
			radiusBlack = 180 / 2;
			radiusWhite = 130 / 2;
		}

		colors.add(Color.argb(255, 246, 141, 186));
		colors.add(Color.argb(255, 210, 185, 140));
		colors.add(Color.argb(255, 115, 211, 144));
		colors.add(Color.argb(255, 118, 184, 234));
		colors.add(Color.argb(255, 245, 133, 130));
		colors.add(Color.argb(255, 178, 241, 255));
		colors.add(Color.argb(255, 110, 158, 58));
		colors.add(Color.argb(255, 247, 235, 35));
		colors.add(Color.argb(255, 129, 131, 130));
		colors.add(Color.argb(255, 249, 3, 176));
		colors.add(Color.argb(255, 55, 118, 133));
		colors.add(Color.argb(255, 61, 224, 157));
		colors.add(Color.argb(255, 246, 245, 155));
		colors.add(Color.argb(255, 238, 236, 239));
		colors.add(Color.argb(255, 249, 113, 159));
		colors.add(Color.argb(255, 50, 40, 235));
		colors.add(Color.argb(255, 63, 98, 65));
		colors.add(Color.argb(255, 246, 125, 34));
		colors.add(Color.argb(255, 186, 54, 226));
		colors.add(Color.argb(255, 56, 234, 38));
		colors.add(Color.argb(255, 247, 9, 32));
		colors.add(Color.argb(255, 94, 37, 144));

		if (listCategorias.size() > colors.size()) {
			for (int i = colors.size(); i < listCategorias.size(); i++) {
				colors.add(colorRandom());
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Log.e("", "onDraw() is called...");

		double radians;
		double x2 = 0;
		double y2 = 0;
		float x3 = 0;
		float y3 = 0;

		float offset = 0;
		float sum = 0;
		for (int a = 0; a < listCategorias.size(); a++) {
			Categoria cat = listCategorias.get(a);
			sum += cat.getTotal();
		}

		float angle = (float) (360 / sum);

		Log.e("angle", "" + angle);

		RectF rectF = new RectF();
		rectF.set(startXWhite - getRadius(), startYWhite - getRadius(),
				startXWhite + getRadius(), startYWhite + getRadius());

		for (int i = 0; i < listCategorias.size(); i++) {
			Categoria cat = listCategorias.get(i);
			p.setColor(colors.get(i));

			if (i == 0) {
				canvas.drawArc(rectF, 0, cat.getTotal() * angle, true, p);

				radians = Math.toRadians((cat.getTotal() * angle) / 2);
				x2 = startXWhite + getRadius() * Math.cos(radians);
				y2 = startYWhite + getRadius() * Math.sin(radians);

				x3 = (float) x2;
				y3 = (float) y2;

				p.setColor(Color.BLACK);

				// double xaux = startXWhite + (getRadius() + 80) *
				// Math.cos(radians);
				// double yaux = startYWhite + (getRadius() + 80) *
				// Math.sin(radians);
				// canvas.drawLine(x3, y3,(float) xaux,(float) yaux, p);
			} else {
				canvas.drawArc(rectF, offset, cat.getTotal() * angle, true, p);

				radians = Math
						.toRadians((offset + (cat.getTotal() * angle) / 2));
				x2 = startXWhite + getRadius() * Math.cos(radians);
				y2 = startYWhite + getRadius() * Math.sin(radians);

				x3 = (float) x2;
				y3 = (float) y2;

				p.setColor(Color.BLACK);

				// Bitmap bm = BitmapFactory.decodeResource(getResources(),
				// R.drawable.ic_launcher);
				// canvas.drawBitmap(bm, (float) xImage, (float) yImage, p);
			}

			int tamanioRaya = 0;
			if (divide1) {
				if (i % 2 == 0) {
					tamanioRaya = 40;
				} else {
					tamanioRaya = 20;
				}
			}else if (divide15){
				if (i % 2 == 0) {
					tamanioRaya = 60;
				} else {
					tamanioRaya = 32;
				}
			} else {
				if (i % 2 == 0) {
					tamanioRaya = 115;
				} else {
					tamanioRaya = 65;
				}
			}

			double xaux = startXWhite + (getRadius() + tamanioRaya)
					* Math.cos(radians);
			double yaux = startYWhite + (getRadius() + tamanioRaya)
					* Math.sin(radians);
			canvas.drawLine(x3, y3, (float) xaux, (float) yaux, p);

			double xImage = xaux;
			double yImage = yaux;

			// p.setColor(Color.GRAY);
			// canvas.drawCircle((float)xImage, (float)yImage, radiusIcon, p);

			Bitmap bm = BitmapFactory.decodeResource(getResources(),
					Util.obtenerIconoCategoria(cat.getIdIcon()));

			int width = bm.getWidth();
			int height = bm.getHeight();
			float scaleWidth = 0;
			float scaleHeight = 0;
			if (divide1) {
				scaleWidth = ((float) 20) / width;
				scaleHeight = ((float) 20) / height;
			}else if (divide15){
				scaleWidth = ((float) 30) / width;
				scaleHeight = ((float) 30) / height;
			} else {
				scaleWidth = ((float) 60) / width;
				scaleHeight = ((float) 60) / height;
			}
			Matrix matrix = new Matrix();
			// resize the bit map
			matrix.postScale(scaleWidth, scaleHeight);
			// recreate the new Bitmap
			Bitmap bm2 = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
					false);

			if (divide1) {
				xImage = xImage - 12;
				yImage = yImage - 12;
			} else if (divide15) {
				xImage = xImage - 16;
				yImage = yImage - 16;
			} else {
				xImage = xImage - 33;
				yImage = yImage - 33;
			}

			canvas.drawBitmap(bm2, (float) xImage, (float) yImage, p);

			offset += (cat.getTotal() * angle);
		}

		p.setColor(Color.BLACK);
		p.setAlpha(120);
		canvas.drawCircle(getStartXWhite(), getStartYWhite(), radiusBlack, p);

		p.setColor(Color.WHITE);
		canvas.drawCircle(getStartXWhite(), getStartYWhite(), radiusWhite, p);

		canvas.save();
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public ArrayList<Integer> getColors() {
		return colors;
	}

	public void setColors(ArrayList<Integer> colors) {
		this.colors = colors;
	}

	public int getStartXWhite() {
		return startXWhite;
	}

	public void setStartXWhite(int startXWhite) {
		this.startXWhite = startXWhite;
	}

	public int getStartYWhite() {
		return startYWhite;
	}

	public void setStartYWhite(int startYWhite) {
		this.startYWhite = startYWhite;
	}

	@SuppressWarnings("deprecation")
	public void obtenerAnchoAlto() {
		Display disp = ((WindowManager) this.getContext().getSystemService(
				Context.WINDOW_SERVICE)).getDefaultDisplay();

		Rect rectangle = new Rect();
		Activity parent = (Activity) getContext();
		// using the activity, get Window reference
		Window window = parent.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
		int statusBarHeight = rectangle.top;
		int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT)
				.getTop();
		int titleBarHeight = contentViewTop - statusBarHeight;

		Point size = new Point();

		// Utilizamos uno u otro metodo para tomar el tamao
		// de pantalla, dependiendo de la version de android
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

			disp.getSize(size);
			if (divide1) {
				startXWhite = size.x / 2;
				startYWhite = 75;
			} else if (divide15) {
				startXWhite = disp.getWidth() / 2;
				startYWhite = (((size.y - statusBarHeight - titleBarHeight) / 3) / 2) + 10;
			} else {
				startXWhite = size.x / 2;
				startYWhite = (((size.y - statusBarHeight - titleBarHeight) / 2) / 2) - 5;
			}
		} else {
			// Esto es deprecated, pero es necesario para
			// versiones anteriores
			if (divide1) {
				startXWhite = disp.getWidth() / 2;
				startYWhite = 75;
			} else if (divide15) {
				startXWhite = disp.getWidth() / 2;
				startYWhite = 50;
			} else {
				startXWhite = disp.getWidth() / 2;
				startYWhite = disp.getHeight() / 2;
			}
		}

	}

	public int colorRandom() {
		Random rand = new Random();
		// Java 'Color' class takes 3 floats, from 0 to 1.
		int r = rand.nextInt(255);
		int g = rand.nextInt(255);
		int b = rand.nextInt(255);
		int color = Color.argb(255, r, g, b);
		return color;
	}
}