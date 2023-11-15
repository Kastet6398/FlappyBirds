package com.example.flappybirds;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SaveOnAnotherDeviceActivity extends CustomApplication {
    private int score;
    private android.widget.EditText recipientInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_on_another_device);
        score = getIntent().getIntExtra("score", 0);
        recipientInput = findViewById(R.id.recipient);
        findViewById(R.id.back).setOnClickListener(v -> startActivity(new Intent(SaveOnAnotherDeviceActivity.this, MainActivity.class)));
        findViewById(R.id.send_to_another_device).setOnClickListener(v -> {
            try {
                android.telephony.SmsManager.getDefault().sendTextMessage(recipientInput.getText().toString(), null, "[flappybirdsgamescore]: " + Lib.encrypt("" + score, EncryptionType.CAESAR), null, null);
                Toast.makeText(this, R.string.sms_successfully_sent, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    void changeScore(int score) {
        this.score = score;
    }
}