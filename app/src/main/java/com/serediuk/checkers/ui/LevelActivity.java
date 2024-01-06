package com.serediuk.checkers.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.serediuk.checkers.R;
import com.serediuk.checkers.enums.PieceRank;
import com.serediuk.checkers.enums.Player;
import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.model.PuzzleModel;
import com.serediuk.checkers.util.CheckersHelper;
import com.serediuk.checkers.util.loader.LevelLoader;
import com.serediuk.checkers.util.PuzzleDelegate;
import com.serediuk.checkers.util.loader.StatisticLoader;
import com.serediuk.checkers.view.PuzzleView;

import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity implements PuzzleDelegate {
    private PuzzleModel puzzleModel;
    private PuzzleView puzzleView;
    private int levelNumber;
    private Player turn = Player.WHITE;
    private ArrayList<Pair<BoardCell, BoardCell>> correctMoves;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        puzzleView = findViewById(R.id.puzzle_deck);
        puzzleView.setPuzzleDelegate(this);
        levelNumber = LevelLoader.getLevelNumber();
        if (levelNumber <= StatisticLoader.getLevelCount()) {
            puzzleModel = new PuzzleModel(levelNumber, turn);
            correctMoves = LevelLoader.getCorrectMoves();
        }
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
    public ArrayList<BoardCell> getTakenPieces() {
        return puzzleModel.getTakenPieces();
    }

    @Override
    public ArrayList<BoardCell> getLastMoves() {
        return puzzleModel.getLastMoves();
    }

    @Override
    public boolean checkMove(BoardCell from, BoardCell to) {
        if (pieceAt(from).getPlayer() != turn)
            return false;
        if (from.getRow() == to.getRow() && from.getCol() == to.getCol())
            return false;
        if (pieceAt(from).getPieceRank() == PieceRank.PAWN)
            if (
                    !CheckersHelper.isPawnTakeMove(
                            puzzleModel.getPieces(),
                            pieceAt(from),
                            to
                    ) && !CheckersHelper.isPawnMove(
                            puzzleModel.getPieces(),
                            pieceAt(from),
                            to,
                            pieceAt(from).getPlayer()
                    )
            )
                return false;
        if (pieceAt(from).getPieceRank() == PieceRank.KING)
            if (
                    !CheckersHelper.isKingTakeMove(
                            puzzleModel.getPieces(),
                            pieceAt(from),
                            to
                    ) && !CheckersHelper.isKingMove(
                            puzzleModel.getPieces(),
                            pieceAt(from),
                            to
                    )
            )
                return false;

        if (correctMoves.size() > 0) {
            BoardCell fromCorrect = correctMoves.get(0).first;
            BoardCell toCorrect = correctMoves.get(0).second;

            LinearLayout resultLayout = findViewById(R.id.puzzle_result_layout);
            TextView resultTitle = findViewById(R.id.puzzle_result_title);
            TextView resultUndertitle = findViewById(R.id.puzzle_result_undertitle);
            Button nextButton = findViewById(R.id.btn_puzzle_next_level);

            if (from.getRow() == fromCorrect.getRow()
                    && from.getCol() == fromCorrect.getCol()
                    && to.getRow() == toCorrect.getRow()
                    && to.getCol() == toCorrect.getCol()
            ) {
                puzzleModel.movePiece(from, to);
                correctMoves.remove(0);
                if (correctMoves.size() > 0) {
                    turn = pieceAt(correctMoves.get(0).first).getPlayer();
                    while (turn == Player.BLACK && correctMoves.size() > 0) {
                        BoardCell opponentFromCorrect = correctMoves.get(0).first;
                        BoardCell opponentToCorrect = correctMoves.get(0).second;

                        puzzleModel.movePiece(opponentFromCorrect, opponentToCorrect);
                        correctMoves.remove(0);
                        if (correctMoves.size() > 0)
                            turn = pieceAt(correctMoves.get(0).first).getPlayer();
                    }
                }

                if (correctMoves.size() == 0) {
                    resultLayout.setVisibility(View.VISIBLE);
                    resultTitle.setText(R.string.win);
                    resultUndertitle.setText(R.string.win_move);
                    nextButton.setVisibility(View.VISIBLE);
                    puzzleView.invalidate();
                }
                return true;
            } else {
                resultLayout.setVisibility(View.VISIBLE);
                resultTitle.setText(R.string.lose);
                resultUndertitle.setText(R.string.lose_move);
                nextButton.setVisibility(View.INVISIBLE);
                puzzleView.invalidate();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean movePiece(BoardCell from, BoardCell to) {
        return puzzleModel.movePiece(from, to);
    }

    public void restartLevel(View view) {
        LinearLayout resultLayout = findViewById(R.id.puzzle_result_layout);
        resultLayout.setVisibility(View.INVISIBLE);
        levelNumber = LevelLoader.getLevelNumber();
        puzzleModel = new PuzzleModel(levelNumber, Player.WHITE);
        correctMoves = LevelLoader.getCorrectMoves();
        puzzleView.invalidate();
    }

    public void nextLevel(View view) {
        if (StatisticLoader.getLevel() < StatisticLoader.getLevelCount()) {
            StatisticLoader.setNextLevel();
            levelNumber = LevelLoader.getLevelNumber();
            puzzleModel = new PuzzleModel(levelNumber, turn);
            puzzleView.invalidate();
            correctMoves = LevelLoader.getCorrectMoves();
            TextView title = findViewById(R.id.level_screen_title);
            title.setText(R.string.level_name);
            String titleString = (String) title.getText();
            title.setText(titleString + " " + levelNumber);
            LinearLayout resultLayout = findViewById(R.id.puzzle_result_layout);
            resultLayout.setVisibility(View.INVISIBLE);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}