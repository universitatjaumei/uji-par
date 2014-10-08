package es.uji.apps.par.exceptions;



@SuppressWarnings("serial")
public class FueraDePlazoVentaInternetException extends GeneralPARException
{
    public FueraDePlazoVentaInternetException(Long sesionId)
    {
        super(VENTA_FUERA_DE_PLAZO_CODE, VENTA_FUERA_DE_PLAZO + "sesionId = " + sesionId);
    }
}
