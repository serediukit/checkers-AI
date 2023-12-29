package com.serediuk.checkers.util;

import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;

public interface PuzzleDelegate {
    public CheckersPiece pieceAt(BoardCell cell);
}
