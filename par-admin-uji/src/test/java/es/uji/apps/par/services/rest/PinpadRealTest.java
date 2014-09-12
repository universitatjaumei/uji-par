package es.uji.apps.par.services.rest;

import java.math.BigDecimal;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.uji.apps.par.pinpad.ResultadoPagoPinpad;
import es.uji.apps.par.services.Pinpad;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-db-test.xml" })
public class PinpadRealTest
{
    @Autowired
    private Pinpad pinpadReal;

    @Test
    @Ignore
    public void realizaPago() throws InterruptedException
    {
        String id = "26";

        ResultadoPagoPinpad resultado = pinpadReal.realizaPago(id, new BigDecimal(1.02), "Prueba desde Java");
        System.out.println(resultado);

        while (true)
        {
            System.out.println(pinpadReal.getEstadoPinpad(id));
            Thread.sleep(2000);
        }
    }

}
