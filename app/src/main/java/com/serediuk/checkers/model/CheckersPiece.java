package com.serediuk.checkers.model;

import com.serediuk.checkers.model.emuns.PieceRank;
import com.serediuk.checkers.model.emuns.Player;

public class CheckersPiece {
    private int row;
    private int col;
    private Player player;
    private PieceRank pieceRank;
    private int imageId;

    public CheckersPiece(int row, int col, Player player, PieceRank pieceRank, int imageId) {
        this.row = row;
        this.col = col;
        this.player = player;
        this.pieceRank = pieceRank;
        this.imageId = imageId;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Player getPlayer() {
        return player;
    }

    public PieceRank getPieceRank() {
        return pieceRank;
    }

    public int getImageId() {
        return imageId;
    }
}
