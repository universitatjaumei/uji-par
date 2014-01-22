package es.uji.apps.par.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import es.uji.apps.fopreports.serialization.ReportSerializationException;
import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.i18n.ResourceProperties;
import es.uji.apps.par.model.EntradaModelReport;
import es.uji.apps.par.report.EntradaReport;
import es.uji.apps.par.report.EntradaTaquillaReport;
import es.uji.apps.par.utils.DateUtils;
import es.uji.apps.par.utils.ReportUtils;

@Service
public class EntradasService
{
    @Autowired
    private ComprasDAO comprasDAO;

    public void generaEntrada(String uuidCompra, OutputStream outputStream) throws ReportSerializationException
    {
        EntradaReport entrada = EntradaReport.create(new Locale("ca"));

        rellenaEntrada(uuidCompra, entrada);

        entrada.serialize(outputStream);
    }
    
    public void generaEntradaTaquilla(String uuidCompra, OutputStream outputStream) throws ReportSerializationException, SAXException, IOException {
    	EntradaTaquillaReport entrada = EntradaTaquillaReport.create(new Locale("ca"));

        rellenaEntradaTaquilla(uuidCompra, entrada);
        entrada.serialize(outputStream);
	}

    private void rellenaEntradaTaquilla(String uuidCompra, EntradaTaquillaReport entrada) {
    	CompraDTO compra = comprasDAO.getCompraByUuid(uuidCompra);

        String tituloEs = compra.getParSesion().getParEvento().getTituloEs();
        String fecha = DateUtils.dateToSpanishString(compra.getParSesion().getFechaCelebracion());
        String hora = DateUtils.dateToHourString(compra.getParSesion().getFechaCelebracion());
        String horaApertura = compra.getParSesion().getHoraApertura();

        entrada.setTitulo(tituloEs);
        entrada.setFecha(fecha);
        entrada.setHora(hora);
        entrada.setHoraApertura(horaApertura);

        for (ButacaDTO butaca : compra.getParButacas())
        {
        	EntradaModelReport entradaModelReport = new EntradaModelReport();
        	entradaModelReport.setFila(butaca.getFila());
        	entradaModelReport.setNumero(butaca.getNumero());
            entradaModelReport.setZona(butaca.getParLocalizacion().getNombreEs());
            entradaModelReport.setTotal(ReportUtils.formatEuros(butaca.getPrecio()));
            entradaModelReport.setBarcode(compra.getUuid() + "-" + butaca.getId());
            entradaModelReport.setTipo(ResourceProperties.getProperty(new Locale("ca"), "entrada." + butaca.getTipo()));
            entrada.generaPaginaButaca(entradaModelReport, Configuration.getUrlPublic());
        }
        
        if (compra.getReciboPinpad() != null)
        {
            for (int i=0; i<2; i++)
            {
                entrada.generaPaginasReciboPinpad(compra.getReciboPinpad());
            }
        }
	}

	private void rellenaEntrada(String uuidCompra, EntradaReport entrada)
    {
        CompraDTO compra = comprasDAO.getCompraByUuid(uuidCompra);

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
        	EntradaModelReport entradaModelReport = new EntradaModelReport();
        	entradaModelReport.setFila(butaca.getFila());
        	entradaModelReport.setNumero(butaca.getNumero());
        	entradaModelReport.setZona(butaca.getParLocalizacion().getNombreVa());
        	entradaModelReport.setTotal(ReportUtils.formatEuros(butaca.getPrecio()));
        	entradaModelReport.setBarcode(compra.getUuid() + "-" + butaca.getId());
        	entradaModelReport.setTipo(butaca.getTipo());
            entrada.generaPaginaButaca(entradaModelReport, Configuration.getUrlPublic());
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
