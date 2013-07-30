package es.uji.apps.par.pinpad;

public class EstadoPinpad
{
    private boolean error;
    private boolean ready;
    private String codigoAccion;
    private String mensaje;
    private boolean pagoCorrecto;
    private String recibo;

    public EstadoPinpad(boolean error)
    {
        this.error = error;
    }

    public boolean getReady()
    {
        return ready;
    }

    public void setReady(boolean pagoCorrecto)
    {
        this.ready = pagoCorrecto;
    }

    public String getCodigoAccion()
    {
        return codigoAccion;
    }

    public void setCodigoAccion(String codigoAccion)
    {
        this.codigoAccion = codigoAccion;
    }

    public void setError(boolean error)
    {
        this.error = error;
    }

    public boolean getError()
    {
        return error;
    }

    public String getMensaje()
    {
        return mensaje;
    }

    public void setMensaje(String mensaje)
    {
        this.mensaje = mensaje;
    }

    public boolean getPagoCorrecto()
    {
        return pagoCorrecto;
    }

    public void setPagoCorrecto(boolean pagoCorrecto)
    {
        this.pagoCorrecto = pagoCorrecto;
    }

    public String getRecibo()
    {
        return recibo;
    }

    public void setRecibo(String recibo)
    {
        this.recibo = recibo;
    }

}
