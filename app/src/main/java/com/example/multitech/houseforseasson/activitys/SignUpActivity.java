package com.example.multitech.houseforseasson.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.multitech.houseforseasson.R;
import com.example.multitech.houseforseasson.database.models.User;
import com.example.multitech.houseforseasson.database.repository.authentication.CallbackCreateUser;
import com.example.multitech.houseforseasson.database.repository.authentication.UserAuthDao;
import com.example.multitech.houseforseasson.protocols.ViewMethods;
import com.example.multitech.houseforseasson.utils.CheckInputValues;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignUpActivity extends AppCompatActivity implements ViewMethods, CallbackCreateUser {
    private EditText name,email,telephone,password;
    private ProgressBar progressBar;
    private User newUser;
    private UserAuthDao userAuthDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.setComponents();
        this.listeningClicks();
    }


    @Override
    public void setComponents() {
        this.name = findViewById(R.id.signup_input_name);
        this.email = findViewById(R.id.signup_input_email);
        this.telephone = findViewById(R.id.signup_input_telephone);
        this.password = findViewById(R.id.signup_input_password);
        this.progressBar = findViewById(R.id.signup_progressbar);

        this.userAuthDao = new UserAuthDao();
    }

    @Override
    public void listeningClicks() {
        findViewById(R.id.signup_txt_gologin).setOnClickListener(v -> {
            finish();
        });
        findViewById(R.id.signup_btn_sendsignup).setOnClickListener(v -> {this.validatorData();});
    }

    private void validatorData(){
        String name = this.name.getText().toString();
        String email = this.email.getText().toString();
        String telephone = this.telephone.getText().toString();
        String password = this.password.getText().toString();

        boolean passwordNotEmpty = CheckInputValues.checkInputIsNotEmpty(password, this.password, "Informe sua senha!!");
        boolean telephoneNotEmpty = CheckInputValues.checkInputIsNotEmpty(password, this.telephone, "Informe seu telephone!!");
        boolean emailIsNotEmpty = CheckInputValues.checkInputIsNotEmpty(email, this.email, "Informe seu email!");
        boolean nameNotEmpty = CheckInputValues.checkInputIsNotEmpty(name, this.name, "Informe seu nome!");

        if (passwordNotEmpty && telephoneNotEmpty && emailIsNotEmpty && nameNotEmpty) {
            this.progressBar.setVisibility(View.VISIBLE);
            this.newUser = new User();
            this.newUser.setName(name);
            this.newUser.setEmail(email);
            this.newUser.setTelephone(telephone);
            this.newUser.setPassword(password);
            this.userAuthDao.createUser(this.newUser, this);
        }
    }

    @Override
    public void saveUserFaillure(Exception err) {
        this.progressBar.setVisibility(View.GONE);
        Log.e("AUTOSTOCK", err.getMessage());
        Toast.makeText(this, "Falha ao salvar usuario" + err.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveUserSucess(Task<AuthResult> task) {
        if (task.isSuccessful()) {
            Toast.makeText(this, "Usuario Cadastrado com sucesso", Toast.LENGTH_LONG).show();
            String id = task.getResult().getUser().getUid();
            this.newUser.setId(id);
            this.progressBar.setVisibility(View.GONE);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        } else if (task.isCanceled()) {
            this.progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Ação Cancelada", Toast.LENGTH_LONG).show();
        }
    }
}