package es.uji.apps.par;

import es.uji.apps.par.exceptions.GeneralPARException;

@SuppressWarnings("serial")
public class CompraAulaTeatroPorInternetException extends GeneralPARException
{
    public CompraAulaTeatroPorInternetException()
    {
        super("Compra de tipo \"aula de teatro\" por internet");
    }
}
