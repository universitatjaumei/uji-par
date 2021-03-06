package es.uji.apps.par.dao;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.database.DatabaseHelper;
import es.uji.apps.par.database.DatabaseHelperFactory;
import es.uji.apps.par.db.*;
import es.uji.apps.par.model.Tarifa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TarifasDAO extends BaseDAO
{
    private QTarifaDTO qTarifaDTO = QTarifaDTO.tarifaDTO;
    
    @SuppressWarnings("unused")
	private DatabaseHelper databaseHelper;

	@Autowired
    public TarifasDAO(Configuration configuration)
    {
        databaseHelper = DatabaseHelperFactory.newInstance(configuration);
    }

    @Transactional
    public TarifaDTO persistTarifa(TarifaDTO tarifaDTO)
    {
        entityManager.persist(tarifaDTO);
        return tarifaDTO;
    }

    @Transactional
    public List<TarifaDTO> getAll(String sortParameter, int start, int limit, String userUID)
    {
		QSalasUsuarioDTO qSalasUsuarioDTO = QSalasUsuarioDTO.salasUsuarioDTO;
		QSalaDTO qSalaDTO = QSalaDTO.salaDTO;
		QCineDTO qCineDTO = QCineDTO.cineDTO;
		QUsuarioDTO qUsuarioDTO = QUsuarioDTO.usuarioDTO;

        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qTarifaDTO)
				.leftJoin(qTarifaDTO.parCine, qCineDTO)
				.leftJoin(qCineDTO.parSalas, qSalaDTO)
				.leftJoin(qSalaDTO.parSalasUsuario, qSalasUsuarioDTO)
				.leftJoin(qSalasUsuarioDTO.parUsuario, qUsuarioDTO)
				.where((qUsuarioDTO.usuario.eq(userUID).or(qCineDTO.isNull())))
				.distinct()
				.orderBy(getSort(qTarifaDTO, sortParameter))
				.offset(start).limit(limit).list(qTarifaDTO);
    }

	@Transactional
	public List<TarifaDTO> getAll(String userUID)
	{
		QSalasUsuarioDTO qSalasUsuarioDTO = QSalasUsuarioDTO.salasUsuarioDTO;
		QSalaDTO qSalaDTO = QSalaDTO.salaDTO;
		QCineDTO qCineDTO = QCineDTO.cineDTO;
		QUsuarioDTO qUsuarioDTO = QUsuarioDTO.usuarioDTO;

		JPAQuery query = new JPAQuery(entityManager);
		return query.from(qTarifaDTO)
				.leftJoin(qTarifaDTO.parCine, qCineDTO)
				.leftJoin(qCineDTO.parSalas, qSalaDTO)
				.leftJoin(qSalaDTO.parSalasUsuario, qSalasUsuarioDTO)
				.leftJoin(qSalasUsuarioDTO.parUsuario, qUsuarioDTO)
				.where((qUsuarioDTO.usuario.eq(userUID).or(qCineDTO.isNull())))
				.distinct().list(qTarifaDTO);
	}

    @Transactional
	public TarifaDTO add(TarifaDTO tarifa) {
		entityManager.persist(tarifa);
		return tarifa;
	}

    @Transactional
	public void update(TarifaDTO tarifa) {
		JPAUpdateClause update = new JPAUpdateClause(entityManager, qTarifaDTO);
		update.set(qTarifaDTO.nombre, tarifa.getNombre()).set(qTarifaDTO.isPublica, tarifa.getIsPublica()).
			set(qTarifaDTO.defecto, tarifa.getDefecto()).
			where(qTarifaDTO.id.eq(tarifa.getId())).execute();
	}

    @Transactional
	public void removeTarifa(Tarifa tarifa) {
		JPADeleteClause delete = new JPADeleteClause(entityManager, qTarifaDTO);
		delete.where(qTarifaDTO.id.eq(tarifa.getId())).execute();
	}
    
    @Transactional
	public TarifaDTO get(int idTarifa, String userUID) {
		QSalasUsuarioDTO qSalasUsuarioDTO = QSalasUsuarioDTO.salasUsuarioDTO;
		QSalaDTO qSalaDTO = QSalaDTO.salaDTO;
		QCineDTO qCineDTO = QCineDTO.cineDTO;
		QUsuarioDTO qUsuarioDTO = QUsuarioDTO.usuarioDTO;

		JPAQuery query = new JPAQuery(entityManager);
		List<TarifaDTO> tarifaDTO = query.from(qTarifaDTO)
				.leftJoin(qTarifaDTO.parCine, qCineDTO)
				.leftJoin(qCineDTO.parSalas, qSalaDTO)
				.leftJoin(qSalaDTO.parSalasUsuario, qSalasUsuarioDTO)
				.leftJoin(qSalasUsuarioDTO.parUsuario, qUsuarioDTO)
				.where((qUsuarioDTO.usuario.eq(userUID).or(qCineDTO.isNull())).and(qTarifaDTO.id.eq(new Long(idTarifa))))
				.distinct()
				.list(qTarifaDTO);
		if (tarifaDTO.size() == 1)
			return tarifaDTO.get(0);
		else
			return null;
    }
}
