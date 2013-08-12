package es.uji.apps.par.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import es.uji.apps.fopreports.serialization.ReportSerializationException;
import es.uji.apps.par.report.EntradaReport;

@Service
public class EntradasService
{
    public void generaEntrada(long idCompra, OutputStream outputStream) throws ReportSerializationException
    {
        EntradaReport entrada = EntradaReport.create(new Locale("ca"));

        rellenaEntrada(idCompra, entrada);

        entrada.serialize(outputStream);
    }

    private void rellenaEntrada(long idCompra, EntradaReport entrada)
    {
        entrada.setTitulo("El muerto y ser feliz");
        entrada.setFecha("12/08/2013");
        entrada.setHora("15:05");
        entrada.setHoraApertura("12:30");
        entrada.setFila("10");
        entrada.setButaca("15");
        entrada.setZona("Platea 2");
        entrada.setTotal("10,05");
        entrada.setUrlPublicidad("http://static.uji.es/templates/common/latest/img/panoramica.jpg");
    }

    public static void main(String[] args) throws FileNotFoundException, ReportSerializationException
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-db.xml");

        EntradasService service = ctx.getBean(EntradasService.class);

        service.generaEntrada(0, new FileOutputStream("/tmp/entrada.pdf"));
    }
}
