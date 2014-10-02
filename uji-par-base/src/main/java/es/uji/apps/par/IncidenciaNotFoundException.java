package es.uji.apps.par;

@SuppressWarnings("serial")
public class IncidenciaNotFoundException extends Exception
{
    public IncidenciaNotFoundException(Integer incidenciaId)
    {
        super("Incidencia no encontrada: " + incidenciaId);
    }

	public IncidenciaNotFoundException()
	{
		super("Incidencia no encontrada");
	}
}
