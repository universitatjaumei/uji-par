package es.uji.apps.par.auth;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.par.services.UJIPerfilesService;
import es.uji.commons.sso.AccessManager;
import es.uji.commons.sso.User;

public class AuthFilterSSOUji implements Filter
{
    private static final Logger log = Logger.getLogger(AuthFilterSSOUji.class);
    private static final Pattern excluded =  Pattern.compile(".*/login|.*/logout|.*/.*\\.png|.*\\.jpg|.*\\.js|.*\\.css|.*/sync");

    private FilterConfig filterConfig;
    private String returnScheme;
    private String returnHost;
    private String returnPort;
    private String defaultUserId;
    private String defaultUserName;
    
    @Autowired
    UJIPerfilesService ujiPerfilesService;

    @Context
    ServletContext ctx;
    @Override
    public void init(FilterConfig config) throws ServletException
    {
        filterConfig = config;
        ctx = config.getServletContext();
        
        returnScheme = filterConfig.getInitParameter("returnScheme");
        returnHost = filterConfig.getInitParameter("returnHost");
        returnPort = filterConfig.getInitParameter("returnPort");
        defaultUserId = filterConfig.getInitParameter("defaultUserId");
        defaultUserName = filterConfig.getInitParameter("defaultUserName");
    }


    public void destroy()
    {
    }

    private boolean isExcluded(String url)
    {
        boolean matches = excluded.matcher(url).matches();
        return matches;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException
    {
        HttpServletRequest sRequest = (HttpServletRequest) request;
        User user = AccessManager.getConnectedUser(sRequest);
        boolean isUserValid = ujiPerfilesService.hasPerfil("ADMIN", user.getId());

        if (isExcluded(sRequest.getRequestURI()))
        {
            chain.doFilter(request, response);
            return;
        }
        
        if (user != null && isUserValid)
        {
        	log.info("Ya autenticados " + sRequest.getRequestURI());
            chain.doFilter(request, response);
        }
        else
        {
        	log.info("Autenticamos " + sRequest.getRequestURI());
        	//chain.doFilter(request, response);
            /* 
            HttpServletResponse sResponse = (HttpServletResponse) response;
            int authResult = this.authClass.authenticate(sRequest);

            if (authResult == Authenticator.AUTH_OK)
            {
            	log.info("Autenticamos " + sRequest.getRequestURI() + " OK");
                chain.doFilter(request, response);
            }
            else
            {
            	String url = ((HttpServletRequest)request).getRequestURL().toString();
            	log.info("Autenticamos " + url + " KO");
            	if (url.toLowerCase().contains("par/rest/index"))
            		sResponse.sendRedirect(Configuration.getUrlAdmin() + "/rest/login");
            	else
            		sResponse.sendError(403);
            }*/
        }
    }
}