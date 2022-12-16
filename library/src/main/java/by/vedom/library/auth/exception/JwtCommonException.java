package by.vedom.library.auth.exception;

import javax.naming.AuthenticationException;

public class JwtCommonException extends AuthenticationException {

    public JwtCommonException(String message) {
        super(message);
    }
}
