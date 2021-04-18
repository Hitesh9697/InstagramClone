package com.example.instagramclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getResources().getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getResources().getString(R.string.back4app_client_key))
                .server(getResources().getString(R.string.back4app_server_url))
                .build()
        );
    }
}
