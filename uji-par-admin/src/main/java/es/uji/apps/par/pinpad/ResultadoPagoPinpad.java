package es.uji.apps.par.pinpad;

public class ResultadoPagoPinpad
{
    private boolean error;
    private String codigo;
    private String mensajeExcepcion;

    public ResultadoPagoPinpad()
    {
    }

    public ResultadoPagoPinpad(boolean error, String mensajeError)
    {
        this.error = error;
        this.mensajeExcepcion = mensajeError;
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

    public String getMensajeExcepcion()
    {
        return mensajeExcepcion;
    }

    public void setMensajeExcepcion(String mensajeError)
    {
        this.mensajeExcepcion = mensajeError;
    }

    @Override
    public String toString()
    {
        StringBuffer buff = new StringBuffer();
        
        buff.append("<error:");
        buff.append(error);
        buff.append(", codigo:");
        buff.append(codigo);
        buff.append(", mensajeError:");
        buff.append(mensajeExcepcion);
        buff.append(">");
        
        return buff.toString();
    }

}
