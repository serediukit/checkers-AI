package com.serediuk.checkers;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import com.serediuk.checkers.model.CheckersModel;

public class PlayWithBot extends AppCompatActivity {
    private final String TAG = "PlayWithBot";
    private CheckersModel checkersModel = new CheckersModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_with_bot);

        Log.d(TAG, checkersModel.toString());
    }
}