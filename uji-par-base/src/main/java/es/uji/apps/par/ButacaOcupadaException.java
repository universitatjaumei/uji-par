package es.uji.apps.par;

@SuppressWarnings("serial")
public class ButacaOcupadaException extends GeneralPARException
{
    private final Long sesionId;
    private final String localizacion;
    private final String fila;
    private final String numero;

    public ButacaOcupadaException(Long sesionId, String localizacion, String fila, String numero)
    {
        super(String.format("Butaca ocupada: sesionId = %d, localizacion=%s, fila=%s, numero=%s", sesionId,
                localizacion, fila, numero));

        this.sesionId = sesionId;
        this.localizacion = localizacion;
        this.fila = fila;
        this.numero = numero;
    }

    public Long getSesionId()
    {
        return sesionId;
    }

    public String getLocalizacion()
    {
        return localizacion;
    }

    public String getFila()
    {
        return fila;
    }

    public String getNumero()
    {
        return numero;
    }
}
