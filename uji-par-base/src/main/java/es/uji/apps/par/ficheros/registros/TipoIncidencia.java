package es.uji.apps.par.ficheros.registros;

import es.uji.apps.par.IncidenciaNotFoundException;

public enum TipoIncidencia
{
    SIN_INCIDENCIAS("000"), VENDA_MANUAL_DEGRADADA("005"), VENDA_MANUAL_ANULACIO("006"),
    VENDA_MANUAL_ANULACIO_PROGRAMACIO("007"), VENDA_MANUAL_ANULACIO_COMPLETA("008"), ANULACIO_VENDES("009"),
    ANULACIO_PROGRAMACIO("010"), ANULACIO_COMPLETA("011");

    private String codigo;

    private TipoIncidencia(String codigo)
    {
        this.codigo = codigo;
    }

    public String getCodigo()
    {
        return codigo;
    }

	public static TipoIncidencia intToTipoIncidencia(int incidenciaId) throws IncidenciaNotFoundException {
		switch(incidenciaId) {
			case 0:
				return SIN_INCIDENCIAS;
			case 5:
				return VENDA_MANUAL_DEGRADADA;
			case 6:
				return VENDA_MANUAL_ANULACIO;
			case 7:
				return VENDA_MANUAL_ANULACIO_PROGRAMACIO; 
			case 8:
				return VENDA_MANUAL_ANULACIO_COMPLETA;
			case 9:
				return ANULACIO_VENDES;
			case 10:
				return ANULACIO_PROGRAMACIO;
			case 11:
				return ANULACIO_COMPLETA;
		}
		throw new IncidenciaNotFoundException(incidenciaId);
	}

}
