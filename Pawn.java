import java.util.*;

public class Pawn extends Piece {
    public Pawn(ChessColor color) {
        super(color);
        setIcon('P');
    }

    @Override
    public void setPossibleMoves(Square currSquare, Board board, Player currPlayer) {
        ArrayList<Move> possibleMoves = new ArrayList<Move>();

        // below includes moves with pawn promotion
        possibleMoves.addAll(straightMoves(currSquare, board, currPlayer));
        possibleMoves.addAll(normalCaptureMoves(currSquare, board, currPlayer));
        possibleMoves.addAll(enPassant(currSquare, board, currPlayer));

        setPossibleMoves(possibleMoves);
    }

    public ArrayList<Move> straightMoves(Square currSquare, Board board, Player currPlayer) {
        ArrayList<Move> possibleEndSquares = new ArrayList<Move>();
        int row = currSquare.getROW();
        int col = currSquare.getCOL();
        int dir = (getColor() == ChessColor.WHITE) ? 1 : -1;
        Square destSquare;
        Piece destPiece;

        // never need to worry about going out of bounds because it can only move
        // forward
        // one at a time (lest first move), and at last row it becomes another piece
        // actually, just gonna keep the check in for the sake of thoroughness... no
        // point in omitting it

        if (row + (dir * 1) >= 0 && row + (dir * 1) < 8) {
            destSquare = board.getSquare(row + (1 * dir), col);
            destPiece = destSquare.getPiece();

            if (destPiece == null) {
                if (row + (1 * dir) == 7 || row + (1 * dir) == 0) {
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.PAWN_PROMOTION,
                            new Queen(getColor())));
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.PAWN_PROMOTION,
                            new Rook(getColor())));
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.PAWN_PROMOTION,
                            new Knight(getColor())));
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.PAWN_PROMOTION,
                            new Bishop(getColor())));
                } else {
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.NORMAL));
                }
            } else {
                return possibleEndSquares;
            }
        }

        if (getMoveCount() == 0) {
            destSquare = board.getSquare(row + (2 * dir), col);
            destPiece = destSquare.getPiece();

            if (destPiece == null) {
                possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.NORMAL));
            }
        }

        return possibleEndSquares;
    }

    public ArrayList<Move> normalCaptureMoves(Square currSquare, Board board, Player currPlayer) {
        ArrayList<Move> possibleEndSquares = new ArrayList<Move>();
        int row = currSquare.getROW();
        int col = currSquare.getCOL();
        int dir = (getColor() == ChessColor.WHITE) ? 1 : -1;
        boolean inRowBound = row + (dir * 1) >= 0 && row + (dir * 1) < 8;

        // never need to worry about going out of bounds because it can only move
        // forward
        // one at a time (lest first move), and at last row it becomes another piece
        Square destSquare;
        Piece destPiece;

        // capture towards kingside
        if (inRowBound && col + 1 < 8) {
            destSquare = board.getSquare(row + (1 * dir), col + 1);
            destPiece = destSquare.getPiece();

            if (destPiece != null && getColor() != destPiece.getColor()) {
                if (row + (1 * dir) == 7 || row + (1 * dir) == 0) {
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.PAWN_PROMOTION,
                            new Queen(getColor())));
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.PAWN_PROMOTION,
                            new Rook(getColor())));
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.PAWN_PROMOTION,
                            new Knight(getColor())));
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.PAWN_PROMOTION,
                            new Bishop(getColor())));
                } else {
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.NORMAL));
                }
            }
        }

        // capture towards queenside
        if (inRowBound && col - 1 >= 0) {
            destSquare = board.getSquare(row + (1 * dir), col - 1);
            destPiece = destSquare.getPiece();

            if (destPiece != null && getColor() != destPiece.getColor()) {
                if (row + (1 * dir) == 7 || row + (1 * dir) == 0) {
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.PAWN_PROMOTION,
                            new Queen(getColor())));
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.PAWN_PROMOTION,
                            new Rook(getColor())));
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.PAWN_PROMOTION,
                            new Knight(getColor())));
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.PAWN_PROMOTION,
                            new Bishop(getColor())));
                } else {
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.NORMAL));
                }
            }
        }

        return possibleEndSquares;
    }

    public ArrayList<Move> enPassant(Square currSquare, Board board, Player currPlayer) {
        ArrayList<Move> possibleEndSquares = new ArrayList<Move>();
        int row = currSquare.getROW();
        int col = currSquare.getCOL();
        // all -1 because of 0 index
        int oppPawnRow = (getColor() == ChessColor.WHITE) ? 4 : 3;
        int destRow = (getColor() == ChessColor.WHITE) ? 5 : 2;
        Square oppPawnSquare;
        Piece oppPawn;
        Square destSquare;

        if (row == oppPawnRow) {
            if (col - 1 >= 0) {
                oppPawnSquare = board.getSquare(row, col - 1);
                oppPawn = oppPawnSquare.getPiece();

                if (oppPawn != null && oppPawn instanceof Pawn && oppPawn.getMoveCount() == 1
                        && oppPawn.equals(board.getLastPieceToMove())) {
                    destSquare = board.getSquare(destRow, col - 1);
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.EN_PASSANT));
                }
            }
            if (col + 1 < 8) {
                oppPawnSquare = board.getSquare(row, col + 1);
                oppPawn = oppPawnSquare.getPiece();

                if (oppPawn != null && oppPawn instanceof Pawn && oppPawn.getMoveCount() == 1
                        && oppPawn.equals(board.getLastPieceToMove())) {
                    destSquare = board.getSquare(destRow, col + 1);
                    possibleEndSquares.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.EN_PASSANT));
                }
            }
        }

        return possibleEndSquares;
    }
}