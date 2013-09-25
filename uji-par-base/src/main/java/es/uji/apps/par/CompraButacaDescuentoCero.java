package es.uji.apps.par;

@SuppressWarnings("serial")
public class CompraButacaDescuentoCero extends GeneralPARException
{
    public CompraButacaDescuentoCero()
    {
        super("Se ha intentado comprar una butaca con descuento cero");
    }
}
