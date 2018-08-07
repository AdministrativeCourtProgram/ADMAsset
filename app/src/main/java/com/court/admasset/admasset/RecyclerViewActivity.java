package com.court.admasset.admasset;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.court.admasset.admasset.Model.CheckedListInfo;
import com.court.admasset.admasset.Model.CheckedListResult;
import com.court.admasset.admasset.Network.ApplicationController;
import com.court.admasset.admasset.Network.NetworkService;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;

public class RecyclerViewActivity extends AppCompatActivity {
    private RecyclerView reView;
    private ArrayList<CheckedListResult.CheckedData> itemdata;
    private RecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;
    private String id;
    private String user_name;
    private String check_court;
    private String group_id;
    private String check_id;
    private String barcode_result;
    private String firstcall;
    private NetworkService service;
    private DuplicateDialogActivity duplicateDialog;
    private Map<String, String> map;

    Intent intent;

    private SharedPreferences sf;
    private CheckedListInfo checkedListInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        sf = getSharedPreferences("asset",0);

        id = sf.getString("id","1");
        user_name = sf.getString("user_name","1");
        check_court = sf.getString("check_court","1");
        group_id = sf.getString("group_id","1");
        check_id = sf.getString("check_id","1");

        //Barcode 결과값 받기
        intent = getIntent();

        // ====== scan Button ====== //
        Button scanBtn = (Button)findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecyclerViewActivity.this, BarcodeReaderActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        firstcall=intent.getStringExtra("Firstcall");
        barcode_result = intent.getStringExtra("status");



//        Log.v("TAG","firstcall" +firstcall);
        Log.v("TAG","barcode_result" +barcode_result);

        if(barcode_result.equals("Asset data duplicated."))
        {
            callDialog();
        }




        reView = (RecyclerView) findViewById(R.id.recyclerlistview);
        reView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);             //리니어레이아웃의 형태이면 방향은 수직
        reView.setLayoutManager(layoutManager);//리사이클러뷰에 레이아웃매니저를 달아준다



        service = ApplicationController.getInstance().getNetworkService();
        checkedListInfo = new CheckedListInfo();
        checkedListInfo.id = Integer.parseInt(id);
        checkedListInfo.user_name = user_name;
        checkedListInfo.check_court = Integer.parseInt(check_court);
        checkedListInfo.group_id = Integer.parseInt(group_id);
        checkedListInfo.check_id = Integer.parseInt(check_id);

        //토큰값 헤더에 추가
        map = new HashMap<>();
        map.put("Content-Type","application/json");
        map.put("Authorization","Bearer " + sf.getString("id_token","1"));



        Log.d("AAAA", checkedListInfo.id+","+checkedListInfo.user_name
                +","+checkedListInfo.check_court+","+checkedListInfo.group_id
                +","+checkedListInfo.check_id);

        Call<CheckedListResult> checkedListResultCall = service.getCheckedListResult(map,checkedListInfo);
        checkedListResultCall.enqueue(new Callback<CheckedListResult>() {
            @Override
            public void onResponse(Call<CheckedListResult> call, Response<CheckedListResult> response) {
//                Toast.makeText(RecyclerViewActivity.this, "통신성공123", Toast.LENGTH_SHORT).show();
                if(response.isSuccessful()) {
//                    Toast.makeText(RecyclerViewActivity.this, "통신성공", Toast.LENGTH_SHORT).show();
                    if(response.body().status != null) {
//                        Log.d("111111111111111",response.body().status);
                        itemdata = response.body().result;
                        adapter = new RecyclerViewAdapter(getApplicationContext(), itemdata, clickEvent);
                        reView.setAdapter(adapter);
                    }
                    else {
                        ApplicationController.showToast(getApplication(), "데이터 가져오기 실패");
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckedListResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "통신 실패", Toast.LENGTH_SHORT).show();
            }

            View.OnClickListener clickEvent = new View.OnClickListener() {
                public void onClick(View v) {

                }

            };



        });
    }
    public void callDialog()
    {
        duplicateDialog = new DuplicateDialogActivity(this);
        duplicateDialog.show();
        duplicateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent intent = new Intent(RecyclerViewActivity.this, SendMsgActivity.class);
                startActivity(intent);
            }
        });
    }
}