package es.uji.apps.par.exceptions;


@SuppressWarnings("serial")
public class TipoEnvioInvalidoException extends GeneralPARException
{
    public TipoEnvioInvalidoException()
    {
        super(TIPO_ENVIO_INVALIDO_CODE);
    }
}
