package by.vedom.library.auth.exception;

public class UserAlreadyActivatedException extends Exception {
    public UserAlreadyActivatedException(String message) {
        super(message);
    }

    public UserAlreadyActivatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyActivatedException(Throwable cause) {
        super(cause);
    }
}
