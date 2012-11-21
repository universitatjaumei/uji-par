package es.uji.apps.par.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;

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
    
    public List<ParEventoDTO> getEventoDTO(Long id) {
    	QParTipoEventoDTO qTipoEventoDTO = QParTipoEventoDTO.parTipoEventoDTO;
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qEventoDTO, qTipoEventoDTO)
        .where(qEventoDTO.parTiposEvento.id.eq(qTipoEventoDTO.id).and(qEventoDTO.id.eq(id)))
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
    	eventoDTO = rellenarParEventoDTOConParEvento(evento, eventoDTO);

        entityManager.persist(eventoDTO);

        evento.setId(eventoDTO.getId());
        return evento;
    }

	private ParEventoDTO rellenarParEventoDTOConParEvento(ParEvento evento, ParEventoDTO eventoDTO) {
		eventoDTO.setCaracteristicas(evento.getCaracteristicas());
        eventoDTO.setComentarios(evento.getComentarios());
        eventoDTO.setCompanyia(evento.getCompanyia());
        eventoDTO.setDescripcion(evento.getDescripcion());
        eventoDTO.setDuracion(evento.getDuracion());
        eventoDTO.setImagen(evento.getImagen());
        eventoDTO.setImagenSrc(evento.getImagenSrc());
        eventoDTO.setImagenContentType(evento.getImagenContentType());
        eventoDTO.setInterpretes(evento.getInterpretes());
        
        if (evento.getParTipoEvento() != null) {
        	ParTipoEventoDTO parTipoEventoDTO = new ParTipoEventoDTO();
        	parTipoEventoDTO.setId(evento.getParTipoEvento().getId());
        	eventoDTO.setParTiposEvento(parTipoEventoDTO);
        }
        eventoDTO.setPremios(evento.getPremios());
        eventoDTO.setTitulo(evento.getTitulo());
        
        return eventoDTO;
	}

    @Transactional
    public ParEvento updateEvento(ParEvento evento)
    {
        /*JPAUpdateClause update = new JPAUpdateClause(entityManager, qEventoDTO);
        update.set(qEventoDTO.caracteristicas, evento.getCaracteristicas())
        	.set(qEventoDTO.comentarios, evento.getComentarios())
        	.set(qEventoDTO.companyia, evento.getCompanyia())
        	.set(qEventoDTO.descripcion, evento.getDescripcion())
        	.set(qEventoDTO.duracion, evento.getDuracion())
        	.set(qEventoDTO.imagen, evento.getImagen())
        	.set(qEventoDTO.imagenSrc, evento.getImagenSrc())
        	.set(qEventoDTO.imagenContentType, evento.getImagenContentType())
        	.set(qEventoDTO.interpretes, evento.getInterpretes())
        	.set(qEventoDTO.parTiposEvento.id, evento.getTipoEvento().getId())
        	.set(qEventoDTO.premios, evento.getPremios())
        	.set(qEventoDTO.titulo, evento.getTitulo())
        	.where(qEventoDTO.id.eq(evento.getId())).execute();*/
    	
    	List<ParEventoDTO> listaEventos = getEventoDTO(evento.getId());
    	
    	if (listaEventos.size() > 0) {
    		ParEventoDTO eventoDTO = listaEventos.get(0);
    		eventoDTO = rellenarParEventoDTOConParEvento(evento, eventoDTO);
    		
    		entityManager.persist(eventoDTO);
    	}
        
        return evento;
    }
}
