package es.uji.apps.par.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResultadoCompra
{
    private boolean correcta;
    private List<Butaca> butacasOcupadas;
    
    public ResultadoCompra()
    {
    }

    public boolean getCorrecta()
    {
        return correcta;
    }

    public void setCorrecta(boolean correcta)
    {
        this.correcta = correcta;
    }

    public List<Butaca> getButacasOcupadas()
    {
        return butacasOcupadas;
    }

    public void setButacasOcupadas(List<Butaca> butacasOcupadas)
    {
        this.butacasOcupadas = butacasOcupadas;
    }

}
