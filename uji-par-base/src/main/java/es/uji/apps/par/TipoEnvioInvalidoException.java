package es.uji.apps.par;


@SuppressWarnings("serial")
public class TipoEnvioInvalidoException extends GeneralPARException
{
	public static final String TIPO_ENVIO_INVALIDO = "El tipo de envío es inválido. Debe ser FL o AT";

    public TipoEnvioInvalidoException()
    {
        super(TIPO_ENVIO_INVALIDO);
    }
}
