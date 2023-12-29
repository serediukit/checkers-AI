package com.serediuk.checkers.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serediuk.checkers.util.PuzzleDelegate;

public class PuzzleView extends CheckersView {
    private PuzzleDelegate puzzleDelegate = null;
    public PuzzleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPuzzleDelegate(PuzzleDelegate puzzleDelegate) {
        this.puzzleDelegate = puzzleDelegate;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        double boardSize = Math.min(getWidth(), getHeight());
        cellSize = (int) (boardSize / 8);
        cellSpace = (int) cellSize / 10;
        drawDeck(canvas);
        drawPieces(canvas);
    }
}
