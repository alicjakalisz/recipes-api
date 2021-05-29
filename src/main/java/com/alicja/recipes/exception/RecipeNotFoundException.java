package com.alicja.recipes.exception;

public class RecipeNotFoundException extends RuntimeException{
    public RecipeNotFoundException() {
    }

    public RecipeNotFoundException(String message) {
        super(message);
    }

    public RecipeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public RecipeNotFoundException(Throwable cause) {
        super(cause);
    }

    public RecipeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
