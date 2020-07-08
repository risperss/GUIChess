import java.util.*;

public class Rook extends Piece {
    public Rook(ChessColor color) {
        super(color);
        setIcon('R');
    }

    @Override
    public void setPossibleMoves(Square currSquare, Board board, Player currPlayer) {
        ArrayList<Move> possibleMoves = new ArrayList<Move>();

        possibleMoves.addAll(oneDirection(1, 0, currSquare, board, currPlayer));
        possibleMoves.addAll(oneDirection(-1, 0, currSquare, board, currPlayer));
        possibleMoves.addAll(oneDirection(0, 1, currSquare, board, currPlayer));
        possibleMoves.addAll(oneDirection(0, -1, currSquare, board, currPlayer));

        setPossibleMoves(possibleMoves);
    }
}