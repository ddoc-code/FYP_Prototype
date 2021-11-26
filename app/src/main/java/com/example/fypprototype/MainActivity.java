package com.example.fypprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //button loads View Events Activity
    public void enterApp(View view) {
        Intent intent = new Intent(this, ViewEventsActivity.class);
        startActivity(intent);
    }
}