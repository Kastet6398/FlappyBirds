package com.example.flappybirds;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.<android.widget.TextView>findViewById(R.id.score).setText("Your score: " + Lib.getScore(getExternalFilesDir("MyFiles")));
    }

    public void startGame(android.view.View v) {
        startActivity(new Intent(this, StartGameActivity.class));
    }
}