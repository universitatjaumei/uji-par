package es.uji.apps.par.exceptions;


@SuppressWarnings("serial")
public class CompraButacaNoExistente extends GeneralPARException
{
    public CompraButacaNoExistente()
    {
        super(COMPRA_BUTACA_NO_EXISTENTE_CODE);
    }
}
