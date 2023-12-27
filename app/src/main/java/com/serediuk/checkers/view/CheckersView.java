package com.serediuk.checkers.view;

import static java.util.Arrays.asList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serediuk.checkers.R;
import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.util.CheckersDelegate;

import java.util.*;

public class CheckersView extends View {
    private final int CELL_SPACE = 10;
    private final int ROW = 8;
    private final int COL = 8;
    private final int LIGHT_COLOR = Color.parseColor("#c2b48d");
    private final int DARK_COLOR = Color.parseColor("#292314");
    private final int MOVES_COLOR = Color.parseColor("#fcba03");
    private final String ERROR_TAG = "ERROR";

    private int fromRow = -1;
    private int fromCol = -1;
    private double movingX = -1;
    private double movingY = -1;
    private int cellSize = 100;
    private double scale = 1;
    private Paint paint;
    private Set<Integer> imagesIds;
    private Map<Integer, Bitmap> bitmaps;
    private Bitmap movingBitmap = null;
    private CheckersPiece movingPiece = null;
    private ArrayList<BoardCell> correctMoves = null;

    private CheckersDelegate checkersDelegate = null;


    public CheckersView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        imagesIds = new HashSet<>(
                asList(
                        R.drawable.white,
                        R.drawable.white_king,
                        R.drawable.black,
                        R.drawable.black_king
                )
        );
        bitmaps = new HashMap<>();
        for (Integer i : imagesIds) {
            bitmaps.put(i, BitmapFactory.decodeResource(getResources(), i));
        }
    }

    public void setCheckersDelegate(CheckersDelegate checkersDelegate) {
        this.checkersDelegate = checkersDelegate;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        drawDeck(canvas);
        drawPieces(canvas);
        double boardSize = Math.min(getWidth(), getHeight()) * scale;
        cellSize = (int) boardSize / 8;
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
                movingPiece = checkersDelegate.pieceAt(new BoardCell(fromRow, fromCol));
                if (movingPiece != null) {
                    movingX = event.getX();
                    movingY = event.getY();
                    movingBitmap = bitmaps.get(movingPiece.getImageId());
                    correctMoves = checkersDelegate.getCorrectMovesForPiece(movingPiece);
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
                    checkersDelegate.movePiece(new BoardCell(fromRow, fromCol), new BoardCell(toRow, toCol));
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

    private void drawDeck(Canvas canvas) {
        try {
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if (inArrayList(correctMoves, i, j)) {
                        drawCellAt(canvas, i, j, MOVES_COLOR);
                    }
                    else
                        drawCellAt(canvas, i, j, (i + j) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
                }
            }
        } catch (Exception e) {
            Log.e(ERROR_TAG, e.getMessage(), e);
        }
    }

    private void drawCellAt(Canvas canvas, int row, int col, int color) {
        paint.setColor(color);
        canvas.drawRect(
                col * cellSize,
                row * cellSize,
                col * cellSize + cellSize,
                row * cellSize + cellSize,
                paint
        );
    }

    private void drawPieces(Canvas canvas) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                CheckersPiece piece = checkersDelegate.pieceAt(new BoardCell(i, j));
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

    private void drawPieceAt(Canvas canvas, int imageId, BoardCell cell) {
        drawPieceAt(canvas, imageId, cell.getRow(), cell.getCol());
    }

    private void drawPieceAt(Canvas canvas, int imageId, int row, int col) {
        try {
            canvas.drawBitmap(
                    Objects.requireNonNull(bitmaps.get(imageId)),
                    null,
                    new Rect(
                            col * cellSize + CELL_SPACE,
                            row * cellSize + CELL_SPACE,
                            (col + 1) * cellSize - CELL_SPACE,
                            (row + 1) * cellSize - CELL_SPACE
                    ),
                    paint
            );
        } catch (Exception e) {
            Log.e(ERROR_TAG, e.getMessage(), e);
        }
    }

    private boolean inArrayList(ArrayList<BoardCell> list, int row, int col) {
        if (list == null || list.size() == 0)
            return false;
        for (BoardCell l : list) {
            if (l.getRow() == row && l.getCol() == col) {
                return true;
            }
        }
        return false;
    }
}
