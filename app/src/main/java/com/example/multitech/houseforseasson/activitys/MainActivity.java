package com.example.multitech.houseforseasson.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multitech.houseforseasson.R;
import com.example.multitech.houseforseasson.activitys.authentication.LoginActivity;
import com.example.multitech.houseforseasson.database.models.Announcement;
import com.example.multitech.houseforseasson.database.repository.FirebaseHelper;
import com.example.multitech.houseforseasson.database.repository.authentication.UserAuthDao;
import com.example.multitech.houseforseasson.protocols.ViewMethods;

public class MainActivity extends AppCompatActivity implements ViewMethods {
    private TextView txtTitle;
    private ImageButton menuSelect;
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
        this.menuSelect = findViewById(R.id.main_menu);
        this.userAuthDao= new UserAuthDao();
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Autentique-se")
                .setMessage("Faça login para acessar seus dados")
                .setCancelable(false)
                .setNegativeButton("Não", (dialog, wich) -> dialog.dismiss())
                .setPositiveButton("Sim", (dialog, wich) -> {
                    finish();
                    startActivity(new Intent(this, LoginActivity.class));
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void listeningClicks() {
        findViewById(R.id.toolbar_btn_goback).setOnClickListener(v-> {
            this.userAuthDao.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        });
        this.menuSelect.setOnClickListener(v -> {
            this.openMenu(v);
        });
    }

    private void openMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this,v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main_activity, popupMenu.getMenu() );
        popupMenu.setOnMenuItemClickListener(menuItem -> this.listeningMenuItemClick(menuItem));
        popupMenu.show();
    }

    private boolean listeningMenuItemClick(MenuItem menuItem) {
        int idMenu = menuItem.getItemId();
        if(idMenu == R.id.menu_filter){
            startActivity(new Intent(this, FilterAnnoucementActivity.class));
        }else if(idMenu == R.id.menu_my_announcements){
            if(FirebaseHelper.userIsAuth()){
                startActivity(new Intent(this, MyAnnouncementsActivity.class));
            }
            else this.showDialog();

        }
        else if(idMenu ==  R.id.menu_my_account){
            if(FirebaseHelper.userIsAuth()){
                startActivity(new Intent(this, MyAccountActivity.class));
            }else this.showDialog();
        }
        else{
            Toast.makeText(this, "Não foi possivel encontrar essa opção", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}