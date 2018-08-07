package com.court.admasset.admasset;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ======= Camera Permission Check ======= //
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        // permission denied
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1000);
        }else{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            startActivity(new Intent(this, LoginActivity.class));
            //startActivity(new Intent(this, WaitingAssetActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }else {
                Toast.makeText(SplashActivity.this, "권한 요청을 거부했습니다.", Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage("권한이 거부됨. 사용을 원하면 설정에서 해당 권한을 직접 허용하십시오")
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
                                Toast.makeText(SplashActivity.this,"You can not use the camera", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        }
    }
}
