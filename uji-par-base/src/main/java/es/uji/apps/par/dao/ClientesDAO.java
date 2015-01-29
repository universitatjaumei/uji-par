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
        JPAQuery jpaQuery = getQueryClientes();

        List<CompraDTO> compras = new ArrayList<CompraDTO>();
        if (jpaQuery != null) {
            compras = jpaQuery.orderBy(getSort(qCompraDTO, sortParameter)).limit(limit).offset(start).list(qCompraDTO);
        }

        List<Cliente> clientes = new ArrayList<Cliente>();
        for (CompraDTO compra : compras) {
            clientes.add(new Cliente(compra));
        }

        return clientes;
    }

    @Transactional
    public int getTotalClientes() {
        JPAQuery jpaQuery = getQueryClientes();

        if (jpaQuery != null) {
            return jpaQuery.list(qCompraDTO).size();
        }
        else {
            return 0;
        }
    }

    @Transactional
    private JPAQuery getQueryClientes() {
        JPAQuery query = new JPAQuery(entityManager);
        JPAQuery subquery = new JPAQuery(entityManager);

        List<Long> ids = subquery.from(qCompraDTO).where(qCompraDTO.infoPeriodica.isTrue()).groupBy(qCompraDTO.email).list(qCompraDTO.id.max());
        if (ids != null && ids.size() > 0) {
            return query.from(qCompraDTO).where(qCompraDTO.id.in(ids));
        }
        else {
            return null;
        }
    }


    public List<String> getMails() {
        JPAQuery queryClientes = getQueryClientes();
        if (queryClientes != null) {
            return queryClientes.distinct().list(qCompraDTO.email);
        }
        else {
            return null;
        }
    }
}
