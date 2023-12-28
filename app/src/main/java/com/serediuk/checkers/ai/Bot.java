package com.serediuk.checkers.ai;

import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.model.enums.Player;

import java.util.ArrayList;

public interface Bot {
    public BoardCell getBestMove(ArrayList<CheckersPiece> pieces, Player turn, int depth);
}
