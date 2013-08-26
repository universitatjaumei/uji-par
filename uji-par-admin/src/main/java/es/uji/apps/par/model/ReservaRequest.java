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
}
