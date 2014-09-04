package es.uji.apps.par.auth;

import javax.servlet.http.HttpServletRequest;

public interface Authenticator
{
    public static final int AUTH_OK = 0;
    public static final int AUTH_FAILED = 1;
    public static final int AUTH_FAILED_INTERNAL_ERROR = 2;
    
    public static final String USER_ATTRIBUTE = "user";
    public static final String READONLY_ATTRIBUTE = "readonly";

    public int authenticate(HttpServletRequest request);
}