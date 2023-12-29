package com.serediuk.checkers.util;

import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;

import java.util.ArrayList;

public interface PuzzleDelegate {
    public CheckersPiece pieceAt(BoardCell cell);
    public ArrayList<BoardCell> getHighlightMovesForPiece(CheckersPiece piece);
    public boolean checkMove(BoardCell from, BoardCell to);
    public boolean movePiece(BoardCell from, BoardCell to);
}
