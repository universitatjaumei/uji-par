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
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import es.uji.apps.par.services.UJIPerfilesService;
import es.uji.commons.sso.AccessManager;
import es.uji.commons.sso.User;

public class AuthFilterSSOUji implements Filter
{
	private static final Logger log = LoggerFactory.getLogger(AuthFilterSSOUji.class);
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
        boolean isUserValid = false;
        
        if (sRequest.getSession().getAttribute("user") == null) {
        	sRequest.getSession().setAttribute("user", user);
            isUserValid = ujiPerfilesService.hasPerfil("ADMIN", user.getId());
            sRequest.getSession().setAttribute("isUserValid", isUserValid);
        } else
        	isUserValid = (Boolean) sRequest.getSession().getAttribute("isUserValid");

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
        	HttpServletResponse sResponse = (HttpServletResponse) response;
        	redirectToEmptyPage(request, sResponse);
        }
    }

	private void redirectToEmptyPage(ServletRequest request, HttpServletResponse sResponse) throws IOException {
		//sResponse.sendRedirect(Configuration.getUrlAdmin() + "/rest/login");
		sResponse.sendError(403);
	}
}