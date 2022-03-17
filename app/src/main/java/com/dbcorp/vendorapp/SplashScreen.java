package com.dbcorp.vendorapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.dbcorp.vendorapp.database.SqliteDatabase;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.ui.Home.HomeActivity;
import com.dbcorp.vendorapp.ui.Login;
import com.dbcorp.vendorapp.ui.servicevendor.VendorActivity;

public class SplashScreen extends AppCompatActivity {

    LoginDetails login;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalce_screen);
        mContext = this;
        login = new SqliteDatabase(this).getLogin();

        Intent intent = null;
        if (login == null) {
            intent = new Intent(mContext, Login.class);
        } else {
            switch (login.getIs_approve()){
                case "1":
                    intent = new Intent(mContext, VendorActivity.class);
                    break;
                case "2":
                    intent = new Intent(mContext, HomeActivity.class);
                    break;
//                case "3":
//                    break;
            }
        }
        startActivity(intent);
        finish();
    }
}
