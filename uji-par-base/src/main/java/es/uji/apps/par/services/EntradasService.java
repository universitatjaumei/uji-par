package es.uji.apps.par.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import es.uji.apps.fopreports.serialization.ReportSerializationException;
import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.report.EntradaReport;
import es.uji.apps.par.utils.DateUtils;

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

    private void rellenaEntrada(String uuidCompra, EntradaReport entrada)
    {
        CompraDTO compra = comprasDAO.getCompraByUuid(uuidCompra);

        String tituloEs = compra.getParSesion().getParEvento().getTituloEs();
        String fecha = DateUtils.dateToSpanishString(compra.getParSesion().getFechaCelebracion());
        String hora = DateUtils.dateToHourString(compra.getParSesion().getFechaCelebracion());
        String horaApertura = compra.getParSesion().getHoraApertura();

        entrada.setTitulo(tituloEs);
        entrada.setFecha(fecha);
        entrada.setHora(hora);
        entrada.setHoraApertura(horaApertura);
        entrada.setUrlPortada(Configuration.getUrlPublic() + "/rest/evento/"
                + compra.getParSesion().getParEvento().getId() + "/imagenEntrada");
        entrada.setUrlPublicidad("http://static.uji.es/templates/common/latest/img/panoramica.jpg");

        for (ButacaDTO butaca : compra.getParButacas())
        {
            entrada.generaPaginaButaca(compra, butaca);
        }
    }

    public static void main(String[] args) throws FileNotFoundException, ReportSerializationException
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-db.xml");

        EntradasService service = ctx.getBean(EntradasService.class);

        service.generaEntrada("", new FileOutputStream("/tmp/entrada.pdf"));
    }
}
