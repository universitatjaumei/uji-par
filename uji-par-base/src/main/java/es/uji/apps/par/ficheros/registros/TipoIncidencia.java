package es.uji.apps.par.ficheros.registros;

import es.uji.apps.par.IncidenciaNotFoundException;

public enum TipoIncidencia
{
    SIN_INCIDENCIAS("000"), VENDA_MANUAL_DEGRADADA("005"), VENDA_MANUAL_I_ANULACIO_VENTES("006"),
    VENDA_MANUAL_I_REPROGRAMACIO("007"), VENDA_MANUAL_I_ANULACIO_VENTES_I_REPROGRAMACIO("008"),
    ANULACIO_VENDES("009"), REPROGRAMACIO("010"), ANULACIO_VENTES_I_REPROGRAMACIO("011");

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
				return VENDA_MANUAL_I_ANULACIO_VENTES;
			case 7:
				return VENDA_MANUAL_I_REPROGRAMACIO;
			case 8:
				return VENDA_MANUAL_I_ANULACIO_VENTES_I_REPROGRAMACIO;
			case 9:
				return ANULACIO_VENDES;
			case 10:
				return REPROGRAMACIO;
			case 11:
				return ANULACIO_VENTES_I_REPROGRAMACIO;
		}
		throw new IncidenciaNotFoundException(incidenciaId);
	}

	public static int tipoIncidenciaToInt(TipoIncidencia tipoIncidencia) throws IncidenciaNotFoundException {
		if (tipoIncidencia == SIN_INCIDENCIAS)
			return 0;
		else if (tipoIncidencia == VENDA_MANUAL_DEGRADADA)
			return 5;
		else if (tipoIncidencia == VENDA_MANUAL_I_ANULACIO_VENTES)
			return 6;
		else if (tipoIncidencia == VENDA_MANUAL_I_REPROGRAMACIO)
			return 7;
		else if (tipoIncidencia == VENDA_MANUAL_I_ANULACIO_VENTES_I_REPROGRAMACIO)
			return 8;
		else if (tipoIncidencia == ANULACIO_VENDES)
			return 9;
		else if (tipoIncidencia == REPROGRAMACIO)
			return 10;
		else if (tipoIncidencia == ANULACIO_VENTES_I_REPROGRAMACIO)
			return 11;

		throw new IncidenciaNotFoundException();
	}

}
