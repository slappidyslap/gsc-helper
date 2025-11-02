package kg.seo.musabaev;

public class CredentialsFileNotFoundException extends RuntimeException {
    public CredentialsFileNotFoundException() {
        super("Файл credentials.json не найден");
    }
}
