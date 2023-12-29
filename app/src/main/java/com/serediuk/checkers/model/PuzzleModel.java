package com.serediuk.checkers.model;

import com.serediuk.checkers.enums.Player;
import com.serediuk.checkers.util.LevelLoader;

import java.util.ArrayList;

public class PuzzleModel extends CheckersModel {
    private int levelNumber;

    public PuzzleModel(int levelNumber, Player turn) {
        playerTurn = turn;
        opponentTurn = playerTurn == Player.WHITE ? Player.BLACK : Player.WHITE;
        this.turn = turn;
        this.levelNumber = levelNumber;

        pieces = new ArrayList<>();
        takenPieces = new ArrayList<>();
        lastMoves = new ArrayList<>();
        initializePieces();
    }

    @Override
    protected void initializePieces() {
        pieces = LevelLoader.getPieces();
    }
}
