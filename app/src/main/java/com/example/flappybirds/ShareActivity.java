package com.example.flappybirds;

import android.widget.Toast;

public class ShareActivity extends CustomApplication {
    private android.widget.EditText recipientInput;
    private int score;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        recipientInput = findViewById(R.id.recipient);
        score = getIntent().getIntExtra("score", 0);
        findViewById(R.id.back).setOnClickListener(v -> startActivity(new android.content.Intent(ShareActivity.this, MainActivity.class)));
        findViewById(R.id.share).setOnClickListener(v -> {
            try {
                android.telephony.SmsManager.getDefault().sendTextMessage(recipientInput.getText().toString(), null, String.format(getResources().getString(R.string.sms_message), score), null, null);
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