package com.example.flappybirds;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ShareActivity extends androidx.appcompat.app.AppCompatActivity {
    private android.widget.EditText recipientInput;
    private int score;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        recipientInput = findViewById(R.id.recipient);
        score = getIntent().getIntExtra("score", 0);
        findViewById(R.id.share).setOnClickListener(v -> {
            if ((ContextCompat.checkSelfPermission(ShareActivity.this, Manifest.permission.SEND_SMS))
                    == PackageManager.PERMISSION_GRANTED) {
sendSMS();} else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.SEND_SMS)) {
                android.widget.Toast.makeText(ShareActivity.this, "Please, give us permission to send SMS.", Toast.LENGTH_LONG).show();
                requestPermissions(
                        new String[] { Manifest.permission.SEND_SMS },
                        1);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.SEND_SMS)) {
                android.widget.Toast.makeText(ShareActivity.this, "Please, give us permission to send SMS.", Toast.LENGTH_LONG).show();
                requestPermissions(
                        new String[] { Manifest.permission.SEND_SMS },
                        1);

            } else {
                android.widget.Toast.makeText(ShareActivity.this, "Please, give us permission to send SMS.", Toast.LENGTH_LONG).show();
                requestPermissions(
                            new String[] { Manifest.permission.SEND_SMS },
                            1);
                }
            }
        );
    }

    private void sendSMS() {
        try {
            String recipient = recipientInput.getText().toString();
            android.telephony.SmsManager.getDefault().sendTextMessage(recipient, null, "Hi! My score was " + score + "! Can you bit me?", null, null);
            android.widget.Toast.makeText(this, R.string.sms_successfully_sent, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            android.widget.Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}