package com.example.multitech.houseforseasson.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.multitech.houseforseasson.R;
import com.example.multitech.houseforseasson.activitys.authentication.LoginActivity;
import com.example.multitech.houseforseasson.adapters.AdapterAnnouncement;
import com.example.multitech.houseforseasson.adapters.Onclick;
import com.example.multitech.houseforseasson.database.models.Announcement;
import com.example.multitech.houseforseasson.database.repository.FirebaseHelper;
import com.example.multitech.houseforseasson.database.repository.annoucements.FirebaseAnnoucementDAO;
import com.example.multitech.houseforseasson.database.repository.annoucements.ViewCallback;
import com.example.multitech.houseforseasson.database.repository.authentication.UserAuthDao;
import com.example.multitech.houseforseasson.protocols.ViewMethods;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewMethods, ViewCallback, Onclick {
    private  FirebaseAnnoucementDAO fbDao;
    private AdapterAnnouncement adapterAnnouncement;
    private List<Announcement> announcementList;

    private TextView txtTitle;
    private ImageButton menuSelect;
    private UserAuthDao userAuthDao;

    private ProgressBar progressBar;
    private LinearLayoutCompat linearNotAnnouncement;
    private TextView txtNotAnnouncement;
    private SwipeableRecyclerView rvAnnouncement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setComponents();
        this.listeningClicks();
        this.configRvProduct();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.linearNotAnnouncement.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.VISIBLE);
        this.getProducts();
    }

    @Override
    public void setComponents() {
        this.fbDao = new FirebaseAnnoucementDAO();
        this.announcementList = new ArrayList<>();

        this.txtTitle = findViewById(R.id.toolbar_txt_title);
        this.txtTitle.setText("House for Seasson");

        this.menuSelect = findViewById(R.id.main_menu);
        this.userAuthDao= new UserAuthDao();
        this.progressBar = findViewById(R.id.progress_bar_not_announcement_main);
        this.rvAnnouncement = findViewById(R.id.rv_announcements_main);
        this.linearNotAnnouncement = findViewById(R.id.not_announcement_main);
        this.txtNotAnnouncement = findViewById(R.id.txt_not_announcement_main);
    }

    private void configRvProduct(){
        this.rvAnnouncement.setLayoutManager(new LinearLayoutManager(this)); // seta orientaçao da recycler view
        this.rvAnnouncement.setHasFixedSize(true); // performance de recycler view

        // Configurar o adapter para a recycler view;
        this.adapterAnnouncement = new AdapterAnnouncement(this.announcementList, this);
        this.rvAnnouncement.setAdapter(this.adapterAnnouncement);

        this.rvAnnouncement.setListener(new SwipeLeftRightCallback.Listener() {

            @Override
            public void onSwipedLeft(int position) {}

            @Override
            public void onSwipedRight(int position) {}
        });
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

    private void getProducts(){
        this.fbDao.findAnnouncement(this);
    }

    private void verifyListIsNotEmpty(){
        if(this.announcementList.size() <= 0){
            this.txtNotAnnouncement.setText("Nenhum anúncio encontrado \n Adicione um anuncio!");
            this.linearNotAnnouncement.setVisibility(View.VISIBLE);
            this.rvAnnouncement.setVisibility(View.GONE);
        }else{
            this.rvAnnouncement.setVisibility(View.VISIBLE);
            this.linearNotAnnouncement.setVisibility(View.GONE);
        }
        this.progressBar.setVisibility(View.GONE);

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showData(List<Announcement> announcementList) {
        this.announcementList.clear();
        this.announcementList.addAll(announcementList);

        for (Announcement announcement : announcementList) {
             Log.i("APP", "Adapter Title: "+ announcement.getTitle());
             Log.i("APP", "Adapter Description: "+ announcement.getDescription());
        }


        verifyListIsNotEmpty();
        Collections.reverse(this.announcementList);
        this.adapterAnnouncement.notifyDataSetChanged();
    }

    @Override
    public void OnClickeListener(Announcement announcement) {
        Intent intent = new Intent(this, DetailAnnounceActivity.class);
        intent.putExtra("announcement", announcement);

        startActivity(intent);
    }
}