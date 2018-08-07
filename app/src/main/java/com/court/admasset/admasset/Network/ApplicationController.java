package com.court.admasset.admasset.Network;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
@SuppressLint("Registered")
public class ApplicationController extends Application {
    // 먼저 어플리케이션 인스턴스 객체를 하나 선언
    private static ApplicationController instance;
//
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    // 베이스 url 초기화
    private static String baseUrl = "http://192.168.2.171:3000/";
//    http://192.168.2.171:3000/

    // 네트워크 서비스 객체 선언
    private NetworkService networkService;

    // 인스턴스 객체 반환  왜? static 안드에서 static 으로 선언된 변수는 매번 객체를 새로 생성하지 않아도 다른 액티비티에서
    // 자유롭게 사용가능합니다.
    public static ApplicationController getInstance() {
        return instance;
    }

    // 네트워크서비스 객체 반환
    public NetworkService getNetworkService() {
        return networkService;
    }

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    //인스턴스 객체 초기화
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        buildService();
//        ApplicationController.
    }

    public void buildService() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();               // okHttp log 확인을 위한 코드
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client
                = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder                         // retrofit 객체 생성
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        networkService= retrofit.create(NetworkService.class);
//        return networkService;

    }
}
