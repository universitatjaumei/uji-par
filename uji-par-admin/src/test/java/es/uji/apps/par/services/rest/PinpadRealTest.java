package es.uji.apps.par.services.rest;

import java.math.BigDecimal;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        String id = "24";

        String resultado = pinpadReal.realizaPago(id, new BigDecimal(1.02), "PruebaJava");
        System.out.println(resultado);

        while (true)
        {
            System.out.println(pinpadReal.getEstadoPinpad(id));
            Thread.sleep(5000);
        }
    }

}
