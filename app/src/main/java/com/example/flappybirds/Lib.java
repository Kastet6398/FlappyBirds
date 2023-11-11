package com.example.flappybirds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Lib {
    public static void setScore(int score, File parent) {
        File file = new File(parent, "flappybirdssore");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("" + score);
            writer.close();
        } catch (IOException ignored) {}
    }

    public static int getScore(File parent) {
        File file = new File(parent, "flappybirdssore");
        String score = "0";
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
        } catch (IOException ignored) {}
        try {
            return Integer.getInteger(score);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }
}
