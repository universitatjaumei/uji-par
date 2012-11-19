package es.uji.apps.par.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.db.ParEventoDTO;
import es.uji.apps.par.db.ParTipoEventoDTO;
import es.uji.apps.par.db.QParEventoDTO;
import es.uji.apps.par.db.QParTipoEventoDTO;
import es.uji.apps.par.model.ParEvento;

@Repository
public class EventosDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    private QParEventoDTO qEventoDTO = QParEventoDTO.parEventoDTO;

    @Transactional
    public List<ParEventoDTO> getEventos()
    {
    	QParTipoEventoDTO qTipoEventoDTO = QParTipoEventoDTO.parTipoEventoDTO;
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qEventoDTO, qTipoEventoDTO)
        .where(qEventoDTO.parTiposEvento.id.eq(qTipoEventoDTO.id))
    	.list(qEventoDTO);
    }

    @Transactional
    public long removeEvento(long id)
    {
        JPADeleteClause delete = new JPADeleteClause(entityManager, qEventoDTO);
        return delete.where(qEventoDTO.id.eq(id)).execute();
    }

    @Transactional
    public ParEvento addEvento(ParEvento evento)
    {
    	ParEventoDTO eventoDTO = new ParEventoDTO();
        eventoDTO.setCaracteristicas(evento.getCaracteristicas());
        eventoDTO.setComentarios(evento.getComentarios());
        eventoDTO.setCompanyia(evento.getCompanyia());
        eventoDTO.setDescripcion(evento.getDescripcion());
        eventoDTO.setDuracion(evento.getDuracion());
        eventoDTO.setImagen(evento.getImagen());
        eventoDTO.setInterpretes(evento.getInterpretes());
        
        if (evento.getTipoEvento() != null) {
        	ParTipoEventoDTO parTipoEventoDTO = new ParTipoEventoDTO();
        	parTipoEventoDTO.setId(evento.getTipoEvento().getId());
        	eventoDTO.setParTiposEvento(parTipoEventoDTO);
        }
        eventoDTO.setPremios(evento.getPremios());
        eventoDTO.setTitulo(evento.getTitulo());

        entityManager.persist(eventoDTO);

        evento.setId(eventoDTO.getId());
        return evento;
    }

    //TODO -> actualizar imagen
    @Transactional
    public ParEvento updateEvento(ParEvento evento)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qEventoDTO);
        update.set(qEventoDTO.caracteristicas, evento.getCaracteristicas())
        	.set(qEventoDTO.comentarios, evento.getComentarios())
        	.set(qEventoDTO.companyia, evento.getCompanyia())
        	.set(qEventoDTO.descripcion, evento.getDescripcion())
        	.set(qEventoDTO.duracion, evento.getDuracion())
        	//.set(qEventoDTO.imagen, evento.getImagen())
        	.set(qEventoDTO.interpretes, evento.getInterpretes())
        	.set(qEventoDTO.parTiposEvento.id, evento.getTipoEvento().getId())
        	.set(qEventoDTO.premios, evento.getPremios())
        	.set(qEventoDTO.titulo, evento.getTitulo())
        	.where(qEventoDTO.id.eq(evento.getId())).execute();
        
        return evento;
    }
}
