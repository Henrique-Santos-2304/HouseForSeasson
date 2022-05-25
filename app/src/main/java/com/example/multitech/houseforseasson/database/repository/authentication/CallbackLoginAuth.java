package com.example.multitech.houseforseasson.database.repository.authentication;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface CallbackLoginAuth {
    void loginCompleteTask(Task<AuthResult> task);
    void loginFailTask(Exception err);
}
