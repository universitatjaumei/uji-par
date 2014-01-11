package es.uji.apps.par;

import es.uji.apps.par.exceptions.GeneralPARException;

@SuppressWarnings("serial")
public class CompraSinButacasException extends GeneralPARException
{
    public CompraSinButacasException()
    {
        super("Compra sin butacas seleccionadas");
    }
}
