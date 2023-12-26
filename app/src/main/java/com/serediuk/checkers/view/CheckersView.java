package com.serediuk.checkers.view;

import static java.util.Arrays.asList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serediuk.checkers.R;
import com.serediuk.checkers.model.CheckersModel;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.util.CheckersDelegate;

import java.util.*;

public class CheckersView extends View {
    private final int CELL_SIZE = 100;
    private final int CELL_SPACE = 10;
    private final int ROW = 8;
    private final int COL = 8;
    private final int LIGHT_COLOR = Color.parseColor("#c2b48d");
    private final int DARK_COLOR = Color.parseColor("#292314");
    private Paint paint;
    private Set<Integer> imagesIds;
    private Map<Integer, Bitmap> bitmaps;

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
    }

    private void drawDeck(Canvas canvas) {
        try {
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    drawCellAt(canvas, i, j, (i + j) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    private void drawCellAt(Canvas canvas, int row, int col, int color) {
        paint.setColor(color);
        canvas.drawRect(
                col * CELL_SIZE,
                row * CELL_SIZE,
                col * CELL_SIZE + CELL_SIZE,
                row * CELL_SIZE + CELL_SIZE,
                paint
        );
    }

    private void drawPieces(Canvas canvas) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                CheckersPiece piece = checkersDelegate.pieceAt(i, j);
                if (piece != null)
                    drawPieceAt(canvas, piece.getImageId(), i, j);
            }
        }
    }

    private void drawPieceAt(Canvas canvas, int imageId, int row, int col) {
        row = ROW - row - 1;
        try {
            canvas.drawBitmap(
                    bitmaps.get(imageId),
                    null,
                    new Rect(
                            col * CELL_SIZE + CELL_SPACE,
                            row * CELL_SIZE + CELL_SPACE,
                            (col + 1) * CELL_SIZE - CELL_SPACE,
                            (row + 1) * CELL_SIZE - CELL_SPACE
                    ),
                    paint
            );
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }
}
