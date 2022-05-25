package com.example.multitech.houseforseasson.database.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper {
    private static FirebaseAuth auth;
    private static DatabaseReference dbRef;
    private static StorageReference storageRef;

    public static  FirebaseAuth getAuth(){
        if(auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static DatabaseReference getDbReference(){
        if(dbRef == null){
            dbRef = FirebaseDatabase.getInstance().getReference();
        }
        return dbRef;
    }

    public static StorageReference getStorageReference(){
        if(storageRef == null){
            storageRef = FirebaseStorage.getInstance().getReference();
        }
        return storageRef;
    }

    public static boolean userIsAuth(){
        return getAuth().getCurrentUser() != null;
    }

    public static String getUid(){
        return getAuth().getUid();
    }
}
