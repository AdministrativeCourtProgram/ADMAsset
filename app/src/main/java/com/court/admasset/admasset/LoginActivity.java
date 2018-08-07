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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText userName, userPwd;
    private TextView loginbtn;

    NetworkService service;

    boolean doubleBackToExitPressedOnce = false;


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

        Log.v("TAG", "네트워크서비스 instance값 확인" + ApplicationController.getInstance());
        Log.v("TAG", "네트워크서비스 networkService값 확인" + ApplicationController.getInstance().getNetworkService());

    }

    // 클릭이벤트 초기화
    private void initClickEvent() {
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 아이디 공백 체크
                if (userName.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter username", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 패스워드 공백 체크
                if (userPwd.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 서버통신부
                LoginInfo loginInfo = new LoginInfo();
                loginInfo.username = userName.getText().toString();
                loginInfo.password = userPwd.getText().toString();

                Call<LoginInfoResult> loginInfoResultCall = service.getLoginInfoResult(loginInfo);
                loginInfoResultCall.enqueue(new Callback<LoginInfoResult>() {  //비동기방식
                    @Override
                    public void onResponse(Call<LoginInfoResult> call, Response<LoginInfoResult> response) { // 클라이언트 객체
                        if (response.isSuccessful() && response.body() != null) {
//                            Toast.makeText(getApplicationContext(), "통신 성공", Toast.LENGTH_SHORT).show();
                            response.body().result = true;

                            if (response.body().result) {
                                // storage sharedPreferences
                                storageSharedPre(response.body().id + "", response.body().user_name, response.body().check_court
                                        , response.body().group_id, response.body().check_id, response.body().id_token, response.body().refreshToken);

                                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter the correct information", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginInfoResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "통신실패", Toast.LENGTH_SHORT).show();

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

        Intent intent = getIntent();
        editor.putString("id", id);
        editor.putString("user_name", user_name);
        editor.putString("check_court", check_court);
        editor.putString("group_id", group_id);
        editor.putString("check_id", check_id);
        editor.putString("id_token", id_token);
        editor.putString("refreshToken", refreshToken);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "뒤로가기를 한 번더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}