package com.example.multitech.houseforseasson.database.repository.authentication;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface CallbackCreateUser {
    void saveUserFaillure(Exception err);
    void saveUserSucess(Task<AuthResult> task);
}
