package es.uji.apps.par.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.uji.apps.par.GeneralPARException;
import es.uji.apps.par.RegistroSerializaException;
import es.uji.apps.par.db.EventoDTO;
import es.uji.apps.par.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static EventoMultisesion objetToEventoMultisesion(Object[] pelicula) {
        EventoMultisesion eventoMultisesion = new EventoMultisesion();
        eventoMultisesion.setId(Utils.safeObjectToInt(pelicula[0]));
        eventoMultisesion.setTituloEs(Utils.safeObjectToString(pelicula[1]));
        eventoMultisesion.setTituloVa(Utils.safeObjectToString(pelicula[2]));
        eventoMultisesion.setVersionLinguistica(Utils.safeObjectToString(pelicula[3]));

        return eventoMultisesion;
    }
}