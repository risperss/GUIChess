import java.util.*;

public class Queen extends Piece {
    public Queen(ChessColor color) {
        super(color);
        setIcon('Q');
    }

    @Override
    public void setPossibleMoves(Square currSquare, Board board, Player currPlayer) {
        ArrayList<Move> possibleMoves = new ArrayList<Move>();

        possibleMoves.addAll(oneDirection(1, 1, currSquare, board, currPlayer));
        possibleMoves.addAll(oneDirection(-1, 1, currSquare, board, currPlayer));
        possibleMoves.addAll(oneDirection(1, -1, currSquare, board, currPlayer));
        possibleMoves.addAll(oneDirection(-1, -1, currSquare, board, currPlayer));
        possibleMoves.addAll(oneDirection(1, 0, currSquare, board, currPlayer));
        possibleMoves.addAll(oneDirection(-1, 0, currSquare, board, currPlayer));
        possibleMoves.addAll(oneDirection(0, 1, currSquare, board, currPlayer));
        possibleMoves.addAll(oneDirection(0, -1, currSquare, board, currPlayer));

        setPossibleMoves(possibleMoves);
    }
}