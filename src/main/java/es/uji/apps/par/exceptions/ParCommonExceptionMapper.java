package es.uji.apps.par.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import es.uji.apps.par.model.ParResponseMessage;

@Provider
public class ParCommonExceptionMapper implements ExceptionMapper<Exception>
{
    public static Logger log = Logger.getLogger(ParCommonExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception)
    {
        log.error(exception.getMessage(), exception);
        String message = ((exception.getMessage() != null && !exception.getMessage().equals(""))? 
        		exception.getMessage() :
        		ParException.ERROR_GENERAL_MESS);

        return Response.serverError().type(MediaType.APPLICATION_JSON).entity(new ParResponseMessage(false, message)).build();
    }
}