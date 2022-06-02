package com.example.multitech.houseforseasson.activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multitech.houseforseasson.R;
import com.example.multitech.houseforseasson.adapters.AdapterAnnouncement;
import com.example.multitech.houseforseasson.adapters.Onclick;
import com.example.multitech.houseforseasson.database.models.Announcement;
import com.example.multitech.houseforseasson.database.repository.FirebaseHelper;
import com.example.multitech.houseforseasson.database.repository.annoucements.FirebaseAnnoucementDAO;
import com.example.multitech.houseforseasson.database.repository.annoucements.ViewCallback;
import com.example.multitech.houseforseasson.protocols.ViewMethods;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyAnnouncementsActivity extends AppCompatActivity implements ViewMethods, Onclick, ViewCallback {
    private SwipeableRecyclerView rvAnnouncement;
    private LinearLayoutCompat linearNotAnnouncement;
    private TextView txtNotAnnouncement, txtTitle;
    private ProgressBar progressBar;

    private AdapterAnnouncement adapterAnnouncement;
    private List<Announcement> announcementList;
    private FirebaseAnnoucementDAO fbDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_announcements);

        this.setComponents();
        this.listeningClicks();
        this.configRvProduct();
    }
    @Override
    protected void onStart() {
        super.onStart();
        this.progressBar.setVisibility(View.VISIBLE);
        this.linearNotAnnouncement.setVisibility(View.VISIBLE);
        this.getProducts();
    }
    public void setComponents() {
        this.rvAnnouncement = findViewById(R.id.rv_announcements);
        this.linearNotAnnouncement = findViewById(R.id.not_announcement);
        this.txtNotAnnouncement = findViewById(R.id.txt_not_announcement);
        this.progressBar = findViewById(R.id.progress_bar_not_announcement);
        this.txtTitle = findViewById(R.id.toolbar_txt_title);

        this.txtTitle.setText("Meus Anúncios");


//
        this.fbDao = new FirebaseAnnoucementDAO();
        this.announcementList = new ArrayList<>();
    }

    @Override
    public void listeningClicks() {
        findViewById(R.id.myannouncement_add).setOnClickListener(v -> {
            startActivity(new Intent(this, FormAnnouncementActivity.class));
        });
        findViewById(R.id.toolbar_btn_goback).setOnClickListener(v -> { finish();});
    }

    private void configRvProduct(){
        this.rvAnnouncement.setLayoutManager(new LinearLayoutManager(this)); // seta orientaçao da recycler view
        this.rvAnnouncement.setHasFixedSize(true); // performance de recycler view

        // Configurar o adapter para a recycler view;
        this.adapterAnnouncement = new AdapterAnnouncement(this.announcementList, this);
        this.rvAnnouncement.setAdapter(this.adapterAnnouncement);

        this.rvAnnouncement.setListener(new SwipeLeftRightCallback.Listener() {

            @Override
            public void onSwipedLeft(int position) {
                Announcement announcement = announcementList.get(position);
                Intent intent = new Intent(getBaseContext(), FormAnnouncementActivity.class);
                intent.putExtra("announcement", announcement);
                startActivity(intent);
                adapterAnnouncement.notifyItemChanged(position);

            }

            @Override
            public void onSwipedRight(int position) {
                Announcement announcement = announcementList.get(position);
                showDialogDelete(announcement,position);
            }
        });
    }

    private void showDialogDelete(Announcement announcement, int position){
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(this)
                .setTitle("Ação destrutiva \n Você perderá esses dados")
                .setMessage("Tem certeza que deseja deletar")
                .setNegativeButton("Não", (dis, i) -> {
                    Toast.makeText(this, "Ação cancelada.", Toast.LENGTH_SHORT).show();
                    adapterAnnouncement.notifyItemChanged(position);
                    dis.dismiss();
                })
                .setPositiveButton("Sim", (dism, i) -> deleteAnnouncement(announcement, position));

        builderDialog.create().show();
    }

    private void deleteAnnouncement(Announcement announcement, int position) {
        announcementList.remove(position);
        fbDao.deleteAnnouncement(announcement.getId());
        adapterAnnouncement.notifyItemRemoved(position);

        verifyListIsNotEmpty();
    }

    private void verifyListIsNotEmpty(){
        if(this.announcementList.size() <= 0){
            this.txtNotAnnouncement.setText("Nenhum Anúncio Cadastrado");
            this.linearNotAnnouncement.setVisibility(View.VISIBLE);
        }else{
            this.linearNotAnnouncement.setVisibility(View.GONE);
        }
        this.progressBar.setVisibility(View.GONE);

    }

    @Override
    public void OnClickeListener(Announcement announcement) {
        Intent intent = new Intent(this, DetailAnnounceActivity.class);
        intent.putExtra("announcement", announcement);

        startActivity(intent);
    }

    private void getProducts(){
        String userId = FirebaseHelper.getUid();
        this.fbDao.findAnnouncementByUser(this,userId);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showData(List<Announcement> list) {
        this.announcementList.clear();
        this.announcementList.addAll(list);


        verifyListIsNotEmpty();
        Collections.reverse(this.announcementList);
        this.adapterAnnouncement.notifyDataSetChanged();

    }
}