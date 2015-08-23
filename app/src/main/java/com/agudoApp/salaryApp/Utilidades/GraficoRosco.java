package com.agudoApp.salaryApp.Utilidades;

import java.util.ArrayList;
import java.util.Locale;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.plot.PiePlot;
import org.afree.chart.title.TextTitle;
import org.afree.data.general.DefaultPieDataset;
import org.afree.data.general.PieDataset;
import org.afree.graphics.geom.Font;

import android.content.Context;
import android.graphics.Typeface;

import com.agudoApp.salaryApp.model.Movimiento;

/**
 * PieChartDemo01View
 */
public class GraficoRosco extends GraficoView {

	/**
	 * constructor
	 * 
	 * @param context
	 */
	public GraficoRosco(Context context, ArrayList<Movimiento> listMov) {
		super(context);

		final PieDataset dataset = createDataset(listMov);
		final AFreeChart chart = createChart(dataset);

		setChart(chart);
	}

	/**
	 * Creates a sample dataset.
	 * 
	 * @return a sample dataset.
	 */
	private static PieDataset createDataset(ArrayList<Movimiento> listMov) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		ArrayList<String> listaCategorias = new ArrayList<String>();
		ArrayList<String[]> listaCategoriasTotal = new ArrayList<String[]>();

		Float total = new Float("0.0");
		for (int i = 0; i < listMov.size(); i++) {
			if (Float.parseFloat(listMov.get(i).getCantidad()) > 0) {
				total = total + Float.parseFloat(listMov.get(i).getCantidad());
			} else {
				Float cant = Float.parseFloat(listMov.get(i).getCantidad())
						* -1;
				total = total + cant;
			}
		}

		for (int i = 0; i < listMov.size(); i++) {
			Movimiento mov = listMov.get(i);
			if (!listaCategorias.contains(mov.getDescCategoria())) {
				Float cant = new Float("0.0");
				Float porcentaje = new Float("0.0");
				if (Float.parseFloat(mov.getCantidad()) > 0) {
					cant = cant + Float.parseFloat(mov.getCantidad());
				} else {
					Float aux = Float.parseFloat(mov.getCantidad()) * -1;
					cant = cant + aux;
				}
				for (int j = 0; j < listMov.size(); j++) {
					Movimiento mov2 = listMov.get(j);
					if (!mov.getId().equals(mov2.getId())
							&& mov.getIdCategoria().equals(
									mov2.getIdCategoria())) {
						if (Float.parseFloat(mov2.getCantidad()) > 0) {
							cant = cant + Float.parseFloat(mov2.getCantidad());
						} else {
							Float cant2 = Float.parseFloat(mov2.getCantidad())
									* -1;
							cant = cant + cant2;
						}
					}
				}
				porcentaje = (cant * 100) / total;
				if (porcentaje < 0) {
					porcentaje = porcentaje * -1;
				}
				String[] catTotal = new String[] { mov.getDescCategoria(),
						String.valueOf(porcentaje) };
				listaCategoriasTotal.add(catTotal);
				listaCategorias.add(mov.getDescCategoria());
			}
		}

		for (int i = 0; i < listaCategoriasTotal.size(); i++) {
			String[] valores = listaCategoriasTotal.get(i);
			String desc = valores[0];
			if (valores[0].equals("-")) {

				Locale locale = Locale.getDefault();
				String languaje = locale.getLanguage();
				if (languaje.equals("es") || languaje.equals("es-rUS")
						|| languaje.equals("ca")) {
					desc = "Sin categoria";
				} else if (languaje.equals("fr")) {
					desc = "Sans catégorie";
				} else if (languaje.equals("de")) {
					desc = "Keine Kategorie";
				} else if (languaje.equals("en")) {
					desc = "No category";
				} else if (languaje.equals("it")) {
					desc = "Senza categoria";
				} else if (languaje.equals("pt")) {
					desc = "Sem categoria";					
				} else {
					desc = "No category";
				}
			}
			dataset.setValue(desc, Float.parseFloat(valores[1]));
		}
		return dataset;
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * @return a chart.
	 */
	private static AFreeChart createChart(PieDataset dataset) {

		Locale locale = Locale.getDefault();
		String languaje = locale.getLanguage();
		String titulo = "";
		if (languaje.equals("es") || languaje.equals("es-rUS")
				|| languaje.equals("ca")) {
			titulo = "Categorías";
		} else if (languaje.equals("fr")) {
			titulo = "Catégories";
		} else if (languaje.equals("de")) {
			titulo = "Kategorien";
		} else if (languaje.equals("en")) {
			titulo = "Categories";
		} else if (languaje.equals("it")) {
			titulo = "Categorias";
		} else if (languaje.equals("pt")) {
			titulo = "Categorie";
		} else {
			titulo = "Categories";
		}

		AFreeChart chart = ChartFactory.createPieChart(titulo, // chart
																// title
				dataset, // data
				true, // include legend
				true, false);
		TextTitle title = chart.getTitle();
		title.setToolTipText("A title tooltip!");
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelFont(new Font("SansSerif", Typeface.NORMAL, 20));
		plot.setNoDataMessage("No data available");
		plot.setCircular(true);
		plot.setLabelGap(0.02);
		return chart;

	}
}