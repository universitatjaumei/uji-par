package es.uji.apps.par.dao;

import com.mysema.query.jpa.impl.JPAQuery;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.db.QCompraDTO;
import es.uji.apps.par.model.Cliente;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientesDAO extends BaseDAO {
    private QCompraDTO qCompraDTO = QCompraDTO.compraDTO;

    @Transactional
    public List<Cliente> getClientes(String sortParameter, int start, int limit) {
        List<CompraDTO> compras = getQueryClientes().orderBy(getSort(qCompraDTO, sortParameter)).limit(limit).offset(start).list(qCompraDTO);

        List<Cliente> clientes = new ArrayList<Cliente>();
        for (CompraDTO compra : compras) {
            clientes.add(new Cliente(compra));
        }

        return clientes;
    }

    @Transactional
    public int getTotalClientes() {
        return getQueryClientes().list(qCompraDTO).size();
    }

    @Transactional
    private JPAQuery getQueryClientes() {
        JPAQuery query = new JPAQuery(entityManager);
        JPAQuery subquery = new JPAQuery(entityManager);

        return query.from(qCompraDTO).where(qCompraDTO.id.in(subquery.from(qCompraDTO).where(qCompraDTO.infoPeriodica.isTrue()).groupBy(qCompraDTO.email).list(qCompraDTO.id.max())));
        //return query.from(qCompraDTO).where(qCompraDTO.infoPeriodica.isTrue()).groupBy(qCompraDTO.email, qCompraDTO.id);
    }


    public List<String> getMails() {
        return getQueryClientes().distinct().list(qCompraDTO.email);
    }
}
