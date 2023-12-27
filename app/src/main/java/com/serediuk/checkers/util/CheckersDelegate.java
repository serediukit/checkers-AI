package com.serediuk.checkers.util;

import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.model.emuns.Player;

import java.util.ArrayList;
import java.util.Set;

public interface CheckersDelegate {
    public CheckersPiece pieceAt(BoardCell cell);
    public boolean movePiece(BoardCell from, BoardCell to);
    public Player getTurn();
    public ArrayList<BoardCell> getHighlightMovesForPiece(CheckersPiece piece);
    public ArrayList<BoardCell> getTakenPieces();
    public ArrayList<BoardCell> getLastMoves();
    public int getWhiteCount();
    public int getBlackCount();
}
