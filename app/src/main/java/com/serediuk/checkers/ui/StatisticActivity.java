package com.serediuk.checkers.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.serediuk.checkers.R;
import com.serediuk.checkers.util.loader.StatisticLoader;

public class StatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        updateStatisticFromLoader();
    }

    public void clearStatistic(View view) {
        StatisticLoader.setLevel(1);
        StatisticLoader.setGamesCount(0);
        StatisticLoader.setWins(0);
        StatisticLoader.setLoses(0);
        StatisticLoader.setLowestMoves(0);
        updateStatisticFromLoader();
        Toast toast = new Toast(this);
        toast.setText(R.string.text_statistic_cleared);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void updateStatisticFromLoader() {
        Log.d("TAG", "1");
        TextView level = findViewById(R.id.statistic_levels_count);
        level.setText(String.valueOf(StatisticLoader.getLevel() - 1));
        Log.d("TAG", "2");
        TextView total = findViewById(R.id.statistic_total);
        total.setText(String.valueOf(StatisticLoader.getGamesCount()));
        Log.d("TAG", "3");
        TextView wins = findViewById(R.id.statistic_wins);
        wins.setText(String.valueOf(StatisticLoader.getWins()));
        Log.d("TAG", "4");
        TextView loses = findViewById((R.id.statistic_loses));
        loses.setText(String.valueOf(StatisticLoader.getLoses()));
        Log.d("TAG", "5");
        TextView winRate = findViewById(R.id.statistic_winrate);
        winRate.setText(StatisticLoader.getWinRate() + "%");
        Log.d("TAG", "6");
        TextView lowestMoves = findViewById(R.id.statistic_lowest_moves);
        lowestMoves.setText(String.valueOf(StatisticLoader.getLowestMoves()));
        Log.d("TAG", "7");
    }
}