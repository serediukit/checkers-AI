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
                    paint.setColor((i + j) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
                    canvas.drawRect(
                            i * CELL_SIZE,
                            j * CELL_SIZE,
                            i * CELL_SIZE + CELL_SIZE,
                            j * CELL_SIZE + CELL_SIZE,
                            paint
                    );
                }
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    private void drawPieces(Canvas canvas) {
        CheckersModel model = new CheckersModel();
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                CheckersPiece piece = model.pieceAt(i, j);
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
