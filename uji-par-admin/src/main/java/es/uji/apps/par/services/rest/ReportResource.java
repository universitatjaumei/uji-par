package es.uji.apps.par.services.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.log4j.Logger;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.fopreports.serialization.ReportSerializationException;
import es.uji.apps.par.exceptions.SinIvaException;
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
	public Response generateExcelTaquilla(@PathParam("fechaInicio") String fechaInicio, @PathParam("fechaFin") String fechaFin) throws TranscoderException, IOException {
		ByteArrayOutputStream ostream = reportService.getExcelTaquilla(fechaInicio, fechaFin);
		return Response.ok(ostream.toByteArray(), MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition","attachment; filename =\"informeTaquilla " + fechaInicio + "-" + fechaFin + ".xls\"").build();
	}
    
    @POST
	@Path("eventos/{fechaInicio}/{fechaFin}")
	@Produces("application/vnd.ms-excel")
	public Response generateExcelEventos(@PathParam("fechaInicio") String fechaInicio, @PathParam("fechaFin") String fechaFin) throws TranscoderException, IOException {
		ByteArrayOutputStream ostream = reportService.getExcelEventos(fechaInicio, fechaFin);
		return Response.ok(ostream.toByteArray(), MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition","attachment; filename =\"informeEvents " + fechaInicio + "-" + fechaFin + ".xls\"").build();
	}
    
    @POST
    @Path("taquilla/{fechaInicio}/{fechaFin}/pdf")
    @Produces("application/pdf")
    public Response generatePdfTaquilla(@PathParam("fechaInicio") String fechaInicio, @PathParam("fechaFin") String fechaFin) throws TranscoderException, IOException, ReportSerializationException, ParseException {
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        
        reportService.getPdfTaquilla(fechaInicio, fechaFin, ostream);
        
        return Response.ok(ostream.toByteArray(), MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition","attachment; filename =\"informeTaquilla " + fechaInicio + "-" + fechaFin + ".pdf\"").build();
    }    
    
    @POST
    @Path("taquilla/{fechaInicio}/{fechaFin}/efectivo/pdf")
    @Produces("application/pdf")
    public Response generatePdfEfectivo(@PathParam("fechaInicio") String fechaInicio, @PathParam("fechaFin") String fechaFin) throws TranscoderException, IOException, ReportSerializationException, ParseException {
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        
        try
        {
            reportService.getPdfEfectivo(fechaInicio, fechaFin, ostream);
        }
        catch (SinIvaException e)
        {
            log.error("Error", e);
            
            String errorMsj = String.format("ERROR: Cal introduir l'IVA de l'event \"%s\"", e.getEvento());
            return Response.serverError().type(MediaType.TEXT_PLAIN).entity(errorMsj).build();
        }
        
        return Response.ok(ostream.toByteArray(), MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition","attachment; filename =\"informeTaquilla " + fechaInicio + "-" + fechaFin + ".pdf\"").build();
    } 
    
    @POST
    @Path("taquilla/{fechaInicio}/{fechaFin}/tpv/pdf")
    @Produces("application/pdf")
    public Response generatePdfTpv(@PathParam("fechaInicio") String fechaInicio, @PathParam("fechaFin") String fechaFin) throws TranscoderException, IOException, ReportSerializationException, ParseException {
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        
        try
        {
            reportService.getPdfTpvSubtotales(fechaInicio, fechaFin, ostream);
        }
        catch (SinIvaException e)
        {
            log.error("Error", e);
            
            String errorMsj = String.format("ERROR: Cal introduir l'IVA de l'event \"%s\"", e.getEvento());
            return Response.serverError().type(MediaType.TEXT_PLAIN).entity(errorMsj).build();
        }
        
        return Response.ok(ostream.toByteArray(), MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition","attachment; filename =\"informeTaquilla " + fechaInicio + "-" + fechaFin + ".pdf\"").build();
    } 
    
    @POST
    @Path("taquilla/{fechaInicio}/{fechaFin}/eventos/pdf")
    @Produces("application/pdf")
    public Response generatePdfEventos(@PathParam("fechaInicio") String fechaInicio, @PathParam("fechaFin") String fechaFin) throws TranscoderException, IOException, ReportSerializationException, ParseException {
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        
        try
        {
            reportService.getPdfEventos(fechaInicio, fechaFin, ostream);
        }
        catch (SinIvaException e)
        {
            log.error("Error", e);
            
            String errorMsj = String.format("ERROR: Cal introduir l'IVA de l'event \"%s\"", e.getEvento());
            return Response.serverError().type(MediaType.TEXT_PLAIN).entity(errorMsj).build();
        }
        
        return Response.ok(ostream.toByteArray(), MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition","attachment; filename =\"informeEventos " + fechaInicio + "-" + fechaFin + ".pdf\"").build();
    } 
}
