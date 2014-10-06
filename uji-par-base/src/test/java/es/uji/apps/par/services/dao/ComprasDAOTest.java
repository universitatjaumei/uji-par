package es.uji.apps.par.services.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import es.uji.apps.par.dao.ButacasDAO;
import es.uji.apps.par.db.ButacaDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.model.Localizacion;
import es.uji.apps.par.model.Sesion;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "/applicationContext-db-test.xml" })
public class ComprasDAOTest extends BaseDAOTest
{
    @Autowired
    ComprasDAO comprasDAO;

    @Autowired
    ButacasDAO butacasDAO;

    private Localizacion localizacion;
    private SesionDTO sesion;

    @Before
    public void before()
    {
        localizacion = preparaLocalizacion("anfiteatro");
        sesion = preparaSesion(Localizacion.localizacionToLocalizacionDTO(localizacion));
    }

    public Sesion preparaSesion()
    {
        return new Sesion(preparaSesion(Localizacion.localizacionToLocalizacionDTO(localizacion)));
    }

    @Test
    @Transactional
    public void guardaCompraOk()
    {
        CompraDTO compraDTO = comprasDAO.insertaCompra(sesion.getId(), new Date(), true, BigDecimal.ONE);

        assertNotNull(compraDTO.getId());
    }

    @Test
    @Transactional
    public void testGetRecaudacionSinCompras()
    {
        Sesion sesion1 = preparaSesion();

        assertEquals(new BigDecimal(0).doubleValue(), comprasDAO.getRecaudacionSesiones(Arrays.asList(sesion1)).doubleValue(), 0.00001);
    }
    
    @Test
    @Transactional
    public void testGetRecaudacion()
    {
        Sesion sesion1 = preparaSesion();
        CompraDTO compra1 = comprasDAO.insertaCompra(sesion1.getId(), new Date(), true, new BigDecimal(2));
        ButacaDTO butaca1 = preparaButaca(Sesion.SesionToSesionDTO(sesion1),
                Localizacion.localizacionToLocalizacionDTO(localizacion),
                "1", "2",
                new BigDecimal(2));
        butaca1.setParCompra(compra1);
        butacasDAO.addButaca(butaca1);
        CompraDTO compra2 = comprasDAO.insertaCompra(sesion1.getId(), new Date(), true, new BigDecimal(3));
        ButacaDTO butaca2 = preparaButaca(Sesion.SesionToSesionDTO(sesion1),
                Localizacion.localizacionToLocalizacionDTO(localizacion),
                "3", "4",
                new BigDecimal(3));
        butaca2.setParCompra(compra2);
        butacasDAO.addButaca(butaca2);

        assertEquals(new BigDecimal(5).doubleValue(), comprasDAO.getRecaudacionSesiones(Arrays.asList(sesion1)).doubleValue(), 0.00001);
    }
    
    @Test
    @Transactional
    public void testGetRecaudacionVariasSesiones()
    {
        Sesion sesion1 = preparaSesion();
        CompraDTO compra1 = comprasDAO.insertaCompra(sesion1.getId(), new Date(), true, new BigDecimal(2));

        ButacaDTO butaca1 = preparaButaca(sesion, Localizacion.localizacionToLocalizacionDTO(localizacion), "1", "2",
                new BigDecimal(2));
        butaca1.setParCompra(compra1);
        butacasDAO.addButaca(butaca1);
        
        Sesion sesion2 = preparaSesion();
        CompraDTO compra2 = comprasDAO.insertaCompra(sesion2.getId(), new Date(), true, new BigDecimal(1));
        ButacaDTO butaca2 = preparaButaca(Sesion.SesionToSesionDTO(sesion2),
                Localizacion.localizacionToLocalizacionDTO(localizacion),
                "1", "2",
                new BigDecimal(1));
        butaca2.setParCompra(compra2);
        butacasDAO.addButaca(butaca2);

        CompraDTO compra3 = comprasDAO.insertaCompra(sesion2.getId(), new Date(), true, new BigDecimal(5));
        ButacaDTO butaca3 = preparaButaca(Sesion.SesionToSesionDTO(sesion2),
                Localizacion.localizacionToLocalizacionDTO(localizacion),
                "3", "4",
                new BigDecimal(5));
        butaca3.setParCompra(compra3);
        butacasDAO.addButaca(butaca3);

        assertEquals(new BigDecimal(8).doubleValue(), comprasDAO.getRecaudacionSesiones(Arrays.asList(sesion1, sesion2)).doubleValue(), 0.00001);
    }

	//TODO Anular compras y ver que marca la sesion con incidencias del ICAA

	//TODO Desanular compras y ver que si no queda ninguna butaca anulada en la sesion, que elimina la incidencia de la sesion

	//TODO Desanular compras y ver que si aun queda alguna butaca anulada en la sesion, NO elimina la incidencia de la sesion
}
