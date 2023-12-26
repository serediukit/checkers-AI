package com.serediuk.checkers.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CheckersView extends View {
    private final int CELL_SIZE = 100;
    private final int ROW = 8;
    private final int COL = 8;

    public CheckersView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        try {
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if ((i + j) % 2 == 0)
                        paint.setColor(Color.BLACK);
                    else
                        paint.setColor(Color.WHITE);
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
}
