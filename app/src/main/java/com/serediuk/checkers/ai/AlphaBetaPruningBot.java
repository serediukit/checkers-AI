package com.serediuk.checkers.ai;

import android.util.Pair;

import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.enums.PieceRank;
import com.serediuk.checkers.enums.Player;
import com.serediuk.checkers.util.CheckersHelper;

import java.util.ArrayList;

public class AlphaBetaPruningBot implements Bot {
    private int MAX_DEPTH;

    public Pair<BoardCell, BoardCell> getBestMove(ArrayList<CheckersPiece> pieces, Player turn, int depth) {
        ArrayList<CheckersPiece> newPieces = clonePieces(pieces);
        MAX_DEPTH = depth;
        int[] result = alphaBeta(newPieces, turn == Player.WHITE ? Player.BLACK : Player.WHITE, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
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
        turn = turn == Player.WHITE ? Player.BLACK : Player.WHITE;
        if (depth == MAX_DEPTH || isGameOver(pieces, turn)) {
            int evaluation = evaluatePosition(pieces, turn);
            return new int[]{evaluation, -1, -1};
        }

        ArrayList<Pair<BoardCell, BoardCell>> availableMoves = generateMoves(pieces, turn);

        int bestMoveRowFrom = -1;
        int bestMoveColFrom = -1;
        int bestMoveRowTo = -1;
        int bestMoveColTo = -1;

        if (turn == Player.BLACK) {
            // Maximizing player (BLACK)
            int maxEval = Integer.MIN_VALUE;
            for (Pair<BoardCell, BoardCell> move : availableMoves) {
                BoardCell from = move.first;
                BoardCell to = move.second;
                ArrayList<CheckersPiece> updatedPieces = makeMove(pieces, from, to);
                int eval = alphaBeta(updatedPieces, Player.WHITE, alpha, beta, depth + 1)[0];

                if (eval > maxEval) {
                    maxEval = eval;
                    bestMoveRowFrom = from.getRow();
                    bestMoveColFrom = from.getCol();
                    bestMoveRowTo = to.getRow();
                    bestMoveColTo = to.getCol();
                }

                alpha = Math.max(alpha, eval);
                if (beta <= alpha)
                    break;
            }
            return new int[]{maxEval, bestMoveRowFrom, bestMoveColFrom, bestMoveRowTo, bestMoveColTo};
        } else {
            // Minimizing player (WHITE)
            int minEval = Integer.MAX_VALUE;
            for (Pair<BoardCell, BoardCell> move : availableMoves) {
                BoardCell from = move.first;
                BoardCell to = move.second;
                ArrayList<CheckersPiece> updatedPieces = makeMove(pieces, from, to);
                int eval = alphaBeta(updatedPieces, Player.BLACK, alpha, beta, depth + 1)[0];

                if (eval < minEval) {
                    minEval = eval;
                    bestMoveRowFrom = from.getRow();
                    bestMoveColFrom = from.getCol();
                    bestMoveRowTo = to.getRow();
                    bestMoveColTo = to.getCol();
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
        for (CheckersPiece piece : newPieces) {
            if (isEqualPosition(piece, from)) {
                piece.setPosition(to);
                break;
            }
        }
        return newPieces;
    }

    private boolean isEqualPosition(CheckersPiece piece, BoardCell cell) {
        return piece.getPosition().getRow() == cell.getRow() && piece.getPosition().getCol() == cell.getCol();
    }

    public int evaluatePosition(ArrayList<CheckersPiece> pieces, Player turn) {
        int eval = 0;
        for (CheckersPiece piece : pieces) {
            int weight = 1;
            if (piece.getPieceRank() == PieceRank.KING)
                weight = 7;
            if (piece.getPlayer() == turn)
                eval += weight;
            else
                eval -= weight;
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
