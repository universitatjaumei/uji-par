package es.uji.apps.par.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CompraRequest
{
    private Long idCompra;
    private List<Butaca> butacasSeleccionadas;

    public Long getIdCompra()
    {
        return idCompra;
    }

    public void setIdCompra(Long idCompra)
    {
        this.idCompra = idCompra;
    }

    public List<Butaca> getButacasSeleccionadas()
    {
        return butacasSeleccionadas;
    }

    public void setButacasSeleccionadas(List<Butaca> butacasSeleccionadas)
    {
        this.butacasSeleccionadas = butacasSeleccionadas;
    }

}