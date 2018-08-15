package com.court.admasset.admasset;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.court.admasset.admasset.Model.CheckedListInfo;
import com.court.admasset.admasset.Model.CheckedListResult;
import com.court.admasset.admasset.Network.ApplicationController;
import com.court.admasset.admasset.Network.NetworkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewActivity extends AppCompatActivity {

    private NetworkService service;
    private ArrayList<CheckedListResult.CheckedData> itemdata;
    private ListView listView;
    private ListViewAdapter adapter;
    private Map<String, String> map;
    private SharedPreferences sf;
    private String id;
    private String user_name;
    private String check_court;
    private String group_id;
    private String check_id;
    private String barcode_result;
    private DuplicateDialogActivity duplicateDialog;
    private NotFoundDialogActivity notfoundDialg;

    Intent intent;
    private CheckedListInfo checkedListInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        service = ApplicationController.getInstance().getNetworkService();


        sf = getSharedPreferences("asset",0);

        id = sf.getString("id","1");
        user_name = sf.getString("user_name","1");
        check_court = sf.getString("check_court","1");
        group_id = sf.getString("group_id","1");
        check_id = sf.getString("check_id","1");

        // Get Barcode results
        intent = getIntent();

        // ====== scan Button ====== //
        Button scanBtn = (Button)findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListViewActivity.this, BarcodeReaderActivity.class);
                startActivity(intent);
                finish();
            }
        });

        barcode_result = intent.getStringExtra("status");
        if(barcode_result.equals("Asset data duplicated.")) {
            //Log.v("TAG","이 데이터는 겹칩니다.");
            callDuplicateDialog();
        }
        if(barcode_result.equals("Asset data not found.")){
            //Log.v("TAG","이 데이터는 없어요");
            callNotFoundDialog();
        }
        listView = (ListView) findViewById(R.id.listviewarea);



        map = new HashMap<>();
        map.put("Content-Type","application/json");
        map.put("Authorization","Bearer " + sf.getString("id_token","1"));

        checkedListInfo = new CheckedListInfo();
        checkedListInfo.id = Integer.parseInt(id);
        checkedListInfo.user_name = user_name;
        checkedListInfo.check_court = Integer.parseInt(check_court);
        checkedListInfo.group_id = Integer.parseInt(group_id);
        checkedListInfo.check_id = Integer.parseInt(check_id);

        Call<CheckedListResult> checkedListResultCall = service.getCheckedListResult(map,checkedListInfo);
        checkedListResultCall.enqueue(new Callback<CheckedListResult>() {
            @Override
            public void onResponse(Call<CheckedListResult> call, Response<CheckedListResult> response) {
//                Toast.makeText(RecyclerViewActivity.this, "통신성공123", Toast.LENGTH_SHORT).show();
                if(response.isSuccessful()) {
                    if(response.body().status != null) {
                        //Toast.makeText(RecyclerViewActivity.this, "통신성공", Toast.LENGTH_SHORT).show();
                        Log.v("TAG","Adapter");
                        itemdata = response.body().result;
                        adapter = new ListViewAdapter(itemdata);
                        listView.setAdapter(adapter);
                    }
                    else {
                        ApplicationController.showToast(getApplication(), "Data import failed");
                    }
                }
            }
            @Override
            public void onFailure(Call<CheckedListResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network communication failure", Toast.LENGTH_SHORT).show();
            }


        });

    }

    public void callDuplicateDialog()
    {
        duplicateDialog = new DuplicateDialogActivity(this);
        duplicateDialog.show();
        duplicateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
    }
    public void callNotFoundDialog() {
        notfoundDialg = new NotFoundDialogActivity(this);
        notfoundDialg.show();
        notfoundDialg.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
    }
}