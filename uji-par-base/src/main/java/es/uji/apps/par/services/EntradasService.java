package es.uji.apps.par.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
        EntradaReport entrada = EntradaReport.create();

        rellenaEntrada(idCompra, entrada);

        entrada.serialize(outputStream);
    }

    private void rellenaEntrada(long idCompra, EntradaReport entrada)
    {
        
    }

    public static void main(String[] args) throws FileNotFoundException, ReportSerializationException
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-db.xml");

        EntradasService service = ctx.getBean(EntradasService.class);

        service.generaEntrada(0, new FileOutputStream("/tmp/entrada.pdf"));
    }
}
