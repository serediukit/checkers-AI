package com.serediuk.checkers.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;
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
        takenPieces = puzzleDelegate.getTakenPieces();
        lastMoves = puzzleDelegate.getLastMoves();
        double boardSize = Math.min(getWidth(), getHeight());
        cellSize = (int) (boardSize / 8);
        cellSpace = cellSize / 10;
        drawDeck(canvas);
        drawPieces(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null)
            return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                fromCol = (int) (event.getX() / cellSize);
                fromRow = (int) (event.getY() / cellSize);
                movingPiece = puzzleDelegate.pieceAt(new BoardCell(fromRow, fromCol));
                if (movingPiece != null) {
                    movingX = event.getX();
                    movingY = event.getY();
                    movingBitmap = bitmaps.get(movingPiece.getImageId());
                    correctMoves = puzzleDelegate.getHighlightMovesForPiece(movingPiece);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                movingX = event.getX();
                movingY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                int toCol = (int) (event.getX() / cellSize);
                int toRow = (int) (event.getY() / cellSize);
                if (movingPiece != null)
                    puzzleDelegate.checkMove(new BoardCell(fromRow, fromCol), new BoardCell(toRow, toCol));
                movingBitmap = null;
                movingPiece = null;
                correctMoves = null;
                fromRow = -1;
                fromCol = -1;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void drawPieces(Canvas canvas) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                CheckersPiece piece = puzzleDelegate.pieceAt(new BoardCell(i, j));
                if (piece != null)
                    if (piece != movingPiece)
                        drawPieceAt(canvas, piece.getImageId(), i, j);
            }
        }

        if (movingBitmap != null && movingPiece != null) {
            canvas.drawBitmap(
                    movingBitmap,
                    null,
                    new Rect(
                            (int) (movingX - cellSize / 2),
                            (int) (movingY - cellSize / 2),
                            (int) (movingX + cellSize / 2),
                            (int) (movingY + cellSize / 2)
                    ),
                    paint
            );
        }
    }
}
