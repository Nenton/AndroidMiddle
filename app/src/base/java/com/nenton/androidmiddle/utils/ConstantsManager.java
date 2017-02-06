package com.nenton.androidmiddle.utils;

/**
 * Created by serge on 21.01.2017.
 */

public interface ConstantsManager {

    String USER_AUTH_TOKEN = "USER_AUTH_TOKEN";

    int REQUEST_PERMISSION_CAMERA = 3000;
    int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 3001;

    int REQUEST_PROFILE_PHOTO_CAMERA = 1000;
    int REQUEST_PROFILE_PHOTO_PICTURE = 1001;
    String FILE_PROVIDER_AUTHORITY = "com.nenton.androidmiddle.fileprovider";
    String LAST_MODIFIED_HEADER = "Last-Modified";
    String IF_MODIFIED_SINCE_HEADER = "If-Modified-Since";
    String REG_EXP_EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    String REG_EXP_PASSWORD = "(||)\\w+";
}
