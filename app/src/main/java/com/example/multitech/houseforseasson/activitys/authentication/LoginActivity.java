package com.example.multitech.houseforseasson.activitys.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.multitech.houseforseasson.R;
import com.example.multitech.houseforseasson.activitys.MainActivity;
import com.example.multitech.houseforseasson.database.repository.authentication.CallbackLoginAuth;
import com.example.multitech.houseforseasson.database.repository.authentication.UserAuthDao;
import com.example.multitech.houseforseasson.protocols.ViewMethods;
import com.example.multitech.houseforseasson.utils.CheckInputValues;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity implements ViewMethods, CallbackLoginAuth {
    private EditText email,password;
    private ProgressBar progressBar;
    private UserAuthDao userAuthDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.setComponents();
        this.listeningClicks();
    }

    @Override
    public void setComponents() {
        this.email = findViewById(R.id.login_input_email);
        this.password = findViewById(R.id.login_input_password);
        this.progressBar = findViewById(R.id.login_progressbar);

        this.userAuthDao = new UserAuthDao();
    }

    @Override
    public void listeningClicks(){
        findViewById(R.id.login_txt_signup).setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });
        findViewById(R.id.login_txt_recovery_password).setOnClickListener(v -> {
            startActivity(new Intent(this, RestoreAccountActivity.class));
        });
        findViewById(R.id.login_btn_send).setOnClickListener(v -> {validatorData();});
        findViewById(R.id.login_btn_goback).setVisibility(View.GONE);

    }

    private void validatorData(){
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        boolean passwordIsEmpty = CheckInputValues.checkInputIsNotEmpty(password, this.password, "Informe sua senha!!");
        boolean emailIsEmpty = CheckInputValues.checkInputIsNotEmpty(email, this.email, "Informe seu email!");

        if(passwordIsEmpty && emailIsEmpty ){
            this.progressBar.setVisibility(View.VISIBLE);
            this.userAuthDao.userLogin(email, password,this);
        }
    }

    @Override
    public void loginCompleteTask(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
            this.progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Usuario logado com sucesso, redirecionando", Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }else if(task.isCanceled()){
            this.progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Ação Cancelada" , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void loginFailTask(@NonNull Exception err) {
        this.progressBar.setVisibility(View.GONE);
        Log.e("AUTOSTOCK", err.getMessage() );
        Toast.makeText(this, "Falha ao realizar o login " + err.getMessage(), Toast.LENGTH_LONG).show();
    }
}