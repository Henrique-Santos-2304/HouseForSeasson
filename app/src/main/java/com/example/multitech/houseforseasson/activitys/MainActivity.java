package com.example.multitech.houseforseasson.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multitech.houseforseasson.R;
import com.example.multitech.houseforseasson.database.repository.authentication.UserAuthDao;
import com.example.multitech.houseforseasson.protocols.ViewMethods;

public class MainActivity extends AppCompatActivity implements ViewMethods {
    private TextView txtTitle;
    private UserAuthDao userAuthDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setComponents();
        this.listeningClicks();
    }

    @Override
    public void setComponents() {
        this.txtTitle = findViewById(R.id.toolbar_txt_title);
        this.txtTitle.setText("House for Seasson");
        this.userAuthDao= new UserAuthDao();
    }

    @Override
    public void listeningClicks() {
        findViewById(R.id.toolbar_btn_goback).setOnClickListener(v-> {
            this.userAuthDao.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        });
    }
}