package com.serediuk.checkers.util;

import com.serediuk.checkers.model.CheckersPiece;

public interface CheckersDelegate {
    public CheckersPiece pieceAt(int row, int col);
}
