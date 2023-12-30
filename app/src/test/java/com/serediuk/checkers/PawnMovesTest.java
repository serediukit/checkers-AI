package com.serediuk.checkers;


import org.junit.Test;

import static org.junit.Assert.*;

import com.serediuk.checkers.enums.PieceRank;
import com.serediuk.checkers.enums.Player;
import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.util.CheckersHelper;

import java.util.ArrayList;
import java.util.Objects;

public class PawnMovesTest {
    ArrayList<CheckersPiece> pieces;

    public PawnMovesTest() {
        pieces = new ArrayList<>();
        pieces.add(new CheckersPiece(new BoardCell(4, 3), Player.WHITE, PieceRank.PAWN ));
        pieces.add(new CheckersPiece(new BoardCell(3, 2), Player.BLACK, PieceRank.PAWN ));
        pieces.add(new CheckersPiece(new BoardCell(5, 4), Player.BLACK, PieceRank.PAWN ));
    }

    @Test
    public void pawnMoveForwardOnNotNullCell() {
        assertFalse(
                CheckersHelper.isPawnMove(
                        pieces,
                        Objects.requireNonNull(CheckersHelper.pieceAt(pieces, 4, 3)),
                        new BoardCell(3, 2),
                        Player.WHITE
                )
        );
    }

    @Test
    public void pawnMoveForwardOnNullCell() {
        assertTrue(
                CheckersHelper.isPawnMove(
                        pieces,
                        Objects.requireNonNull(CheckersHelper.pieceAt(pieces, 4, 3)),
                        new BoardCell(3, 4),
                        Player.WHITE
                )
        );
    }

    @Test
    public void pawnMoveBackOnNotNullCell() {
        assertFalse(
                CheckersHelper.isPawnMove(
                        pieces,
                        Objects.requireNonNull(CheckersHelper.pieceAt(pieces, 4, 3)),
                        new BoardCell(5, 4),
                        Player.WHITE
                )
        );
    }

    @Test
    public void pawnMoveBackOnNullCell() {
        assertFalse(
                CheckersHelper.isPawnMove(
                        pieces,
                        Objects.requireNonNull(CheckersHelper.pieceAt(pieces, 4, 3)),
                        new BoardCell(5, 2),
                        Player.WHITE
                )
        );
    }
}
