package exceptions;

public class RejectedPayment extends RuntimeException {
    public RejectedPayment(String message) {
        super(message);
    }
}
