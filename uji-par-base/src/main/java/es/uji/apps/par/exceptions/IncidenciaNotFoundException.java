package es.uji.apps.par.exceptions;

@SuppressWarnings("serial")
public class IncidenciaNotFoundException extends GeneralPARException
{
    public IncidenciaNotFoundException(Integer incidenciaId)
    {
        super(NOT_FOUND_INCIDENCIA_CODE, "Codigo incidencia: " + incidenciaId);
    }

	public IncidenciaNotFoundException()
	{
		super(NOT_FOUND_INCIDENCIA_CODE);
	}
}
