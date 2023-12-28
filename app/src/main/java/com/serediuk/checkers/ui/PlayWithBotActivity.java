package com.serediuk.checkers.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.serediuk.checkers.R;
import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersModel;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.model.enums.Player;
import com.serediuk.checkers.util.CheckersDelegate;
import com.serediuk.checkers.view.CheckersView;

import java.util.ArrayList;

public class PlayWithBotActivity extends AppCompatActivity implements CheckersDelegate {
    private CheckersModel checkersModel = new CheckersModel();
    private CheckersView checkersView;
    private ImageView playerImageView;
    private ImageView opponentImageView;
    private TextView playerScore;
    private TextView opponentScore;
    private Bitmap whiteIcon;
    private Bitmap blackIcon;
    private boolean playerWhite = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_with_bot);
        checkersView = findViewById(R.id.checkers_deck);
        checkersView.setCheckersDelegate(this);
        whiteIcon = BitmapFactory.decodeResource(getResources(), R.drawable.white);
        blackIcon = BitmapFactory.decodeResource(getResources(), R.drawable.black);
        playerImageView = findViewById(R.id.player_image);
        playerImageView.setImageBitmap(blackIcon);
        opponentImageView = findViewById(R.id.opponent_image);
        opponentImageView.setImageBitmap(whiteIcon);
        playerScore = findViewById(R.id.player_score);
        playerScore.setText(String.valueOf(12 - getBlackCount()));
        opponentScore = findViewById(R.id.opponent_score);
        opponentScore.setText(String.valueOf(12 - getWhiteCount()));
    }

    public void changeTurn(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(R.string.text_warning)
                .setMessage(R.string.text_changing)
                .setPositiveButton("Так", (dialog, which) -> {
                    checkersModel.changeTurn();
                    playerWhite = !playerWhite;
                    if (playerWhite) {
                        playerImageView.setImageBitmap(blackIcon);
                        opponentImageView.setImageBitmap(whiteIcon);
                    } else {
                        playerImageView.setImageBitmap(whiteIcon);
                        opponentImageView.setImageBitmap(blackIcon);
                    }
                    checkersView.clearLists();
                    playerScore = findViewById(R.id.player_score);
                    playerScore.setText(String.valueOf(12 - getBlackCount()));
                    opponentScore = findViewById(R.id.opponent_score);
                    opponentScore.setText(String.valueOf(12 - getWhiteCount()));
                    ((TextView) findViewById(R.id.turn_title)).setText(getTurn() == Player.WHITE ? R.string.white_move_title : R.string.black_move_title);
                    checkersView.invalidate();
                })
                .setNegativeButton("Ні", (dialog, which) -> {})
                .show();
    }

    public void restartGame(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(R.string.text_warning)
                .setMessage(R.string.text_restarting)
                .setPositiveButton("Так", (dialog, which) -> {
                    checkersModel.restart();
                    checkersView.clearLists();
                    if (playerWhite) {
                        playerScore = findViewById(R.id.player_score);
                        playerScore.setText(String.valueOf(12 - getBlackCount()));
                        opponentScore = findViewById(R.id.opponent_score);
                        opponentScore.setText(String.valueOf(12 - getWhiteCount()));
                    } else {
                        playerScore = findViewById(R.id.player_score);
                        playerScore.setText(String.valueOf(12 - getWhiteCount()));
                        opponentScore = findViewById(R.id.opponent_score);
                        opponentScore.setText(String.valueOf(12 - getBlackCount()));
                    }
                    ((TextView) findViewById(R.id.turn_title)).setText(getTurn() == Player.WHITE ? R.string.white_move_title : R.string.black_move_title);
                    checkersView.invalidate();
                })
                .setNegativeButton("Ні", (dialog, which) -> {})
                .show();
    }

    public void startGame(View view) {
        checkersModel.changeTurn();
        playerWhite = !playerWhite;
        if (playerWhite) {
            playerImageView.setImageBitmap(blackIcon);
            opponentImageView.setImageBitmap(whiteIcon);
        } else {
            playerImageView.setImageBitmap(whiteIcon);
            opponentImageView.setImageBitmap(blackIcon);
        }
        LinearLayout finishLayout = findViewById(R.id.finish_layout);
        finishLayout.setVisibility(View.INVISIBLE);
        Button buttonChange = findViewById(R.id.btn_change_turn);
        buttonChange.setVisibility(View.VISIBLE);
        Button buttonRestart = findViewById(R.id.btn_restart);
        buttonRestart.setVisibility(View.VISIBLE);
        checkersView.clearLists();
        if (playerWhite) {
            playerScore = findViewById(R.id.player_score);
            playerScore.setText(String.valueOf(12 - getBlackCount()));
            opponentScore = findViewById(R.id.opponent_score);
            opponentScore.setText(String.valueOf(12 - getWhiteCount()));
        } else {
            playerScore = findViewById(R.id.player_score);
            playerScore.setText(String.valueOf(12 - getWhiteCount()));
            opponentScore = findViewById(R.id.opponent_score);
            opponentScore.setText(String.valueOf(12 - getBlackCount()));
        }
        ((TextView) findViewById(R.id.turn_title)).setText(getTurn() == Player.WHITE ? R.string.white_move_title : R.string.black_move_title);
        checkersView.invalidate();
    }

    @Override
    public CheckersPiece pieceAt(BoardCell cell) {
        return checkersModel.pieceAt(cell);
    }

    @Override
    public boolean movePiece(BoardCell from, BoardCell to) {
        boolean res = checkersModel.movePiece(from, to);
        ((TextView) findViewById(R.id.turn_title)).setText(getTurn() == Player.WHITE ? R.string.white_move_title : R.string.black_move_title);
        if (getBlackCount() == 0) {
            TextView title = findViewById(R.id.finish_win_title);
            TextView undertitle = findViewById(R.id.finish_win_undertitle);
            title.setText(R.string.white_win);
            if (playerWhite)
                undertitle.setText(R.string.text_win);
            else
                undertitle.setText(R.string.text_lose);
            LinearLayout finishLayout = findViewById(R.id.finish_layout);
            finishLayout.setVisibility(View.VISIBLE);
            Button buttonChange = findViewById(R.id.btn_change_turn);
            buttonChange.setVisibility(View.INVISIBLE);
            Button buttonRestart = findViewById(R.id.btn_restart);
            buttonRestart.setVisibility(View.INVISIBLE);
        }
        if (getWhiteCount() == 0) {
            TextView title = findViewById(R.id.finish_win_title);
            TextView undertitle = findViewById(R.id.finish_win_undertitle);
            title.setText(R.string.black_win);
            if (playerWhite)
                undertitle.setText(R.string.text_lose);
            else
                undertitle.setText(R.string.text_win);
            LinearLayout finishLayout = findViewById(R.id.finish_layout);
            finishLayout.setVisibility(View.VISIBLE);
            Button buttonChange = findViewById(R.id.btn_change_turn);
            buttonChange.setVisibility(View.INVISIBLE);
            Button buttonRestart = findViewById(R.id.btn_restart);
            buttonRestart.setVisibility(View.INVISIBLE);
        }
        playerScore.setText(String.valueOf(playerWhite ? (12 - getBlackCount()) : (12 - getWhiteCount())));
        opponentScore.setText(String.valueOf(playerWhite ? (12 - getWhiteCount()) : (12 - getBlackCount())));
        findViewById(R.id.checkers_deck).invalidate();
        if (res) {
            if (checkersModel.checkTie()) {
                TextView title = findViewById(R.id.finish_win_title);
                TextView undertitle = findViewById(R.id.finish_win_undertitle);
                title.setText(R.string.tie);
                undertitle.setText(R.string.text_tie);
                LinearLayout finishLayout = findViewById(R.id.finish_layout);
                finishLayout.setVisibility(View.VISIBLE);
                Button buttonChange = findViewById(R.id.btn_change_turn);
                buttonChange.setVisibility(View.INVISIBLE);
                Button buttonRestart = findViewById(R.id.btn_restart);
                buttonRestart.setVisibility(View.INVISIBLE);
            }
        }
        return res;
    }

    @Override
    public Player getTurn() {
        return checkersModel.getTurn();
    }

    @Override
    public ArrayList<BoardCell> getHighlightMovesForPiece(CheckersPiece piece) {
        return checkersModel.getHighlightMovesForPiece(piece);
    }

    @Override
    public ArrayList<BoardCell> getTakenPieces() {
        return checkersModel.getTakenPieces();
    }

    @Override
    public int getWhiteCount() {
        return checkersModel.getWhiteCount();
    }

    @Override
    public int getBlackCount() {
        return checkersModel.getBlackCount();
    }

    @Override
    public ArrayList<BoardCell> getLastMoves() {
        return checkersModel.getLastMoves();
    }
}