package exceptions;

public class ConnectionRefusedException extends Exception {
    public ConnectionRefusedException() {
    }

    public ConnectionRefusedException(String message) {
        super(message);
    }

    public ConnectionRefusedException(String message, Throwable cause) {
        super(message, cause);
    }
}
