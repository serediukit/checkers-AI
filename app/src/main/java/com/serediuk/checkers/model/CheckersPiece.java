package com.serediuk.checkers.model;

import com.serediuk.checkers.model.enums.PieceRank;
import com.serediuk.checkers.model.enums.Player;

import java.util.ArrayList;

public class CheckersPiece {
    private BoardCell position;
    private Player player;
    private PieceRank pieceRank;
    private int imageId;

    public CheckersPiece(BoardCell position, Player player, PieceRank pieceRank, int imageId) {
        this.position = position;
        this.player = player;
        this.pieceRank = pieceRank;
        this.imageId = imageId;
    }

    public BoardCell getPosition() {
        return position;
    }

    public void setPosition(BoardCell position) {
        this.position = position;
    }

    public void setPosition(int row, int col) {
        this.position = new BoardCell(row, col);
    }

    public Player getPlayer() {
        return player;
    }

    public PieceRank getPieceRank() {
        return pieceRank;
    }

    public void setPieceRank(PieceRank pieceRank) {
        this.pieceRank = pieceRank;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public ArrayList<BoardCell> getAllPossibleMoves() {
        ArrayList<BoardCell> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getCol();
        if (pieceRank  == PieceRank.PAWN) {
            for( int i = -2; i < 3; i++) {
                if (i == 0)
                    continue;
                if (row + i >= 0 && row + i < 8) {
                    if (col + i >= 0 && col + i < 8)
                        moves.add(new BoardCell(row + i, col + i));
                    if (col - i >= 0 && col - i < 8)
                        moves.add(new BoardCell(row + i, col - i));
                }
            }
        }
        if (pieceRank == PieceRank.KING) {
            for( int i = -7; i < 8; i++) {
                if (i == 0)
                    continue;
                if (row + i >= 0 && row + i < 8) {
                    if (col + i >= 0 && col + i < 8)
                        moves.add(new BoardCell(row + i, col + i));
                    if (col - i >= 0 && col - i < 8)
                        moves.add(new BoardCell(row + i, col - i));
                }
            }
        }
        return moves;
    }
}
