package com.serediuk.checkers.util;

import android.content.SharedPreferences;

public class StatisticLoader {
    public static SharedPreferences preferences;

    public static int getLevel() {
        return preferences.getInt("levelNumber", 0);
    }

    public static void setLevel(int level) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("levelNumber", level);
        editor.apply();
    }

    public static void setNextLevel() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("levelNumber", getLevel() + 1);
        editor.apply();
    }

    public static int getLevelCount() {
        return preferences.getInt("levelCount", 0);
    }

    public static void setLevelCount(int levelCount) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("levelCount", levelCount);
        editor.apply();
    }

    public static int getGamesCount() {
        return preferences.getInt("gamesCount", 0);
    }

    public static void setGamesCount(int gamesCount) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("gamesCount", gamesCount);
        editor.apply();
    }

    public static void increaseGamesCount() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("gamesCount", getGamesCount() + 1);
        editor.apply();
    }

    public static int getWins() {
        return preferences.getInt("wins", 0);
    }

    public static void setWins(int wins) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("wins", wins);
        editor.apply();
    }

    public static void increaseWins() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("wins", getWins() + 1);
        editor.apply();
    }

    public static int getLoses() {
        return preferences.getInt("loses", 0);
    }

    public static void setLoses(int loses) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("loses", loses);
        editor.apply();
    }

    public static void increaseLoses() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("loses", getLoses() + 1);
        editor.apply();
    }

    public static int getWinRate() {
        int wins = getWins();
        int total = getGamesCount();
        return total == 0 ? 0 : (int) ((double) wins / total * 100);
    }

    public static int getLowestMoves() {
        return preferences.getInt("lowestMoves", 0);
    }

    public static void setLowestMoves(int lowestMoves) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("lowestMoves", lowestMoves);
        editor.apply();
    }
}
