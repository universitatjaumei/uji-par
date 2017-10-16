package es.uji.apps.par.services.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.core.InjectParam;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import es.uji.apps.par.exceptions.CompraButacaNoExistente;
import es.uji.apps.par.model.Usuario;
import es.uji.apps.par.services.ComprasService;
import es.uji.apps.par.services.EntradasService;
import es.uji.apps.par.services.UsersService;

@Path("compra")
public class ComprasResource extends BaseResource
{
    private static final Logger log = LoggerFactory.getLogger(ComprasResource.class);
    @InjectParam
    EntradasService entradasService;

    @InjectParam
    UsersService usersService;

    @Context
    HttpServletResponse currentResponse;

    @InjectParam
    ComprasService comprasService;

    @GET
    @Path("{id}/pdf")
    @Produces("application/pdf")
    public Response datosEntrada(@PathParam("id") String uuidCompra) throws Exception
    {
        Usuario user = usersService.getUserByServerName(currentRequest.getServerName());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        entradasService.generaEntrada(uuidCompra, bos, user.getUsuario(), configurationSelector.getUrlPublicSinHTTPS(), configurationSelector.getUrlPieEntrada());

        Response response = Response.ok(bos.toByteArray())
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .header("Content-Disposition","attachment; filename =\"entrada_" + uuidCompra + ".pdf\"")
                .build();

        return response;
    }

    @GET
    @Path("{uuidCompra}/{idButaca}/passbook")
    public Response getPassbook(
        @PathParam("uuidCompra") String uuidCompra,
        @PathParam("idButaca") Long idButaca
    ) throws Exception
    {
        byte[] entradaPassbook;
        String urlPublicSinHTTPS = configurationSelector.getUrlPublicSinHTTPS();
        Response response = Response.seeOther(URI.create(urlPublicSinHTTPS + "/rest/notfound")).build();
        try {
            Usuario user = usersService.getUserByServerName(currentRequest.getServerName());
            boolean existeCompraYButacaYSeCorresponden = comprasService.existeCompraButaca(uuidCompra, idButaca, user.getId());

            if (existeCompraYButacaYSeCorresponden) {
                entradaPassbook = getEntradaPassbook(uuidCompra, idButaca);
                response = Response.ok(entradaPassbook).header("Cache-Control", "no-cache, no-store, must-revalidate")
                    .header("Pragma", "no-cache").header("Expires", "0")
                    .header("Content-Type", "application/vnd.apple.pkpass")
                    .header("Content-Disposition", "attachment; filename =\"entrada_" + idButaca + ".pkpass\"").build();
            } else {
                throw new CompraButacaNoExistente();
            }
        } catch (Exception e){
            log.error("", e);
            response = Response.seeOther(URI.create(urlPublicSinHTTPS + "/rest/notfound")).build();
        } finally {
            return response;
        }
    }

    private byte[] getEntradaPassbook(String uuidCompra, Long idButaca) throws IOException {
        String url = getUrlPassbook() + uuidCompra + "/" + idButaca;
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse r = webResource.get(ClientResponse.class);
        InputStream entityInputStream = r.getEntityInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(entityInputStream, bos);
        return bos.toByteArray();
    }

    private String getUrlPassbook(){
        String puerto = "";
        if(configuration.isDebug()) {
            puerto = configuration.getPortPassbookDebug();
        }
        String path = "par-passbook/rest/compra/";
        String protocolo = "http";
        if(this.currentRequest.isSecure()){
            protocolo = "https";
        }
        return String.format("%s://%s%s/%s", protocolo, this.currentRequest.getServerName(), puerto, path);
    }
}
