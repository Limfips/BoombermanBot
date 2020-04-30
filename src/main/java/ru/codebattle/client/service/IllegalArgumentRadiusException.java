package ru.codebattle.client.service;

/**
 * Ошибка используется, когда радиус меньше 1 или больше радиуса,
 * указанного в переменной класса {@link SonarService}
 * @author Dudka Leonid RPIS-81
 */
public class IllegalArgumentRadiusException extends IllegalArgumentException {
    public IllegalArgumentRadiusException() {
    }

    public IllegalArgumentRadiusException(String s) {
        super(s);
    }

    public IllegalArgumentRadiusException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalArgumentRadiusException(Throwable cause) {
        super(cause);
    }
}
