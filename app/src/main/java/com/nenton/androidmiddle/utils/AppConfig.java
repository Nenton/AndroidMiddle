package com.nenton.androidmiddle.utils;

public interface AppConfig {
    String BASE_URL = "https://skba1.mgbeta.ru/api/v1/";
    long MAX_CONNECTION_TIMEOUT = 5 * 1000;
    long MAX_READ_TIMEOUT = 5 * 1000;
    long MAX_WRITE_TIMEOUT = 5 * 1000;
}
