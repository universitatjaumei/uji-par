package es.uji.apps.par.auth;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import es.uji.apps.par.config.Configuration;

public class AuthFilter implements Filter
{
    private static final Logger log = Logger.getLogger(AuthFilter.class);
    private static final Pattern excluded =  Pattern.compile(".*/login|.*/logout|.*/.*\\.png|.*\\.jpg|.*\\.js|.*\\.css");

    private Authenticator authClass;

    public void init(FilterConfig filterConfig) throws ServletException
    {
        try
        {
            authClass = (Authenticator) Class.forName(Configuration.getAuthClass()).newInstance();
        }
        catch (Exception e)
        {
            log.error("Error instanciando clase de autenticación", e);
        }
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
        HttpServletResponse sResponse = (HttpServletResponse) response;
        HttpSession session = sRequest.getSession();

        if (isExcluded(sRequest.getRequestURI()))
        {
            chain.doFilter(request, response);
            return;
        }

        if (session.getAttribute(Authenticator.USER_ATTRIBUTE) != null)
        {
            chain.doFilter(request, response);
        }
        else
        {
            int authResult = this.authClass.authenticate(sRequest);

            if (authResult == Authenticator.AUTH_OK)
            {
                chain.doFilter(request, response);
            }
            else
            {
                sResponse.sendRedirect(Configuration.getUrlAdmin() + "/rest/login");
            }
        }
    }
}