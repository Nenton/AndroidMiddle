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
}
