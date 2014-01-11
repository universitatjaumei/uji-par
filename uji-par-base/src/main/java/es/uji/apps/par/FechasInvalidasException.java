package es.uji.apps.par;

import java.util.Date;

import es.uji.apps.par.exceptions.GeneralPARException;

@SuppressWarnings("serial")
public class FechasInvalidasException extends GeneralPARException
{

    public final static String FECHA_INICIO_VENTA_POSTERIOR_FECHA_FIN_VENTA = "Fecha de inicio de venta online posterior a fecha de fin";
    public final static String FECHA_FIN_VENTA_POSTERIOR_FECHA_CELEBRACION = "Fecha de fin de venta online posterior a fecha de celebración";

    public FechasInvalidasException(String message, Date fecha1, Date fecha2)
    {
        super(message + "(" + fecha1 + ", " + fecha2 + ")");
    }
}
