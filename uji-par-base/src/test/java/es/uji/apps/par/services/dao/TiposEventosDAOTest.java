package es.uji.apps.par.services.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.dao.TiposEventosDAO;
import es.uji.apps.par.model.TipoEvento;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "/applicationContext-db-test.xml" })
public class TiposEventosDAOTest
{

    @Autowired
    TiposEventosDAO tiposEventosDAO;

    @Test
    @Transactional
    public void getTiposEventos()
    {
        Assert.assertNotNull(tiposEventosDAO.getTiposEventos());
    }

    @Test
    @Transactional
    public void addTipoEvento()
    {
        TipoEvento parTipoEvento = preparaTipoEvento();
        TipoEvento tipoEvento = tiposEventosDAO.addTipoEvento(parTipoEvento);

        Assert.assertNotNull(tipoEvento.getId());
    }

    private TipoEvento preparaTipoEvento()
    {
        return new TipoEvento("NombreTipoEvento");
    }

    @Test
    @Transactional
    public void updateTipoEvento()
    {
    	TipoEvento parTipoEvento = preparaTipoEvento();
        parTipoEvento = tiposEventosDAO.addTipoEvento(parTipoEvento);
        
        parTipoEvento.setNombreEs("Prueba2");
        TipoEvento tipoEventoActualizado = tiposEventosDAO.updateTipoEvento(parTipoEvento);
        Assert.assertEquals(parTipoEvento.getId(), tipoEventoActualizado.getId());
    }

    @Test
    @Transactional
    public void addTipoEventoConIdiomas()
    {
        TipoEvento parTipoEvento = preparaTipoEvento();
        parTipoEvento.setNombreVa("valencia");
        TipoEvento tipoEvento = tiposEventosDAO.addTipoEvento(parTipoEvento);

        Assert.assertNotNull(tipoEvento.getId());
    }
}
