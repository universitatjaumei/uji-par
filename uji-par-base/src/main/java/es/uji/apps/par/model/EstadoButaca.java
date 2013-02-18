package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EstadoButaca
{
    private boolean ocupada;

    public EstadoButaca()
    {
    }

    public EstadoButaca(boolean ocupada)
    {
        this.ocupada = ocupada;
    }

    public boolean isOcupada()
    {
        return ocupada;
    }

    public void setOcupada(boolean ocupada)
    {
        this.ocupada = ocupada;
    }

}
