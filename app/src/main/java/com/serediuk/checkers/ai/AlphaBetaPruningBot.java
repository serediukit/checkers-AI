package com.serediuk.checkers.ai;

import android.util.Log;
import android.util.Pair;

import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.enums.PieceRank;
import com.serediuk.checkers.enums.Player;
import com.serediuk.checkers.util.CheckersHelper;

import java.util.ArrayList;
import java.util.Objects;

public class AlphaBetaPruningBot implements Bot {
    private int MAX_DEPTH;
    private Player botTurn;

    public Pair<BoardCell, BoardCell> getBestMove(ArrayList<CheckersPiece> pieces, Player turn, int depth) {
        ArrayList<CheckersPiece> newPieces = clonePieces(pieces);
        MAX_DEPTH = depth;
        botTurn = turn;
        int[] result = alphaBeta(newPieces, turn, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        return new Pair<>(new BoardCell(result[1], result[2]), new BoardCell(result[3], result[4]));
    }

    private ArrayList<CheckersPiece> clonePieces(ArrayList<CheckersPiece> pieces) {
        ArrayList<CheckersPiece> res = new ArrayList<>();
        for (CheckersPiece piece : pieces) {
            int row = piece.getPosition().getRow();
            int col = piece.getPosition().getCol();
            Player player = piece.getPlayer() == Player.WHITE ? Player.WHITE : Player.BLACK;
            PieceRank rank = piece.getPieceRank() == PieceRank.PAWN ? PieceRank.PAWN : PieceRank.KING;
            CheckersPiece newPiece = new CheckersPiece(new BoardCell(row, col), player, rank);
            res.add(newPiece);
        }
        return res;
    }

    private int[] alphaBeta(ArrayList<CheckersPiece> pieces, Player turn, int alpha, int beta, int depth) {
        if (depth == MAX_DEPTH || isGameOver(pieces, turn)) {
            int evaluation = evaluatePosition(pieces);
            return new int[]{evaluation, -1, -1};
        }

        ArrayList<Pair<BoardCell, BoardCell>> availableMoves = generateMoves(pieces, turn);

        int bestMoveRowFrom = -1;
        int bestMoveColFrom = -1;
        int bestMoveRowTo = -1;
        int bestMoveColTo = -1;

        if (turn == botTurn) {
            int maxEval = Integer.MIN_VALUE;
            for (Pair<BoardCell, BoardCell> move : availableMoves) {
                BoardCell from = move.first;
                BoardCell to = move.second;
                ArrayList<CheckersPiece> updatedPieces = new ArrayList<>(makeMove(pieces, from, to));
                int eval = alphaBeta(updatedPieces, turn == Player.WHITE ? Player.BLACK : Player.WHITE, alpha, beta, depth + 1)[0];

                if (eval > maxEval) {
                    maxEval = eval;
                    bestMoveRowFrom = from.getRow();
                    bestMoveColFrom = from.getCol();
                    bestMoveRowTo = to.getRow();
                    bestMoveColTo = to.getCol();
                }
                if (eval == maxEval) {
                    double rnd = Math.random();
                    if (rnd <= 0.2) {
                        bestMoveRowFrom = from.getRow();
                        bestMoveColFrom = from.getCol();
                        bestMoveRowTo = to.getRow();
                        bestMoveColTo = to.getCol();
                    }
                }

                alpha = Math.max(alpha, eval);
                if (beta <= alpha)
                    break;
            }
            return new int[]{maxEval, bestMoveRowFrom, bestMoveColFrom, bestMoveRowTo, bestMoveColTo};
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Pair<BoardCell, BoardCell> move : availableMoves) {
                BoardCell from = move.first;
                BoardCell to = move.second;
                ArrayList<CheckersPiece> updatedPieces = new ArrayList<>(makeMove(pieces, from, to));
                int eval = alphaBeta(updatedPieces, turn == Player.WHITE ? Player.BLACK : Player.WHITE, alpha, beta, depth + 1)[0];

                if (eval < minEval) {
                    minEval = eval;
                    bestMoveRowFrom = from.getRow();
                    bestMoveColFrom = from.getCol();
                    bestMoveRowTo = to.getRow();
                    bestMoveColTo = to.getCol();
                }
                if (eval == minEval) {
                    double rnd = Math.random();
                    if (rnd <= 0.2) {
                        bestMoveRowFrom = from.getRow();
                        bestMoveColFrom = from.getCol();
                        bestMoveRowTo = to.getRow();
                        bestMoveColTo = to.getCol();
                    }
                }

                beta = Math.min(beta, eval);
                if (beta <= alpha)
                    break;
            }
            return new int[]{minEval, bestMoveRowFrom, bestMoveColFrom, bestMoveRowTo, bestMoveColTo};
        }
    }

    private ArrayList<Pair<BoardCell, BoardCell>> generateMoves(ArrayList<CheckersPiece> pieces, Player turn) {
        ArrayList<Pair<BoardCell, BoardCell>> moves = new ArrayList<>();
        for (CheckersPiece piece : pieces) {
            if (piece.getPlayer() == turn) {
                ArrayList<BoardCell> takeMoves = generateTakeMovesForPiece(pieces, piece);
                if (takeMoves.size() > 0) {
                    for (BoardCell move : takeMoves) {
                        moves.add(new Pair<>(piece.getPosition(), move));
                    }
                }
            }
        }
        if (moves.size() == 0) {
            for (CheckersPiece piece : pieces) {
                if (piece.getPlayer() == turn) {
                    ArrayList<BoardCell> justMoves = generateJustMovesForPiece(pieces, piece, turn);
                    if (justMoves.size() > 0) {
                        for (BoardCell move : justMoves) {
                            moves.add(new Pair<>(piece.getPosition(), move));
                        }
                    }
                }
            }
        }
        return moves;
    }

    private ArrayList<BoardCell> generateTakeMovesForPiece(ArrayList<CheckersPiece> pieces, CheckersPiece piece) {
        ArrayList<BoardCell> allMoves = piece.getAllPossibleMoves();
        ArrayList<BoardCell> takeMoves = new ArrayList<>();
        for (BoardCell move : allMoves) {
            if (CheckersHelper.pieceAt(pieces, move) == null) {
                if (piece.getPieceRank() == PieceRank.PAWN) {
                    if (CheckersHelper.isPawnTakeMove(pieces, piece, move)) {
                        takeMoves.add(move);
                    }
                }
                if (piece.getPieceRank() == PieceRank.KING) {
                    if (CheckersHelper.isKingTakeMove(pieces, piece, move)) {
                        takeMoves.add(move);
                    }
                }
            }
        }
        return takeMoves;
    }

    private ArrayList<BoardCell> generateJustMovesForPiece(ArrayList<CheckersPiece> pieces, CheckersPiece piece, Player turn) {
        ArrayList<BoardCell> allMoves = piece.getAllPossibleMoves();
        ArrayList<BoardCell> correctMoves = new ArrayList<>();
        for (BoardCell move : allMoves) {
            if (CheckersHelper.pieceAt(pieces, move) == null) {
                if (piece.getPieceRank() == PieceRank.PAWN)
                    if (CheckersHelper.isPawnMove(pieces, piece, move, turn == Player.WHITE ? Player.BLACK : Player.WHITE))
                        correctMoves.add(move);
                if (piece.getPieceRank() == PieceRank.KING)
                    if (CheckersHelper.isKingMove(pieces, piece, move))
                        correctMoves.add(move);
            }
        }
        return correctMoves;
    }

    private ArrayList<CheckersPiece> makeMove(ArrayList<CheckersPiece> pieces, BoardCell from, BoardCell to) {
        ArrayList<CheckersPiece> newPieces = clonePieces(pieces);
        boolean takeWasMaden = false;
        CheckersPiece takenPiece = null;
        for (CheckersPiece piece : newPieces) {
            if (isEqualPosition(piece, from)) {
                if (piece.getPieceRank() == PieceRank.PAWN) {
                    if (CheckersHelper.isPawnTakeMove(pieces, piece, to)) {
                        piece.setPosition(to);
                        int takeRow = (piece.getPosition().getRow() + to.getRow()) / 2;
                        int takeCol = (piece.getPosition().getCol() + to.getCol()) / 2;
                        newPieces.remove(CheckersHelper.pieceAt(pieces, takeRow, takeCol));
                        takeWasMaden = true;
                        takenPiece = piece;
                        break;
                    }
                }
                if (piece.getPieceRank() == PieceRank.KING) {
                    if (CheckersHelper.isKingTakeMove(pieces, piece, to)) {
                        piece.setPosition(to);
                        int takeRow = to.getRow() - (to.getRow() - piece.getPosition().getRow() > 0 ? 1 : -1);
                        int takeCol = to.getCol() - (to.getCol() - piece.getPosition().getCol() > 0 ? 1 : -1);
                        newPieces.remove(CheckersHelper.pieceAt(pieces, takeRow, takeCol));
                        takeWasMaden = true;
                        takenPiece = piece;
                        break;
                    }
                }
                piece.setPosition(to);
                break;
            }
        }
        int maxDepth = 3;
        int depth = 0;
        while (takeWasMaden && depth < maxDepth && !isGameOver(newPieces, takenPiece.getPlayer())) {
            takeWasMaden = false;
            ArrayList<BoardCell> takenMoves = generateTakeMovesForPiece(newPieces, takenPiece);
            if (takenMoves.size() > 0) {
                depth++;
                BoardCell toTake = takenMoves.get(0);
                if (takenPiece.getPieceRank() == PieceRank.PAWN) {
                    if (CheckersHelper.isPawnTakeMove(newPieces, takenPiece, toTake)) {
                        takenPiece.setPosition(toTake);
                        int takeRow = (takenPiece.getPosition().getRow() + toTake.getRow()) / 2;
                        int takeCol = (takenPiece.getPosition().getCol() + toTake.getCol()) / 2;
                        newPieces.remove(CheckersHelper.pieceAt(newPieces, takeRow, takeCol));
                        takeWasMaden = true;
                    }
                }
                if (takenPiece.getPieceRank() == PieceRank.KING) {
                    if (CheckersHelper.isKingTakeMove(newPieces, takenPiece, toTake)) {
                        takenPiece.setPosition(toTake);
                        int takeRow = toTake.getRow() - (toTake.getRow() - takenPiece.getPosition().getRow() > 0 ? 1 : -1);
                        int takeCol = toTake.getCol() - (toTake.getCol() - takenPiece.getPosition().getCol() > 0 ? 1 : -1);
                        newPieces.remove(CheckersHelper.pieceAt(newPieces, takeRow, takeCol));
                        takeWasMaden = true;
                    }
                }
            }
        }
        return newPieces;
    }

    private boolean isEqualPosition(CheckersPiece piece, BoardCell cell) {
        return piece.getPosition().getRow() == cell.getRow() && piece.getPosition().getCol() == cell.getCol();
    }

    public int evaluatePosition(ArrayList<CheckersPiece> pieces) {
        if (getBlackCount(pieces) == 0 && botTurn == Player.WHITE)
            return Integer.MAX_VALUE;
        else if (getBlackCount(pieces) == 0 && botTurn == Player.BLACK)
            return Integer.MIN_VALUE;
        else if (getWhiteCount(pieces) == 0 && botTurn == Player.WHITE)
            return Integer.MIN_VALUE;
        else if (getWhiteCount(pieces) == 0 && botTurn == Player.BLACK)
            return Integer.MAX_VALUE;
        else if (isTie(pieces, botTurn) || isTie(pieces, botTurn == Player.WHITE ? Player.BLACK : Player.WHITE))
            return 0;
        int eval = 0;
        for (CheckersPiece piece : pieces) {
            int weight = generateTakeMovesForPiece(pieces, piece).size();
            if (weight == 0)
                weight = generateJustMovesForPiece(pieces, piece, piece.getPlayer()).size();
            if (piece.getPieceRank() == PieceRank.KING)
                weight *= 4;
            if (piece.getPlayer() == botTurn)
                eval += weight + 1;
            else
                eval -= weight + 1;
        }
        return eval;
    }

    private boolean isGameOver(ArrayList<CheckersPiece> pieces, Player turn) {
        return getWhiteCount(pieces) == 0 || getBlackCount(pieces) == 0 || isTie(pieces, turn);
    }

    private int getWhiteCount(ArrayList<CheckersPiece> pieces) {
        int count = 0;
        for (CheckersPiece piece : pieces)
            if (piece.getPlayer() == Player.WHITE)
                count++;
        return count;
    }

    private int getBlackCount(ArrayList<CheckersPiece> pieces) {
        int count = 0;
        for (CheckersPiece piece : pieces)
            if (piece.getPlayer() == Player.BLACK)
                count++;
        return count;
    }

    private boolean isTie(ArrayList<CheckersPiece> pieces, Player turn) {
        ArrayList<Pair<BoardCell, BoardCell>> moves = generateMoves(pieces, turn);
        return moves.size() == 0;
    }
}
