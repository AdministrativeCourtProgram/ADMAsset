package com.court.admasset.admasset;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.court.admasset.admasset.Network.NetworkService;
import com.court.admasset.admasset.Network.ApplicationController;
import com.court.admasset.admasset.Model.LoginInfo;
import com.court.admasset.admasset.Model.LoginInfoResult;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText userName, userPwd;
    private TextView loginbtn;

    NetworkService service;

    boolean doubleBackToExitPressedOnce = false;
    private SharedPreferences sf = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView loginbtn = (TextView) findViewById(R.id.loginbtn);

        initView();
        initNetwork();
        initClickEvent();
    }

    private void initView() {
        userName = (EditText) findViewById(R.id.userName);
        userPwd = (EditText) findViewById(R.id.userPwd);
        loginbtn = (TextView) findViewById(R.id.loginbtn);
    }


    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }

    // ClickEvnet initial setting
    private void initClickEvent() {
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check Id blank
                if (userName.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter username", Toast.LENGTH_SHORT).show();
                    return;
                }
                // check Password blank
                if (userPwd.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Server communication
                LoginInfo loginInfo = new LoginInfo();
                loginInfo.username = userName.getText().toString();
                loginInfo.password = userPwd.getText().toString();

                Call<LoginInfoResult> loginInfoResultCall = service.getLoginInfoResult(loginInfo);
                loginInfoResultCall.enqueue(new Callback<LoginInfoResult>() {  // Asynchronous method
                    @Override
                    public void onResponse(Call<LoginInfoResult> call, Response<LoginInfoResult> response) { // Client object
                        if (response.isSuccessful() && response.body() != null) {
                            response.body().result = true;

                            if (response.body().result) {
                                if(getSharedPreferences("asset",MODE_PRIVATE).getString("loginDate","1") != "1"){
                                    Log.d("AAAA","1,"+getSharedPreferences("asset",MODE_PRIVATE).getString("loginDate","1"));
                                    sf = getSharedPreferences("asset",MODE_PRIVATE);
                                    Log.d("AAAA","2,"+sf.getString("loginDate","2"));
                                    SharedPreferences.Editor editor = sf.edit();

                                    // check date
                                    long now = System.currentTimeMillis();
                                    Date date = new Date(now);
                                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd");
                                    Log.d("AAAA","3,"+sdfNow.format(date));

                                    if(!sdfNow.format(date).equals(sf.getString("loginDate","1"))
                                            || !sf.getString("user_name","1").equals(response.body().user_name)){

                                        Log.d("AAAA","4"+"not equals"+response.body().user_name+sf.getString("user_name","1"));
                                        storageSharedPre(response.body().id + "", response.body().user_name, response.body().check_court
                                                , response.body().group_id, response.body().check_id, response.body().id_token, response.body().refreshToken);

                                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Log.d("AAAA","4"+"equals"+response.body().user_name+sf.getString("user_name","1"));
                                        Log.d("AAAA","5"+"equals"+response.body().check_id+" , "+sf.getString("check_id","1"));
                                        editor.putString("id", response.body().id+"");
                                        editor.putString("check_court", response.body().check_court);
                                        editor.putString("group_id", response.body().group_id);
                                        editor.putString("id_token", response.body().id_token);
                                        editor.putString("refreshToken", response.body().refreshToken);
                                        editor.commit();

                                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                else{
                                    // Does not have Login date
                                    storageSharedPre(response.body().id + "", response.body().user_name, response.body().check_court
                                            , response.body().group_id, response.body().check_id, response.body().id_token, response.body().refreshToken);

                                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter the correct information", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginInfoResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Network communication failure", Toast.LENGTH_SHORT).show();

                    }
                });
            }

        });
    }

    // storage sharedPreferences
    public void storageSharedPre(String id, String user_name, String check_court
            , String group_id, String check_id, String id_token, String refreshToken) {
        SharedPreferences sf = getSharedPreferences("asset", 0);
        SharedPreferences.Editor editor = sf.edit();

        editor.putString("id", id);
        editor.putString("user_name", user_name);
        editor.putString("check_court", check_court);
        editor.putString("group_id", group_id);
        editor.putString("check_id", check_id);
        editor.putString("id_token", id_token);
        editor.putString("refreshToken", refreshToken);
        // currentTime
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd");
        editor.putString("loginDate", sdfNow.format(date));

        editor.commit();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressing Back once again will end the application", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}