package es.uji.apps.par.auth;

import javax.servlet.http.HttpServletRequest;

public class MockAuthenticator implements Authenticator
{
    @Override
    public int authenticate(HttpServletRequest request)
    {
        request.getSession().setAttribute(USER_ATTRIBUTE, "admin");
        return 0;
    }
}
