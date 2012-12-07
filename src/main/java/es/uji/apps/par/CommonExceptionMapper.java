package es.uji.apps.par;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;


@Provider
public class CommonExceptionMapper implements ExceptionMapper<Exception>
{
    public static Logger log = Logger.getLogger(CommonExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception)
    {
        log.error(exception.getMessage(), exception);
        String message = ((exception.getMessage() != null && !exception.getMessage().equals(""))? 
        		exception.getMessage() :
        		GeneralPARException.ERROR_GENERAL_MESS);

        return Response.serverError().type(MediaType.APPLICATION_JSON).entity(new ResponseMessage(false, message)).build();
    }
}