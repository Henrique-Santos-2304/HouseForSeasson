package com.example.multitech.houseforseasson.activitys;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multitech.houseforseasson.R;
import com.example.multitech.houseforseasson.database.models.Announcement;
import com.example.multitech.houseforseasson.database.repository.FirebaseHelper;
import com.example.multitech.houseforseasson.database.repository.annoucements.FirebaseAnnoucementDAO;
import com.example.multitech.houseforseasson.protocols.ViewMethods;
import com.example.multitech.houseforseasson.utils.CheckInputValues;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FormAnnouncementActivity extends AppCompatActivity implements ViewMethods {
    private static final int REQUEST_GALERY = 102;

    private Announcement announcement;
    private FirebaseAnnoucementDAO fbAnnoucementDao;

    private EditText title,description,bedrooms, bethroom, garage;
    private ImageButton addAnnouncement;
    private CheckBox disponibility;
    private ImageView imgProfile;
    private String urlImg;
    private Bitmap img;

    private String txtTitle, txtDescription, txtBethroom, txtBedroom, txtGarage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_announcement);

        this.setComponents();
        this.listeningClicks();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            this.announcement = (Announcement) bundle.getSerializable("announcement");
            this.editAnnouncement();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_GALERY && data != null) {
            Uri urlImageLocale = data.getData();
            this.urlImg = urlImageLocale.toString();
            if (Build.VERSION.SDK_INT < 28) {
                try {
                    this.img = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), urlImageLocale);
                    this.saveAnnouncement();
                } catch (Exception err) {
                    Log.e("AUTOSTOCK", err.getMessage());
                }
            }
            else {
                ImageDecoder.Source imgDecode = ImageDecoder.createSource(getBaseContext().getContentResolver(), urlImageLocale);
                this.saveAnnouncement();
                try {
                    this.img = ImageDecoder.decodeBitmap(imgDecode);
                } catch (Exception err) {
                    Log.e("AUTOSTOCK", err.getMessage());
                }
            }

            this.imgProfile.setImageBitmap(this.img);

        }
    }

    private boolean checkInputs(){
        this.txtTitle = this.title.getText().toString();
        this.txtDescription = this.description.getText().toString();
        this.txtBethroom = this.bethroom.getText().toString();
        this.txtBedroom = this.bedrooms.getText().toString();
        this.txtGarage = this.garage.getText().toString();

        boolean titleNotEmpty = CheckInputValues.checkInputIsNotEmpty(this.txtTitle, this.title, "Informe o titulo do anuncio");
        boolean descriptionNotEmpty = CheckInputValues.checkInputIsNotEmpty(this.txtDescription, this.description, "Informe a descrição do anúncio");
        boolean bethroomNotEmpty = CheckInputValues.checkInputIsNotEmpty(this.txtBethroom , this.bethroom, "Informe a quantidade de quartos");
        boolean bedroomNotEmpty = CheckInputValues.checkInputIsNotEmpty(this.txtBedroom, this.bedrooms, "Informe a quantidade de banheiros");
        boolean garageNotEmpty = CheckInputValues.checkInputIsNotEmpty(this.txtGarage , this.garage, "Informe a quantidade de garagens");

        if(titleNotEmpty && descriptionNotEmpty && bethroomNotEmpty && bedroomNotEmpty && garageNotEmpty){
            return Boolean.TRUE;
        }else return Boolean.FALSE;
    }

    private void saveAnnouncement(){
        boolean inputsNotEmpty = this.checkInputs();

        if(inputsNotEmpty){
            if(this.announcement == null) this.announcement =  new Announcement();
            this.announcement.setUserId(FirebaseHelper.getUid());
            this.announcement.setTitle(this.txtTitle);
            this.announcement.setDescription(this.txtDescription);
            this.announcement.setBethroom(Integer.parseInt(this.txtBethroom));
            this.announcement.setBedroom(Integer.parseInt(this.txtBedroom));
            this.announcement.setGarage(Integer.parseInt(this.txtGarage));
            this.announcement.setDisponibility(this.disponibility.isChecked());

            if(this.urlImg != null ){
                this.fbAnnoucementDao.saveImage(this.announcement, this.urlImg);
                Toast.makeText(this, "Dados inseridos com sucesso", Toast.LENGTH_LONG).show();
                finish();
            }else{
                if(this.announcement.getUrlImg() != null){
                    this.fbAnnoucementDao.saveAnnouncement(this.announcement);
                    Toast.makeText(this, "Dados inseridos com sucesso", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(this, "Selecione uma imagem", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void editAnnouncement(){
        this.txtTitle = this.announcement.getTitle();
        this.txtDescription = this.announcement.getDescription();
        this.txtBethroom = String.valueOf(this.announcement.getBethroom());
        this.txtBedroom = String.valueOf(this.announcement.getBedroom());
        this.txtGarage = String.valueOf(this.announcement.getGarage());

        if(
            this.txtTitle.isEmpty() || this.txtDescription.isEmpty()
            || this.txtBethroom.isEmpty() || this.txtBedroom.isEmpty()
            || this.txtGarage.isEmpty() )
        {
            Log.i("AUTOSTOCK", "Campos Inválidos para editar anuncio");
        }
        else{
            this.title.setText(this.txtTitle);
            this.description.setText(this.txtDescription);
            this.bethroom.setText(this.txtBethroom);
            this.bedrooms.setText(this.txtBedroom);
            this.garage.setText(this.txtGarage);
            this.disponibility.setChecked(this.announcement.isDisponibility());

            Picasso.get().load(this.announcement.getUrlImg()).into(this.imgProfile);
        }


    }

    @Override
    public void setComponents() {
        this.title = findViewById(R.id.announcement_input_title);
        this.description = findViewById(R.id.announcement_input_description);
        this.bedrooms = findViewById(R.id.announcement_input_quarts);
        this.bethroom = findViewById(R.id.announcement_input_toallete);
        this.garage = findViewById(R.id.announcement_input_garage);
        this.disponibility = findViewById(R.id.announcement_check_disponibility);
        this.imgProfile = findViewById(R.id.announcement_img);

        this.fbAnnoucementDao = new FirebaseAnnoucementDAO();
    }

    @Override
    public void listeningClicks() {
        findViewById(R.id.toolbar_btn_goback).setOnClickListener(v -> {
            finish();
        });
        this.imgProfile.setOnClickListener(v -> {
            this.checkPermission();
        });
        findViewById(R.id.announcement_check).setOnClickListener(v -> {
            this.saveAnnouncement();
        });
    }

    private void checkPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openGallery();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        };

        this.showDialogPermission(
                permissionListener,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
        );
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, this.REQUEST_GALERY);
    }

    private void showDialogPermission(PermissionListener listener, String[] permissions) {
        TedPermission.create()
                .setPermissionListener(listener)
                .setDeniedTitle("Permissão")
                .setDeniedMessage("Por favor permita acesso a galeria para poder ver as fotos")
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(permissions)
                .check();
    }
}