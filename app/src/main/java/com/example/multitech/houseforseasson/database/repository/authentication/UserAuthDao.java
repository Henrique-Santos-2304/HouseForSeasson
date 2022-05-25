package com.example.multitech.houseforseasson.database.repository.authentication;

import com.example.multitech.houseforseasson.database.models.User;
import com.example.multitech.houseforseasson.database.repository.FirebaseHelper;

public class UserAuthDao {
    public void userLogin(String email, String password, CallbackLoginAuth callbackLoginAuth){
        FirebaseHelper
                .getAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> { callbackLoginAuth.loginCompleteTask(task);})
                .addOnFailureListener(err -> { callbackLoginAuth.loginFailTask(err);});
    }

    public void createUser(User user, CallbackCreateUser callbackCreateUser){
        String email = user.getEmail();
        String password = user.getPassword();

        FirebaseHelper
                .getAuth()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    callbackCreateUser.saveUserSucess(task);
                })
                .addOnFailureListener(err -> {
                    callbackCreateUser.saveUserFaillure(err);
                });
    }

    public void signOut(){
        FirebaseHelper.getAuth().signOut();
    }

    public void restoreAccount(String email, CallbackRestoreAccount callbackRestoreAccount){
        FirebaseHelper
                .getAuth()
                .sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> callbackRestoreAccount.restoreAccountSucess(task))
                .addOnFailureListener(err -> callbackRestoreAccount.restoreAccountFailure(err));
    }
}
