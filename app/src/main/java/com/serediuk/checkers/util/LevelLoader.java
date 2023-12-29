package com.serediuk.checkers.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import com.serediuk.checkers.enums.PieceRank;
import com.serediuk.checkers.enums.Player;
import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;

import java.util.ArrayList;

public class LevelLoader {
    public static SharedPreferences preferences;

    public static int getLevelNumber() {
        return StatisticLoader.getLevel();
    }

    public static ArrayList<CheckersPiece> getPieces() {
        int levelNumber = getLevelNumber();
        ArrayList<CheckersPiece> pieces = getLevelData(levelNumber).getPieces();
        return pieces;
    }

    public static ArrayList<Pair<BoardCell, BoardCell>> getCorrectMoves() {
        int levelNumber = getLevelNumber();
        ArrayList<Pair<BoardCell, BoardCell>> moves = getLevelData(levelNumber).getCorrectMoves();
        return moves;
    }

    public static void setLevelData(LevelData levelData) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("level " + levelData.getLevelNumber(), createString(levelData));
        editor.commit();
    }

    private static LevelData getLevelData(int levelNumber) {
        String stringLevelData = preferences.getString("level " + levelNumber, "");
        LevelData levelData = getLevelDataFromString(stringLevelData);
        return levelData;
    }

    private static String createString(LevelData levelData) {
        String data = "";
        data += "level=" + levelData.getLevelNumber() + " ";
        data += "pieces=<";
        for (CheckersPiece piece : levelData.getPieces()) {
            String pieceString = "(";
            pieceString += piece.getPosition().getRow() + ";" + piece.getPosition().getCol() + ";";
            pieceString += piece.getPlayer() + ";";
            pieceString += piece.getPieceRank() + "),";
            data += pieceString;
        }
        data += "> moves=<";
        for (Pair<BoardCell, BoardCell> move : levelData.getCorrectMoves()) {
            String pairString = "(";
            pairString += move.first.getRow() + ";" + move.first.getCol() + ";" + move.second.getRow() + ";" + move.second.getCol() + "),";
            data += pairString;
        }
        data += ">";
        return data;
    }

    private static LevelData getLevelDataFromString(String string) {
        Log.d("DATA", string);
        String[] dataArray = string.split(" ");
        String[] levelArray = dataArray[0].split("=");
        int levelNumber = Integer.parseInt(levelArray[1]);
        String piecesArrayList = dataArray[1].split("=")[1];
        piecesArrayList = piecesArrayList.substring(1, piecesArrayList.length() - 2);
        String[] pieces = piecesArrayList.split(",");
        ArrayList<CheckersPiece> piecesRes = new ArrayList<>();
        for (String piece : pieces) {
            if (!piece.equals("")) {
                piece = piece.substring(1, piece.length() - 1);
                String[] pieceData = piece.split(";");
                piecesRes.add(
                        new CheckersPiece(
                                new BoardCell(
                                        Integer.parseInt(pieceData[0]),
                                        Integer.parseInt(pieceData[1])
                                ),
                                pieceData[2].equals("WHITE") ? Player.WHITE : Player.BLACK,
                                pieceData[3].equals("PAWN") ? PieceRank.PAWN : PieceRank.KING
                        )
                );
            }
        }
        String movesArray = dataArray[2].split("=")[1];
        movesArray = movesArray.substring(1, movesArray.length() - 2);
        String[] moves = movesArray.split(",");
        ArrayList<Pair<BoardCell, BoardCell>> correctMoves = new ArrayList<>();
        for (String move : moves) {
            if (!move.equals("")) {
                move = move.substring(1, move.length() - 1);
                String[] moveData = move.split(";");
                if (moveData.length == 4) {
                    correctMoves.add(
                            new Pair<>(
                                    new BoardCell(
                                            Integer.parseInt(moveData[0]),
                                            Integer.parseInt(moveData[1])
                                    ),
                                    new BoardCell(
                                            Integer.parseInt(moveData[2]),
                                            Integer.parseInt(moveData[3])
                                    )
                            )
                    );
                }
            }
        }
        return new LevelData(levelNumber, piecesRes, correctMoves);
    }
}
