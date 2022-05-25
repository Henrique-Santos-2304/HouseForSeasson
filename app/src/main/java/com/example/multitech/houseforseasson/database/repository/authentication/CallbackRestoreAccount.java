package com.example.multitech.houseforseasson.database.repository.authentication;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface CallbackRestoreAccount {
    void restoreAccountSucess(Task<Void> task);
    void restoreAccountFailure(Exception err);
}
