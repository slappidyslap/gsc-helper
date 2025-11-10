package kg.musabaev.gschelper.swinggui.util;

public class Result<T, E> {

    private final T value;
    private final E exception;
    private final boolean isSuccess;

    private Result(T value, E exception) {
        this.value = value;
        this.exception = exception;
        this.isSuccess = value != null;
    }

    public static <T, E> Result<T, E> success(T value) {
        return new Result<>(value, null);
    }

    public static <T, E> Result<T, E> failure(E exception) {
        return new Result<>(null, exception);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFailure() {
        return !isSuccess;
    }

    public T value() {
        if (isFailure())
            throw new IllegalStateException("Can not get value from a failed Result");
        return value;
    }

    public E exception() {
        if (isSuccess())
            throw new IllegalStateException("Cannot get exception from a successful Result.");
        return exception;
    }
}
