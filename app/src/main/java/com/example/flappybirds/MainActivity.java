package com.example.flappybirds;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;

public class MainActivity extends CustomApplication {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int score = Lib.getScore();
        this.<android.widget.TextView>findViewById(R.id.score).setText(String.format(getResources().getString(R.string.score), score));
        findViewById(R.id.save_progress_on_another_device).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SaveOnAnotherDeviceActivity.class).putExtra("score", score)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
            Toast.makeText(this, getResources().getString(R.string.request_storage_access), Toast.LENGTH_SHORT).show();
        }
        if (androidx.core.content.ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, android.Manifest.permission.SEND_SMS)) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.request_send_sms_permission), Toast.LENGTH_LONG).show();
                requestPermissions(
                        new String[]{android.Manifest.permission.SEND_SMS},
                        1);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, android.Manifest.permission.SEND_SMS)) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.request_send_sms_permission), Toast.LENGTH_LONG).show();
                requestPermissions(
                        new String[]{android.Manifest.permission.SEND_SMS},
                        1);

            } else {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.request_send_sms_permission), Toast.LENGTH_LONG).show();
                requestPermissions(
                        new String[]{Manifest.permission.SEND_SMS},
                        1);
            }
        }

        if (androidx.core.content.ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, android.Manifest.permission.READ_SMS)) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.request_read_sms_permission), Toast.LENGTH_LONG).show();
                requestPermissions(
                        new String[]{android.Manifest.permission.READ_SMS},
                        1);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, android.Manifest.permission.READ_SMS)) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.request_read_sms_permission), Toast.LENGTH_LONG).show();
                requestPermissions(
                        new String[]{android.Manifest.permission.READ_SMS},
                        1);

            } else {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.request_read_sms_permission), Toast.LENGTH_LONG).show();
                requestPermissions(
                        new String[]{Manifest.permission.READ_SMS},
                        1);
            }
        }
    }

    public void startGame(View v) {
        startActivity(new Intent(this, StartGameActivity.class));
    }

    @Override
    void changeScore(int score) {
        this.<android.widget.TextView>findViewById(R.id.score).setText(String.format(getResources().getString(R.string.score), score));
    }
}