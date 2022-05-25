package com.example.multitech.houseforseasson.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multitech.houseforseasson.R;
import com.example.multitech.houseforseasson.database.repository.authentication.CallbackRestoreAccount;
import com.example.multitech.houseforseasson.database.repository.authentication.UserAuthDao;
import com.example.multitech.houseforseasson.protocols.ViewMethods;
import com.example.multitech.houseforseasson.utils.CheckInputValues;
import com.google.android.gms.tasks.Task;

public class RestoreAccountActivity extends AppCompatActivity implements ViewMethods, CallbackRestoreAccount {
    private TextView txt_title;
    private ProgressBar progressBar;
    private EditText email;
    private UserAuthDao userAuthDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_account);

        this.setComponents();
        this.listeningClicks();
    }

    @Override
    public void setComponents() {
        this.txt_title = findViewById(R.id.toolbar_txt_title);
        this.txt_title.setText("Recuperar Conta");

        this.progressBar = findViewById(R.id.restoreaccount_progressbar);
        this.email = findViewById(R.id.restoreaccount_input_email);
        this.userAuthDao = new UserAuthDao();
    }

    @Override
    public void listeningClicks() {
        findViewById(R.id.toolbar_btn_goback).setOnClickListener(v -> {
            finish();
        });
        findViewById(R.id.restoreaccount_btn_sendrestore).setOnClickListener(v -> {
            this.validatorData();
        });
    }

    private void validatorData(){
        String email = this.email.getText().toString();
        boolean emailNotEmpty = CheckInputValues.checkInputIsNotEmpty(email, this.email, "Digite seu email");
        if(emailNotEmpty){
            this.progressBar.setVisibility(View.VISIBLE);
            this.userAuthDao.restoreAccount(email, this);
        }
    }

    @Override
    public void restoreAccountSucess(Task<Void> task) {
        if(task.isSuccessful()){
            Log.i("HOUSE", "restoreAccountSucess: Sucess");
            Toast.makeText(this, "Mensagem enviada com sucesso \n Siga as intrução na sua caixa de entrada", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
        }
        else if (task.isCanceled()){
            Toast.makeText(this, "Ação Cancelada", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Erro ao realizar essa ação", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void restoreAccountFailure(Exception err) {
        this.progressBar.setVisibility(View.GONE);
        Log.e("AUTOSTOCK", err.getMessage() );
        Toast.makeText(this, "Error ao realizar essa ação \n " + err.getMessage(), Toast.LENGTH_LONG).show();
    }
}