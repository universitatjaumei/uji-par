package es.uji.apps.par;

import es.uji.apps.par.exceptions.GeneralPARException;


@SuppressWarnings("serial")
public class FueraDePlazoVentaInternetException extends GeneralPARException
{
    public FueraDePlazoVentaInternetException(Long sesionId)
    {
        super("Fuera de plazo para venta por internet: sesionId = " + sesionId);
    }
}
