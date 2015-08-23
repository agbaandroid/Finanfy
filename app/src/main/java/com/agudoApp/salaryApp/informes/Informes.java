package com.agudoApp.salaryApp.informes;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import android.os.Environment;

import com.agudoApp.salaryApp.model.Movimiento;

/**
 * Ejemplo sencillo de como crear una hoja Excel con POI
 * 
 * @author chuidiang
 * 
 */
public class Informes {

	/**
	 * Crea una hoja Excel y la guarda.
	 * 
	 * @param args
	 */
	public static void CrearExcel(String nombre, ArrayList<Movimiento> listMov) {
		// Se crea el libro
		HSSFWorkbook libro = new HSSFWorkbook();

		// Se crea una hoja dentro del libro
		HSSFSheet hoja = libro.createSheet();
		// Se crea una fila dentro de la hoja
		HSSFRow fila = hoja.createRow(0);
		fila.setHeight((short) 1000);
		HSSFCell celda = fila.createCell(0);
		HSSFCell celda1 = fila.createCell(1);
		HSSFCell celda2 = fila.createCell(2);
		HSSFCell celda3 = fila.createCell(3);
		HSSFCell celda4 = fila.createCell(4);
		HSSFCell celda5 = fila.createCell(5);
		HSSFCell celda6 = fila.createCell(6);
		HSSFCell celda7 = fila.createCell(7);

		HSSFRow fila3 = hoja.createRow(2);
		hoja.setColumnWidth(130, 25);
		HSSFCell celdaA3 = fila3.createCell(0);
		HSSFCell celdaB3 = fila3.createCell(1);
		HSSFCell celdaC3 = fila3.createCell(2);
		HSSFCell celdaD3 = fila3.createCell(3);
		HSSFCell celdaE3 = fila3.createCell(4);
		HSSFCell celdaF3 = fila3.createCell(5);
		HSSFCell celdaG3 = fila3.createCell(6);
		HSSFCell celdaH3 = fila3.createCell(7);
		// Se crea una celda dentro de la fila

		// Se crea el contenido de la celda y se mete en ella.
		HSSFRichTextString texto;
		HSSFRichTextString textoA3;
		HSSFRichTextString textoB3;
		HSSFRichTextString textoC3;
		HSSFRichTextString textoD3;
		HSSFRichTextString textoE3;
		HSSFRichTextString textoF3;
		HSSFRichTextString textoG3;
		HSSFRichTextString textoH3;

		Locale locale = Locale.getDefault();
		String languaje = locale.getLanguage();
		
		if (languaje.equals("es") || languaje.equals("es-rUS")
				|| languaje.equals("ca")) {
			texto = new HSSFRichTextString("Control de Gastos");
			textoA3 = new HSSFRichTextString("  Id  ");
			textoB3 = new HSSFRichTextString("  Fecha  ");
			textoC3 = new HSSFRichTextString("  Concepto  ");
			textoD3 = new HSSFRichTextString("  Categ.  ");
			textoE3 = new HSSFRichTextString("  Subcateg.  ");
			textoF3 = new HSSFRichTextString("  Modo de pago  ");
			textoG3 = new HSSFRichTextString("  Tarjeta  ");
			textoH3 = new HSSFRichTextString("  Cantidad  ");
		} else if (languaje.equals("en")) {
			texto = new HSSFRichTextString("Expense control");
			textoA3 = new HSSFRichTextString("  Id  ");
			textoB3 = new HSSFRichTextString("  Date  ");
			textoC3 = new HSSFRichTextString("  Description  ");
			textoD3 = new HSSFRichTextString("  Categ.  ");
			textoE3 = new HSSFRichTextString("  Subcateg.  ");
			textoF3 = new HSSFRichTextString("  Payment method  ");
			textoG3 = new HSSFRichTextString("  Card  ");
			textoH3 = new HSSFRichTextString("  Amount  ");
		} else if (languaje.equals("de")) {
			texto = new HSSFRichTextString("Kostenkontrolle");
			textoA3 = new HSSFRichTextString("  Id  ");
			textoB3 = new HSSFRichTextString("  Datum  ");
			textoC3 = new HSSFRichTextString("  Beschreibung  ");
			textoD3 = new HSSFRichTextString("  Kategorie  ");
			textoE3 = new HSSFRichTextString("  Unterkategorie  ");
			textoF3 = new HSSFRichTextString("  Art der Zahlung  ");
			textoG3 = new HSSFRichTextString("  Karte  ");
			textoH3 = new HSSFRichTextString("  Quantität  ");
		} else if (languaje.equals("fr")) {
			texto = new HSSFRichTextString("Contrôle de dépenses");
			textoA3 = new HSSFRichTextString("  Id  ");
			textoB3 = new HSSFRichTextString("  Date  ");
			textoC3 = new HSSFRichTextString("  Concept  ");
			textoD3 = new HSSFRichTextString("  Catégories  ");
			textoE3 = new HSSFRichTextString("  Sous-catégories  ");
			textoF3 = new HSSFRichTextString("  Mode de paiement  ");
			textoG3 = new HSSFRichTextString("  Carte  ");
			textoH3 = new HSSFRichTextString("  Montant  ");
		} else if (languaje.equals("it")){
			texto = new HSSFRichTextString("Controllo dei costi");
			textoA3 = new HSSFRichTextString("  Id  ");
			textoB3 = new HSSFRichTextString("  Data  ");
			textoC3 = new HSSFRichTextString("  Concetto  ");
			textoD3 = new HSSFRichTextString("  Categoria.  ");
			textoE3 = new HSSFRichTextString("  Sottocategoria.  ");
			textoF3 = new HSSFRichTextString("  Modalità di pagamento  ");
			textoG3 = new HSSFRichTextString("  Scheda  ");
			textoH3 = new HSSFRichTextString("  Importo  ");
		} else if (languaje.equals("pt")){
			texto = new HSSFRichTextString("Controle de despesas");
			textoA3 = new HSSFRichTextString("  Id  ");
			textoB3 = new HSSFRichTextString("  Data  ");
			textoC3 = new HSSFRichTextString("  Conceito");
			textoD3 = new HSSFRichTextString("  Categ.  ");
			textoE3 = new HSSFRichTextString("  Subcateg.  ");
			textoF3 = new HSSFRichTextString("  Forma de pagamento  ");
			textoG3 = new HSSFRichTextString("  Cartão  ");
			textoH3 = new HSSFRichTextString("  Quantidade  ");
		} else {
			texto = new HSSFRichTextString("Expense control");
			textoA3 = new HSSFRichTextString("  Id  ");
			textoB3 = new HSSFRichTextString("  Date  ");
			textoC3 = new HSSFRichTextString("  Description  ");
			textoD3 = new HSSFRichTextString("  Categ.  ");
			textoE3 = new HSSFRichTextString("  Subcateg.  ");
			textoF3 = new HSSFRichTextString("  Payment method  ");
			textoG3 = new HSSFRichTextString("  Card  ");
			textoH3 = new HSSFRichTextString("  Amount  ");
		}

		HSSFFont fuenteTituloGeneral = libro.createFont();
		fuenteTituloGeneral.setFontHeightInPoints((short) 32);
		fuenteTituloGeneral.setColor(HSSFColor.WHITE.index);

		HSSFCellStyle estiloTituloAplicacion = libro.createCellStyle();
		estiloTituloAplicacion.setFont(fuenteTituloGeneral);
		estiloTituloAplicacion.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);		
		estiloTituloAplicacion.setFillForegroundColor(HSSFColor.BLUE.index);

		HSSFFont fuenteTitulo = libro.createFont();
		HSSFCellStyle estiloTitulo = libro.createCellStyle();
		fuenteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fuenteTitulo.setColor(HSSFColor.WHITE.index);
		estiloTitulo.setFont(fuenteTitulo);
		estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		estiloTitulo.setFillBackgroundColor(HSSFColor.RED.index);
		estiloTitulo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		estiloTitulo.setFillForegroundColor(HSSFColor.BLUE.index);

		celda.setCellValue(texto);
		celda.setCellStyle(estiloTituloAplicacion);
		celda1.setCellStyle(estiloTituloAplicacion);
		celda2.setCellStyle(estiloTituloAplicacion);
		celda3.setCellStyle(estiloTituloAplicacion);
		celda4.setCellStyle(estiloTituloAplicacion);
		celda5.setCellStyle(estiloTituloAplicacion);
		celda6.setCellStyle(estiloTituloAplicacion);
		celda7.setCellStyle(estiloTituloAplicacion);

		celdaA3.setCellValue(textoA3);
		celdaA3.setCellStyle(estiloTitulo);
		celdaB3.setCellValue(textoB3);
		celdaB3.setCellStyle(estiloTitulo);
		celdaC3.setCellValue(textoC3);
		celdaC3.setCellStyle(estiloTitulo);
		celdaD3.setCellValue(textoD3);
		celdaD3.setCellStyle(estiloTitulo);
		celdaE3.setCellValue(textoE3);
		celdaE3.setCellStyle(estiloTitulo);
		celdaF3.setCellValue(textoF3);
		celdaF3.setCellStyle(estiloTitulo);
		celdaG3.setCellValue(textoG3);
		celdaG3.setCellStyle(estiloTitulo);
		celdaH3.setCellValue(textoH3);
		celdaH3.setCellStyle(estiloTitulo);

		rellenarCeldas(listMov, hoja, libro);
		rellenarFilaTotal(hoja, libro, listMov);

		// Se salva el libro.
		try {
			// Volcamos la informacin a un archivo.
			File dbFile = new File(Environment.getExternalStorageDirectory(),
					"/SalaryControl/excel");

			if (!dbFile.exists()) {
				dbFile.mkdirs();
			}

			File file = null;
			// File file = new File(dbFile, "ControlGastos.xls");
			if (nombre != null && !nombre.equals("")) {
				file = new File(dbFile, nombre + ".xls");
			} else {
				final Calendar c = Calendar.getInstance();
				int mYear = c.get(Calendar.YEAR);
				int mMonth = c.get(Calendar.MONTH);
				int mDay = c.get(Calendar.DAY_OF_MONTH);
				String fecha = String.valueOf(mYear) + "-"
						+ String.valueOf(mMonth + 1) + "-" + String.valueOf(mDay);
				file = new File(dbFile, fecha + ".xls");
			}

			file.createNewFile();
			FileOutputStream archivoSalida = new FileOutputStream(file);
			libro.write(archivoSalida);
			archivoSalida.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void rellenarCeldas(ArrayList<Movimiento> listaMov,
			HSSFSheet hoja, HSSFWorkbook libro) {
		int contFila = 4;
		HSSFCellStyle estiloMovimientos = libro.createCellStyle();
		estiloMovimientos.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		estiloMovimientos
				.setFillForegroundColor(HSSFColor.WHITE.index);
		estiloMovimientos.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		HSSFCellStyle estiloCantidad = libro.createCellStyle();
		estiloCantidad.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		estiloCantidad.setFillForegroundColor(HSSFColor.WHITE.index);
		estiloCantidad.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		rellenarFilaPrevia(hoja, estiloMovimientos);

		for (int i = 0; i < listaMov.size(); i++) {
			Movimiento mov = listaMov.get(i);
			HSSFRow fila = hoja.createRow(contFila);

			HSSFCell celdaA = fila.createCell(0);
			HSSFCell celdaB = fila.createCell(1);
			HSSFCell celdaC = fila.createCell(2);
			HSSFCell celdaD = fila.createCell(3);
			HSSFCell celdaE = fila.createCell(4);
			HSSFCell celdaF = fila.createCell(5);
			HSSFCell celdaG = fila.createCell(6);
			HSSFCell celdaH = fila.createCell(7);

			HSSFRichTextString textoA = new HSSFRichTextString(
					String.valueOf(i + 1));
			HSSFRichTextString textoB = new HSSFRichTextString(
					String.valueOf(mov.getFecha()));
			HSSFRichTextString textoC = new HSSFRichTextString(mov.toString());
			HSSFRichTextString textoD = new HSSFRichTextString(
					mov.getDescCategoria());
			HSSFRichTextString textoE = new HSSFRichTextString(
					mov.getDescSubcategoria());
			HSSFRichTextString textoF;
			Locale locale = Locale.getDefault();
			String languaje = locale.getLanguage();
			
			if (mov.isTarjeta()) {
				if (languaje.equals("es") || languaje.equals("es-rUS")
						|| languaje.equals("ca")) {
					textoF = new HSSFRichTextString(String.valueOf("Tarjeta"));
				} else if (languaje.equals("en")) {
					textoF = new HSSFRichTextString(String.valueOf("Credit card"));
				} else if (languaje.equals("fr")) {
					textoF = new HSSFRichTextString(String.valueOf("Carte de crédit"));
				} else if (languaje.equals("de")) {
					textoF = new HSSFRichTextString(String.valueOf("karte"));
				} else if (languaje.equals("it")){
					textoF = new HSSFRichTextString(String.valueOf("Carta di credito"));					
				} else if (languaje.equals("pt")){
					textoF = new HSSFRichTextString(String.valueOf("Cartão de crédito"));
				} else {
					textoF = new HSSFRichTextString(String.valueOf("Credit card"));
				}
			} else {				
				if (languaje.equals("es") || languaje.equals("es-rUS")
						|| languaje.equals("ca")) {
					textoF = new HSSFRichTextString(String.valueOf("Metálico"));
				} else if (languaje.equals("en")) {
					textoF = new HSSFRichTextString(String.valueOf("Cash"));
				} else if (languaje.equals("fr")) {
					textoF = new HSSFRichTextString(String.valueOf("Liquide"));
				} else if (languaje.equals("de")) {
					textoF = new HSSFRichTextString(String.valueOf("Bargeld"));
				}else if (languaje.equals("it")){
					textoF = new HSSFRichTextString(String.valueOf("Contanti"));					
				}else if (languaje.equals("pt")){
					textoF = new HSSFRichTextString(String.valueOf("Numerário"));
				} else {
					textoF = new HSSFRichTextString(String.valueOf("Cash"));
				}
			}

			HSSFRichTextString textoG = new HSSFRichTextString("");
			if (mov.isTarjeta()) {
				textoG = new HSSFRichTextString(mov.getDescTarjeta());
			} else {
				textoG = new HSSFRichTextString("");
				;
			}

			HSSFRichTextString textoH = new HSSFRichTextString(
					mov.getCantidadAux());

			celdaA.setCellStyle(estiloMovimientos);
			celdaB.setCellStyle(estiloMovimientos);
			celdaC.setCellStyle(estiloMovimientos);
			celdaD.setCellStyle(estiloMovimientos);
			celdaE.setCellStyle(estiloMovimientos);
			celdaF.setCellStyle(estiloMovimientos);
			celdaG.setCellStyle(estiloMovimientos);
			celdaH.setCellStyle(estiloCantidad);

			celdaA.setCellValue(textoA);
			celdaB.setCellValue(textoB);
			celdaC.setCellValue(textoC);
			celdaD.setCellValue(textoD);
			celdaE.setCellValue(textoE);
			celdaF.setCellValue(textoF);
			celdaG.setCellValue(textoG);
			celdaH.setCellValue(textoH);
			contFila++;
		}
	}

	public static void rellenarFilaPrevia(HSSFSheet hoja,
			HSSFCellStyle estiloMovimientos) {
		HSSFRow filaPrevia = hoja.createRow(3);
		HSSFCell cA = filaPrevia.createCell(0);
		HSSFCell cB = filaPrevia.createCell(1);
		HSSFCell cC = filaPrevia.createCell(2);
		HSSFCell cD = filaPrevia.createCell(3);
		HSSFCell cE = filaPrevia.createCell(4);
		HSSFCell cF = filaPrevia.createCell(5);
		HSSFCell cG = filaPrevia.createCell(6);
		HSSFCell cH = filaPrevia.createCell(7);

		cA.setCellStyle(estiloMovimientos);
		cB.setCellStyle(estiloMovimientos);
		cC.setCellStyle(estiloMovimientos);
		cD.setCellStyle(estiloMovimientos);
		cE.setCellStyle(estiloMovimientos);
		cF.setCellStyle(estiloMovimientos);
		cG.setCellStyle(estiloMovimientos);
		cH.setCellStyle(estiloMovimientos);
	}

	public static void rellenarFilaTotal(HSSFSheet hoja, HSSFWorkbook libro,
			ArrayList<Movimiento> listaMov) {
		HSSFCellStyle estiloTotal = libro.createCellStyle();
		estiloTotal.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		estiloTotal.setFillForegroundColor(HSSFColor.WHITE.index);
		estiloTotal.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		float total = 0;
		for (int i = 0; i < listaMov.size(); i++) {
			Movimiento mov = listaMov.get(i);
			total = total + Float.parseFloat(mov.getCantidad());
		}

		HSSFRichTextString texto = new HSSFRichTextString("Total: ");

		HSSFRow filaPreviaTotal = hoja.createRow(listaMov.size() + 4);
		HSSFCell cA2 = filaPreviaTotal.createCell(0);
		HSSFCell cB2 = filaPreviaTotal.createCell(1);
		HSSFCell cC2 = filaPreviaTotal.createCell(2);
		HSSFCell cD2 = filaPreviaTotal.createCell(3);
		HSSFCell cE2 = filaPreviaTotal.createCell(4);
		HSSFCell cF2 = filaPreviaTotal.createCell(5);
		HSSFCell cG2 = filaPreviaTotal.createCell(6);
		HSSFCell cH2 = filaPreviaTotal.createCell(7);

		cA2.setCellStyle(estiloTotal);
		cB2.setCellStyle(estiloTotal);
		cC2.setCellStyle(estiloTotal);
		cD2.setCellStyle(estiloTotal);
		cE2.setCellStyle(estiloTotal);
		cF2.setCellStyle(estiloTotal);
		cG2.setCellStyle(estiloTotal);
		cH2.setCellStyle(estiloTotal);

		HSSFRow filaTotal = hoja.createRow(listaMov.size() + 5);
		HSSFCell cA = filaTotal.createCell(0);
		HSSFCell cB = filaTotal.createCell(1);
		HSSFCell cC = filaTotal.createCell(2);
		HSSFCell cD = filaTotal.createCell(3);
		HSSFCell cE = filaTotal.createCell(4);
		HSSFCell cF = filaTotal.createCell(5);
		HSSFCell cG = filaTotal.createCell(6);
		HSSFCell cH = filaTotal.createCell(7);

		cA.setCellStyle(estiloTotal);
		cB.setCellStyle(estiloTotal);
		cC.setCellStyle(estiloTotal);
		cD.setCellStyle(estiloTotal);
		cE.setCellStyle(estiloTotal);
		cF.setCellStyle(estiloTotal);
		cG.setCellStyle(estiloTotal);
		cH.setCellStyle(estiloTotal);

		cG.setCellValue(texto);
		DecimalFormat df = new DecimalFormat("0.00");
		cH.setCellValue(df.format(total));
	}
}