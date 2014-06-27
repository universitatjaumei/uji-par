package es.uji.apps.par.services.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.print.PrintException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.fopreports.serialization.ReportSerializationException;
import es.uji.apps.par.ButacaOcupadaAlActivarException;
import es.uji.apps.par.ButacaOcupadaException;
import es.uji.apps.par.CompraSinButacasException;
import es.uji.apps.par.NoHayButacasLibresException;
import es.uji.apps.par.auth.AuthChecker;
import es.uji.apps.par.model.Butaca;
import es.uji.apps.par.model.DisponiblesLocalizacion;
import es.uji.apps.par.model.ResultadoCompra;
import es.uji.apps.par.services.ButacasService;
import es.uji.apps.par.services.ComprasService;
import es.uji.apps.par.services.EntradasService;
import es.uji.apps.par.services.ReportService;
import es.uji.apps.par.services.SesionesService;

@Path("compra")
public class CompraResource extends BaseResource {
	public static Logger log = Logger.getLogger(CompraResource.class);

	@InjectParam
	private ComprasService comprasService;

	@InjectParam
	private SesionesService sesionesService;

	@InjectParam
	private ButacasService butacasService;

	@InjectParam
	private EntradasService entradasService;

	@Context
	HttpServletResponse currentResponse;
	
	@InjectParam
	private ReportService reportService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompras(@PathParam("id") Long sesionId,
			@QueryParam("showAnuladas") int showAnuladas,
			@QueryParam("showOnline") int showOnline,
			@QueryParam("sort") String sort, @QueryParam("start") int start,
			@QueryParam("limit") @DefaultValue("1000") int limit,
			@QueryParam("search") @DefaultValue("") String search) {
		return Response
				.ok()
				.entity(new RestResponse(true, comprasService
						.getComprasBySesionFechaSegundos(sesionId,
								showAnuladas, sort, start, limit, showOnline,
								search), comprasService
						.getTotalComprasBySesion(sesionId, showAnuladas,
								showOnline, search))).build();
	}

	@PUT
	@Path("{idSesion}/{idCompraReserva}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response anularCompraOReserva(@PathParam("idSesion") Long sesionId,
			@PathParam("idCompraReserva") Long idCompraReserva) {
		AuthChecker.canWrite(currentRequest);

		comprasService.anularCompraReserva(idCompraReserva);
		return Response.ok().build();
	}

	@PUT
	@Path("{idSesion}/desanuladas/{idCompraReserva}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response desanularCompraOReserva(
			@PathParam("idSesion") Long sesionId,
			@PathParam("idCompraReserva") Long idCompraReserva) {
		AuthChecker.canWrite(currentRequest);

		try {
			comprasService.desanularCompraReserva(idCompraReserva);
			return Response.ok().build();
		} catch (ButacaOcupadaAlActivarException e) {
			return errorResponse("error.butacaOcupadaAlActivar",
					e.getTaquilla() ? "taquilla" : "online", e.getComprador(),
					getProperty("localizacion." + e.getLocalizacion()),
					e.getFila(), e.getNumero());
		}
	}

	@PUT
	@Path("{idSesion}/{idCompraReserva}/{idButaca}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response anularButaca(@PathParam("idSesion") Long sesionId,
			@PathParam("idCompraReserva") Long idCompraReserva,
			@PathParam("idButaca") Long idButaca) {
		AuthChecker.canWrite(currentRequest);

		comprasService.anularButacas(Arrays.asList(idButaca));
		return Response.ok().build();
	}

	@PUT
	@Path("{idSesion}/butacas/anuladas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response anularButacas(@PathParam("idSesion") Long sesionId,
			List<Long> idsButacas) {
		AuthChecker.canWrite(currentRequest);

		comprasService.anularButacas(idsButacas);
		return Response.ok().build();
	}

	@POST
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response compraEntrada(@PathParam("id") Long sesionId,
			List<Butaca> butacasSeleccionadas)
			throws NoHayButacasLibresException, ButacaOcupadaException {
		AuthChecker.canWrite(currentRequest);

		try {
			ResultadoCompra resultadoCompra = comprasService
					.registraCompraTaquilla(sesionId, butacasSeleccionadas);
			return Response.ok(resultadoCompra).build();
		} catch (NoHayButacasLibresException e) {
			return errorResponse("error.noHayButacas",
					getProperty("localizacion." + e.getLocalizacion()));
		} catch (ButacaOcupadaException e) {
			return errorResponse("error.butacaOcupada",
					getProperty("localizacion." + e.getLocalizacion()),
					e.getFila(), e.getNumero());
		} catch (CompraSinButacasException e) {
			return errorResponse("error.compraSinButacas");
		}
	}

	@GET
	@Path("{id}/precios")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPreciosSesion(@PathParam("id") Long sesionId,
			@QueryParam("sort") String sort, @QueryParam("start") int start,
			@QueryParam("limit") @DefaultValue("1000") int limit) {
		return Response
				.ok()
				.entity(new RestResponse(true, sesionesService
						.getPreciosSesion(sesionId, sort, start, limit, true),
						sesionesService.getTotalPreciosSesion(sesionId)))
				.build();
	}

	// Para una sesión no numerada devuelve las butacas disponibles por
	// localización
	@GET
	@Path("{id}/disponibles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOcupacionesNoNumerada(@PathParam("id") Long sesionId) {
		List<DisponiblesLocalizacion> listadoOcupacionesNoNumeradas = butacasService
				.getDisponiblesNoNumerada(sesionId);
		return Response
				.ok()
				.entity(new RestResponse(true, listadoOcupacionesNoNumeradas,
						listadoOcupacionesNoNumeradas.size())).build();
	}

	@POST
	@Path("{id}/importe")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getImportesButacas(@PathParam("id") Long sesionId,
			List<Butaca> butacasSeleccionadas) {
		AuthChecker.canWrite(currentRequest);

		BigDecimal importe = comprasService.calculaImporteButacas(sesionId,
				butacasSeleccionadas, true);

		return Response.ok().entity(importe.setScale(2).toString()).build();
	}

	@GET
	@Path("{idCompra}/butacas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getButacasCompra(@PathParam("idCompra") Long idCompra,
			@QueryParam("sort") String sort, @QueryParam("start") int start,
			@QueryParam("limit") @DefaultValue("1000") int limit) {
		return Response
				.ok()
				.entity(new RestResponse(true, butacasService.getButacasCompra(
						idCompra, sort, start, limit), butacasService
						.getTotalButacasCompra(idCompra))).build();
	}

	@GET
	@Path("{id}/pdf")
	@Produces("application/pdf")
	public Response generaEntradaPrintAtHome(@PathParam("id") String uuidCompra)
			throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		entradasService.generaEntrada(uuidCompra, bos);

		Response response = Response.ok(bos.toByteArray())
				.header("Cache-Control", "no-cache, no-store, must-revalidate")
				.header("Pragma", "no-cache").header("Expires", "0").build();

		return response;
	}

	@GET
	@Path("{id}/pdftaquilla")
	@Produces("application/pdf")
	public Response generaEntradaTaquilla(@PathParam("id") String uuidCompra)
			throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		entradasService.generaEntradaTaquilla(uuidCompra, bos);

		Response response = Response.ok(bos.toByteArray())
				.header("Cache-Control", "no-cache, no-store, must-revalidate")
				.header("Pragma", "no-cache").header("Expires", "0").build();

		return response;
	}

	@GET
	@Path("{id}/silentpdftaquilla")
	public void printDirectamente(@PathParam("id") String uuidCompra)
			throws ReportSerializationException, SAXException, IOException,
			InterruptedException, PrintException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		entradasService.generaEntradaTaquilla(uuidCompra, bos);
		reportService.silentPrint(bos);
	}
}
