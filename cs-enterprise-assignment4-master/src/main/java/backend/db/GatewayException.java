package backend.db;

public class GatewayException extends RuntimeException {

    public GatewayException(Exception e) {
        super(e);
    }

    public GatewayException(String msg) {
        super(msg);
    }
}