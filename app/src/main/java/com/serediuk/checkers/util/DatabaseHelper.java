package com.serediuk.checkers.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "GameLevels.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_LEVELS = "levels";
    private static final String COLUMN_LEVEL_ID = "_id";
    private static final String COLUMN_LEVEL_NUMBER = "level_number";
    private static final String COLUMN_LEVEL_PIECES = "level_pieces";
    private static final String COLUMN_LEVEL_MOVES = "level_moves";

    private static final String CREATE_TABLE_LEVELS =
            "CREATE TABLE " + TABLE_LEVELS + "("
            + COLUMN_LEVEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_LEVEL_NUMBER + " INTEGER,"
            + COLUMN_LEVEL_PIECES + " TEXT,"
            + COLUMN_LEVEL_MOVES + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LEVELS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS);
        onCreate(db);
    }

    public long insertLevel(LevelData levelData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LEVEL_NUMBER, levelData.getLevelNumber());
        values.put(COLUMN_LEVEL_PIECES, serializePieces(levelData.getPieces()));
        values.put(COLUMN_LEVEL_MOVES, serializeMoves(levelData.getCorrectMoves()));

        long levelId = db.insert(TABLE_LEVELS, null, values);
        db.close();

        return levelId;
    }

    public ArrayList<LevelData> getAllLevels() {
        ArrayList<LevelData> levelDataList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_LEVELS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int level = cursor.getInt(cursor.getColumnIndex(COLUMN_LEVEL_NUMBER));
                @SuppressLint("Range") ArrayList<CheckersPiece> figures = deserializePieces(cursor.getString(cursor.getColumnIndex(COLUMN_LEVEL_PIECES)));
                @SuppressLint("Range") ArrayList<Pair<BoardCell, BoardCell>> moves = deserializeMoves(cursor.getString(cursor.getColumnIndex(COLUMN_LEVEL_MOVES)));

                LevelData levelData = new LevelData(level, figures, moves);
                levelDataList.add(levelData);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return levelDataList;
    }

    public LevelData getLevelByNumber(int levelNumber) {
        String selectQuery = "SELECT * FROM " + TABLE_LEVELS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        LevelData levelData = null;
        boolean isFinded = false;

        if (cursor.moveToFirst() && !isFinded) {
            do {
                @SuppressLint("Range") int level = cursor.getInt(cursor.getColumnIndex(COLUMN_LEVEL_NUMBER));
                if (level == levelNumber) {
                    @SuppressLint("Range") ArrayList<CheckersPiece> figures = deserializePieces(cursor.getString(cursor.getColumnIndex(COLUMN_LEVEL_PIECES)));
                    @SuppressLint("Range") ArrayList<Pair<BoardCell, BoardCell>> moves = deserializeMoves(cursor.getString(cursor.getColumnIndex(COLUMN_LEVEL_MOVES)));

                    levelData = new LevelData(level, figures, moves);
                    isFinded = true;
                }
            } while (cursor.moveToNext() && !isFinded);
        }

        cursor.close();
        db.close();

        return levelData;
    }

    private String serializePieces(ArrayList<CheckersPiece> pieces) {
        return new Gson().toJson(pieces);
    }

    private String serializeMoves(ArrayList<Pair<BoardCell, BoardCell>> moves) {
        return new Gson().toJson(moves);
    }

    private ArrayList<CheckersPiece> deserializePieces(String serializedPieces) {
        return new Gson().fromJson(serializedPieces, new TypeToken<ArrayList<CheckersPiece>>(){}.getType());
    }

    private ArrayList<Pair<BoardCell, BoardCell>> deserializeMoves(String serializedMoves) {
        return new Gson().fromJson(serializedMoves, new TypeToken<ArrayList<Pair<BoardCell, BoardCell>>>(){}.getType());
    }
}
