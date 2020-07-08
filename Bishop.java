import java.util.*;

public class Bishop extends Piece {
    public Bishop(ChessColor color) {
        super(color);
        setIcon('B');
    }

    @Override
    public void setPossibleMoves(Square currSquare, Board board, Player currPlayer) {
        ArrayList<Move> possibleMoves = new ArrayList<Move>();

        possibleMoves.addAll(oneDirection(1, 1, currSquare, board, currPlayer));
        possibleMoves.addAll(oneDirection(-1, 1, currSquare, board, currPlayer));
        possibleMoves.addAll(oneDirection(1, -1, currSquare, board, currPlayer));
        possibleMoves.addAll(oneDirection(-1, -1, currSquare, board, currPlayer));

        setPossibleMoves(possibleMoves);
    }
}