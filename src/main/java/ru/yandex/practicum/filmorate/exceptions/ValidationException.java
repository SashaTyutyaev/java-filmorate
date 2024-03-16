package ru.yandex.practicum.filmorate.exceptions;

public class ValidationException extends Exception {

    public ValidationException() {
    }

    public ValidationException(String message) {
        super.getMessage();
    }
}
