package com.nenton.androidmiddle.utils;


import com.nenton.androidmiddle.BuildConfig;

public interface ConstantsManager {
    String USER_AUTH_TOKEN = "USER_AUTH_TOKEN";

    int REQUEST_PERMISSION_CAMERA = 3000;
    int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 3001;

    int REQUEST_PROFILE_PHOTO_CAMERA = 1000;
    int REQUEST_PROFILE_PHOTO_PICTURE = 1001;
    String FILE_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider";
    String LAST_MODIFIED_HEADER = "Last-Modified";
    String IF_MODIFIED_SINCE_HEADER = "If-Modified-Since";
//    String BASE_URL = BuildConfig.HOST;

    String REALM_USER = "nenton@nenton.nenton";
    String REALM_PASSWORD = "nenton";
    String REALM_AUTH_URL = "http://192.168.100.4:9080/auth";
    String REALM_DB_URL = "realm://192.168.100.4:9080/~/default";
}
