package es.uji.apps.par.model;

import es.uji.apps.par.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Tuple;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EventoMultisesion
{
	private static final Logger logger = LoggerFactory.getLogger(EventoMultisesion.class);

    private long id;
    private String versionLinguistica;
    private String tituloEs;
    private String tituloVa;

    public EventoMultisesion(){
    }

	public EventoMultisesion(Integer idEvento) {
		this.id = idEvento;
	}

    public EventoMultisesion(Integer idEvento, String versionLinguistica) {
        this.id = idEvento;
        this.versionLinguistica = versionLinguistica;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVersionLinguistica() {
        return versionLinguistica;
    }

    public void setVersionLinguistica(String versionLinguistica) {
        this.versionLinguistica = versionLinguistica;
    }

    public String getTituloEs() {
        return tituloEs;
    }

    public void setTituloEs(String tituloEs) {
        this.tituloEs = tituloEs;
    }

    public String getTituloVa() {
        return tituloVa;
    }

    public void setTituloVa(String tituloVa) {
        this.tituloVa = tituloVa;
    }

    public static EventoMultisesion tupleToEventoMultisesion(long id, String tituloEs, String tituloVa,
			String versionLinguistica) {
        EventoMultisesion eventoMultisesion = new EventoMultisesion();
        eventoMultisesion.setId(id);
        eventoMultisesion.setTituloEs(tituloEs);
        eventoMultisesion.setTituloVa(tituloVa);
        eventoMultisesion.setVersionLinguistica(versionLinguistica);

        return eventoMultisesion;
    }
}