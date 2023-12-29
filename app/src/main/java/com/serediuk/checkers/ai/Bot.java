package com.serediuk.checkers.ai;

import android.util.Pair;

import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.enums.Player;

import java.util.ArrayList;

public interface Bot {
    public Pair<BoardCell, BoardCell> getBestMove(ArrayList<CheckersPiece> pieces, Player turn, int depth);
    public int evaluatePosition(ArrayList<CheckersPiece> pieces, Player turn);
}
