package by.vedom.library.auth.exception;

import javax.naming.AuthenticationException;

public class RoleNotFoundException extends AuthenticationException {

    public RoleNotFoundException(String message) {
        super(message);
    }
}
