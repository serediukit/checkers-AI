package com.serediuk.checkers.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.serediuk.checkers.R;
import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersModel;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.model.PuzzleModel;
import com.serediuk.checkers.util.PuzzleDelegate;
import com.serediuk.checkers.view.PuzzleView;

public class LevelActivity extends AppCompatActivity implements PuzzleDelegate {
    private PuzzleModel puzzleModel = new PuzzleModel();
    private PuzzleView puzzleView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        puzzleView = findViewById(R.id.puzzle_deck);
        puzzleView.setPuzzleDelegate(this);
    }

    @Override
    public CheckersPiece pieceAt(BoardCell cell) {
        return null;
    }
}