package es.uji.apps.par.butacas;

import es.uji.apps.par.model.Butaca;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class EstadoButacasRequest
{
    List<Butaca> butacas;
    String uuidCompra;

    public EstadoButacasRequest()
    {
        butacas = new ArrayList<Butaca>();
    }

    public List<Butaca> getButacas()
    {
        return butacas;
    }

    public void setButacas(List<Butaca> butacas)
    {
        this.butacas = butacas;
    }

    public String getUuidCompra()
    {
        return uuidCompra;
    }

    public void setUuidCompra(String uuidCompra)
    {
        this.uuidCompra = uuidCompra;
    }
}
