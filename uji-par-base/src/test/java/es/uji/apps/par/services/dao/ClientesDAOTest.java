package es.uji.apps.par.services.dao;

import es.uji.apps.par.dao.ClientesDAO;
import es.uji.apps.par.model.Cliente;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = {"/applicationContext-db-test.xml"})
public class ClientesDAOTest extends BaseDAOTest {
    @Autowired
    ClientesDAO clientesDAO;

    @Test
    @Transactional
    public void getClientes() {
        List<Cliente> clientes = clientesDAO.getClientes("nombre", 0, 10);

        assertNotNull(clientes);
        assertEquals(clientes.size(), clientesDAO.getTotalClientes());
    }
}
