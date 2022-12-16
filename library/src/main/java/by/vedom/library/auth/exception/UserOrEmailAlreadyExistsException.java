package by.vedom.library.auth.exception;

import javax.naming.AuthenticationException;

public class UserOrEmailAlreadyExistsException extends AuthenticationException {

    public UserOrEmailAlreadyExistsException(String message) {
        super(message);
    }
}
