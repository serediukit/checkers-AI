package com.serediuk.checkers.model;

import com.serediuk.checkers.R;
import com.serediuk.checkers.enums.PieceRank;
import com.serediuk.checkers.enums.Player;
import com.serediuk.checkers.util.loader.LevelLoader;

import java.util.ArrayList;

public class PuzzleModel extends CheckersModel {
    private int levelNumber;

    public PuzzleModel(int levelNumber, Player turn) {
        playerTurn = turn;
        opponentTurn = playerTurn == Player.WHITE ? Player.BLACK : Player.WHITE;
        this.turn = turn;
        this.levelNumber = levelNumber;

        pieces = LevelLoader.getPieces();
        for (CheckersPiece piece : pieces) {
            if (piece.getPieceRank() == PieceRank.PAWN) {
                if (piece.getPlayer() == Player.WHITE)
                    piece.setImageId(R.drawable.white);
                if (piece.getPlayer() == Player.BLACK)
                    piece.setImageId(R.drawable.black);
            }
            if (piece.getPieceRank() == PieceRank.KING) {
                if (piece.getPlayer() == Player.WHITE)
                    piece.setImageId(R.drawable.white_king);
                if (piece.getPlayer() == Player.BLACK)
                    piece.setImageId(R.drawable.black_king);
            }
        }
        takenPieces = new ArrayList<>();
        lastMoves = new ArrayList<>();
    }
}
