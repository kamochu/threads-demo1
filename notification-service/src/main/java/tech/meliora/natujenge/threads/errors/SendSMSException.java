package tech.meliora.natujenge.threads.errors;

public class SendSMSException extends Exception{
    public SendSMSException() {
    }

    public SendSMSException(String s) {
        super(s);
    }

    public SendSMSException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SendSMSException(Throwable throwable) {
        super(throwable);
    }

    public SendSMSException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
