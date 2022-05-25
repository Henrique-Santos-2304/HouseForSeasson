package com.example.multitech.houseforseasson.database.repository.annoucements;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.multitech.houseforseasson.database.models.Announcement;
import com.example.multitech.houseforseasson.database.repository.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class FirebaseAnnoucementDAO {
    private List<Announcement> getAnnoucementByUser;

    public FirebaseAnnoucementDAO() {
        this.getAnnoucementByUser = new ArrayList<>();
    }

    public void saveAnnouncement(Announcement announcement){
        String idAnnouncement = announcement.getId();
        DatabaseReference dbRef =
                FirebaseHelper
                        .getDbReference()
                        .child("announcement")
                        .child(FirebaseHelper.getUid())
                        .child(idAnnouncement);

        dbRef.setValue(announcement);
    }

    public void findAnnouncementByUser(ViewCallback view, String userId){
        List<Announcement> mData = new ArrayList<>();
        DatabaseReference dbRef =
                FirebaseHelper
                        .getDbReference()
                        .child("announcement")
                        .child(userId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mData.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    // Data parsing is being done within the extending classes.
                    Announcement announcement = snap.getValue(Announcement.class);
                    mData.add(announcement);
                }
                view.showData(mData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };

        dbRef.addValueEventListener(eventListener);

    }

    public void deleteAnnouncement(String annoucementId){
        DatabaseReference dbRef =
                FirebaseHelper
                        .getDbReference()
                        .child("announcement")
                        .child(FirebaseHelper.getUid())
                        .child(annoucementId);

        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("images")
                .child("announcement")
                .child(FirebaseHelper.getUid())
                .child(annoucementId + ".jpeg");

        dbRef.removeValue();
        storageReference.delete();
    }

    public void saveImage(Announcement announcement, String urlImg){
        StorageReference storageReference = FirebaseHelper.getStorageReference()
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
