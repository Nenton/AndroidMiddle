package com.nenton.androidmiddle.data.network.error;

/**
 * Created by serge on 03.01.2017.
 */

public class ApiError extends Throwable {
    private int statusCode;
    private String message;

    @Override
    public String getMessage() {return message;}
}
