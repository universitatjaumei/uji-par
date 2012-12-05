package es.uji.apps.par.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.QTuple;

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

        List<Tuple> listadoTuples = query.from(qEventoDTO, qTipoEventoDTO)
        .where(qEventoDTO.parTiposEvento.id.eq(qTipoEventoDTO.id))
    	.list(new QTuple(qEventoDTO.caracteristicasEs, qEventoDTO.caracteristicasVa,
    			qEventoDTO.comentariosEs, qEventoDTO.comentariosVa, 
    			qEventoDTO.companyiaEs, qEventoDTO.companyiaVa,
    			qEventoDTO.descripcionEs, qEventoDTO.descripcionVa,
    			qEventoDTO.duracionEs, qEventoDTO.duracionVa,
    			qEventoDTO.id, qEventoDTO.parTiposEvento,
    			qEventoDTO.interpretesEs, qEventoDTO.interpretesVa,
    			qEventoDTO.premiosEs, qEventoDTO.premiosVa,
    			qEventoDTO.tituloEs, qEventoDTO.tituloVa,
    			qEventoDTO.imagenSrc, qEventoDTO.imagenContentType));
        
        return tuplesToParEventoDTO(listadoTuples);

    }
    
	private List<ParEventoDTO> tuplesToParEventoDTO(List<Tuple> listadoTuples) {
		List<ParEventoDTO> listadoParEventoDTO = new ArrayList<ParEventoDTO>();
		
		for (Tuple tupla: listadoTuples) {
			listadoParEventoDTO.add(rellenarParEventoDTOConTupla(tupla));
		}
		
		return listadoParEventoDTO;
	}

	private ParEventoDTO rellenarParEventoDTOConTupla(Tuple tupla) {
		ParEventoDTO parEventoDTO = new ParEventoDTO();
		
		parEventoDTO.setCaracteristicasEs(tupla.get(qEventoDTO.caracteristicasEs));
		parEventoDTO.setCaracteristicasVa(tupla.get(qEventoDTO.caracteristicasVa));
		
        parEventoDTO.setComentariosEs(tupla.get(qEventoDTO.comentariosEs));
        parEventoDTO.setComentariosVa(tupla.get(qEventoDTO.comentariosVa));
        
        parEventoDTO.setCompanyiaEs(tupla.get(qEventoDTO.companyiaEs));
        parEventoDTO.setCompanyiaVa(tupla.get(qEventoDTO.companyiaVa));
        
        parEventoDTO.setDescripcionEs(tupla.get(qEventoDTO.descripcionEs));
        parEventoDTO.setDescripcionVa(tupla.get(qEventoDTO.descripcionVa));
        
        parEventoDTO.setDuracionEs(tupla.get(qEventoDTO.duracionEs));
        parEventoDTO.setDuracionVa(tupla.get(qEventoDTO.duracionVa));
        
        parEventoDTO.setInterpretesEs(tupla.get(qEventoDTO.interpretesEs));
        parEventoDTO.setInterpretesVa(tupla.get(qEventoDTO.interpretesVa));
        
        if (tupla.get(qEventoDTO.parTiposEvento) != null) {
        	parEventoDTO.setParTiposEvento(tupla.get(qEventoDTO.parTiposEvento));
        }
        parEventoDTO.setPremiosEs(tupla.get(qEventoDTO.premiosEs));
        parEventoDTO.setPremiosVa(tupla.get(qEventoDTO.premiosVa));
        
        parEventoDTO.setTituloEs(tupla.get(qEventoDTO.tituloEs));
        parEventoDTO.setTituloVa(tupla.get(qEventoDTO.tituloVa));
        
        parEventoDTO.setImagenSrc(tupla.get(qEventoDTO.imagenSrc));
        parEventoDTO.setImagenContentType(tupla.get(qEventoDTO.imagenContentType));
        
        parEventoDTO.setId(tupla.get(qEventoDTO.id));
        
        return parEventoDTO;
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
		eventoDTO.setCaracteristicasEs(evento.getCaracteristicasEs());
		eventoDTO.setCaracteristicasVa(evento.getCaracteristicasVa());
		
        eventoDTO.setComentariosEs(evento.getComentariosEs());
        eventoDTO.setComentariosVa(evento.getComentariosVa());
        
        eventoDTO.setCompanyiaEs(evento.getCompanyiaEs());
        eventoDTO.setCompanyiaVa(evento.getCompanyiaVa());
        
        eventoDTO.setDescripcionEs(evento.getDescripcionEs());
        eventoDTO.setDescripcionVa(evento.getDescripcionVa());
        
        eventoDTO.setDuracionEs(evento.getDuracionEs());
        eventoDTO.setDuracionVa(evento.getDuracionVa());
        
        if (evento.getImagen() != null && evento.getImagen().length > 0) {
        	eventoDTO.setImagen(evento.getImagen());
        	eventoDTO.setImagenSrc(evento.getImagenSrc());
        	eventoDTO.setImagenContentType(evento.getImagenContentType());
        }
        
        eventoDTO.setInterpretesEs(evento.getInterpretesEs());
        eventoDTO.setInterpretesVa(evento.getInterpretesVa());
        
        if (evento.getParTipoEvento() != null) {
        	ParTipoEventoDTO parTipoEventoDTO = new ParTipoEventoDTO();
        	parTipoEventoDTO.setId(evento.getParTipoEvento().getId());
        	eventoDTO.setParTiposEvento(parTipoEventoDTO);
        }
        eventoDTO.setPremiosEs(evento.getPremiosEs());
        eventoDTO.setPremiosVa(evento.getPremiosVa());
        
        eventoDTO.setTituloEs(evento.getTituloEs());
        eventoDTO.setTituloVa(evento.getTituloVa());
        
        if (evento.getId() != 0)
        	eventoDTO.setId(evento.getId());
        
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

    @Transactional
	public void deleteImagen(long eventoId) {
		JPAUpdateClause jpaUpdate = new JPAUpdateClause(entityManager, qEventoDTO);
		
		jpaUpdate.setNull(qEventoDTO.imagen).setNull(qEventoDTO.imagenContentType).setNull(qEventoDTO.imagenSrc).
			where(qEventoDTO.id.eq(eventoId)).execute();
	}
}
