package com.example.multitech.houseforseasson.database.repository.annoucements;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.multitech.houseforseasson.database.models.Announcement;
import com.example.multitech.houseforseasson.database.repository.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirebaseAnnoucementDAO {
    private DatabaseReference dbRef;
    private StorageReference storageRef;

    public FirebaseAnnoucementDAO() {
        this.dbRef =  FirebaseHelper.getDbReference();
        this.storageRef = FirebaseHelper.getStorageReference();
    }

    public void saveAnnouncement(Announcement announcement){
        String idAnnouncement = announcement.getId();
        DatabaseReference dbQuery = this.dbRef
                .child("announcement")
                .child(idAnnouncement);

        dbQuery.setValue(announcement);
    }

    public void findAnnouncementByUser(ViewCallback view, String userId){
        List<Announcement> mData = new ArrayList<>();

        Query dbQuery = this.dbRef
                        .child("announcement").orderByChild("userId").equalTo(userId);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mData.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    // Data parsing is being done within the extending classes.
                    Announcement announcement = snap.getValue(Announcement.class);
                    Log.i("APP", "UserId: " + userId);
                    Log.i("APP", "GetUserId: " + announcement.getUserId());
                    mData.add(announcement);
                }
                view.showData(mData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        dbQuery.addValueEventListener(eventListener);
    }

    public void findAnnouncement(ViewCallback view){

        List<Announcement> mData = new ArrayList<>();
        DatabaseReference dbQuery = this.dbRef.child("announcement");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mData.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    // Data parsing is being done within the extending classes.
                    Announcement announcement = snap.getValue(Announcement.class);
                    Log.i("APP", "onDataChange: " + snap.getValue());

                    mData.add(announcement);
                }
                view.showData(mData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };

        dbQuery.addValueEventListener(eventListener);

    }

    public void deleteAnnouncement(String annoucementId){
        DatabaseReference dbQuery = this.dbRef
                        .child("announcement")
                        .child(annoucementId);

        StorageReference storageReference = this.storageRef
                .child("images")
                .child("announcement")
                .child(FirebaseHelper.getUid())
                .child(annoucementId + ".jpeg");

        dbQuery.removeValue();
        storageReference.delete();
    }

    public void saveImage(Announcement announcement, String urlImg){
        StorageReference storageReference = this.storageRef
                .child("images")
                .child("announcement")
                .child(FirebaseHelper.getUid())
                .child(announcement.getId() + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(urlImg));
        uploadTask.addOnSuccessListener(task ->
                storageReference.getDownloadUrl().addOnCompleteListener(value -> {
                    announcement.setUrlImg(value.getResult().toString());
                    this.saveAnnouncement(announcement);
                })).addOnFailureListener(err -> Log.e("AUTOSTOCK", err.getMessage() ));
    }
}
