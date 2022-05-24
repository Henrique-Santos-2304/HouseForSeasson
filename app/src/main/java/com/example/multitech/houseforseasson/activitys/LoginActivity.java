package com.example.multitech.houseforseasson.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.multitech.houseforseasson.R;
import com.example.multitech.houseforseasson.protocols.ViewMethods;

public class LoginActivity extends AppCompatActivity implements ViewMethods {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        this.setComponents();
        this.listeningClicks();
    }

    @Override
    public void setComponents(){

    }

    @Override
    public void listeningClicks(){
        findViewById(R.id.login_txt_signup).setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });
    }
}