package tech.meliora.natujenge.threads.domain;

public class Message {

    private String from;

    private String to;

    private String message;

    private String refId;

    public Message(String from, String to, String message, String refId) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.refId = refId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                ", refId='" + refId + '\'' +
                '}';
    }
}
