package kg.seo.musabaev.searchconsole;

public enum GscResourceType {
    DOMAIN("Доменный ресурс"),
    WITH_PREFIX("Ресурс с префиксом в URL");

    public final String rus;

    public String rus() {
        return rus;
    }

    GscResourceType(String rus) {
        this.rus = rus;
    }
}
