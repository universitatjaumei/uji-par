package es.uji.apps.par.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.mysema.query.Tuple;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.dao.EventosDAO;
import es.uji.apps.par.db.EventoDTO;
import es.uji.apps.par.exceptions.CampoRequeridoException;
import es.uji.apps.par.exceptions.EventoConCompras;
import es.uji.apps.par.exceptions.EventoNoEncontradoException;
import es.uji.apps.par.exceptions.GuardarImagenException;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.EventoMultisesion;
import es.uji.apps.par.model.EventoParaSync;

@Service
public class EventosService
{
	private static final Logger log = LoggerFactory.getLogger(EventosService.class);

    @Autowired
    private EventosDAO eventosDAO;
    
    @Autowired
    private ComprasDAO comprasDAO;

	@Autowired
	Configuration configuration;

	@Value("${uji.par.fitxersServiceURL:}")
	private String fitxersServiceURL;

	@Value("${uji.par.authToken:}")
	private String authToken;

    public List<Evento> getEventosConSesiones(String userUID)
    {
       return eventosDAO.getEventosConSesiones(userUID);
    }
    
    public List<Evento> getEventos(String sort, int start, int limit, String userUID)
    {
       return getEventos(false, sort, start, limit, userUID);
    }
    
    public List<Evento> getEventosActivos(String sort, int start, int limit, String userUID)
    {
       return getEventos(true, sort, start, limit, userUID);
    }
    
    private List<Evento> getEventos(boolean activos, String sort, int start, int limit, String userUID)
    {
        if (activos)
            return eventosDAO.getEventosActivos(sort, start, limit, userUID);
        else
            return eventosDAO.getEventos(sort, start, limit, userUID);
    }

    public void removeEvento(Long eventoId)
    {
        removeImagenes(eventoId);
        eventosDAO.removeEvento(eventoId);
    }

    public Evento addEvento(Evento evento) throws CampoRequeridoException
    {
        checkRequiredFields(evento);
		checkAndSaveImage(evento);
		return eventosDAO.addEvento(evento);
    }

	private void checkAndSaveImage(Evento evento) {
		byte[] imagen = evento.getImagen();
		if (imagen != null && imagen.length > 0) {
			String imagenSrc = evento.getImagenSrc();
			String imagenContentType = evento.getImagenContentType();
			if (!fitxersServiceURL.isEmpty() && !authToken.isEmpty() && imagenContentType != null) {
				String reference = insertImageToAdeWS(imagen, imagenSrc, imagenContentType);
				evento.setImagenUUID(reference);
			} else {
				evento.setImagen(imagen);
			}
		}

		byte[] imagenPubli = evento.getImagenPubli();
		if (imagenPubli != null && imagenPubli.length > 0)
		{
			String imagenPubliSrc = evento.getImagenPubliSrc();
			String imagenPubliContentType = evento.getImagenPubliContentType();
			if (!fitxersServiceURL.isEmpty() && !authToken.isEmpty() && imagenPubliContentType != null) {
				String reference = insertImageToAdeWS(imagenPubli, imagenPubliSrc, imagenPubliContentType);
				evento.setImagenPubliUUID(reference);
			}
			else {
				evento.setImagenPubli(imagenPubli);
			}
		}
	}

	private byte[] getImageFromAdeWS(String uuid) {
		try {
			ClientResponse response = getWebResource(fitxersServiceURL + "/storage/" + uuid).get(ClientResponse.class);
			InputStream is = response.getEntityInputStream();
			return IOUtils.toByteArray(is);
		} catch (Exception e) {
            log.error("La imagen con uuid " + uuid + " no existe");
		}
		return null;
	}

	private String insertImageToAdeWS(
		byte[] imagen,
		String imagenSrc,
		String imagenContentType
	) {
        try {
            FormDataMultiPart responseMultipart = getFormDataMultiPart(imagenSrc, imagenContentType, imagen);
            ClientResponse response = getWebResource(fitxersServiceURL + "/storage").type("multipart/form-data")
                .post(ClientResponse.class, responseMultipart);
            JsonNode responseMessage = response.getEntity(JsonNode.class);
            return responseMessage.path("data").path("reference").asText();
        }
        catch (Exception e) {
            log.error("No se ha podido guardar la imagen " + imagenSrc);
            throw new GuardarImagenException(imagenSrc);
        }
	}

    private void removeImageFromAdeWS(String uuid) {
        if (uuid != null) {
            try {
                ClientResponse response = getWebResource(fitxersServiceURL + "/storage/" + uuid).delete(ClientResponse.class);
                response.getEntityInputStream();
            } catch (Exception e) {
                log.error("No se ha podido eliminar la imagen con uuid " + uuid);
            }
        }
    }

	private WebResource.Builder getWebResource(String url) {
		Client client = Client.create();
		return client.resource(url).header("X-UJI-AuthToken", authToken);
	}

	private FormDataMultiPart getFormDataMultiPart(
		String name,
		String mimeType,
		byte[] bytes
	) {
		FormDataMultiPart responseMultipart = new FormDataMultiPart();
		responseMultipart.field("name", name);
		responseMultipart.field("mimetype", mimeType);
		responseMultipart.field("contents", bytes, MediaType.valueOf(mimeType));
		return responseMultipart;
	}

    private void checkRequiredFields(Evento evento) throws CampoRequeridoException
    {
        if (evento.getTituloEs() == null || evento.getTituloEs().isEmpty())
            throw new CampoRequeridoException("Título");
        if (evento.getParTiposEvento() == null)
            throw new CampoRequeridoException("Tipo de evento");
    }

    public void updateEvento(Evento evento, String userUID) throws CampoRequeridoException, EventoConCompras
    {
        checkRequiredFields(evento);

        if (hasEventoCompras(evento) && modificanAsientosNumerados(evento, userUID))
        	throw new EventoConCompras(evento.getId());
        else {
			checkAndSaveImage(evento);
			eventosDAO.updateEvento(evento, userUID);
		}
    }

	private boolean modificanAsientosNumerados(Evento evento, String userUID) {
		EventoDTO eventoDTO = eventosDAO.getEventoById(evento.getId(), userUID);
		return eventoDTO.getAsientosNumerados() != evento.getAsientosNumerados();
	}

	private boolean hasEventoCompras(Evento evento) {
		return comprasDAO.getComprasOfEvento(evento.getId()).size() > 0;
	}

    public Evento getEvento(Long eventoId, String userUID) throws EventoNoEncontradoException
    {
        EventoDTO eventoDTO = eventosDAO.getEventoById(eventoId.longValue(), userUID);
		if (eventoDTO != null) {
			return getEventoConImagen(eventoDTO);
		}

        throw new EventoNoEncontradoException(eventoId);
    }

	public Evento getEventoByRssId(Long contenidoId, String userUID) throws EventoNoEncontradoException
    {
        EventoDTO eventoDTO = eventosDAO.getEventoByRssId(contenidoId.toString(), userUID);

        if (eventoDTO == null)
            throw new EventoNoEncontradoException(contenidoId);
        else {
			return getEventoConImagen(eventoDTO);
		}
    }

	private Evento getEventoConImagen(EventoDTO eventoDTO) {
		Evento evento = new Evento(eventoDTO, true);
		if (evento.getImagenUUID() != null) {
			byte[] fitxerFromAdeWS = getImageFromAdeWS(evento.getImagenUUID());
			evento.setImagen(fitxerFromAdeWS);
		}

		if (evento.getImagenPubliUUID() != null) {
			byte[] fitxerFromAdeWS = getImageFromAdeWS(evento.getImagenPubliUUID());
			evento.setImagenPubli(fitxerFromAdeWS);
		}

		return evento;
	}

    public void removeImagenes(Long eventoId)
    {
        Tuple imagenesEvento = eventosDAO.getImagenUUID(eventoId);

        removeImagen(eventoId, imagenesEvento);
        removeImagenPubli(eventoId, imagenesEvento);
    }

    public void removeImagen(Long eventoId)
    {
        Tuple imagenesEvento = eventosDAO.getImagenUUID(eventoId);
        removeImagen(eventoId, imagenesEvento);
    }

    public void removeImagenPubli(Long eventoId)
	{
        Tuple imagenesEvento = eventosDAO.getImagenUUID(eventoId);
        removeImagenPubli(eventoId, imagenesEvento);
	}

    private void removeImagenPubli(
        Long eventoId,
        Tuple imagenesEvento
    ) {
        eventosDAO.deleteImagenPubli(eventoId);
        String imagenPubliUUID = imagenesEvento.get(1, String.class);
        if (imagenPubliUUID != null) {
            removeImageFromAdeWS(imagenPubliUUID);
        }
    }

    private void removeImagen(
        Long eventoId,
        Tuple imagenesEvento
    ) {
        eventosDAO.deleteImagen(eventoId);
        String imagenUUID = imagenesEvento.get(0, String.class);
        if (imagenUUID != null) {
            removeImageFromAdeWS(imagenUUID);
        }
    }

	public int getTotalEventosActivos(String userUID) {
		return eventosDAO.getTotalEventosActivos(userUID);
	}

	public int getTotalEventos(String userUID) {
		return eventosDAO.getTotalEventos(userUID);
	}

	public List<EventoParaSync> getEventosActivosParaVentaOnline(String urlPublic) {
		List<EventoDTO> eventosDTO = eventosDAO.getEventosActivosParaVentaOnline();
		List<EventoParaSync> eventosParaSync = new ArrayList<EventoParaSync>();
		String urlPrefix = urlPublic + "/rest/evento/";
		String urlSuffix = "?lang=##IDIOMA##";
		
		for (EventoDTO eventoDTO: eventosDTO) {
			EventoParaSync eventoParaSync = new EventoParaSync(eventoDTO.getRssId(), urlPrefix + eventoDTO.getRssId() + urlSuffix);
			eventosParaSync.add(eventoParaSync);
		}
		return eventosParaSync;
	}

	public byte[] getImagenSustitutivaSiExiste() throws IOException {
		String path = configuration.getPathImagenSustitutiva();
		if (path != null) {
			FileInputStream fis = new FileInputStream("/etc/uji/par/imagenes/" + path);
			return IOUtils.toByteArray(fis);
		} else
			return null;
	}

	public byte[] getImagenPubliSustitutivaSiExiste() throws IOException {
		String path = configuration.getPathImagenPubliSustitutiva();
		if (path != null) {
			FileInputStream fis = new FileInputStream("/etc/uji/par/imagenes/" + path);
			return IOUtils.toByteArray(fis);
		} else
			return null;
	}

	public String getImagenSustitutivaContentType() {
		return configuration.getImagenSustitutivaContentType();
	}

	public String getImagenPubliSustitutivaContentType() {
		return configuration.getImagenPubliSustitutivaContentType();
	}

	public List<Evento> getPeliculas() {
		List<EventoDTO> listPeliculasDTO = eventosDAO.getPeliculas();
		return getEventos(listPeliculasDTO);
	}

	private List<Evento> getEventos(List<EventoDTO> listPeliculasDTO) {
		List<Evento> listPeliculas = new ArrayList<Evento>();
		for (EventoDTO pelicula: listPeliculasDTO) {
			Evento evento = Evento.eventoDTOtoEvento(pelicula);
			listPeliculas.add(evento);
		}
		return listPeliculas;
	}

    private List<EventoMultisesion> getEventosMultisesion(List<Tuple> list) {
        List<EventoMultisesion> listPeliculas = new ArrayList<EventoMultisesion>();
        for (Tuple pelicula: list) {
			EventoDTO eventoDTO = pelicula.get(0, EventoDTO.class);
			String versionLinguistica = pelicula.get(1, String.class);
            EventoMultisesion evento = EventoMultisesion.tupleToEventoMultisesion(eventoDTO.getId(), eventoDTO.getTituloEs(),
					eventoDTO.getTituloVa(), versionLinguistica);
            listPeliculas.add(evento);
        }
        return listPeliculas;
    }

	public List<EventoMultisesion> getPeliculas(long eventoId) {
		List<Tuple> listPeliculasDTO = eventosDAO.getPeliculasMultisesion(eventoId);
		return getEventosMultisesion(listPeliculasDTO);
	}
}
