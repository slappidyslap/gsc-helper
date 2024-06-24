package kg.seo.musabaev;

public enum GscResourceType {
    DOMAIN("Доменный ресурс"),
    WITH_PREFIX("Ресурс с префиксом в URL");

    public final String rus;

    GscResourceType(String rus) {
        this.rus = rus;
    }
}
