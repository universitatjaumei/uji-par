package es.uji.apps.par.services;

import es.uji.apps.par.dao.ClientesDAO;
import es.uji.apps.par.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientesService {
    @Autowired
    private ClientesDAO clientesDAO;

    public List<Cliente> getClientes(String sortParameter, int start, int limit) {
        return clientesDAO.getClientes(sortParameter, start, limit);
    }

    public int getTotalClientes() {
        return clientesDAO.getTotalClientes();
    }

    public List<String> getMails() {
        return clientesDAO.getMails();
    }
}
