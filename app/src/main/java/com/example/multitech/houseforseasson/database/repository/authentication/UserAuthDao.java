package com.example.multitech.houseforseasson.database.repository.authentication;

import com.example.multitech.houseforseasson.database.models.User;
import com.example.multitech.houseforseasson.database.repository.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

public class UserAuthDao {
    public void userLogin(String email, String password, CallbackLoginAuth callbackLoginAuth){
        FirebaseHelper
                .getAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> { callbackLoginAuth.loginCompleteTask(task);})
                .addOnFailureListener(err -> { callbackLoginAuth.loginFailTask(err);});
    }


    private boolean createUserDb(User user){
        try{
            DatabaseReference dbRef = FirebaseHelper.getDbReference().child("users").child(user.getId());
            dbRef.setValue(user);
            return Boolean.TRUE;
        }
        catch(Exception err){
            return Boolean.FALSE;
        }
    }

    public void createUser(User user, CallbackCreateUser callbackCreateUser){
        String email = user.getEmail();
        String password = user.getPassword();

        FirebaseHelper
                .getAuth()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    String userId = task.getResult().getUser().getUid();
                    user.setId(userId);

                    boolean created = this.createUserDb(user);

                    if(created) callbackCreateUser.saveUserSucess(task);
                    else callbackCreateUser.saveUserFaillure(new Exception("Does not possible saved user in database"));

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
