package kg.seo.musabaev;

/**
 * Тип исключений у которых текст исключительно отображается в Error Dialog от swing
 * @see <a href="https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html">Oracle docs</a>
 */
public class BaseErrorDialogException extends RuntimeException {
    public BaseErrorDialogException(String message) {
        super(message);
    }
}
