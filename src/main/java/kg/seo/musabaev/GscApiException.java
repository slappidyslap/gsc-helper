package kg.seo.musabaev;

public class GscApiException extends RuntimeException {
    public GscApiException(String s, Exception e) {
        super(s, e);
    }

}
