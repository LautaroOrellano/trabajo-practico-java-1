package exceptions;

public class EmptyCatalogException extends RuntimeException {
    public EmptyCatalogException(String message) {
        super(message);
    }
}
