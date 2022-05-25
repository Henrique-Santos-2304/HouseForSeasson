package com.example.multitech.houseforseasson.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.multitech.houseforseasson.R;
import com.example.multitech.houseforseasson.activitys.authentication.LoginActivity;
import com.example.multitech.houseforseasson.database.repository.FirebaseHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler(Looper.getMainLooper()).postDelayed(this::checkAuthUser, 5000);
    }

    private void checkAuthUser(){
        if(FirebaseHelper.userIsAuth()){
            startActivity(new Intent(this, MainActivity.class));
        }else{
            Toast.makeText(this, "Por favor faça login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}