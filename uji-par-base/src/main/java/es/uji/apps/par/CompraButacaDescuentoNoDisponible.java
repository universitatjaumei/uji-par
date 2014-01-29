package es.uji.apps.par;


@SuppressWarnings("serial")
public class CompraButacaDescuentoNoDisponible extends GeneralPARException
{
    public CompraButacaDescuentoNoDisponible()
    {
        super("Se ha intentado comprar una butaca con descuento cero");
    }
}
