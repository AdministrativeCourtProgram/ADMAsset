package com.court.admasset.admasset;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.court.admasset.admasset.Model.ScanInfo;
import com.court.admasset.admasset.Model.ScanInfoResult;
import com.court.admasset.admasset.Network.ApplicationController;
import com.court.admasset.admasset.Network.NetworkService;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WritecodeDialogActivity extends AppCompatActivity {

    NetworkService service;
    private ScanInfo scanInfo;
    private Map<String, String> map;
    private Context context;
    private SharedPreferences sf;
    Intent intent;
    private int flag = 0;
    private EditText asset_no;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writecodedialog);
        Button sendbtn = (Button) findViewById(R.id.sendbtn);

        sf = getSharedPreferences("asset", 0);
        asset_no = (EditText) findViewById(R.id.asset_noWriteView);


        scanInfo = new ScanInfo();
        scanInfo.id = Integer.parseInt(sf.getString("id", "1"));
        scanInfo.user_name = sf.getString("user_name", "1");
        scanInfo.check_court = Integer.parseInt(sf.getString("check_court", "1"));
        scanInfo.group_id = Integer.parseInt(sf.getString("group_id", "1"));
        scanInfo.check_id = Integer.parseInt(sf.getString("check_id", "1"));
        scanInfo.asset_no = asset_no.getText().toString();
        // Add HeaderMap
        map = new HashMap<>();
        map.put("Content-Type", "application/json");
        map.put("Authorization", "Bearer " + sf.getString("id_token", "1"));

        service = ApplicationController.getInstance().getNetworkService();

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scanInfo.asset_no = asset_no.getText().toString();

                if (asset_no.length() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.enterscancode, Toast.LENGTH_SHORT).show();
                    return;
                }


                Call<ScanInfoResult> scanInfoResultCall = service.getScanInfoResult(map, scanInfo);
                scanInfoResultCall.enqueue(new Callback<ScanInfoResult>() {
                    @Override
                    public void onResponse(Call<ScanInfoResult> call, Response<ScanInfoResult> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                //Toast.makeText(getApplicationContext(),"succes: "+response.body().status,Toast.LENGTH_LONG).show();

                                int i = response.body().result.get(0).samecourt;

                                if (i == 0) {
                                    Intent intent = new Intent(WritecodeDialogActivity.this, SendMsgActivity.class);
                                    intent.putExtra("status", response.body().status);
                                    intent.putExtra("result", response.body().result);
                                    intent.putExtra("flag", flag);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(WritecodeDialogActivity.this, ListViewActivity.class);
                                    intent.putExtra("status", response.body().status);
                                    Log.v("TAG", "iiiiii" + i);
                                    Log.v("TAG", "status" + response.body().status);
                                    startActivity(intent);
                                }
                            }
                        } else {
                            try {
                                JSONObject json = new JSONObject(response.errorBody().string());
//                        Toast.makeText(getApplicationContext(), json.getString("status"), Toast.LENGTH_LONG).show();
                                // Asset data duplicated or Asset data not found
                                Intent intent = new Intent(WritecodeDialogActivity.this, ListViewActivity.class);
                                intent.putExtra("status", json.getString("status"));
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (
                                    NumberFormatException e)

                            {
                                Toast.makeText(context, "It isn't Number", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, MenuActivity.class);
                                context.startActivity(intent);

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ScanInfoResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Network communication failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });


    }
}


