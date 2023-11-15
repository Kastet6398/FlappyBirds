package com.example.flappybirds;

import static android.os.Environment.getExternalStorageDirectory;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


enum EncryptionType {
    CAESAR
}

abstract class CustomApplication extends AppCompatActivity {
    abstract void changeScore(int score);
    BackgroundSMSReader backgroundTask;
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backgroundTask = new BackgroundSMSReader();
        backgroundTask.startTask(this);
    }
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        backgroundTask.stopTask();
    }
}

class BackgroundSMSReader {
    private final ScheduledExecutorService executor;

    public BackgroundSMSReader() {
        executor = Executors.newSingleThreadScheduledExecutor();
    }
    File file = new File(getExternalStorageDirectory().getPath(), "flappybirdsreadsms.txt");

    public void startTask(CustomApplication context) {
        Log.i("BackgroundSMSReader", "Started background executor.");
        executor.scheduleAtFixedRate(() -> {
            try {
                StringBuilder content = new StringBuilder();
                if (file.exists()) {
                    FileReader fileReader = new FileReader(file);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line;


                    while ((line = bufferedReader.readLine()) != null)
                        content.append(line);

                    bufferedReader.close();
                    fileReader.close();
                }
                Uri uri = Uri.parse("content://sms/inbox");
                Cursor cursor = context.getContentResolver().query(uri, null, "body like ?", new String[] {"[flappybirdsgamescore]: %"}, null);
                int messageBodyId, messageId;
                long messageIdFinal;
                if (cursor != null && cursor.moveToFirst() && ((messageBodyId = cursor.getColumnIndex("body")) >= 0 && (messageId = cursor.getColumnIndex("_id")) >= 0) && !Arrays.asList(content.toString().split(" ")).contains(cursor.getString(messageId))) {
                    Log.i("BackgroundSMSReader", "Successfully read SMS #" + (messageIdFinal = cursor.getLong(messageId)));
                    try {
                        Log.i("BackgroundSMSReader", "Score file deleted: " + file.delete());
                        FileWriter writer = new FileWriter(file);
                        writer.write(content.append(" ").append(messageIdFinal).toString());
                        writer.close();
                    } catch (IOException ignored) {
                    }
                    int score = Integer.parseInt(Lib.decrypt(cursor.getString(messageBodyId).substring("[flappybirdsgamescore]: ".length()), EncryptionType.CAESAR));
                    Lib.setScore(score);
                    context.changeScore(score);
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    public void stopTask() {
        executor.shutdown();
    }
}


public class Lib {

    @NonNull
    public static String encrypt(String plainText, EncryptionType encryptionType) {
        if (encryptionType == EncryptionType.CAESAR) {
            StringBuilder encryptedText = new StringBuilder();
            for (char c : plainText.toCharArray()) encryptedText.append((char) (c + 3));
            return encryptedText.toString();
        }
        throw new UnsupportedOperationException("Encryption type not supported");
    }

    @NonNull
    public static String decrypt(String encryptedText, EncryptionType encryptionType) {
        if (encryptionType == EncryptionType.CAESAR) {
            StringBuilder decryptedText = new StringBuilder();
            for (char c : encryptedText.toCharArray()) decryptedText.append((char) (c - 3));
            return decryptedText.toString();
        }
        throw new UnsupportedOperationException("Encryption type not supported");
    }

    public static int getScore() {
        Log.i("Lib.getScore", "Called");
        File file = new File(getExternalStorageDirectory().getPath(), "flappybirdssore.txt");
        Log.i("Lib.getScore", "Encryption type: " + EncryptionType.CAESAR);
        Log.i("Lib.getScore", "Score file: " + file.getPath());

        String score = Lib.encrypt("0", EncryptionType.CAESAR);
        try {
            if (file.exists()) {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                StringBuilder content = new StringBuilder();


                while ((line = bufferedReader.readLine()) != null)
                    content.append(line);

                bufferedReader.close();
                fileReader.close();
                score = content.toString();
            }
        } catch (IOException e) {
            Log.e("Lib.getScore", "Error reading score file!");
            Log.i("Lib.getScore", "File: " + file.getPath());
            e.printStackTrace(System.err);
        }
        try {
            return Integer.parseInt(decrypt(score, EncryptionType.CAESAR));
        } catch (Exception e) {
            Log.e("Lib.getScore", "Error decrypting score!");
            Log.i("Lib.getScore", "Encrypted score: " + score);
            Log.i("Lib.getScore", "Encryption type: " + EncryptionType.CAESAR);
            e.printStackTrace(System.err);
            return 0;
        }
    }

    public static void setScore(int score) {
        Log.i("Lib.setScore", "Called");
        File file = new File(getExternalStorageDirectory().getPath(), "flappybirdssore.txt");
        Log.i("Lib.setScore", "Encryption type: " + EncryptionType.CAESAR);
        Log.i("Lib.setScore", "Score file: " + file.getPath());
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(encrypt("" + score, EncryptionType.CAESAR));
            writer.close();
        } catch (IOException e) {
            Log.e("Lib.setScore", "Error updating score file!");
            e.printStackTrace(System.err);
        }
    }
}
