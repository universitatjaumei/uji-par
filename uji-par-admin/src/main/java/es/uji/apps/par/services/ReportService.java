package es.uji.apps.par.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.model.Informe;
import es.uji.apps.par.utils.DateUtils;
import es.uji.apps.par.utils.Utils;

@Service
public class ReportService {
	
	@Autowired
	ComprasDAO comprasDAO;
	
	public ByteArrayOutputStream getExcelTaquilla(String fechaInicio, String fechaFin) throws IOException {
		List<Object[]> files = comprasDAO.getComprasInFechas(fechaInicio, fechaFin);
		ExcelService excelService = new ExcelService();
		int rownum = 0;
		
		if (files != null && files.size() > 0) {
			excelService.addFulla("Informe taquilla " + fechaInicio + " - " + fechaFin);
			excelService.generaCeldes(excelService.getEstilNegreta(), 0, "Event", "Sessió", "Tipus d'entrada", "Nombre d'entrades", "Total");
			
			for (Object[] fila: files) {
				rownum++;
				addDadesTaquilla(rownum, objectToInforme(fila), excelService);
			}
			/*rownum++;
			addFilaResum(rownum, iniciSeccio, excelService);
			rownum++;
			addFilaTotal(rownum, llistatDades, excelService);*/
		}
		return excelService.getExcel();
	}
	
	private Informe objectToInforme(Object[] fila) {
		Informe informe = new Informe();
		informe.setEvento(Utils.safeObjectToString(fila[0]));
		informe.setSesion(DateUtils.dateToSpanishStringWithHour(Utils.objectToDate(fila[1])).toString());
		informe.setTipoEntrada(Utils.safeObjectToString(fila[2]));
		informe.setNumeroEntradas(Utils.safeObjectBigDecimalToInt(fila[3]));
		informe.setTotal(Utils.safeObjectToFloat(fila[4]));
		
		return informe;
	}

	private void addDadesTaquilla(int i, Informe fila, ExcelService excelService) {
		Row row = excelService.getNewRow(i);
		excelService.addCell(0, fila.getEvento(), null, row);
		excelService.addCell(1, fila.getSesion(), null, row);
		excelService.addCell(2, fila.getTipoEntrada(), null, row);
		excelService.addCell(3, fila.getNumeroEntradas(), null, row);
		excelService.addCell(4, fila.getTotal(), row);
		
		/*List<Float> valors = new ArrayList<Float>();
		valors.add((float)itemGraella.getQuantitatLliures());
		valors.add((float)itemGraella.getQuantitatReservades());
		valors.add((float)itemGraella.getQuantitatOcupades());
		valors.add(itemGraella.getMetresLinealsLliures());
		valors.add(itemGraella.getMetresLinealsReservades());
		valors.add(itemGraella.getMetresLinealsOcupades());
		
		return valors;*/
	}
}
