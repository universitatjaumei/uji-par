package es.uji.apps.par.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReservaRequest
{
    private Date desde;
    private Date hasta;
    private List<Butaca> butacasSeleccionadas;
    private String observaciones;
    private int horaInicial;
    private int horaFinal;
    private int minutoInicial;
    private int minutoFinal;

    public ReservaRequest()
    {
        butacasSeleccionadas = new ArrayList<Butaca>();
    }

    public Date getDesde()
    {
        return desde;
    }

    public void setDesde(Date desde)
    {
        this.desde = desde;
    }

    public Date getHasta()
    {
        return hasta;
    }

    public void setHasta(Date hasta)
    {
        this.hasta = hasta;
    }

    public List<Butaca> getButacasSeleccionadas()
    {
        return butacasSeleccionadas;
    }

    public void setButacasSeleccionadas(List<Butaca> butacasSeleccionadas)
    {
        this.butacasSeleccionadas = butacasSeleccionadas;
    }

    public String getObservaciones()
    {
        return observaciones;
    }

    public void setObservaciones(String observaciones)
    {
        this.observaciones = observaciones;
    }

	public int getHoraInicial() {
		return horaInicial;
	}

	public void setHoraInicial(int horaInicial) {
		this.horaInicial = horaInicial;
	}

	public int getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(int horaFinal) {
		this.horaFinal = horaFinal;
	}

	public int getMinutoInicial() {
		return minutoInicial;
	}

	public void setMinutoInicial(int minutoInicial) {
		this.minutoInicial = minutoInicial;
	}

	public int getMinutoFinal() {
		return minutoFinal;
	}

	public void setMinutoFinal(int minutoFinal) {
		this.minutoFinal = minutoFinal;
	}
}
