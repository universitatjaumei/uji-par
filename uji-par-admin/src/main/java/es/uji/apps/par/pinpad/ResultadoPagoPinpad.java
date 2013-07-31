package es.uji.apps.par.pinpad;

public class ResultadoPagoPinpad
{
    private boolean error;
    private String codigo;
    private String mensajeError;

    public ResultadoPagoPinpad()
    {
    }

    public ResultadoPagoPinpad(boolean error, String mensajeError)
    {
        this.error = error;
        this.mensajeError = mensajeError;
    }

    public boolean getError()
    {
        return error;
    }

    public void setError(boolean error)
    {
        this.error = error;
    }

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    public String getMensajeError()
    {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError)
    {
        this.mensajeError = mensajeError;
    }

}
