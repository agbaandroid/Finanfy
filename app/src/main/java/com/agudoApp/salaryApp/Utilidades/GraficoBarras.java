/* ===========================================================
 * AFreeChart : a free chart library for Android(tm) platform.
 *              (based on JFreeChart and JCommon)
 * ===========================================================
 * Original Author:  Niwano Masayoshi (for ICOMSYSTECH Co.,Ltd);
 * Contributor(s):   Mohit Gupt;
 *
 * Changes
 * -------
 * 19-Nov-2010 : Version 0.0.1 (NM);
 * 31-Mar-2013 : Restructuring
 */

package com.agudoApp.salaryApp.Utilidades;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.axis.CategoryAxis;
import org.afree.chart.axis.CategoryLabelPositions;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.plot.CategoryPlot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.renderer.category.BarRenderer;
import org.afree.data.category.CategoryDataset;
import org.afree.data.category.DefaultCategoryDataset;
import org.afree.graphics.GradientColor;
import org.afree.graphics.SolidColor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.agudoApp.salaryApp.database.GestionBBDD;
import com.agudoApp.salaryApp.model.Movimiento;

public class GraficoBarras extends GraficoView {

	final static GestionBBDD gestion = new GestionBBDD();
	private static SQLiteDatabase db;
	private static final String BD_NOMBRE = "BDGestionGastos";

	/**
	 * constructor
	 * 
	 * @param context
	 */
	public GraficoBarras(Context context, ArrayList<Movimiento> list) {
		super(context);
		
		CategoryDataset dataset = createDataNomina(list);
		AFreeChart chart = createChart(dataset);
		setChart(chart);
	}

	public GraficoBarras(Context context, ArrayList<Movimiento> list,
			String tipoGrafico, SQLiteDatabase db, int idCuenta) {
		super(context);

		CategoryDataset dataset = createDataFecha(list, tipoGrafico, db, idCuenta);
		AFreeChart chart = createChart(dataset);

		setChart(chart);
	}

	/**
	 * Returns a sample dataset.
	 * 
	 * @return The dataset.
	 */
	private static CategoryDataset createDataNomina(ArrayList<Movimiento> list) {

		// por idioma
		String series1 = "Ingresos";
		String series2 = "Gastos";

		Locale locale = Locale.getDefault();
		String languaje = locale.getLanguage();
		if (languaje.equals("es") || languaje.equals("es-rUS")
				|| languaje.equals("ca")) {
			series1 = "Ingresos";
			series2 = "Gastos";
		} else if (languaje.equals("fr")) {
			series1 = "Recettes";
			series2 = "Dépenses";
		} else if (languaje.equals("de")) {
			series1 = "Einnahmen";
			series2 = "Ausgaben";
		} else if (languaje.equals("en")) {
			series1 = "Entries";
			series2 = "Expenses";
		} else if (languaje.equals("it")) {
			series1 = "Proventi";
			series2 = "Oneri";
		} else if (languaje.equals("pt")) {
			series1 = "Entradas";
			series2 = "Despesas";
		} else {
			series1 = "Entries";
			series2 = "Expenses";
		}

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if(list!= null && list.size()>0){
			Date fechaIniDate = list.get(0).getFecha();
			String fechaIni = list.get(0).getFecha().toString();
			int anioIni = Integer.parseInt(fechaIni.substring(0, 4));
			int mesIni = Integer.parseInt(fechaIni.substring(5, 7));
			int diaIni = Integer.parseInt(fechaIni.substring(8, 10));
			Calendar fechaIn = Calendar.getInstance();
			fechaIn.set(anioIni, mesIni, diaIni);
	
			Date fechaFinDate = list.get(list.size() - 1).getFecha();
	
			Date fechaDate = list.get(0).getFecha();
			Calendar fecha = Calendar.getInstance();
			fecha.set(anioIni, mesIni, diaIni);
	
			int index = 0;
	
			do {
				Float ingresosDia = new Float("0.0");
				Float gastosDia = new Float("0.0");
	
				Date fechaMov = list.get(index).getFecha();
	
				if (fechaDate.equals(fechaMov)) {
					if (Float.parseFloat(list.get(index).getCantidad()) > 0) {
						ingresosDia = ingresosDia
								+ Float.parseFloat(list.get(index).getCantidad());
					} else {
						gastosDia = gastosDia
								+ Float.parseFloat(list.get(index).getCantidad());
					}
					int aux = index;
					for (int i = index + 1; i < list.size(); i++) {
						Date fechaMov2 = list.get(i).getFecha();
	
						if (fechaMov.equals(fechaMov2)) {
							if (Float.parseFloat(list.get(i).getCantidad()) > 0) {
								ingresosDia = ingresosDia
										+ Float.parseFloat(list.get(i)
												.getCantidad());
							} else {
								gastosDia = gastosDia
										+ Float.parseFloat(list.get(i)
												.getCantidad());
							}
							aux = i;
						}
					}
					index = aux + 1;
				} else {
					ingresosDia = Float.parseFloat("0.0");
					gastosDia = Float.parseFloat("0.0");
				}
	
				gastosDia = gastosDia * -1;
				dataset.addValue(ingresosDia, series1, fechaDate.toString());
				dataset.addValue(gastosDia, series2, fechaDate.toString());
	
				fecha.set(fechaDate.getYear(), fechaDate.getMonth(),
						fechaDate.getDate());
				fecha.add(Calendar.DATE, 1);
				fechaDate = new Date(fecha.get(Calendar.YEAR),
						fecha.get(Calendar.MONTH), fecha.get(Calendar.DAY_OF_MONTH));
	
			} while (fechaDate.before(fechaFinDate)
					|| fechaDate.equals(fechaFinDate));
			
		}

		return dataset;

	}

	private static CategoryDataset createDataFecha(ArrayList<Movimiento> list,
			String tipo, SQLiteDatabase db, int idCuenta) {

		String series1 = "Ingresos";
		String series2 = "Gastos";

		Locale locale = Locale.getDefault();
		String languaje = locale.getLanguage();
		if (languaje.equals("es") || languaje.equals("es-rUS")
				|| languaje.equals("ca")) {
			series1 = "Ingresos";
			series2 = "Gastos";
		} else if (languaje.equals("fr")) {
			series1 = "Recettes";
			series2 = "Dépenses";
		} else if (languaje.equals("de")) {
			series1 = "Einnahmen";
			series2 = "Ausgaben";
		} else if (languaje.equals("en")) {
			series1 = "Entries";
			series2 = "Expenses";
		} else if (languaje.equals("it")) {
			series1 = "Proventi";
			series2 = "Oneri";
		} else if (languaje.equals("pt")) {
			series1 = "Entradas";
			series2 = "Despesas";
		} else {
			series1 = "Entries";
			series2 = "Expenses";
		}

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if (list != null && list.size() > 0) {
			if (tipo.equals("dia")) {
				CategoryDataset dataset2 = createDataNomina(list);
				return dataset2;
			} else if (tipo.equals("mes")) {
				String fechaDesde = list.get(0).getFecha().toString();
				int anioDesde = Integer.parseInt(fechaDesde.substring(0, 4));
				int mesDesde = Integer.parseInt(fechaDesde.substring(5, 7));
				String fechaHasta = list.get(list.size() - 1).getFecha()
						.toString();
				int anioHasta = Integer.parseInt(fechaHasta.substring(0, 4));
				int mesHasta = Integer.parseInt(fechaHasta.substring(5, 7));

				for (int i = anioDesde; i <= anioHasta; i++) {
					int mesIni = 0;
					int mesFin = 0;
					if (i == anioDesde) {
						mesIni = mesDesde;
						mesFin = 12;
					} else if (i == anioHasta) {
						mesIni = 1;
						mesFin = mesHasta;
					} else {
						mesIni = 1;
						mesFin = 12;
					}
					for (int j = mesIni; j <= mesFin; j++) {
						Date fechaIni = new Date(i - 1900, j - 1, 1);
						Date fechaFin = getFinMes(j, i);

						Cursor movimientos = gestion.consultarMovimientos(db,
								fechaIni, fechaFin, idCuenta);
						ArrayList<Movimiento> listaMovimientos = GestionBBDD
								.obtenerDatosMovimientos(movimientos);

						float totalIngresos = new Float("0.0");
						float totalGastos = new Float("0.0");
						for (int k = 0; k < listaMovimientos.size(); k++) {
							if (Float.parseFloat(listaMovimientos.get(k)
									.getCantidad()) > 0) {
								totalIngresos = totalIngresos
										+ Float.parseFloat(listaMovimientos
												.get(k).getCantidad());
							} else {
								totalGastos = totalGastos
										+ Float.parseFloat(listaMovimientos
												.get(k).getCantidad());
							}
						}

						totalGastos = totalGastos * -1;
						dataset.addValue(totalIngresos, series1,
								String.valueOf(j) + "/" + String.valueOf(i));
						dataset.addValue(totalGastos, series2,
								String.valueOf(j) + "/" + String.valueOf(i));
					}
				}
			} else if (tipo.equals("anio")) {
				String fechaDesde = list.get(0).getFecha().toString();
				int anioDesde = Integer.parseInt(fechaDesde.substring(0, 4));
				String fechaHasta = list.get(list.size() - 1).getFecha()
						.toString();
				int anioHasta = Integer.parseInt(fechaHasta.substring(0, 4));
				for (int i = anioDesde; i <= anioHasta; i++) {
					Date fechaIni = new Date(i - 1900, 0, 1);
					Date fechaFin = new Date(i - 1900, 11, 31);

					Cursor movimientos = gestion.consultarMovimientos(db,
							fechaIni, fechaFin, idCuenta);
					ArrayList<Movimiento> listaMovimientos = GestionBBDD
							.obtenerDatosMovimientos(movimientos);

					float totalIngresos = new Float("0.0");
					float totalGastos = new Float("0.0");
					for (int k = 0; k < listaMovimientos.size(); k++) {
						if (Float.parseFloat(listaMovimientos.get(k)
								.getCantidad()) > 0) {
							totalIngresos = totalIngresos
									+ Float.parseFloat(listaMovimientos.get(k)
											.getCantidad());
						} else {
							totalGastos = totalGastos
									+ Float.parseFloat(listaMovimientos.get(k)
											.getCantidad());
						}
					}

					totalGastos = totalGastos * -1;
					dataset.addValue(totalIngresos, series1, String.valueOf(i));
					dataset.addValue(totalGastos, series2, String.valueOf(i));
				}
			}
		}
		return dataset;
	}

	/**
	 * Creates a sample chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * 
	 * @return The chart.
	 */
	private static AFreeChart createChart(CategoryDataset dataset) {

		Locale locale = Locale.getDefault();
		String languaje = locale.getLanguage();
		String titulo = "";
		String ingrGas = "";
		String fecha = "";

		if (languaje.equals("es") || languaje.equals("es-rUS")
				|| languaje.equals("ca")) {
			titulo = "Gráfico de barras";
			ingrGas = "Ingresos/Gastos";
			fecha = "Fecha";
		} else if (languaje.equals("fr")) {
			titulo = "Graphique";
			ingrGas = "Recettes/Dépenses";
			fecha = "Date";
		} else if (languaje.equals("de")) {
			titulo = "Grafik";
			ingrGas = "Einnahmen/Ausgaben";
			fecha = "Datum";
		} else if (languaje.equals("en")) {
			titulo = "Graphic";
			ingrGas = "Entries/Expenses";
			fecha = "Date";
		} else if (languaje.equals("it")) {
			titulo = "Grafica";
			ingrGas = "Proventi/Oneri";
			fecha = "Data";			
		} else if (languaje.equals("pt")) {
			titulo = "Gráficos";
			ingrGas = "Entradas/Despesas";
			fecha = "Data";			
		} else {
			titulo = "Graphic";
			ingrGas = "Entries/Expenses";
			fecha = "Date";
		}

		// create the chart...
		AFreeChart chart = ChartFactory.createBarChart(titulo, // chart title
				fecha, // domain axis label
				ingrGas, // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaintType(new SolidColor(Color.WHITE));

		// get a reference to the plot for further customisation...
		CategoryPlot plot = (CategoryPlot) chart.getPlot();

		// set the range axis to display integers only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		// set up gradient paints for series...
		GradientColor gp0 = new GradientColor(Color.BLUE, Color.rgb(51, 102,
				204));
		GradientColor gp1 = new GradientColor(Color.RED, Color.rgb(255, 0, 0));
		renderer.setSeriesPaintType(0, gp0);
		renderer.setSeriesPaintType(1, gp1);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;

	}

	public static Date getFinMes(int mes, int anio) {
		Date fechaFin = null;
		switch (mes) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			fechaFin = new Date(anio - 1900, mes - 1, 31);
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			fechaFin = new Date(anio - 1900, mes - 1, 30);
			break;
		case 2:
			if (((anio % 4 == 0) && !(anio % 100 == 0)) || (anio % 400 == 0))
				fechaFin = new Date(anio - 1900, mes - 1, 29);
			else
				fechaFin = new Date(anio - 1900, mes - 1, 28);
			break;
		}
		return fechaFin;
	}
}