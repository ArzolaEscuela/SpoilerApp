package com.back4app.quickstartexampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.back4app.userregistrationexample.R;
import com.parse.ParseInstallation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }

}
