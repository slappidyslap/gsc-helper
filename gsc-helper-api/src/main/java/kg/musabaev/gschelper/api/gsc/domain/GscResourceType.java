package kg.musabaev.gschelper.api.gsc.domain;

public enum GscResourceType {
    DOMAIN("Доменный ресурс"),
    WITH_PREFIX("Ресурс с префиксом в URL");

    public final String rus;

    GscResourceType(String rus) {
        this.rus = rus;
    }

    public String rus() {
        return rus;
    }
}
