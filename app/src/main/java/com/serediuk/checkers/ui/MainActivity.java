package com.serediuk.checkers.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.serediuk.checkers.R;
import com.serediuk.checkers.util.data.DataInsertion;
import com.serediuk.checkers.util.loader.LevelLoader;
import com.serediuk.checkers.util.loader.StatisticLoader;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LevelLoader.preferences = getSharedPreferences("levels", Context.MODE_PRIVATE);
        StatisticLoader.preferences = getSharedPreferences("statistic", Context.MODE_PRIVATE);
        boolean needToReloadPreferences = true;
        if (StatisticLoader.getLevelCount() == 0 || needToReloadPreferences) {
            StatisticLoader.setLevel(8);
            StatisticLoader.setLevelCount(9);
            StatisticLoader.setGamesCount(0);
            StatisticLoader.setWins(0);
            StatisticLoader.setLoses(0);
            StatisticLoader.setLowestMoves(0);
            int levelCount = StatisticLoader.getLevelCount();
            for (int i = 1; i <= levelCount; i++) {
                LevelLoader.setLevelData(Objects.requireNonNull(DataInsertion.getLevelData(i)));
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        Button playLevelButton = findViewById(R.id.btn_play_level);
        playLevelButton.setText(R.string.button_play_level_name);
        String playLevelButtonText = playLevelButton.getText().toString();
        playLevelButton.setText(playLevelButtonText + " " + StatisticLoader.getLevel());
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

    public void openStatisticActivity(View view) {
        Intent intent = new Intent(this, StatisticActivity.class);
        startActivity(intent);
    }
}