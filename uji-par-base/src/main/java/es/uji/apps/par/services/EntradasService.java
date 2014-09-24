package es.uji.apps.par.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import es.uji.apps.fopreports.serialization.ReportSerializationException;
import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.dao.TarifasDAO;
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.db.TarifaDTO;
import es.uji.apps.par.report.EntradaModelReport;
import es.uji.apps.par.report.EntradaReportFactory;
import es.uji.apps.par.report.EntradaReportOnlineInterface;
import es.uji.apps.par.report.EntradaReportTaquillaInterface;
import es.uji.apps.par.utils.DateUtils;
import es.uji.apps.par.utils.ReportUtils;

@Service
public class EntradasService
{
    public static final String BUTACAS_COMPRA = "butacasCompra";
    public static final String UUID_COMPRA = "uuidCompra";
    public static final String ID_SESION = "idSesion";
	private static final Logger log = LoggerFactory.getLogger(EntradasService.class);
    
    @Autowired
    private ComprasDAO comprasDAO;
    
    @Autowired
    private TarifasDAO tarifasDAO;
    
    private static EntradaReportTaquillaInterface entradaTaquillaReport;
    private static EntradaReportOnlineInterface entradaOnlineReport;
    
    static
    {
       	entradaTaquillaReport = EntradaReportFactory.newInstanceTaquilla();
       	entradaOnlineReport = EntradaReportFactory.newInstanceOnline();
    }

    public void generaEntrada(String uuidCompra, OutputStream outputStream) throws ReportSerializationException
    {
    	EntradaReportOnlineInterface entrada = entradaOnlineReport.create(new Locale(Configuration.getIdiomaPorDefecto()));

		try {
        	rellenaEntrada(uuidCompra, entrada);
	        entrada.serialize(outputStream);
		} catch (NullPointerException e) {
			log.error("La compra con uuid " + uuidCompra + " no existe");
		}
    }
    
    public void generaEntradaTaquilla(String uuidCompra, OutputStream outputStream) throws ReportSerializationException, SAXException, IOException {
    	EntradaReportTaquillaInterface entrada = entradaTaquillaReport.create(new Locale(Configuration.getIdiomaPorDefecto()));

        rellenaEntradaTaquilla(uuidCompra, entrada);
        entrada.serialize(outputStream);
	}

    private void rellenaEntradaTaquilla(String uuidCompra, EntradaReportTaquillaInterface entrada) {
    	CompraDTO compra = comprasDAO.getCompraByUuid(uuidCompra);
    	List<TarifaDTO> tarifas = tarifasDAO.getAll("", 0, 100);

        String tituloVa = compra.getParSesion().getParEvento().getTituloVa();
        String fecha = DateUtils.dateToSpanishString(compra.getParSesion().getFechaCelebracion());
        String hora = DateUtils.dateToHourString(compra.getParSesion().getFechaCelebracion());
        String horaApertura = compra.getParSesion().getHoraApertura();

        entrada.setTitulo(tituloVa);
        entrada.setFecha(fecha);
        entrada.setHora(hora);
        entrada.setHoraApertura(horaApertura);
        entrada.setUrlPortada(Configuration.getUrlPublic() + "/rest/evento/"
                + compra.getParSesion().getParEvento().getId() + "/imagenEntrada");

        for (ButacaDTO butaca : compra.getParButacas())
        {
        	if (butaca.getAnulada() == null || butaca.getAnulada() == false) {
	        	EntradaModelReport entradaModelReport = new EntradaModelReport();
	        	entradaModelReport.setFila(butaca.getFila());
	        	entradaModelReport.setNumero(butaca.getNumero());
	            entradaModelReport.setZona(butaca.getParLocalizacion().getNombreVa());
	            entradaModelReport.setTotal(ReportUtils.formatEuros(butaca.getPrecio()));
	            entradaModelReport.setBarcode(compra.getUuid() + "-" + butaca.getId());
	            entradaModelReport.setTipo(butaca.getTipo());
	            entradaModelReport.setIniciales(butaca.getParLocalizacion().getIniciales());
	            
	            for (TarifaDTO tarifa: tarifas) {
	            	if (tarifa.getId() == Long.valueOf(butaca.getTipo())) {
	            		entradaModelReport.setTipo(tarifa.getNombre());
	            		break;
	            	}
	            }
	            
	            entrada.generaPaginaButaca(entradaModelReport, Configuration.getUrlPublic());
        	}
        }
        
        if (compra.getReciboPinpad() != null)
        {
            for (int i=0; i<2; i++)
            {
                entrada.generaPaginasReciboPinpad(compra.getReciboPinpad());
            }
        }
	}

	private void rellenaEntrada(String uuidCompra, EntradaReportOnlineInterface entrada) throws NullPointerException
    {
        CompraDTO compra = comprasDAO.getCompraByUuid(uuidCompra);
		if (compra == null)
			throw new NullPointerException();

        String tituloCa = compra.getParSesion().getParEvento().getTituloVa();
        String fecha = DateUtils.dateToSpanishString(compra.getParSesion().getFechaCelebracion());
        String hora = DateUtils.dateToHourString(compra.getParSesion().getFechaCelebracion());
        String horaApertura = compra.getParSesion().getHoraApertura();

        entrada.setTitulo(tituloCa);
        entrada.setFecha(fecha);
        entrada.setHora(hora);
        entrada.setHoraApertura(horaApertura);
        entrada.setUrlPortada(Configuration.getUrlPublic() + "/rest/evento/"
                + compra.getParSesion().getParEvento().getId() + "/imagenEntrada");
        entrada.setUrlPublicidad(Configuration.getUrlPieEntrada());

        for (ButacaDTO butaca : compra.getParButacas())
        {
        	if (butaca.getAnulada() == null || butaca.getAnulada() == false) {
	        	TarifaDTO tarifaCompra = tarifasDAO.get(Integer.valueOf(butaca.getTipo()));
	        	EntradaModelReport entradaModelReport = new EntradaModelReport();
	        	entradaModelReport.setFila(butaca.getFila());
	        	entradaModelReport.setNumero(butaca.getNumero());
	        	entradaModelReport.setZona(butaca.getParLocalizacion().getNombreVa());
	        	entradaModelReport.setTotal(ReportUtils.formatEuros(butaca.getPrecio()));
	        	entradaModelReport.setBarcode(compra.getUuid() + "-" + butaca.getId());
	        	entradaModelReport.setTipo(tarifaCompra.getNombre());
	        	entradaModelReport.setTarifaDefecto(tarifaCompra.getDefecto());
	            entrada.generaPaginaButaca(entradaModelReport, Configuration.getUrlPublic());
        	}
        }
    }

    public static void main(String[] args) throws FileNotFoundException, ReportSerializationException
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-db.xml");

        EntradasService service = ctx.getBean(EntradasService.class);

        //service.generaEntradaTaquilla("e3a762c9-9107-47b7-b13d-175e308aa24f", new FileOutputStream("/tmp/entrada.pdf"));
        service.generaEntrada("e3a762c9-9107-47b7-b13d-175e308aa24f", new FileOutputStream("/tmp/entrada.pdf"));
    }
}
