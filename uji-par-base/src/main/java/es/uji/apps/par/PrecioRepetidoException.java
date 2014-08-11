package es.uji.apps.par;

@SuppressWarnings("serial")
public class PrecioRepetidoException extends GeneralPARException {

    public PrecioRepetidoException() {
        super(PRECIO_REPETIDO_CODE);
    }
}
