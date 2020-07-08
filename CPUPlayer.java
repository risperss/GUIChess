import java.util.*;

public class CPUPlayer extends Player {
    CPUPlayer(ChessColor color) {
        super(color);
    }

    @Override
    public Move getMove(Board board) {
        Random rand = new Random();
        int max = getAllLegalMoves().size();
        return getAllLegalMoves().get(rand.nextInt(max));
    }

    @Override
    public Piece getPromotionPiece(Board board) {
        Random rand = new Random();
        switch (rand.nextInt(4)) {
            case 0:
                return new Queen(getColor());
            case 1:
                return new Rook(getColor());
            case 2:
                return new Bishop(getColor());
            default:
                return new Knight(getColor());
        }
    }
}