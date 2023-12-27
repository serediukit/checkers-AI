package com.serediuk.checkers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void openLevelActivity(View view) {
        Intent intent = new Intent(this, LevelActivity.class);
        startActivity(intent);
    }

    public void openPlayWithBotActivity(View view) {
        Intent intent = new Intent(this, PlayWithBotActivity.class);
        startActivity(intent);
    }

    public void openLocalGameActivity(View view) {
        Intent intent = new Intent(this, LocalGameActivity.class);
        startActivity(intent);
    }
}