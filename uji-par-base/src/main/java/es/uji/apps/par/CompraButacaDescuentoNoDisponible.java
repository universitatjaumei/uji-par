package es.uji.apps.par;

import es.uji.apps.par.exceptions.GeneralPARException;

@SuppressWarnings("serial")
public class CompraButacaDescuentoNoDisponible extends GeneralPARException
{
    public CompraButacaDescuentoNoDisponible()
    {
        super("Se ha intentado comprar una butaca con descuento cero");
    }
}
