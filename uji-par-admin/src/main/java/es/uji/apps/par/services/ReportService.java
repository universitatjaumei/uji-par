package es.uji.apps.par.services;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import es.uji.apps.fopreports.serialization.ReportSerializationException;
import es.uji.apps.par.SinIvaException;
import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.database.DatabaseHelper;
import es.uji.apps.par.database.DatabaseHelperFactory;
import es.uji.apps.par.model.Informe;
import es.uji.apps.par.report.InformeEfectivoReport;
import es.uji.apps.par.report.InformeEventosReport;
import es.uji.apps.par.report.InformeTaquillaReport;
import es.uji.apps.par.report.InformeTaquillaTpvSubtotalesReport;
import es.uji.apps.par.utils.DateUtils;
import es.uji.apps.par.utils.Utils;

@Service
public class ReportService
{

    @Autowired
    ComprasDAO comprasDAO;
    
    private DatabaseHelper dbHelper;
    
    public ReportService()
    {
        dbHelper = DatabaseHelperFactory.newInstance();
    }

    public ByteArrayOutputStream getExcelTaquilla(String fechaInicio, String fechaFin) throws IOException
    {
        List<Object[]> files = comprasDAO.getComprasInFechas(fechaInicio, fechaFin);
        ExcelService excelService = new ExcelService();
        int rownum = 0;

        if (files != null && files.size() > 0)
        {
            excelService.addFulla("Informe taquilla " + fechaInicio + " - " + fechaFin);
            excelService.generaCeldes(excelService.getEstilNegreta(), 0, "Event", "Sessió", "Tipus d'entrada",
                    "Localització", "Nombre d'entrades", "Total");

            for (Object[] fila : files)
            {
                rownum++;
                addDadesTaquilla(rownum, objectToInforme(fila), excelService);
            }
        }
        return excelService.getExcel();
    }

    private Informe objectToInforme(Object[] fila)
    {
        Informe informe = new Informe();
        informe.setEvento(Utils.safeObjectToString(fila[0]));
        informe.setSesion(DateUtils.dateToSpanishStringWithHour(Utils.objectToDate(fila[1])).toString());
        String tipoEntrada = Utils.safeObjectToString(fila[2]);
        tipoEntrada = tipoEntradaBBDDToText(tipoEntrada);
        informe.setTipoEntrada(tipoEntrada);
        informe.setLocalizacion(localizacionBBDDToText(Utils.safeObjectToString(fila[3])));
        informe.setNumeroEntradas(Utils.safeObjectBigDecimalToInt(fila[4]));
        informe.setTotal((BigDecimal) fila[5]);

        return informe;
    }

    private Informe objectToInformeIva(Object[] fila)
    {
        Informe informe = new Informe();
        informe.setEvento(Utils.safeObjectToString(fila[0]));
        informe.setSesion(DateUtils.dateToSpanishStringWithHour(Utils.objectToDate(fila[1])).toString());
        String tipoEntrada = Utils.safeObjectToString(fila[2]);
        tipoEntrada = tipoEntradaBBDDToText(tipoEntrada);
        informe.setTipoEntrada(tipoEntrada);
        informe.setNumeroEntradas(Utils.safeObjectBigDecimalToInt(dbHelper.castBigDecimal(fila[3])));
        informe.setTotal(dbHelper.castBigDecimal(fila[4]));
        informe.setIva(dbHelper.castBigDecimal(fila[6]));

        return informe;
    }

    private Informe objectToInformeTpv(Object[] fila)
    {
        Informe informe = new Informe();
        informe.setEvento(Utils.safeObjectToString(fila[0]));
        informe.setSesion(DateUtils.dateToSpanishStringWithHour(Utils.objectToDate(fila[1])).toString());
        String tipoEntrada = Utils.safeObjectToString(fila[2]);
        tipoEntrada = tipoEntradaBBDDToText(tipoEntrada);
        informe.setTipoEntrada(tipoEntrada);
        informe.setNumeroEntradas(Utils.safeObjectBigDecimalToInt(fila[3]));
        informe.setTotal((BigDecimal) fila[4]);
        informe.setIva((BigDecimal) fila[6]);
        informe.setFechaCompra(DateUtils.dateToSpanishString(Utils.objectToDate(fila[8])));

        return informe;
    }
    
    private Informe objectToEvento(Object[] fila)
    {
        Informe informe = new Informe();
        informe.setEvento(Utils.safeObjectToString(fila[0]));
        informe.setSesion(DateUtils.dateToSpanishStringWithHour(Utils.objectToDate(fila[1])).toString());
        String tipoEntrada = Utils.safeObjectToString(fila[2]);
        tipoEntrada = tipoEntradaBBDDToText(tipoEntrada);
        informe.setTipoEntrada(tipoEntrada);
        informe.setNumeroEntradas(Utils.safeObjectBigDecimalToInt(fila[3]));
        informe.setTotal((BigDecimal) fila[4]);
        informe.setIva((BigDecimal) fila[5]);
        informe.setEventoId(Utils.safeObjectBigDecimalToLong(fila[7]));
        informe.setSesionId(Utils.safeObjectBigDecimalToLong(fila[8]));

        return informe;
    }

    private String tipoEntradaBBDDToText(String tipoEntrada)
    {
        if (tipoEntrada.equals("normal"))
            tipoEntrada = "Normal";
        else if (tipoEntrada.equals("descuento"))
            tipoEntrada = "Descompte";
        else if (tipoEntrada.equals("invitacion"))
            tipoEntrada = "Invitació";
        else if (tipoEntrada.equals("aulaTeatro"))
            tipoEntrada = "Teatre";        
        return tipoEntrada;
    }

    private String localizacionBBDDToText(String localizacion)
    {
        String result = "";

        if (localizacion.equals("platea1"))
            result = "Platea 1";
        else if (localizacion.equals("platea2"))
            result = "Platea 2";
        else if (localizacion.equals("anfiteatro"))
            result = "Amfiteatre";
        else if (localizacion.equals("discapacitados1"))
            result = "Discapacitats Platea 1";
        else if (localizacion.equals("discapacitados2"))
            result = "Discapacitats Platea 2";
        else if (localizacion.equals("discapacitados3"))
            result = "Discapacitats Amfiteatre";

        return result;
    }

    private Informe objectToInformeEvento(Object[] fila)
    {
        Informe informe = new Informe();
        informe.setEvento(Utils.safeObjectToString(fila[1]));
        String tipoEntrada = Utils.safeObjectToString(fila[2]);
        tipoEntrada = tipoEntradaBBDDToText(tipoEntrada);
        informe.setTipoEntrada(tipoEntrada);
        informe.setNumeroEntradas(Utils.safeObjectBigDecimalToInt(fila[3]));
        informe.setTotal((BigDecimal) fila[4]);

        int taquilla = Utils.safeObjectBigDecimalToInt(fila[5]);
        informe.setTipoCompra((taquilla == 0) ? "ONLINE" : "TAQUILLA");

        return informe;
    }

    private void addDadesTaquilla(int i, Informe fila, ExcelService excelService)
    {
        Row row = excelService.getNewRow(i);
        excelService.addCell(0, fila.getEvento(), null, row);
        excelService.addCell(1, fila.getSesion(), null, row);
        excelService.addCell(2, fila.getTipoEntrada(), null, row);
        excelService.addCell(3, fila.getLocalizacion(), null, row);
        excelService.addCell(4, fila.getNumeroEntradas(), null, row);
        excelService.addCell(5, fila.getTotal().floatValue(), row);
    }

    private void addDadesEvento(int i, Informe fila, ExcelService excelService)
    {
        Row row = excelService.getNewRow(i);
        excelService.addCell(0, fila.getEvento(), null, row);
        excelService.addCell(1, fila.getTipoEntrada(), null, row);
        excelService.addCell(2, fila.getTipoCompra(), null, row);
        excelService.addCell(3, fila.getNumeroEntradas(), null, row);
        excelService.addCell(4, fila.getTotal().floatValue(), row);
    }

    public ByteArrayOutputStream getExcelEventos(String fechaInicio, String fechaFin) throws IOException
    {
        List<Object[]> files = comprasDAO.getComprasPorEventoInFechas(fechaInicio, fechaFin);
        ExcelService excelService = new ExcelService();
        int rownum = 0;

        if (files != null && files.size() > 0)
        {
            excelService.addFulla("Informe taquilla " + fechaInicio + " - " + fechaFin);
            excelService.generaCeldes(excelService.getEstilNegreta(), 0, "Event", "Tipus d'entrada",
                    "Online o taquilla", "Nombre d'entrades", "Total");

            for (Object[] fila : files)
            {
                rownum++;
                addDadesEvento(rownum, objectToInformeEvento(fila), excelService);
            }
        }
        return excelService.getExcel();
    }

    public void getPdfTaquilla(String fechaInicio, String fechaFin, OutputStream bos)
            throws ReportSerializationException, ParseException
    {
        InformeTaquillaReport informe = InformeTaquillaReport.create(new Locale("ca"));

        List<Informe> compras = objectsToInformes(comprasDAO.getComprasPorEventoInFechas(fechaInicio, fechaFin));

        BigDecimal totalTaquillaTpv = comprasDAO.getTotalTaquillaTpv(fechaInicio, fechaFin);
        BigDecimal totalTaquillaEfectivo = comprasDAO.getTotalTaquillaEfectivo(fechaInicio, fechaFin);
        BigDecimal totalOnline = comprasDAO.getTotalOnline(fechaInicio, fechaFin);

        informe.genera(DateUtils.databaseStringToDate(fechaInicio), DateUtils.databaseStringToDate(fechaFin), compras,
                totalTaquillaTpv, totalTaquillaEfectivo, totalOnline);

        informe.serialize(bos);
    }

    public void getPdfEfectivo(String fechaInicio, String fechaFin, OutputStream bos)
            throws ReportSerializationException, ParseException, SinIvaException
    {
        InformeEfectivoReport informe = InformeEfectivoReport.create(new Locale("ca"));

        List<Informe> compras = objectsSesionesToInformesIva(comprasDAO.getComprasEfectivo(fechaInicio, fechaFin));

        informe.genera(DateUtils.databaseStringToDate(fechaInicio), DateUtils.databaseStringToDate(fechaFin), compras);

        informe.serialize(bos);
    }

    public void getPdfTpvSubtotales(String fechaInicio, String fechaFin, OutputStream bos)
            throws ReportSerializationException, ParseException, SinIvaException
    {
        InformeTaquillaTpvSubtotalesReport informe = InformeTaquillaTpvSubtotalesReport.create(new Locale("ca"));

        List<Informe> compras = objectsSesionesToInformesTpv(comprasDAO.getComprasTpv(fechaInicio, fechaFin));

        informe.genera(DateUtils.databaseStringToDate(fechaInicio), DateUtils.databaseStringToDate(fechaFin), compras);

        informe.serialize(bos);
    }
    
    public void getPdfEventos(String fechaInicio, String fechaFin, OutputStream bos)
            throws ReportSerializationException, ParseException, SinIvaException
    {
        InformeEventosReport informe = InformeEventosReport.create(new Locale("ca"));

        List<Informe> compras = objectsSesionesToInformesEventos(comprasDAO.getComprasEventos(fechaInicio, fechaFin));

        informe.genera(DateUtils.databaseStringToDate(fechaInicio), DateUtils.databaseStringToDate(fechaFin), compras);

        informe.serialize(bos);
    }

    private List<Informe> objectsToInformes(List<Object[]> compras)
    {
        List<Informe> result = new ArrayList<Informe>();

        for (Object[] compra : compras)
        {
            result.add(objectToInformeEvento(compra));
        }

        return result;
    }

    private List<Informe> objectsSesionesToInformesIva(List<Object[]> compras)
    {
        List<Informe> result = new ArrayList<Informe>();

        for (Object[] compra : compras)
        {
            result.add(objectToInformeIva(compra));
        }

        return result;
    }
    
    private List<Informe> objectsSesionesToInformesTpv(List<Object[]> compras)
    {
        List<Informe> result = new ArrayList<Informe>();

        for (Object[] compra : compras)
        {
            result.add(objectToInformeTpv(compra));
        }

        return result;
    }
    
    private List<Informe> objectsSesionesToInformesEventos(List<Object[]> compras)
    {
        List<Informe> result = new ArrayList<Informe>();

        for (Object[] compra : compras)
        {
            result.add(objectToEvento(compra));
        }

        return result;
    }

    public static void main(String[] args) throws Exception
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-db.xml");

        ReportService service = ctx.getBean(ReportService.class);

        service.getPdfEventos("2013-10-01", "2013-10-30", new FileOutputStream("/tmp/informe.pdf"));
    }
}
