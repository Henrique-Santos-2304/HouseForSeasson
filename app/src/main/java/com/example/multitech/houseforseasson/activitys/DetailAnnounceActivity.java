package com.example.multitech.houseforseasson.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multitech.houseforseasson.R;
import com.example.multitech.houseforseasson.database.models.Announcement;
import com.example.multitech.houseforseasson.protocols.ViewMethods;
import com.squareup.picasso.Picasso;

public class DetailAnnounceActivity extends AppCompatActivity implements ViewMethods {
    private Announcement announcement;

    private TextView title,description,toolbarTitle;
    private EditText bedroom,bethroom,garage;
    private ImageView img;
    private AppCompatButton btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_announce);

        this.setComponents();
        this.listeningClicks();


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            this.announcement = (Announcement) bundle.getSerializable("announcement");
            this.setData();
        }
    }

    private void validateData(String data, TextView txtView){
        if(data.isEmpty()) txtView.setText(" ");
        else {
            data = data.substring(0,1).toUpperCase().concat(data.substring(1));
            txtView.setText(data);
        };
    }

    private void validateData(String data, EditText editText){
        if(data.isEmpty()) editText.setText(" ");
        else {
            data = data.substring(0,1).toUpperCase().concat(data.substring(1));
            editText.setText(data);
        };
    }

    private void setData() {

        String txtTitle = this.announcement.getTitle();
        String txtDescription = this.announcement.getDescription();
        String txtBedroom = String.valueOf(this.announcement.getBedroom());
        String txtBethRoom = String.valueOf(this.announcement.getBethroom());
        String txtGarage = String.valueOf(this.announcement.getGarage());
        String urlImg = String.valueOf(this.announcement.getUrlImg());


        this.validateData(txtTitle, this.title);
        this.validateData(txtDescription, this.description);
        this.validateData(txtBedroom, this.bedroom);
        this.validateData(txtBethRoom, this.bethroom);
        this.validateData(txtGarage, this.garage);

        if(urlImg.isEmpty()){
            findViewById(R.id.detail_carView_img).setVisibility(View.GONE);
        }else{
            Picasso.get().load(urlImg).into(this.img);
        }
    }

    @Override
    public void setComponents() {
        this.title = findViewById(R.id.detail_title);
        this.description = findViewById(R.id.detail_description);
        this.bedroom = findViewById(R.id.detail_input_bedroom);
        this.bethroom = findViewById(R.id.detail_input_bethroom);
        this.garage = findViewById(R.id.detail_input_garage);
        this.img = findViewById(R.id.detail_img);


        this.toolbarTitle = findViewById(R.id.toolbar_txt_title);
        this.toolbarTitle.setText("Detalhes do Anúncio");
    }

    @Override
    public void listeningClicks() {
        findViewById(R.id.toolbar_btn_goback).setOnClickListener(v -> finish());
    }
}