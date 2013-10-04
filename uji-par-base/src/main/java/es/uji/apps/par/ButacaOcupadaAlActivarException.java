package es.uji.apps.par;

@SuppressWarnings("serial")
public class ButacaOcupadaAlActivarException extends GeneralPARException
{
    private final Long sesionId;
    private final String localizacion;
    private final String fila;
    private final String numero;
    private final boolean taquilla;
    private final String comprador;

    public ButacaOcupadaAlActivarException(Long sesionId, String localizacion, String fila, String numero,
            String comprador, boolean taquilla)
    {
        super(String.format("Butaca ocupada: sesionId = %d, localizacion=%s, fila=%s, numero=%s", sesionId,
                localizacion, fila, numero));

        this.sesionId = sesionId;
        this.localizacion = localizacion;
        this.fila = fila;
        this.numero = numero;
        this.comprador = comprador;
        this.taquilla = taquilla;
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

    public boolean getTaquilla()
    {
        return taquilla;
    }

    public String getComprador()
    {
        return comprador;
    }
}
