package es.uji.apps.par.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jasypt.util.password.BasicPasswordEncryptor;

import es.uji.apps.par.config.Configuration;

public class FileAuthenticator implements Authenticator
{
    private static final String LOGIN_PARAM = "login";
    private static final String PASSWORD_PARAM = "password";
    
    private BasicPasswordEncryptor encryptor;

    public FileAuthenticator()
    {
        encryptor = new BasicPasswordEncryptor();
    }

    @Override
    public int authenticate(HttpServletRequest request)
    {
        HttpSession session = request.getSession();

        if (loginAdminOk(request))
        {
            session.setAttribute(USER_ATTRIBUTE, request.getParameter(LOGIN_PARAM));
            return AUTH_OK;
        }
        else if (loginUserReadonlyOk(request))
        {
            session.setAttribute(USER_ATTRIBUTE, request.getParameter(LOGIN_PARAM));
            session.setAttribute(READONLY_ATTRIBUTE, true);
            return AUTH_OK;
        }
        else
        {
            return AUTH_FAILED;
        }
    }

    private boolean loginAdminOk(HttpServletRequest request)
    {
        return loginOk(request, Configuration.getAdminLogin(), Configuration.getAdminPassword());
    }

    private boolean loginUserReadonlyOk(HttpServletRequest request)
    {
        return loginOk(request, Configuration.getUserReadonlyLogin(), Configuration.getUserReadonlyPassword());
    }
    
    private boolean loginOk(HttpServletRequest request, String login, String passwordCifrado)
    {
        String loginFromRequest = request.getParameter(LOGIN_PARAM);

        if (loginFromRequest != null && loginFromRequest.equals(login))
        {
            return encryptor.checkPassword(request.getParameter(PASSWORD_PARAM), passwordCifrado);
        }
        else
        {
            return false;
        }
    }
    
    public static void main(String[] args)
    {
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        
        System.out.println(encryptor.encryptPassword("x"));
    }
}
