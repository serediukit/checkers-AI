package com.serediuk.checkers.util;

import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.model.emuns.Player;

import java.util.ArrayList;

public interface CheckersDelegate {
    public CheckersPiece pieceAt(BoardCell cell);
    public boolean movePiece(BoardCell from, BoardCell to);
    public Player getTurn();
    public ArrayList<BoardCell> getCorrectMovesForPiece(CheckersPiece piece);
}
