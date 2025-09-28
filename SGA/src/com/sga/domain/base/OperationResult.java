package com.sga.domain.base;

public class OperationResult<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> OperationResult<T> ok(T data) {
        OperationResult<T> result = new OperationResult<>();
        result.success = true;
        result.data = data;
        return result;
    }

    public static <T> OperationResult<T> fail(String message) {
        OperationResult<T> result = new OperationResult<>();
        result.success = false;
        result.message = message;
        return result;
    }

    // Getters and setters
}
