package br.com.constran.mobile.exception;

public class AlertException extends Exception {

    private static final long serialVersionUID = 1L;

    public AlertException() {
        super();
    }

    public AlertException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AlertException(String detailMessage) {
        super(detailMessage);
    }

    public AlertException(Throwable throwable) {
        super(throwable);
    }
}
