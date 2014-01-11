package es.uji.apps.par;

import es.uji.apps.par.exceptions.GeneralPARException;

@SuppressWarnings("serial")
public class CompraInvitacionPorInternetException extends GeneralPARException
{
    public CompraInvitacionPorInternetException()
    {
        super("Compra de entrada tipo invitación por internet");
    }
}
