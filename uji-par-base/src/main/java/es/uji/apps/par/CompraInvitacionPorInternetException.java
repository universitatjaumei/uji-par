package es.uji.apps.par;

@SuppressWarnings("serial")
public class CompraInvitacionPorInternetException extends GeneralPARException
{
    public CompraInvitacionPorInternetException()
    {
        super("Compra sin butacas seleccionadas");
    }
}
