package es.uji.apps.par.exceptions;


@SuppressWarnings("serial")
public class CompraAulaTeatroPorInternetException extends GeneralPARException
{
    public CompraAulaTeatroPorInternetException()
    {
        super(TIPO_INCORRECTO_COMPRA_INTERNET_CODE);
    }
}
