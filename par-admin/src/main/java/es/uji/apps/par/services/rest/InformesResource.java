package es.uji.apps.par.services.rest;

import com.sun.jersey.api.core.InjectParam;
import es.uji.apps.par.model.TipoInforme;
import es.uji.apps.par.services.InformesService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("informe")
public class InformesResource extends BaseResource
{
    @InjectParam
    private InformesService informesService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll()
    {
        List<TipoInforme> tiposInforme = informesService.getTiposInforme(getLocale().getLanguage());
                
        return Response.ok().entity(new RestResponse(true, tiposInforme, tiposInforme.size())).build();
    }

	@GET
	@Path("generales")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGenerales()
	{
		List<TipoInforme> tiposInforme = informesService.getTiposInformeGenerales(getLocale().getLanguage());

		return Response.ok().entity(new RestResponse(true, tiposInforme, tiposInforme.size())).build();
	}
}
