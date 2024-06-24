package kg.seo.musabaev;

public class GscSitesNotFoundException extends RuntimeException {
    public GscSitesNotFoundException() {
        super("Это исключение происходит, когда сайты из GSC не были извлечены. " +
                "Скорее всего вы авторизовались через не тот аккаунт. " +
                "Если вы уверены, что сайты есть, то создайте тикет в Jira");
    }
}
