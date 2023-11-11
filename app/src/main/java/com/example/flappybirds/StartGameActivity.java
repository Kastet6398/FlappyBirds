package com.example.flappybirds;

public class StartGameActivity extends androidx.appcompat.app.AppCompatActivity {
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameView gameView = new GameView(this, Lib.getScore(getExternalFilesDir("MyFiles")));
        setContentView(gameView);
    }
}
