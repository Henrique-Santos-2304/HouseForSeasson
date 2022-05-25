package com.example.multitech.houseforseasson.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.multitech.houseforseasson.R;
import com.example.multitech.houseforseasson.protocols.ViewMethods;

public class FormAnnouncementActivity extends AppCompatActivity implements ViewMethods {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_announcement);

        this.setComponents();
        this.listeningClicks();
    }

    @Override
    public void setComponents() {

    }

    @Override
    public void listeningClicks() {
        findViewById(R.id.toolbar_btn_goback).setOnClickListener(v -> {
            finish();
        });
    }
}