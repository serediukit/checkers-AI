package com.serediuk.checkers.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Pair;
import android.widget.TextView;

import com.serediuk.checkers.R;
import com.serediuk.checkers.enums.Player;
import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersModel;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.model.PuzzleModel;
import com.serediuk.checkers.util.LevelLoader;
import com.serediuk.checkers.util.PuzzleDelegate;
import com.serediuk.checkers.view.PuzzleView;

import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity implements PuzzleDelegate {
    private PuzzleModel puzzleModel;
    private PuzzleView puzzleView;
    private int levelNumber;
    private Player turn;
    private ArrayList<Pair<BoardCell, BoardCell>> correctMoves;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        puzzleView = findViewById(R.id.puzzle_deck);
        puzzleView.setPuzzleDelegate(this);
        levelNumber = LevelLoader.getLevelNumber();
        puzzleModel = new PuzzleModel(levelNumber, turn);
        correctMoves = LevelLoader.getCorrectMoves();
        TextView title = findViewById(R.id.level_screen_title);
        title.setText(R.string.level_name);
        String titleString = (String) title.getText();
        title.setText(titleString + " " + levelNumber);

    }

    @Override
    public CheckersPiece pieceAt(BoardCell cell) {
        return puzzleModel.pieceAt(cell);
    }

    @Override
    public ArrayList<BoardCell> getHighlightMovesForPiece(CheckersPiece piece) {
        return puzzleModel.getHighlightMovesForPiece(piece);
    }

    @Override
    public boolean checkMove(BoardCell from, BoardCell to) {
        BoardCell fromCorrect = correctMoves.get(levelNumber).first;
        BoardCell toCorrect = correctMoves.get(levelNumber).second;

        return from.getRow() == fromCorrect.getRow()
                && from.getCol() == fromCorrect.getCol()
                && to.getRow() == toCorrect.getRow()
                && to.getCol() == toCorrect.getCol();
    }
}