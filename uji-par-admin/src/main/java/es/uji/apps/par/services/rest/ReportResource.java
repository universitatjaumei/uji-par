package es.uji.apps.par.services.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.log4j.Logger;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.services.ReportService;

@Path("report")
public class ReportResource extends BaseResource
{
    public static Logger log = Logger.getLogger(ReportResource.class);
    
    @InjectParam
    private ReportService reportService;
    
    @POST
	@Path("taquilla/{fechaInicio}/{fechaFin}")
	@Produces("application/vnd.ms-excel")
	public Response gridToExcel(@PathParam("fechaInicio") String fechaInicio, @PathParam("fechaFin") String fechaFin) throws TranscoderException, IOException {
		ByteArrayOutputStream ostream = reportService.getExcelTaquilla(fechaInicio, fechaFin);
		return Response.ok(ostream.toByteArray(), MediaType.APPLICATION_OCTET_STREAM).header("content-disposition","attachment; filename = informeTaquilla " + fechaInicio + "-" + fechaFin + ".xls").build();
	}
}
