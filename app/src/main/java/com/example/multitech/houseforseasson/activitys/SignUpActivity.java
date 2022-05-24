package com.example.multitech.houseforseasson.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.multitech.houseforseasson.R;
import com.example.multitech.houseforseasson.protocols.ViewMethods;

public class SignUpActivity extends AppCompatActivity implements ViewMethods {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.setComponents();
        this.listeningClicks();
    }


    @Override
    public void setComponents() {

    }

    @Override
    public void listeningClicks() {
        findViewById(R.id.signup_txt_gologin).setOnClickListener(v -> {
            finish();
        });
    }
}