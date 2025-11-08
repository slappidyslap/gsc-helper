package kg.musabaev.gschelper.swinggui.util;

public class ExceptionMessages {

    public static String setTextUnsupportedOper(Class<?> clazz, String fieldName) {
        return "Текст нельзя менять через " + clazz.getName() + "#setText(String). " +
            "Используйте #model()." + fieldName + "(String)";
    }

    private ExceptionMessages() {
    }
}
