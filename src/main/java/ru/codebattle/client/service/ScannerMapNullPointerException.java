package ru.codebattle.client.service;

/**
 * Ошибка используется, когда в классе {@link SonarService} пытаются получить данные, при пустой карте
 * @author Dudka Leonid RPIS-81
 */
public class ScannerMapNullPointerException extends RuntimeException {
    public ScannerMapNullPointerException() {
    }

    public ScannerMapNullPointerException(String message) {
        super(message);
    }

    public ScannerMapNullPointerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScannerMapNullPointerException(Throwable cause) {
        super(cause);
    }

    public ScannerMapNullPointerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
