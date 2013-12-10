package es.uji.apps.par.ficheros;

public enum TipoIncidencia
{
    // TODO: Falta poner códigos de tabla ICAA

    SIN_INCIDENCIAS("001"), CANCELADA("002");

    private String codigo;

    private TipoIncidencia(String codigo)
    {
        this.codigo = codigo;
    }

    public String getCodigo()
    {
        return codigo;
    }
}
