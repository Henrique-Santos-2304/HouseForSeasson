package com.example.multitech.houseforseasson.utils;

import android.widget.EditText;

public class CheckInputValues {
    public static boolean checkInputIsNotEmpty(String txt, EditText input, String msgError){
        if(txt.trim().isEmpty()){
            input.requestFocus();
            input.setError(msgError);
            return Boolean.FALSE;
        }
        else{
            return Boolean.TRUE;
        }
    }
}
