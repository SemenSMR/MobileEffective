package com.example.mobileeffective.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ErrorResponse {
    private String error;
    private String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public ErrorResponse(int value, String message) {
    }
}
