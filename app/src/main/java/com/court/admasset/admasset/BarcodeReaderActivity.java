package com.court.admasset.admasset;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import java.util.Map;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.court.admasset.admasset.Model.ScanInfo;
import com.court.admasset.admasset.Model.ScanInfoResult;
import com.court.admasset.admasset.Network.ApplicationController;
import com.court.admasset.admasset.Network.NetworkService;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import com.google.zxing.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class BarcodeReaderActivity extends AppCompatActivity {

    private ZXingScannerView scannerView;
    NetworkService service;
    private ScanInfo scanInfo;
    private Map<String, String> map;

    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        sf = getSharedPreferences("asset",0);

        scanInfo = new ScanInfo();
        scanInfo.id = Integer.parseInt(sf.getString("id","1"));
        scanInfo.user_name = sf.getString("user_name","1");
        scanInfo.check_court = Integer.parseInt(sf.getString("check_court","1"));
        scanInfo.group_id = Integer.parseInt(sf.getString("group_id","1"));
        scanInfo.check_id = Integer.parseInt(sf.getString("check_id","1"));

        // Add HeaderMap
        map = new HashMap<>();
        map.put("Content-Type","application/json");
        map.put("Authorization","Bearer " + sf.getString("id_token","1"));

        //String refreshToken = intent.getStringExtra("refreshToken");

        Log.d("통신",scanInfo.id+","+scanInfo.user_name+","+scanInfo.check_court+","+scanInfo.group_id+","+scanInfo.check_id);

        service = ApplicationController.getInstance().getNetworkService();

        // 카메라 권한 체크
        checkCameraPermission();

    }
    // 카메라 권한 체크
    public void checkCameraPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        // 권한 없음
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("카메라 권한이 거부되어있어 사용을 원하면 설정에서 해당 권한을 직접 허용하십시오")
                    .setNegativeButton("Setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:"+getPackageName()));
                            startActivity(intent);
                        }
                    })
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(BarcodeReaderActivity.this,"You can not use the camera", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BarcodeReaderActivity.this, MenuActivity.class));
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        }else{ // 권한 있음
            //바코드 스캔
            scanBarcode();
        }
    }

    // ====================== 바코드 스캔 ====================== //
    public void scanBarcode(){
        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(new ZXingScannerResultHandler());
        scannerView.setAutoFocus(true);
        setContentView(scannerView);
        scannerView.startCamera();
    }

//    @Override
//    public void onPause(){
//        super.onPause();
//    }
    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler {
        @Override
        public void handleResult(final Result result) {
            try{
                // 숫자확인용
                Long.parseLong(result.getText());

                scanInfo.asset_no  = result.getText();

                Call<ScanInfoResult> scanInfoResultCall = service.getScanInfoResult(map, scanInfo);
                scanInfoResultCall.enqueue(new Callback<ScanInfoResult>() {
                    @Override
                    public void onResponse(Call<ScanInfoResult> call, Response<ScanInfoResult> response) {
                        if(response.isSuccessful()){
                            if(response.body()!= null){
                                Toast.makeText(getApplicationContext(),"succes: "+response.body().status,Toast.LENGTH_LONG).show();

                                finish();
                                Intent intent = new Intent(BarcodeReaderActivity.this,RecyclerViewActivity.class);
                                intent.putExtra("status", response.body().status);
                                startActivity(intent);
                            }
                        }else{
                            try {
                                JSONObject json = new JSONObject(response.errorBody().string());

                                Toast.makeText(getApplicationContext(),json.getString("status"),Toast.LENGTH_LONG).show();
                                finish();
//                                 Asset data duplicated or Asset data not found
                                Intent intent = new Intent(BarcodeReaderActivity.this,RecyclerViewActivity.class);
//                                intent.putExtra("status", response.body().status);
                                intent.putExtra("status", json.getString("status"));
                                startActivity(intent);

//                                onDestroy();

//                                setContentView(R.layout.activity_menu);
//                                scannerView.stopCamera();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ScanInfoResult> call, Throwable t) {
                        finish();
                        Toast.makeText(getApplicationContext(), "통신실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }catch(NumberFormatException e){
                Toast.makeText(BarcodeReaderActivity.this, "It isn't Number", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BarcodeReaderActivity.this,MenuActivity.class);
                startActivity(intent);
            }
        }
    }
}
