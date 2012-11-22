package com.matburt.mobileorg.Synchronizers;

import java.lang.Exception;

public class SynchronizeException extends Exception {

    private String message;

    public SynchronizeException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}