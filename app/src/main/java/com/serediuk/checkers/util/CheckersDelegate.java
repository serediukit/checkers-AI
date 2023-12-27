package com.serediuk.checkers.util;

import com.serediuk.checkers.model.CheckersPiece;

public interface CheckersDelegate {
    public CheckersPiece pieceAt(int row, int col);

    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol);
}
