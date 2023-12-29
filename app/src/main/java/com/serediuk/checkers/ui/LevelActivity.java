package com.serediuk.checkers.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.serediuk.checkers.R;
import com.serediuk.checkers.enums.PieceRank;
import com.serediuk.checkers.enums.Player;
import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersModel;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.model.PuzzleModel;
import com.serediuk.checkers.util.CheckersHelper;
import com.serediuk.checkers.util.DatabaseDataInsertion;
import com.serediuk.checkers.util.LevelLoader;
import com.serediuk.checkers.util.PuzzleDelegate;
import com.serediuk.checkers.view.PuzzleView;

import java.util.ArrayList;
import java.util.Objects;

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
        LevelLoader.preferences = getSharedPreferences("levels", Context.MODE_PRIVATE);
        LevelLoader.setLevelData(Objects.requireNonNull(DatabaseDataInsertion.getLevelData(1)));
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

        Log.d("FROM", from.getRow() + " " + from.getCol());
        Log.d("TO", to.getRow() + " " + to.getCol());

        LinearLayout resultLayout = findViewById(R.id.puzzle_result_layout);
        TextView resultTitle = findViewById(R.id.puzzle_result_title);
        TextView resultUndertitle = findViewById(R.id.puzzle_result_undertitle);
        Button nextButton = findViewById(R.id.btn_puzzle_next_level);
        resultLayout.setVisibility(View.VISIBLE);

        for (Pair<BoardCell, BoardCell> move : correctMoves) {
            BoardCell fromCorrect = move.first;
            BoardCell toCorrect = move.second;

            if (from.getRow() == fromCorrect.getRow()
                    && from.getCol() == fromCorrect.getCol()
                    && to.getRow() == toCorrect.getRow()
                    && to.getCol() == toCorrect.getCol()
            ) {
                resultTitle.setText(R.string.win);
                resultUndertitle.setText(R.string.win_move);
                nextButton.setVisibility(View.VISIBLE);
                return true;
            }
        }
        resultTitle.setText(R.string.lose);
        resultUndertitle.setText(R.string.lose_move);
        nextButton.setVisibility(View.INVISIBLE);
        return false;
    }

    @Override
    public boolean movePiece(BoardCell from, BoardCell to) {
        return puzzleModel.movePiece(from, to);
    }

    public void restartLevel(View view) {

    }

    public void nextLevel(View view) {

    }
}