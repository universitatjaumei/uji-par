package es.uji.apps.par;

@SuppressWarnings("serial")
public class IncidenciaNotFoundException extends GeneralPARException
{
    public IncidenciaNotFoundException(Integer incidenciaId)
    {
        super(GeneralPARException.NOT_FOUND_INCIDENCIA_CODE, "Codigo incidencia: " + incidenciaId);
    }

	public IncidenciaNotFoundException()
	{
		super(GeneralPARException.NOT_FOUND_INCIDENCIA_CODE);
	}
}
